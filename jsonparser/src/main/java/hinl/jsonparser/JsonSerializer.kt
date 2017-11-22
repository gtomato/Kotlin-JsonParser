package hinl.jsonparser

import android.util.JsonWriter
import android.util.Log
import java.io.StringWriter
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure

/**
 * Created by wingy26 on 23/10/2017.
 */
class JsonSerializer {

    internal fun serialize(obj: Any, typeAdapters: HashMap<KClass<*>, SerializeAdapter<*>>, config: JsonParserConfig): String {
        // for obj is a single object
        val stringWriter = StringWriter()
        val jsonWriter = JsonWriter(stringWriter)
        checkForClass(jsonWriter, obj, typeAdapters, config)
        jsonWriter.close()
        return stringWriter.toString()
    }

    private fun checkForClass(jsonWriter: JsonWriter, obj: Any, typeAdapters: HashMap<KClass<*>, SerializeAdapter<*>>, config: JsonParserConfig) {

        when (obj) {
            is Collection<*> -> {
                // list
                jsonWriter.beginArray()
                for (any in obj) {
                    if (any != null) {
                        createJsonObject(jsonWriter, any, typeAdapters, config)
                    } else {
                        jsonWriter.nullValue()
                    }
                }
                jsonWriter.endArray()
            }
            is Map<*, *> -> {
                // map
                jsonWriter.beginObject()
                obj.iterator().forEach {
                    val key = it.key?.toString() ?: "null"
                    val value = it.value
                    val typeAdapter = if (value != null) JsonFormatter.getSerializeAdapter(value::class, typeAdapters) as? SerializeAdapter<Any> else null
                    createNode(jsonWriter, key, value, typeAdapter, typeAdapters, config)
                }
                jsonWriter.endObject()
            }
            else -> {
                // normal single object
                createJsonObject(jsonWriter, obj, typeAdapters, config)
            }
        }
    }

    private fun createJsonObject(jsonWriter: JsonWriter, obj: Any, typeAdapters: HashMap<KClass<*>, SerializeAdapter<*>>, config: JsonParserConfig) {
        val typeAdapter = JsonFormatter.getSerializeAdapter(obj::class, typeAdapters) as? SerializeAdapter<Any>
        if (typeAdapter != null) {
            typeAdapter.write(jsonWriter, obj, config)
        } else {
            jsonWriter.beginObject()
            obj::class.memberProperties.forEachIndexed { index, it ->
                try {
                    if (it.isSerializable()){
                        val key = it.getJsonName() ?: it.name
                        val typeAdapter = JsonFormatter.getSerializeAdapter(it.returnType.jvmErasure, typeAdapters) as? SerializeAdapter<Any>
                        val value = it.getter.call(obj)
                        createNode(jsonWriter, key, value, typeAdapter, typeAdapters, config)
                    }
                } catch (e: Exception) {
                    Log.e("JsonSerializer", "Error in $index, key = ${it.name}", e)
                }
            }
            jsonWriter.endObject()
        }
    }

    private fun createNode(jsonWriter: JsonWriter, key: String, value: Any?, typeAdapter: SerializeAdapter<Any>?, typeAdapters: HashMap<KClass<*>, SerializeAdapter<*>>, config: JsonParserConfig) {
        jsonWriter.name(key)
        if (typeAdapter != null) {
            typeAdapter.write(
                    output = jsonWriter,
                    value = value,
                    config = config)
        } else {
            if (value == null) {
                jsonWriter.nullValue()
            } else {
                checkForClass(jsonWriter, value, typeAdapters, config)
            }
        }
    }
}

