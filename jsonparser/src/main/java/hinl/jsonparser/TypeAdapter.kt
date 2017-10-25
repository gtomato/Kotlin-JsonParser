package hinl.jsonparser

import android.util.JsonReader
import android.util.JsonWriter
import org.json.JSONObject

import java.io.IOException
import kotlin.reflect.KClass

abstract class TypeAdapter<T> {
    protected fun getTypeAdapterMap(): Map<KClass<*>, TypeAdapter<*>> {
        TODO("refer to the static ref of map")
    }

    abstract fun write(output: JSONObject, key: String, value: T?): JSONObject

    abstract fun read(input: JSONObject, key: String): T?
}
