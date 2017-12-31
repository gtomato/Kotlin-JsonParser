package tomatobean.jsonparser.typeadapter

import org.json.JSONArray
import tomatobean.jsonparser.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure


class CollectionTypeAdapter: TypeAdapter<Collection<*>>() {
    override fun write(output: JsonWriter, value: Collection<*>?, config: JsonParserConfig): JsonWriter {
        //Not exec function
        return output
    }

    override fun read(json: String, config: JsonParserConfig): Collection<*>? {
        //Not exec function
        return null
    }

    override fun read(json: String, kType: KType?, config: JsonParserConfig, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>): Collection<*>? {
        if (kType == null) {
            return null
        }
        val childType = kType.arguments[0].type
        val jsonArr = JSONArray(json)
        val collections = ArrayList<Any?>()
        if (childType?.jvmErasure?.isSubclassOf(Collection::class) == true) {
            for (index in 0 until jsonArr.length()) {
                val childJson = jsonArr[index].toString()
                val obj = read(childJson, childType, config, typeAdapterMap)
                if (!childType.isMarkedNullable && obj == null) {
                    throw MissingParamException(childType, index)
                }
                collections.add(obj)
            }
        } else if (childType != null){
            for (index in 0 until jsonArr.length()) {
                val childJson = jsonArr[index].toString()
                val obj = JsonDeserializer().parseJson(childJson, childType, typeAdapterMap, config)
                if (!childType.isMarkedNullable && obj == null) {
                    throw MissingParamException(childType, index)
                }
                collections.add(obj)
            }
        }
        if (collections.isEmpty()) {
            return null
        }
        return collections
    }
}