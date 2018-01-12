package tomatobean.jsonparser

import tomatobean.jsonparser.typeadapter.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf


class JsonFormatter(
        deserializeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>? = null,
        serializeAdapterMap: HashMap<KClass<*>, SerializeAdapter<*>>? = null,
        config: JsonParserConfig = JsonParserConfig()) {

    companion object {
        val DEFAULT_Serialize_TypeAdapterMap = hashMapOf<KClass<*>, TypeAdapter<*>>(
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

        val DEFAULT_Deserialize_TypeAdapterMap = hashMapOf<KClass<*>, TypeAdapter<*>>(
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
                Map::class to HashMapTypeAdapter(),
                Collection::class to CollectionTypeAdapter(),
                Array<Any>::class to ArrayTypeAdapter()
        )

        val DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"

        internal fun getDeserializeAdapter(kClass: KClass<*>, typeAdapterMap: HashMap<KClass<*>, DeserializeAdapter<*>>): DeserializeAdapter<*>? {
            return typeAdapterMap[kClass] ?: return when {
                kClass.isSubclassOf(Enum::class) -> {
                    typeAdapterMap[Enum::class]
                }
                kClass.isSubclassOf(Collection::class) -> {
                    typeAdapterMap[Collection::class]
                }
                kClass.isSubclassOf(Map::class)-> {
                    typeAdapterMap[Map::class]
                }
                kClass.java.isArray -> {
                    typeAdapterMap[Array<Any>::class]
                }
                else -> {
                    null
                }
            }
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
        putAll(DEFAULT_Deserialize_TypeAdapterMap)
        deserializeAdapterMap?.let {
            putAll(it)
        }
    }

    val mSerializeAdapterMap = hashMapOf<KClass<*>, SerializeAdapter<*>>().apply {
        putAll(DEFAULT_Serialize_TypeAdapterMap)
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
     * Function to deserialize json string to kotlin object in collection or map
     *
     * @param [T] target kotlin object type
     * @param [json] json string to deserialize
     * @param [typeToken] The [TypeToken] object with kotlin class of the target kotlin type [T] member
     *
     * @return [T] The instance of Collection or Map output
     */
    fun <T: Any> parseJson(json: String, typeToken: TypeToken<T>): T? {
        return mJsonDeserializer.parseJson(json, typeToken, mDeserializeAdapterMap, mConfig)
    }
}