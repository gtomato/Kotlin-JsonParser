package hinl.jsonparser.typeadapter

import android.util.JsonWriter
import hinl.jsonparser.JsonParserConfig
import hinl.jsonparser.TypeAdapter
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wingy26 on 27/10/2017.
 */


class DateTypeAdapter: TypeAdapter<Date>() {
    override fun write(output: JsonWriter, value: Date?, config: JsonParserConfig): JsonWriter {
        val formatter = SimpleDateFormat(config.dataFormat)
        val dateString = try {
            formatter.format(value)
        } catch (e: Exception) {
            null
        }
        return output.value(dateString)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Date? {
        val formatter = SimpleDateFormat(config.dataFormat)
        val dateString = input.getString(key)

        return try {
            formatter.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    override fun read(json: String, config: JsonParserConfig): Date? {
        val formatter = SimpleDateFormat(config.dataFormat)

        return try {
            formatter.parse(json)
        } catch (e: Exception) {
            null
        }
    }
}