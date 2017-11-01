package hinl.kotlin.jsonparser.testing.model

import android.content.Context
import android.util.Log
import java.io.IOException
import java.util.logging.Logger


data class ObjectA(val listOfObjectB: List<ObjectB>,
                   val objectC: ObjectC)


data class ObjectB(val arrayListOfString: ArrayList<String>,
                   val arrayListOfInt: ArrayList<Int>,
                   val boolean: Boolean? = null,
                   val string: String = "default String",
                   val int: Int = -1)

data class ObjectC(val enumA: EnumA = ObjectC.EnumA.B,
                   val listOfEnumA: List<EnumA>) {
    enum class EnumA {
        A,
        B,
        C
    }
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
        Log.e("Assets", "File Error", e)
        return null
    }
    return json
}