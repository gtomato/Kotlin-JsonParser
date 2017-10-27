package hinl.jsonparser.typeadapter

import android.util.JsonWriter
import hinl.jsonparser.JsonParserConfig
import hinl.jsonparser.TypeAdapter
import org.json.JSONObject
import java.lang.Enum.valueOf
import kotlin.reflect.KClass


class EnumTypeAdapter: TypeAdapter<Enum<*>>() {
    override fun write(output: JsonWriter, value: Enum<*>?, config: JsonParserConfig): JsonWriter {
        return output.value(value?.name)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Enum<*>? {
        //Not exec function
        return null
    }

    override fun read(json: String, config: JsonParserConfig): Enum<*>? {
        //Not exec function
        return null
    }

    override fun read(kClass: KClass<*>, input: JSONObject, key: String, config: JsonParserConfig): Enum<*>? {
        val enumString = input.getString(key)
        return (kClass as KClass<Enum<*>>).java.enumConstants.find {
            it.name == enumString
        }
    }

    override fun read(kClass: KClass<*>, json: String, config: JsonParserConfig): Enum<*>? {
        return (kClass as KClass<Enum<*>>).java.enumConstants.find {
            it.name == json
        }
    }
}