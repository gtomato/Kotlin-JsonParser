package tomatobean.jsonparser.typeadapter

import org.json.JSONObject
import tomatobean.jsonparser.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf
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
//        val kClass = kType.jvmErasure
        val jsonObj = JSONObject(json)
        val childType = kType.arguments[1].type
        val hashMap = linkedMapOf<String, Any?>()

        for (key in jsonObj.keys()) {
            val childJson = jsonObj[key].toString()
            val obj = JsonDeserializer().parseJson(childJson, childType, typeAdapterMap, config)
            if (obj == null && childType?.isMarkedNullable != true) {
                throw IllegalArgumentException("Object in key: $key is null while variable defined is a non-nullable object")
            }
            hashMap.put(key, obj)
        }
        if (hashMap.isEmpty()) {
            return null
        }
        return hashMap
    }
}


