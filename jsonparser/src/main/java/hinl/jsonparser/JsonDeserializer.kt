package hinl.jsonparser

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure


class JsonDeserializer {

    inline fun <reified F, reified S: Any, reified C: Map<F, S?>>parseJson(json: String, typeToken: TypeToken<C>, typeAdapterMap: HashMap<KClass<*>, TypeAdapter<*>>, config: JsonParserConfig): Map<F, S?> {
        if (F::class.isSubclassOf(CharSequence::class) || F::class.isSubclassOf(Char::class)) {
            val hashMap = hashMapOf<F, S?>()
            val jsonMap = JSONObject(json)
            for (key in jsonMap.keys()) {
                val objJson = jsonMap.get(key).toString()
                val obj = parseJson(json = objJson, kClass = S::class, typeAdapterMap = typeAdapterMap, config = config)
                if (obj == null && !typeToken.nullableParams) {
                    throw IllegalArgumentException("Object in key: $key is null while variable defined is a non-nullable object")
                }
                if (F::class.isSubclassOf(Char::class)) {
                    hashMap.put(key.single() as F, obj)
                } else {
                    hashMap.put(key as F, obj)
                }
            }
            return hashMap
        } else {
            throw IllegalArgumentException("Key of the map must be in terms of CharSequence or Char")
        }
    }

    inline fun <reified T: Any, reified C: Collection<T?>>parseJson(json: String, typeToken: TypeToken<C>, typeAdapterMap: HashMap<KClass<*>, TypeAdapter<*>>, config: JsonParserConfig): Collection<T?>? {
        val kList = ArrayList<T?>()
        val jsonArr = JSONArray(json)
        for (index in 0..jsonArr.length() - 1) {
            val obj = parseJson(json = jsonArr[index].toString(), kClass = T::class, typeAdapterMap = typeAdapterMap, config = config)
            if (obj != null) {
                kList.add(obj)
            } else {
                if (typeToken.nullableParams) {
                    kList.add(obj)
                } else {
                    throw IllegalArgumentException("Object in index: $index is null while variable defined is a non-nullable object")
                }
            }
        }
        if (kList.isEmpty()) {
            return null
        }
        return kList
    }

    fun <T : Any> parseJson(json: String, kClass: KClass<T>, typeAdapterMap: HashMap<KClass<*>, TypeAdapter<*>>, config: JsonParserConfig): T? {
        if (json.equals("null", true)) {
            return null
        }
        val typeAdapter = JsonFormatter.getTypeAdapter(kClass, typeAdapterMap)
        if (typeAdapter != null) {
            return typeAdapter.read(kClass = kClass, json = json, config = config) as T?
        }
        val constructor = kClass.primaryConstructor
        val paramsMap = hashMapOf<KParameter, Any?>()
        val members = kClass.memberProperties
        val jsonObject = JSONObject(json)

        members.forEach {
            if (!it.isDeSerializable()) {
                return@forEach
            }
            val memberKClass = it.returnType.jvmErasure
            val jsonKey = if (it.getJsonName().isNotEmpty()) {
                it.getJsonName()
            } else {
                it.name
            }
            val param: Any?
            if (jsonObject.has(jsonKey)) {
                if (memberKClass.isSubclassOf(Collection::class)) {
                    val jsonArr = JSONArray(jsonObject[jsonKey].toString())
                    if (jsonArr.length() > 0) {
                        val memberList = ArrayList<Any>()
                        for (index in 0..jsonArr.length() - 1) {
                            val childClass = it.returnType.arguments[0].type?.jvmErasure
                            if (childClass != null) {
                                val obj = parseJson(jsonArr[index].toString(), childClass, typeAdapterMap, config)
                                if (obj != null) {
                                    memberList.add(obj)
                                }
                            }
                        }
                        param = memberList
                    } else if (it.returnType.isMarkedNullable) {
                        param = null
                    } else {
                        throw IllegalArgumentException("Object in key: $jsonKey is null while variable defined is a non-nullable object")
                    }
                } else if (memberKClass.isSubclassOf(Map::class)) {
                    val childClass = it.returnType.arguments[1].type?.jvmErasure
                    val hashMap = hashMapOf<String, Any?>()
                    if (childClass != null) {
                        val jsonMap = jsonObject.getJSONObject(jsonKey)
                        for (key in jsonMap.keys()) {
                            val objJson = jsonMap.get(key).toString()
                            val obj = parseJson(objJson, childClass, typeAdapterMap, config)
                            hashMap.put(key, obj)
                        }
                        param = hashMap
                    } else {
                        param = null
                    }
                } else {
                    val memberTypeAdapter = JsonFormatter.getTypeAdapter(memberKClass, typeAdapterMap)
                    if (memberTypeAdapter != null) {
                        val obj: Any?
                        if (jsonObject.get(jsonKey).toString().equals("null", true)) {
                            obj = null
                        } else {
                            obj = memberTypeAdapter.read(memberKClass, jsonObject, jsonKey, config)
                        }
                        if (it.returnType.isMarkedNullable || obj != null) {
                            param = obj
                        } else {
                            throw IllegalArgumentException("Object in key: $jsonKey is null while variable defined is a non-nullable object")
                        }
                    } else {
                        param = recursiveParseJson(it.returnType.isMarkedNullable, jsonObject, jsonKey, memberKClass, typeAdapterMap, config)
                    }
                }
                val kParams = constructor?.findParameterByName(it.name)
                if (kParams != null) {
                    paramsMap.put(kParams, param)
                }
            }
        }
        if (paramsMap.isEmpty()) {
            throw IllegalArgumentException("No params can parse with this json: $json, please check this json string is valid")
        }

        return constructor?.callBy(paramsMap)
    }


    private fun recursiveParseJson(isMarkedNullable: Boolean,
                                   jsonObject: JSONObject,
                                   jsonKey: String,
                                   kClass: KClass<*>,
                                   typeAdapterMap: HashMap<KClass<*>, TypeAdapter<*>>, config: JsonParserConfig): Any? {
        if (jsonObject.has(jsonKey) && !jsonObject.isNull(jsonKey)) {
            return parseJson(jsonObject[jsonKey].toString(), kClass, typeAdapterMap, config)
        } else if (isMarkedNullable) {
            return null
        } else {
            throw IllegalArgumentException("Object in key: $jsonKey is null while variable defined is a non-nullable object")
        }
    }
}



class TypeToken<T>(val nullableParams: Boolean = false)