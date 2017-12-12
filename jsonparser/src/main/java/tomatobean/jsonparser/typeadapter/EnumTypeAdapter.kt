package tomatobean.jsonparser.typeadapter

import tomatobean.jsonparser.JsonParserConfig
import tomatobean.jsonparser.JsonWriter
import tomatobean.jsonparser.TypeAdapter
import kotlin.reflect.KClass


class EnumTypeAdapter: TypeAdapter<Enum<*>>() {
    override fun write(output: JsonWriter, value: Enum<*>?, config: JsonParserConfig): JsonWriter {
        return output.value(value?.name)
    }

    override fun read(json: String, config: JsonParserConfig): Enum<*>? {
        //Not exec function
        return null
    }

    override fun read(kClass: KClass<*>, json: String, config: JsonParserConfig): Enum<*>? {
        return (kClass as KClass<Enum<*>>).java.enumConstants.find {
            it.name == json
        }
    }
}