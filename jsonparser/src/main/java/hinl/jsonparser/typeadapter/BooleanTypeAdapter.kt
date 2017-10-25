package hinl.jsonparser.typeadapter

import android.util.JsonWriter
import hinl.jsonparser.JsonParserConfig
import hinl.jsonparser.TypeAdapter
import org.json.JSONObject


class BooleanTypeAdapter: TypeAdapter<Boolean>(){
    override fun write(output: JsonWriter, key: String, value: Boolean?, config: JsonParserConfig): JsonWriter {
        output.name(key)
        if (value != null) {
            output.value(value)
        } else {
            output.nullValue()
        }
        return output
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Boolean? {
        return input.getBoolean(key)
    }
}