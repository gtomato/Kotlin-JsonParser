package hinl.jsonparser

import android.util.Log
import hinl.jsonparser.typeadapter.*
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure


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

    val mTypeAdapterMap = mutableMapOf<KClass<*>, TypeAdapter<*>>().apply {
        putAll(DEFAULT_TypeAdapterMap)
        typeAdapterMap?.let {
            putAll(it)
        }
    }
    val mDateFormat: String = dateFormat

}