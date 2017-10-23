package hinl.jsonparser

import android.util.JsonReader
import android.util.JsonWriter
import org.json.JSONObject

import java.io.IOException

abstract class TypeAdapter<T> {

    abstract fun write(output: JSONObject, value: T?): JSONObject

    abstract fun read(input: JSONObject): T
}
