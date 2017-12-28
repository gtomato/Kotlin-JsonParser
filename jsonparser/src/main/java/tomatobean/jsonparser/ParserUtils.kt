package tomatobean.jsonparser

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaField


@Target (AnnotationTarget.FIELD)
@Retention (AnnotationRetention.RUNTIME)
annotation class JsonFormat(val JsonName: String = "",
                            val Serializable: Boolean = true,
                            val Deserializable: Boolean = true)

/**
 * Extension function to serialize Kotlin object to json String with default config
 */
fun Any.toJson(): String {
    return JsonFormatter().toJson(this)
}

/**
 * Extension function to deserialize json String to Kotlin object with default config
 *
 * @param [kClass] The Kotlin class of target Kotlin type [T]
 *
 * @return [T] The instance of target Kotlin class output
 */
inline fun <reified T: Any> String.parseJson(kClass: KClass<T>): T? {
    return JsonFormatter().parseJson(this, kClass)
}

/**
 * Extension function to deserialize json String to Collection or Map of Kotlin object with default config
 *
 * @param [typeToken] The [TypeToken] instance with [Collection] [C] which have target Kotlin type [T] as member
 *
 * @return [T] The output instance of collection or map with target Kotlin class[T]
 */
inline fun <reified T: Any> String.parseJson(typeToken: TypeToken<T>): T? {
    return JsonFormatter().parseJson(this, typeToken)
}

/**
 * Internal function to get JsonName annotation @see[JsonFormat]
 */
internal fun KProperty1<*, *>.getJsonName(): String? {
    val schema = (this.javaField?.annotations?.find { it is JsonFormat } as? JsonFormat)
    if (schema != null && schema.JsonName.isNotEmpty()) {
        return schema.JsonName
    } else {
        return null
    }
}

/**
 * Internal function to get Deserializable annotation @see[JsonFormat]
 */
internal fun KProperty1<*, *>.isDeserializable(): Boolean {
    val schema = (this.javaField?.annotations?.find { it is JsonFormat } as? JsonFormat)
    if (schema != null) {
        return schema.Deserializable
    } else {
        return true
    }
}

/**
 * Internal function to get Serializable annotation @see[JsonFormat]
 */
internal fun KProperty1<*, *>.isSerializable(): Boolean {
    val schema = (this.javaField?.annotations?.find { it is JsonFormat } as? JsonFormat)
    if (schema != null) {
        return schema.Serializable
    } else {
        return true
    }
}
