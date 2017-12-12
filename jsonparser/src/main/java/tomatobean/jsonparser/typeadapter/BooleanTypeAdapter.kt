package tomatobean.jsonparser.typeadapter

import tomatobean.jsonparser.JsonParserConfig
import tomatobean.jsonparser.JsonWriter
import tomatobean.jsonparser.TypeAdapter


class BooleanTypeAdapter: TypeAdapter<Boolean>(){
    override fun write(output: JsonWriter, value: Boolean?, config: JsonParserConfig): JsonWriter {
        if (value != null) {
            output.value(value)
        } else {
            output.nullValue()
        }
        return output
    }

    override fun read(json: String, config: JsonParserConfig): Boolean? {
        return json.toBoolean()
    }
}