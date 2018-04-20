package tomatobean.jsonparser.retrofit

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import tomatobean.jsonparser.JsonFormatter
import tomatobean.jsonparser.toKType
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure


class JsonConverterFactory(val formatter: JsonFormatter = JsonFormatter()) : Converter.Factory() {
    override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        val kType = type?.toKType()
        return kType?.let { nonNullKType ->
            val kClass = kType.jvmErasure
            JsonResponseBodyConverter(nonNullKType, kClass, formatter)
        }

    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        val kClass = type?.toKType()?.jvmErasure
        return kClass?.let {
            JsonRequestBodyConverter(it, formatter)
        }
    }
}

private class JsonRequestBodyConverter<T : Any>(val kClass: KClass<T>, val formatter: JsonFormatter) : Converter<T, RequestBody> {

    val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")

    override fun convert(value: T?): RequestBody {
        val json = value?.let { formatter.toJson(it) } ?: ""
        return RequestBody.create(MEDIA_TYPE, json.toByteArray())
    }
}

private class JsonResponseBodyConverter<T : Any>(val kType: KType, val kClass: KClass<T>, val formatter: JsonFormatter) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody?): T? {
        return value?.string()?.let {
            formatter.mJsonDeserializer.parseJson(it, kType, formatter.mDeserializeAdapterMap, formatter.mConfig) as T?
        }
    }
}