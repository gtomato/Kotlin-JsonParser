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

//    @Throws
    abstract fun write(output: JsonWriter, key: String, value: T?, config: JsonParserConfig): JsonWriter

//    @Throws
    abstract fun read(input: JSONObject, key: String, config: JsonParserConfig): T?
}
