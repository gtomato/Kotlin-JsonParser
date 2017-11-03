package hinl.jsonparser

import android.util.Log
import hinl.jsonparser.typeadapter.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType


class JsonFormatter(
        typeAdapterMap: HashMap<KClass<*>, TypeAdapter<*>>? = null,
        config: JsonParserConfig = JsonParserConfig()) {

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
                BigInteger::class to BigIntegerTypeAdapter(),
                Enum::class to EnumTypeAdapter(),
                Date::class to DateTypeAdapter(),
                Calendar::class to CalendarTypeAdapter()
        )

        val DEFAULT_DATE_FORMAT = "YYYY-MM-DD hh:mm:ss Z"

        internal fun getTypeAdapter(kClass: KClass<*>, typeAdapterMap: HashMap<KClass<*>, TypeAdapter<*>>): TypeAdapter<*>? {
            if (kClass.isSubclassOf(Enum::class)) {
                return typeAdapterMap[Enum::class]
            } else {
                return typeAdapterMap[kClass]
            }
        }
    }

    val mTypeAdapterMap = hashMapOf<KClass<*>, TypeAdapter<*>>().apply {
        putAll(DEFAULT_TypeAdapterMap)
        typeAdapterMap?.let {
            putAll(it)
        }
    }
    val mConfig: JsonParserConfig = config

    fun <T: Any> parseJson(json: String, kClass: KClass<T>): T? {
        return JsonDeserializer().parseJson(json, kClass, mTypeAdapterMap, mConfig)
    }

    fun toJson(obj: Any): String {
        return JsonSerializer().serialize(obj, JsonFormatter.DEFAULT_TypeAdapterMap, mConfig)
    }

    inline fun <reified T: Any, reified C: Collection<T?>> parseJson(json: String, typeToken: TypeToken<C>): Collection<T?>? {
        return JsonDeserializer().parseJson(json, typeToken, mTypeAdapterMap, mConfig)
    }

    inline fun <reified F: Any, reified S: Any, reified C: Map<F, S?>> parseJson(json: String, typeToken: TypeToken<C>): Map<F, S?> {
        return JsonDeserializer().parseJson(json, typeToken, mTypeAdapterMap, mConfig)
    }
}