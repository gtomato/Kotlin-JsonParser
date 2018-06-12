package tomatobean.jsonparser

import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure


class JsonDeserializer {
    fun <T : Any> parseJson(json: String, typeToken: TypeToken<T>, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>, config: JsonParserConfig): T? {
        val rawType = typeToken.rawType
        return parseJson(json, rawType, typeAdapterMap, config) as T?
    }

    fun <T : Any> parseJson(json: String, kType: KType?, kClass: KClass<T>, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>, config: JsonParserConfig): T? {
        return parseJson(json, kType, typeAdapterMap, config) as T?
    }

    internal fun parseJson(json: String, kType: KType?, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>, config: JsonParserConfig): Any? {
        if (kType == null) {
            return null
        }
        val kClass = kType.jvmErasure
        checkKClassValid(kClass)
        if (json.equals("null", true)) {
            return null
        }
        val typeAdapter = JsonFormatter.getDeserializeAdapter(kClass, typeAdapterMap)
        if (typeAdapter != null) {
            when (typeAdapter) {
                is TypeAdapter -> {
                    return typeAdapter.read(json = json, kType = kType, config = config, typeAdapterMap = typeAdapterMap)
                }
                else -> {
                    return typeAdapter.read(json, config)
                }
            }
        }
        val constructor = kClass.primaryConstructor
        val paramsMap = hashMapOf<KParameter, Any?>()
        val members = kClass.memberProperties
        val jsonObject = JSONObject(json)

        val kParamList = ArrayList<KParameter>()
        val nonConstructorValueMap = hashMapOf<String, Any?>()
        members.forEach {

            if (!it.isDeserializable()) {
                return@forEach
            }
            val kParams = constructor?.findParameterByName(it.name)
            if (kParams?.isOptional == false) {
                kParamList.add(kParams)
            }
            val memberKClass = it.returnType.jvmErasure
            val memberType = it.returnType
            val jsonKey = it.getJsonName() ?: it.name
            var param: Any? = null
            if (jsonObject.has(jsonKey)) {
                if (memberKClass.isSubclassOf(Collection::class)) {
                    val jsonArr = JSONArray(jsonObject[jsonKey].toString())
                    param = parseCollectionType(jsonArray = jsonArr,
                            property = it,
                            typeAdapterMap = typeAdapterMap,
                            config = config)
                    if (param == null && !it.returnType.isMarkedNullable && kParams?.isOptional == false) {
                        throw MissingParamException(kClass, jsonKey)
                    }
                } else if (memberKClass.isSubclassOf(Map::class)) {
                    val jsonMap = jsonObject.getJSONObject(jsonKey)
                    param = parseMapType(jsonMap = jsonMap,
                            property = it,
                            typeAdapterMap = typeAdapterMap,
                            config = config)
                    if (param == null && !it.returnType.isMarkedNullable && kParams?.isOptional == false) {
                        throw MissingParamException(kClass, jsonKey)
                    }
                } else {
                    param = parserSingleObjectType(jsonObject = jsonObject,
                            jsonKey = jsonKey,
                            memberKClass = memberKClass,
                            memberType = memberType,
                            property = it,
                            typeAdapterMap = typeAdapterMap,
                            config = config)
                    if (param == null && !it.returnType.isMarkedNullable && kParams?.isOptional == false) {
                        throw MissingParamException(kClass, jsonKey)
                    }
                }
            }
            if (kParams != null) {
                if (param != null) {
                    paramsMap.put(kParams, param)
                } else if (!kParams.isOptional && it.returnType.isMarkedNullable) {
                    paramsMap.put(kParams, param)
                }
            } else {
                nonConstructorValueMap.put(it.name, param)
            }
        }
        val missingParams = ArrayList<KParameter>()
        kParamList.filterNotTo(missingParams) { paramsMap.containsKey(it) }

        if (!missingParams.isEmpty()) {
            val paramList = ArrayList<String?>()
            missingParams.forEach {
                paramList.add(it.name)
            }
            throw MissingParamException(kClass, paramList)
        }

        val obj = constructor?.callBy(paramsMap)

        members.forEach {
            if (nonConstructorValueMap.containsKey(it.name)) {
                when (it) {
                    is KMutableProperty<*> -> {
                        val value = nonConstructorValueMap[it.name]
                        if (value != null) {
                            it.isAccessible = true
                            it.setter.call(obj, it.returnType.jvmErasure.safeCast(value))
                        }
                    }
                }
            }
        }

        return obj
    }

    private fun parseCollectionType(jsonArray: JSONArray, property: KProperty1<*, *>, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>, config: JsonParserConfig): ArrayList<Any?>? {
        val memberList = ArrayList<Any?>()
        return if (jsonArray.length() > 0) {
            for (index in 0 until jsonArray.length()) {
                val childType = property.returnType.arguments[0].type
                val childClass = property.returnType.arguments[0].type?.jvmErasure
                if (childClass != null) {
                    checkKClassValid(childClass)
                    val obj = parseJson(jsonArray[index].toString(), childType, childClass, typeAdapterMap, config)
                    if (obj != null) {
                        memberList.add(obj)
                    }
                }
            }
            memberList
        } else {
            return memberList
        }
    }

    private fun parseMapType(jsonMap: JSONObject, property: KProperty1<*, *>, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>, config: JsonParserConfig): HashMap<String, Any?>? {
        val childType = property.returnType.arguments[1].type
        val childClass = property.returnType.arguments[1].type?.jvmErasure
        return if (childClass != null) {
            checkKClassValid(childClass)
            val hashMap = hashMapOf<String, Any?>()
            for (key in jsonMap.keys()) {
                val objJson = jsonMap.get(key).toString()
                val obj = parseJson(objJson, childType, childClass, typeAdapterMap, config)
                hashMap.put(key, obj)
            }
            hashMap
        } else {
            null
        }
    }

    private fun parserSingleObjectType(jsonObject: JSONObject, jsonKey: String, memberKClass: KClass<*>, memberType: KType, property: KProperty1<*, *>, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>, config: JsonParserConfig): Any? {
        val memberTypeAdapter = JsonFormatter.getDeserializeAdapter(memberKClass, typeAdapterMap)
        return if (memberTypeAdapter != null) {
            val memberString = jsonObject.get(jsonKey).toString()
            if (memberString.equals("null", true)) {
                null
            } else {
                when (memberTypeAdapter) {
                    is TypeAdapter -> {
                        memberTypeAdapter.read(memberString, memberType, config, typeAdapterMap)
                    }
                    else -> {
                        memberTypeAdapter.read(memberString, config)
                    }
                }
            }

        } else {
            recursiveParseJson(property.returnType.isMarkedNullable, jsonObject, jsonKey, memberType, memberKClass, typeAdapterMap, config)
        }
    }

    private fun checkKClassValid(kClass: KClass<*>) {
        when (kClass) {
            Any::class -> {
                throw TypeNotSupportException(kClass)
            }
        }
    }

    private fun recursiveParseJson(isMarkedNullable: Boolean,
                                   jsonObject: JSONObject,
                                   jsonKey: String,
                                   kType: KType,
                                   kClass: KClass<*>,
                                   typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>, config: JsonParserConfig): Any? {
        if (jsonObject.has(jsonKey) && !jsonObject.isNull(jsonKey)) {
            return parseJson(jsonObject[jsonKey].toString(), kType, kClass, typeAdapterMap, config)
        } else if (isMarkedNullable) {
            return null
        } else {
            throw MissingParamException(kClass, jsonKey)
        }
    }
}