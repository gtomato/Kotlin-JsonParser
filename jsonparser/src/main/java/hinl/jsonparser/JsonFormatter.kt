package hinl.jsonparser

import hinl.jsonparser.typeadapter.*
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass


class JsonFormatter(
        typeAdapterMap: HashMap<KClass<*>, TypeAdapter<*>>? = null,
        dateFormat: String = DAFAULT_DATE_FORMAT

) {

    companion object {
        val DEFAULT_TypeAdapterMap = hashMapOf<KClass<*>, TypeAdapter<*>>(
                Boolean::class to BooleanTypeAdapter(),
                CharSequence::class to CharSequenceTypeAdapter(),
                String::class to StringTypeAdapter(),
                Char::class to CharTypeAdapter(),
                Int::class to IntTypeAdapter(),
                Long::class to LongTypeAdapter(),
                Short::class to ShortTypeAdapter(),
                Double::class to DoubleTypeAdapter(),
                Float::class to FloatTypeAdapter(),
                BigDecimal::class to BigDecimalTypeAdapter(),
                BigInteger::class to BigIntegerTypeAdapter()
        )

        val DAFAULT_DATE_FORMAT = ""//TODO("DateFormate")
    }

    val mTypeAdapterMap = hashMapOf<KClass<*>, TypeAdapter<*>>().apply {
        putAll(DEFAULT_TypeAdapterMap)
        typeAdapterMap?.let {
            putAll(it)
        }
    }
    val mDateFormat: String = dateFormat
    val mConfig: JsonParserConfig = JsonParserConfig()

    fun <T: Any> parseJson(json: String, kClass: KClass<T>): T? {
        return JsonDeserializer().parseJson(json, kClass, mTypeAdapterMap, mConfig)
    }

    inline fun <reified T: Any, reified C: Collection<T>> parseJson(json: String, typeToken: TypeToken<C>): Collection<T>? {
        return JsonDeserializer().parseJson(json, typeToken, mTypeAdapterMap, mConfig)
    }

    inline fun <reified F: Any, reified S: Any, reified C: Map<F, S>> parseJson(json: String, kClass: TypeToken<C>, typeAdapterMap: HashMap<KClass<*>, TypeAdapter<*>>): Map<F, S> {
        return JsonDeserializer().parseJson(json, kClass, mTypeAdapterMap, mConfig)
    }
}