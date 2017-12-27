package tomatobean.jsonparser.typeadapter

import tomatobean.jsonparser.*
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure


class EnumTypeAdapter: TypeAdapter<Enum<*>>() {
    override fun write(output: JsonWriter, value: Enum<*>?, config: JsonParserConfig): JsonWriter {
        value?.let {
            val jsonFormat = it.javaClass.getField(it.name).annotations.find { it is JsonFormat } as? JsonFormat
            jsonFormat?.let {
                output.value(it.JsonName)
                return output
            }
        }
        return output.value(value?.name)
    }

    override fun read(json: String, config: JsonParserConfig): Enum<*>? {
        //Not exec function
        return null
    }

    override fun read(kType: KType?, json: String, config: JsonParserConfig, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>): Enum<*>? {
        val kClass = kType?.jvmErasure ?: return null
        var enum: Enum<*>? = null
        (kClass as KClass<Enum<*>>).java.enumConstants.forEach {
            val jsonFormat = it.javaClass.getField(it.name).annotations.find { it is JsonFormat } as? JsonFormat
            if (jsonFormat?.JsonName == json) {
                enum = it
                return@forEach
            }
        }
        if (enum == null) {
            enum = kClass.java.enumConstants.find {
                it.name == json
            }
        }
        return enum
    }
}