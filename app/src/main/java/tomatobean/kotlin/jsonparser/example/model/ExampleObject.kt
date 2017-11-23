package tomatobean.kotlin.jsonparser.example.model

import android.content.Context
import java.io.IOException


data class ExampleClassA(val stringA: String,
                         val innerClassA: InnerClassA,
                         val listOfInnerClassB: List<InnerClassB>)
data class InnerClassA(val stringA: String,
                       val intA: Int,
                       val booleanA: Boolean,
                       val doubleA: Double)
data class InnerClassB(val listOfString: List<String>,
                       val listOfInt: List<Int>,
                       val listOfBoolean: List<Boolean>,
                       val listOfInnerClassC: List<InnerClassC>)
data class InnerClassC(val enumA: EnumA,
                       val listOfEnumA: List<EnumA>)
enum class EnumA {
    EnumA,
    EnumB,
    EnumC,
}


fun getAssetsJson(context: Context, filePath: String): String? {
    val json: String

    try {
        val inputStream = context.assets.open(filePath)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer)
    } catch (e: IOException) {
        return null
    }
    return json
}