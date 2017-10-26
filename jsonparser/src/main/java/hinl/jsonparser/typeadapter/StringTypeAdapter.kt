package hinl.jsonparser.typeadapter

import android.util.JsonWriter
import hinl.jsonparser.JsonParserConfig
import hinl.jsonparser.TypeAdapter
import org.json.JSONObject


class CharSequenceTypeAdapter: TypeAdapter<CharSequence>() {
    override fun write(output: JsonWriter, value: CharSequence?, config: JsonParserConfig): JsonWriter {
        return output.value(value?.toString())
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): CharSequence? {
        return input.getString(key)
    }
}

class StringTypeAdapter: TypeAdapter<String>(){
    override fun write(output: JsonWriter, value: String?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): String? {
        return input.getString(key)
    }
}

class CharTypeAdapter: TypeAdapter<Char>(){
    override fun write(output: JsonWriter, value: Char?, config: JsonParserConfig): JsonWriter {
        return output.value(value?.toString())
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Char? {
        return input.getString(key).single()
    }
}