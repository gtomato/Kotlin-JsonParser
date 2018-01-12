package tomatobean.jsonparser.typeadapter

import org.json.JSONObject
import tomatobean.jsonparser.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure


class HashMapTypeAdapter: TypeAdapter<HashMap<*, *>>() {
    override fun write(output: JsonWriter, value: HashMap<*, *>?, config: JsonParserConfig): JsonWriter {
        //Not exec function
        return output
    }

    override fun read(json: String, config: JsonParserConfig): HashMap<*, *>? {
        //Not exec function
        return null
    }

    override fun read(json: String, kType: KType?, config: JsonParserConfig, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>): HashMap<*, *>? {
        if (kType == null) {
            return null
        }
        val jsonObj = JSONObject(json)
        val childType = kType.arguments[1].type
        val hashMap = linkedMapOf<String, Any?>()

        if (childType != null) {
            for (key in jsonObj.keys()) {
                val childJson = jsonObj[key].toString()
                val obj = JsonDeserializer().parseJson(childJson, childType, typeAdapterMap, config)
                if (obj == null && !childType.isMarkedNullable) {
                    throw MissingParamException(childType.jvmErasure, key)
                }
                hashMap.put(key, obj)
            }
        }
        if (hashMap.isEmpty()) {
            return null
        }
        return hashMap
    }
}


