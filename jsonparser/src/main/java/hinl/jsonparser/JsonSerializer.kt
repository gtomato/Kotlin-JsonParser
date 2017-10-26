package hinl.jsonparser

import android.util.JsonWriter
import org.json.JSONObject
import java.io.StringWriter
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
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

        when (obj) {
            is Collection<*> -> {
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
                jsonWriter.beginObject()
                obj.iterator().forEach {
                    val key = it.key?.toString() ?: "null"
                    it.value?.let {
                        val typeAdapter = typeAdapters[it::class] as? TypeAdapter<Any>
                        if (typeAdapter != null) {
                            typeAdapter.write(
                                    output = jsonWriter,
                                    key = key,
                                    value = it,
                                    config = config)
                        } else {
                            jsonWriter.name(key)
                            createJsonObject(jsonWriter, it, typeAdapters, config)
                        }
                    } ?: let {
                        jsonWriter.nullValue()
                    }
                }
                jsonWriter.endObject()
            }
            else -> createJsonObject(jsonWriter, obj, typeAdapters, config)
        }
        jsonWriter.close()
        return stringWriter.toString()
    }

    private fun createJsonObject(jsonWriter: JsonWriter, obj: Any, typeAdapters: HashMap<KClass<*>, TypeAdapter<*>>, config: JsonParserConfig) {
        jsonWriter.beginObject()
        obj::class.memberProperties.forEach {
            val schema = it.javaField?.annotations?.find { it is Schema } as? Schema
            if (schema?.Serializable == true || schema == null) {
                val key = schema?.JsonName ?: it.name
                val typeAdapter = typeAdapters[it.returnType.jvmErasure] as? TypeAdapter<Any>
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
}

