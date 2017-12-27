package tomatobean.jsonparser.typeadapter

import org.json.JSONObject
import tomatobean.jsonparser.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure


class HashMapTypeAdapter: TypeAdapter<HashMap<*, *>>() {
    override fun write(output: JsonWriter, value: HashMap<*, *>?, config: JsonParserConfig): JsonWriter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(json: String, config: JsonParserConfig): HashMap<*, *>? {
        //Not exec function
        return null
    }

    override fun read(kType: KType?, json: String, config: JsonParserConfig, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>): HashMap<*, *>? {
        if (kType == null) {
            return null
        }
//        val kClass = kType.jvmErasure
        val jsonObj = JSONObject(json)
        val childType = kType.arguments[1].type
        if (childType?.jvmErasure?.isSubclassOf(Map::class) == true) {
            val hashMap = hashMapOf<String, Any?>()
            for (key in jsonObj.keys()) {
                val childJson = jsonObj.get(key).toString()
                hashMap.put(key, read(childType, childJson, config, typeAdapterMap))
            }
            return hashMap
        } else {
            val hashMap = hashMapOf<String, Any?>()
            val deserializeAdapter = typeAdapterMap[childType?.jvmErasure]
            for (key in jsonObj.keys()) {
                val childJson = jsonObj.get(key).toString()
                hashMap.put(key, deserializeAdapter?.read(childJson, config))
            }
            return hashMap
        }
    }
}


