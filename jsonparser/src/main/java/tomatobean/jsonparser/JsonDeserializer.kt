package tomatobean.jsonparser

import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.*
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure


class JsonDeserializer {
    fun <T : Any> parseJson(json: String, typeToken: TypeToken<T>, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>, config: JsonParserConfig): T? {
        val rawType = typeToken.rawType
        return when (rawType) {
            is Map<*, *> -> {
                if (!rawType.isSupertypeOf(LinkedHashMap::class.getKTypeImpl())) {
                    throw IllegalArgumentException("Parser Not Support This Type $rawType currently")
                }
                val typeAdapter = typeAdapterMap[Map::class]
                when (typeAdapter) {
                    is TypeAdapter<*> -> {
                        typeAdapter.read(json = json, kType = rawType, typeAdapterMap = typeAdapterMap, config = config)
                    }
                    else -> {
                        typeAdapter?.read(json, config)
                    }
                } as T?
            }
            is Collection<*> -> {
                val typeAdapter = typeAdapterMap[Collection::class]
                when (typeAdapter) {
                    is TypeAdapter<*> -> {
                        typeAdapter.read(json = json, kType = rawType, typeAdapterMap = typeAdapterMap, config = config)
                    }
                    else -> {
                        typeAdapter?.read(json, config)
                    }
                } as T?
            }
            else -> {
                parseJson(json, rawType, typeAdapterMap, config) as T?
            }
        }
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
        members.forEach {

            if (!it.isDeserializable()) {
                return@forEach
            }
            val kParams = constructor?.findParameterByName(it.name) ?: return@forEach
            if (!kParams.isOptional) {
                kParamList.add(kParams)
            }
            val memberKClass = it.returnType.jvmErasure
            val memberType = it.returnType
            val jsonKey = it.getJsonName() ?: it.name
            var param: Any? = null
            if (jsonObject.has(jsonKey)) {
                if (memberKClass.isSubclassOf(Collection::class)) {
                    val jsonArr = JSONArray(jsonObject[jsonKey].toString())
                    if (jsonArr.length() > 0) {
                        val memberList = ArrayList<Any>()
                        for (index in 0..jsonArr.length() - 1) {
                            val childType = it.returnType.arguments[0].type
                            val childClass = it.returnType.arguments[0].type?.jvmErasure
                            if (childClass != null) {
                                checkKClassValid(childClass)
                                val obj = parseJson(jsonArr[index].toString(), childType, childClass, typeAdapterMap, config)
                                if (obj != null) {
                                    memberList.add(obj)
                                }
                            }
                        }
                        param = memberList
                    } else if (it.returnType.isMarkedNullable) {
                        param = null
                    } else if (!kParams.isOptional) {
                        throw IllegalArgumentException("Object in key: $jsonKey is null while variable defined is a non-nullable object")
                    }
                } else if (memberKClass.isSubclassOf(Map::class)) {
                    val childType = it.returnType.arguments[1].type
                    val childClass = it.returnType.arguments[1].type?.jvmErasure
                    if (childClass != null) {
                        checkKClassValid(childClass)
                        val hashMap = hashMapOf<String, Any?>()
                        val jsonMap = jsonObject.getJSONObject(jsonKey)
                        for (key in jsonMap.keys()) {
                            val objJson = jsonMap.get(key).toString()
                            val obj = parseJson(objJson, childType, childClass, typeAdapterMap, config)
                            hashMap.put(key, obj)
                        }
                        param = hashMap
                    } else {
                        param = null
                    }
                } else {
                    val memberTypeAdapter = JsonFormatter.getDeserializeAdapter(memberKClass, typeAdapterMap)
                    if (memberTypeAdapter != null) {
                        val obj: Any?
                        if (jsonObject.get(jsonKey).toString().equals("null", true)) {
                            obj = null
                        } else {
                            val jsonObjectString = jsonObject.get(jsonKey).toString()
                            when (memberTypeAdapter) {
                                is TypeAdapter -> {
                                    obj =  memberTypeAdapter.read(jsonObjectString, memberType, config, typeAdapterMap)
                                }
                                else -> {
                                    obj =  memberTypeAdapter.read(jsonObjectString, config)
                                }
                            }
                        }
                        if (it.returnType.isMarkedNullable || obj != null) {
                            param = obj
                        } else if (!kParams.isOptional) {
                            throw IllegalArgumentException("Object in key: $jsonKey is null while variable defined is a non-nullable object")
                        }
                    } else {
                        param = recursiveParseJson(it.returnType.isMarkedNullable, jsonObject, jsonKey, memberType, memberKClass, typeAdapterMap, config)
                    }
                }
            }
            if (param != null) {
                paramsMap.put(kParams, param)
            } else if (!kParams.isOptional && it.returnType.isMarkedNullable) {
                paramsMap.put(kParams, param)
            }
        }
        val missingParams = ArrayList<KParameter>()
        for (kParam in kParamList) {
            if (!paramsMap.containsKey(kParam)) {
                missingParams.add(kParam)
            }
        }

        if (!missingParams.isEmpty()) {
            val stringBuilder = StringBuilder("Object Class: ${kClass.simpleName}\n")
            missingParams.forEach {
                stringBuilder.append("Param : ${it.name}\n")
            }
            stringBuilder.append("is(are) missing, please check json String and/or Object Structure.")

            throw IllegalArgumentException(stringBuilder.toString())
        }

        return constructor?.callBy(paramsMap)
    }

