package hinl.jsonparser.typeadapter

import android.util.JsonWriter
import hinl.jsonparser.JsonParserConfig
import hinl.jsonparser.TypeAdapter
import org.json.JSONObject


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