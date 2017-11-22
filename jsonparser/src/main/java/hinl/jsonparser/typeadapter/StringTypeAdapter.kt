package hinl.jsonparser.typeadapter

import android.util.JsonWriter
import hinl.jsonparser.JsonParserConfig
import hinl.jsonparser.TypeAdapter


class CharSequenceTypeAdapter: TypeAdapter<CharSequence>() {
    override fun write(output: JsonWriter, value: CharSequence?, config: JsonParserConfig): JsonWriter {
        return output.value(value?.toString())
    }

    override fun read(json: String, config: JsonParserConfig): CharSequence? {
        return json
    }
}

class StringTypeAdapter: TypeAdapter<String>(){
    override fun write(output: JsonWriter, value: String?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(json: String, config: JsonParserConfig): String? {
        return json
    }
}

class CharTypeAdapter: TypeAdapter<Char>(){
    override fun write(output: JsonWriter, value: Char?, config: JsonParserConfig): JsonWriter {
        return output.value(value?.toString())
    }

    override fun read(json: String, config: JsonParserConfig): Char? {
        return json.single()
    }
}