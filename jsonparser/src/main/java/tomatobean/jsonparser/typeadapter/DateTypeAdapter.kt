package tomatobean.jsonparser.typeadapter

import tomatobean.jsonparser.JsonParserConfig
import tomatobean.jsonparser.JsonWriter
import tomatobean.jsonparser.TypeAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wingy26 on 27/10/2017.
 */


class DateTypeAdapter : TypeAdapter<Date>() {
    override fun write(output: JsonWriter, value: Date?, config: JsonParserConfig): JsonWriter {
        val formatter = SimpleDateFormat(config.dateFormat)
        val dateString = try {
            formatter.format(value)
        } catch (e: Exception) {
            null
        }
        return output.value(dateString)
    }

    override fun read(json: String, config: JsonParserConfig): Date? {
        val formatter = SimpleDateFormat(config.dateFormat)

        return try {
            formatter.parse(json)
        } catch (e: Exception) {
            null
        }
    }
}

class CalendarTypeAdapter : TypeAdapter<Calendar>() {
    override fun write(output: JsonWriter, value: Calendar?, config: JsonParserConfig): JsonWriter {
        val formatter = SimpleDateFormat(config.dateFormat)
        val dateString = try {
            formatter.format(value?.time)
        } catch (e: Exception) {
            null
        }
        return output.value(dateString)
    }

    override fun read(json: String, config: JsonParserConfig): Calendar? {
        val formatter = SimpleDateFormat(config.dateFormat)

        return try {
            Calendar.getInstance().apply {
                time = formatter.parse(json)
            }
        } catch (e: Exception) {
            null
        }
    }
}