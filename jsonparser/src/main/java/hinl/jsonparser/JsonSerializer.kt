package hinl.jsonparser

import android.util.JsonWriter
import org.json.JSONObject
import java.io.StringWriter
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

/**
 * Created by wingy26 on 23/10/2017.
 */
internal class JsonSerializer {

    internal fun serialize(obj: Any, typeAdapters: HashMap<KClass<*>, TypeAdapter<*>>, config: JsonParserConfig): String {
        // for obj is a single object
        val stringWriter = StringWriter()
        val jsonWriter = JsonWriter(stringWriter)
        createJsonObject(jsonWriter, obj, typeAdapters, config)
        jsonWriter.close()
        return stringWriter.toString()
    }

    private fun createJsonObject(jsonWriter: JsonWriter, obj: Any, typeAdapters: HashMap<KClass<*>, TypeAdapter<*>>, config: JsonParserConfig) {
        jsonWriter.beginObject()
        obj::class.memberProperties.forEach {
            val schema = it.javaField?.annotations?.find { it is Schema } as? Schema
            if (schema?.Serializable == true || schema == null) {
                val typeAdapter = typeAdapters[it.returnType.jvmErasure] as? TypeAdapter<Any>
                val key = schema?.JsonName ?: it.name
                val value = it.getter.call(obj)
                if (typeAdapter != null) {
                    typeAdapter.write(
                            output = jsonWriter,
                            key = key,
                            value = value,
                            config = config)
                } else {
                    if (value == null) {
                        jsonWriter.name(key).nullValue()
                    } else {
                        jsonWriter.name(key)
                        createJsonObject(jsonWriter, value, typeAdapters, config)
                    }
                }
            }
        }
        jsonWriter.endObject()
    }


//    private fun Any.getJsonValue(typeAdapters: HashMap<KClass<*>, TypeAdapter<*>>): Any {
//        // find all
//        val typeAdapter = typeAdapters[this::class]
//        typeAdapter?.write()
//        return when (this) {
//
//            else -> serialize(this)
//        }
//    }
}

