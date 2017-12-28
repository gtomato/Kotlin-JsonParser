package tomatobean.jsonparser.typeadapter

import org.json.JSONArray
import tomatobean.jsonparser.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.cast
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure


class ArrayTypeAdapter: TypeAdapter<Array<*>>() {
    override fun write(output: JsonWriter, value: Array<*>?, config: JsonParserConfig): JsonWriter {
        //Not exec function
        return output
    }

    override fun read(json: String, config: JsonParserConfig): Array<*>? {
        //Not exec function
        return null
    }

    override fun read(json: String, kType: KType?, config: JsonParserConfig, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>): Array<*>? {
        if (kType == null) {
            return null
        }

        val childType = kType.arguments[0].type
        val jsonArr = JSONArray(json)
        val collections = arrayListOf<Any?>()
        for (index in 0 until jsonArr.length()) {
            val childJson = jsonArr[index].toString()
            val obj = JsonDeserializer().parseJson(childJson, childType, typeAdapterMap, config)
            if (childType?.isMarkedNullable != true && obj == null) {
                throw IllegalArgumentException("Object in index: $index is null while variable defined is a non-nullable object")
            }
            collections.add(childType?.jvmErasure?.cast(obj))
        }
        if (collections.isEmpty()) {
            return null
        }
        val childClass = kType.jvmErasure.java.componentType
        val javaListOfChild = java.lang.reflect.Array.newInstance(childClass, collections.size)
        return collections.toArray(javaListOfChild as Array<*>)
    }
}