    private fun checkKClassValid(kClass: KClass<*>) {
        when (kClass) {
            Any::class -> {
                throw IllegalArgumentException("kotlin.Any class is not support Json Deserialize with this library")
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
            throw IllegalArgumentException("Object in key: $jsonKey is null while variable defined is a non-nullable object")
        }
    }
}



open class TypeToken<T>(val nullableParams: Boolean = false) {
    val rawType: KType = try {
        getKTypeImpl()
    } catch (e: Exception) {
        this::class.starProjectedType
    }
}

fun KClass<*>.getKTypeImpl(): KType = java.genericSuperclass.toKType().arguments.single().type!!

fun TypeToken<*>.getKTypeImpl(): KType =
        javaClass.genericSuperclass.toKType().arguments.single().type!!

fun Type.toKType(): KType = toKTypeProjection().type!!

fun Type.toKTypeProjection(): KTypeProjection = when (this) {
    is Class<*> -> this.kotlin.toInvariantFlexibleProjection(if(this.isArray) listOf(this.componentType.toKTypeProjection()) else emptyList())
    is ParameterizedType -> {
        val erasure = (rawType as Class<*>).kotlin
        erasure.toInvariantFlexibleProjection((erasure.typeParameters.zip(actualTypeArguments).map { (parameter, argument) ->
            val projection = argument.toKTypeProjection()
            projection.takeIf {
                // Get rid of use-site projections on arguments, where the corresponding parameters already have a declaration-site projection
                parameter.variance == KVariance.INVARIANT || parameter.variance != projection.variance
            } ?: KTypeProjection.invariant(projection.type!!)
        }))
    }
    is WildcardType -> when {
        lowerBounds.isNotEmpty() -> KTypeProjection.contravariant(lowerBounds.single().toKType())
        upperBounds.isNotEmpty() -> KTypeProjection.covariant(upperBounds.single().toKType())
    // This looks impossible to obtain through Java reflection API, but someone may construct and pass such an instance here anyway
        else -> KTypeProjection.STAR
    }
    is GenericArrayType -> Array<Any>::class.toInvariantFlexibleProjection(listOf(genericComponentType.toKTypeProjection()))
    is TypeVariable<*> -> TODO()
    else -> throw IllegalArgumentException("Unsupported type: $this")
}


fun KClass<*>.toInvariantFlexibleProjection(arguments: List<KTypeProjection> = emptyList()): KTypeProjection {
    return KTypeProjection.invariant(createType(arguments, nullable = false))
}