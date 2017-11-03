package hinl.jsonparser

import android.util.JsonWriter
import org.json.JSONObject

import kotlin.reflect.KClass

abstract class TypeAdapter<T: Any>: SerializeAdapter<T>, DeserializeAdapter<T> {
    protected fun getTypeAdapterMap(): Map<KClass<*>, TypeAdapter<*>> {
        TODO("refer to the static ref of map")
    }

    //    @Throws
    internal open fun read(kClass: KClass<*>, input: JSONObject, key: String, config: JsonParserConfig): T? {
        return read(input, key, config)
    }

    //    @Throws
    internal open fun read(kClass: KClass<*>, json: String, config: JsonParserConfig): T? {
        return read(json, config)
    }
}

interface SerializeAdapter<in T: Any> {
    //    @Throws
    fun write(output: JsonWriter, value: T?, config: JsonParserConfig): JsonWriter
}

interface DeserializeAdapter<out T: Any> {
    //    @Throws
    fun read(input: JSONObject, key: String, config: JsonParserConfig): T?

    fun read(json: String, config: JsonParserConfig): T?
}
