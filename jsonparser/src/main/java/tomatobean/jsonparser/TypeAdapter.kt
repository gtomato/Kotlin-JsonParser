package tomatobean.jsonparser

import kotlin.reflect.KClass
import kotlin.reflect.KType

abstract class TypeAdapter<T: Any>: SerializeAdapter<T>, DeserializeAdapter<T> {
    //    @Throws
    internal open fun read(json: String, kType: KType?, config: JsonParserConfig, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>): T? {
        return read(json, config)
    }
}

/**
 * An Adapter to call when serialize kotlin object to json string
 *
 * @param T The type of kotlin object class parse to json string
 */
interface SerializeAdapter<in T: Any> {
    /**
     *
     * Called when formatter serializing kotlin object
     *
     * @param [output] The [JsonWriter] object during the whole serialize process
     * @param [value] The kotlin object instance which will serialize
     * @param [config] The [JsonParserConfig] which contain current json parsing config
     *
     * @return [JsonWriter] The JsonWriter come from [output] after putting Json element
     */
    fun write(output: JsonWriter, value: T?, config: JsonParserConfig): JsonWriter
}

/**
 * An Adapter to call when deserialize json string to kotlin object
 *
 * @param T The type of kotlin object output
 */
interface DeserializeAdapter<out T: Any> {
    /**
     *
     * Called when formatter deserializing json string
     *
     * @param [json] The json string input which will deserialize to kotlin object [T]
     * @param [config] The [JsonParserConfig] which contain current json parsing config
     *
     * @return [T] The instance of Kotlin object type [T] output
     */
    fun read(json: String, config: JsonParserConfig): T?
}
