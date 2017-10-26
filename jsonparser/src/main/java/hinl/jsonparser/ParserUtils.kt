package hinl.jsonparser

import org.json.JSONObject
import java.util.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField


@Target (AnnotationTarget.FIELD)
@Retention (AnnotationRetention.RUNTIME)
annotation class Schema(val JsonName: String = "",
                        val Serializable: Boolean = true,
                        val DeSerializable: Boolean = true)

fun Any.toJson(): String {
    return JsonSerializer().serialize(this, JsonFormatter.DEFAULT_TypeAdapterMap, JsonParserConfig())
}
