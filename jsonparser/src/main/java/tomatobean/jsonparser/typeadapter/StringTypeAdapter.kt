package tomatobean.jsonparser.typeadapter

import tomatobean.jsonparser.JsonParserConfig
import tomatobean.jsonparser.JsonWriter
import tomatobean.jsonparser.TypeAdapter


class CharSequenceTypeAdapter: TypeAdapter<CharSequence>() {
    override fun write(output: JsonWriter, value: CharSequence?, config: JsonParserConfig): JsonWriter {
        return output.value(value?.toString()?.replace("\"", "\\\""))
    }

    override fun read(json: String, config: JsonParserConfig): CharSequence? {
        return json
    }
}

class StringTypeAdapter: TypeAdapter<String>(){
    override fun write(output: JsonWriter, value: String?, config: JsonParserConfig): JsonWriter {
        return output.value(value?.replace("\"", "\\\""))
    }

    override fun read(json: String, config: JsonParserConfig): String? {
        return json
    }
}

class CharTypeAdapter: TypeAdapter<Char>(){
    override fun write(output: JsonWriter, value: Char?, config: JsonParserConfig): JsonWriter {
        return output.value(value?.toString()?.replace("\"", "\\\""))
    }

    override fun read(json: String, config: JsonParserConfig): Char? {
        return json.single()
    }
}