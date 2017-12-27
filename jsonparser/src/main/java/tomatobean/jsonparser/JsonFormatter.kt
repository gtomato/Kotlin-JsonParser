package tomatobean.jsonparser

import tomatobean.jsonparser.typeadapter.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubclassOf


class JsonFormatter(
        deserializeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>? = null,
        serializeAdapterMap: HashMap<KClass<*>, SerializeAdapter<*>>? = null,
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
                Calendar::class to CalendarTypeAdapter(),

                HashMap::class to HashMapTypeAdapter(),
                Map::class to HashMapTypeAdapter()
//                L::class to HashMapTypeAdapter(),
//                HashMap::class to HashMapTypeAdapter()
        )

        val DEFAULT_DATE_FORMAT = "YYYY-MM-DD hh:mm:ss Z"

        internal fun getDeserializeAdapter(kClass: KClass<*>, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>): DeserializeAdapter<*>? {
            val typeAdapter = typeAdapterMap[kClass]
            if (typeAdapter == null && kClass.isSubclassOf(Enum::class)) {
                return typeAdapterMap[Enum::class]
            }
            return typeAdapter
        }

        internal fun getSerializeAdapter(kClass: KClass<*>, typeAdapterMap: HashMap<KClass<*>, SerializeAdapter<*>>): SerializeAdapter<*>? {
            if (kClass.isSubclassOf(Enum::class)) {
                return typeAdapterMap[Enum::class]
            } else {
                return typeAdapterMap[kClass]
            }
        }
    }

    val mDeserializeAdapterMap = hashMapOf<KClass<*>, DeserializeAdapter<*>>().apply {
        putAll(DEFAULT_TypeAdapterMap)
        deserializeAdapterMap?.let {
            putAll(it)
        }
    }

    val mSerializeAdapterMap = hashMapOf<KClass<*>, SerializeAdapter<*>>().apply {
        putAll(DEFAULT_TypeAdapterMap)
        serializeAdapterMap?.let {
            putAll(it)
        }
    }
    val mConfig: JsonParserConfig = config
    val mJsonDeserializer = JsonDeserializer()
    val mJsonSerializer = JsonSerializer()

    /**
     *
     * Function to serialize kotlin object to json string
     *
     * @param [obj] kotlin object in subclass of [Any] class type
     * @return [String] return json string after serializing
     */
    fun toJson(obj: Any): String {
        return mJsonSerializer.serialize(obj, mSerializeAdapterMap, mConfig)
    }

    /**
     * Function to deserialize json string to kotlin object
     *
     * @param [T] target kotlin object type
     * @param [json] json string to deserialize
     * @param [kClass] The kotlin class of the target kotlin type [T]
     *
     * @return [T] The instance of Kotlin object type [T] output
     */
    inline fun <reified T: Any> parseJson(json: String, kClass: KClass<T>): T? {
        val kType = object : TypeToken<T>(){}.getKTypeImpl()
        return mJsonDeserializer.parseJson(json, kType, kClass, mDeserializeAdapterMap, mConfig)
    }

    /**
     * Function to deserialize json string to kotlin object in collection
     *
     * @param [T] target kotlin object type
     * @param [json] json string to deserialize
     * @param [typeToken] The [TypeToken] object with kotlin class of the target kotlin type [T] member
     *
     * @return [Collection] The instance of Collection with member type of [T] output
     */
    inline fun <reified T: Any, reified C: Collection<T?>> parseJson(json: String, typeToken: TypeToken<C>): Collection<T?>? {
        return mJsonDeserializer.parseJson(json, typeToken, mDeserializeAdapterMap, mConfig)
    }

    /**
     * Function to deserialize json string to kotlin object in map
     *
     * @param [F] target map key of kotlin object type
     * @param [S] target map value of kotlin object type
     * @param [json] json string to deserialize
     * @param [typeToken] The [TypeToken] object with [Map] class contain [F] class as key, [S] class as value
     *
     * @return [Map] The instance of Map with [F] class as key, [S] class as value output
     */
    inline fun <reified F: Any, reified S: Any, reified C: Map<F, S?>> parseJson(json: String, typeToken: TypeToken<C>): Map<F, S?> {
        return mJsonDeserializer.parseJson(json, typeToken, mDeserializeAdapterMap, mConfig)
    }
}