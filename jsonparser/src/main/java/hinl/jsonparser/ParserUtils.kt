package hinl.jsonparser

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField


@Target (AnnotationTarget.FIELD)
@Retention (AnnotationRetention.RUNTIME)
annotation class Schema(val JsonName: String = "",
                        val Serializable: Boolean = true,
                        val DeSerializable: Boolean = true)

fun Any.toJson(): String {
    return JsonFormatter().toJson(this)
}


fun <T: Any> String.parseJson(kClass: KClass<T>): T? {
    return JsonFormatter().parseJson(this, kClass)
}

inline fun <reified T: Any, reified C: Collection<T?>> String.parseJson(typeToken: TypeToken<C>): Collection<T?>? {
    return JsonFormatter().parseJson(this, typeToken)
}

inline fun <reified F: Any, reified S: Any, reified C: Map<F, S?>> String.parseJson(typeToken: TypeToken<C>): Map<F, S?> {
    return JsonFormatter().parseJson(this, typeToken)
}

internal fun KProperty1<*, *>.getJsonName(): String {
    val schema = (this.javaField?.annotations?.find { it is Schema } as? Schema)
    if (schema != null) {
        return schema.JsonName
    } else {
        return ""
    }
}

internal fun KProperty1<*, *>.isDeSerializable(): Boolean {
    val schema = (this.javaField?.annotations?.find { it is Schema } as? Schema)
    if (schema != null) {
        return schema.DeSerializable
    } else {
        return true
    }
}
