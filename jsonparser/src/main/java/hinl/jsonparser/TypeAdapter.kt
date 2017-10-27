package hinl.jsonparser

import android.util.JsonWriter
import org.json.JSONObject

import kotlin.reflect.KClass

abstract class TypeAdapter<T: Any> {
    protected fun getTypeAdapterMap(): Map<KClass<*>, TypeAdapter<*>> {
        TODO("refer to the static ref of map")
    }

//    @Throws
    abstract fun write(output: JsonWriter, value: T?, config: JsonParserConfig): JsonWriter

    internal open fun read(kClass: KClass<*>, input: JSONObject, key: String, config: JsonParserConfig): T? {
        return read(input, key, config)
    }

    //    @Throws
    abstract fun read(input: JSONObject, key: String, config: JsonParserConfig): T?

    internal open fun read(kClass: KClass<*>, json: String, config: JsonParserConfig): T? {
        return read(json, config)
    }

    abstract fun read(json: String, config: JsonParserConfig): T?
}
