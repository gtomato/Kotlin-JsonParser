package tomatobean.jsonparser

import android.util.JsonWriter
import kotlin.reflect.KClass

abstract class TypeAdapter<T: Any>: SerializeAdapter<T>, DeserializeAdapter<T> {
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
    fun read(json: String, config: JsonParserConfig): T?
}
