package hinl.jsonparser

import android.util.JsonReader
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName


class JsonDeserializer {

    inline fun <reified F, reified S, reified C: Map<F, S>>parseJson(json: String, kClass: TypeToken<C>, typeAdapterMap: HashMap<KClass<*>, TypeAdapter<*>>): Map<F, S> {
        TODO("Not Support Yet")
        return mapOf<F, S>()
    }

    inline fun <reified T: Any, reified C: Collection<T>>parseJson(json: String, kClass: TypeToken<C>, typeAdapterMap: HashMap<KClass<*>, TypeAdapter<*>>): Collection<T>? {
        val kList = ArrayList<T>()
        val jsonArr = JSONArray(json)
        for (index in 0..jsonArr.length() - 1) {
            val obj = parseJson(json = jsonArr[index].toString(), kClass = T::class, typeAdapterMap = typeAdapterMap)
            if (obj != null) {
                kList.add(obj)
            }
        }
        if (kList.isEmpty()) {
            return null
        }
        return kList
    }

    fun <T : Any> parseJson(json: String, kClass: KClass<T>, typeAdapterMap: HashMap<KClass<*>, TypeAdapter<*>>): T? {
        if (json.equals("null", true)) {
            return null
        }
        val typeAdapter = typeAdapterMap[kClass]
        if (typeAdapter != null) {
            return typeAdapter.read(json) as T?
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
            var param: Any?
            if (jsonObject.has(jsonKey)) {
                if (memberKClass.isSubclassOf(Collection::class)) {
                    val jsonArr = JSONArray(jsonObject[jsonKey].toString())
                    if (jsonArr.length() > 0) {
                        val memberList = ArrayList<Any>()
                        for (index in 0..jsonArr.length() - 1) {
                            val childClass = it.returnType.arguments[0].type?.jvmErasure
                            if (childClass != null) {
                                val obj = parseJson(jsonArr[index].toString(), childClass, typeAdapterMap)
                                if (obj != null) {
                                    memberList.add(obj)
                                }
                            }
                        }
                        param = memberList
                    } else if (it.returnType.isMarkedNullable) {
                        param = null
                    } else {
                        TODO("Throw Exception")
                        throw IllegalArgumentException()
                    }
                } else {
                    val typeAdapter = typeAdapterMap[memberKClass]
                    if (typeAdapter != null) {
                        val obj = typeAdapter.read(jsonObject, jsonKey)
                        if (it.returnType.isMarkedNullable || obj != null) {
                            param = obj
                        } else {
                            TODO("Throw Exception")
                            throw IllegalArgumentException()
                        }
                    } else {
                        param = recursiveParseJson(it.returnType.isMarkedNullable, jsonObject, jsonKey, memberKClass, typeAdapterMap)
                    }
                }
                val kParams = constructor?.findParameterByName(it.name)
                if (kParams != null) {
                    paramsMap.put(kParams, param)
                }
            }
        }

        Log.d("DLLM", "Map: " + paramsMap)
        Log.d("DLLM", "constructor: " + constructor)
        if (paramsMap.isEmpty()) {
            throw IllegalArgumentException()
            TODO("Throw Exception")
        }

        return constructor?.callBy(paramsMap)
    }


    private fun recursiveParseJson(isMarkedNullable: Boolean,
                                   jsonObject: JSONObject,
                                   jsonKey: String,
                                   kClass: KClass<*>,
                                   typeAdapterMap: HashMap<KClass<*>, TypeAdapter<*>>): Any? {
        if (jsonObject.has(jsonKey) && !jsonObject.isNull(jsonKey)) {
            return parseJson(jsonObject[jsonKey].toString(), kClass, typeAdapterMap)
        } else if (isMarkedNullable) {
            return null
        } else {
            TODO("Throw Exception")
            throw IllegalArgumentException()
        }
    }
}



class TypeToken<T>