package tomatobean.jsonparser


class JsonWriter {
    companion object {
        val ARRAY_BEGIN = "["
        val ARRAY_END = "],"
        val OBJECT_BEGIN = "{"
        val OBJECT_END = "},"
        val SEPERATOR = ":"
        val NULL = "null"
    }
    private val stringBuilder = StringBuilder()
    fun beginArray(){
        stringBuilder.append(ARRAY_BEGIN)
    }
    fun nullValue(){
        stringBuilder.append(NULL)
    }
    fun endArray(){
        stringBuilder.deleteCharAt(stringBuilder.lastIndex)
        stringBuilder.append(ARRAY_END)
    }
    fun beginObject(){
        stringBuilder.append(OBJECT_BEGIN)
    }
    fun endObject(){
        stringBuilder.deleteCharAt(stringBuilder.lastIndex)
        stringBuilder.append(OBJECT_END)
    }
    fun name(name: String) {
        stringBuilder.append("\"$name\"$SEPERATOR")
    }
    fun value(any: Any?): JsonWriter {
        when (any) {
            null -> {
                nullValue()
            }
            is Number -> {
                stringBuilder.append(any.toString())
            }
            is CharSequence -> {
                stringBuilder.append("\"$any\"")
            }
            is Boolean -> {
                stringBuilder.append(any.toString())
            }
            else -> {
                stringBuilder.append("\"${any.toString()}\"")
            }
        }
        stringBuilder.append(",")
        return this
    }

    override fun toString(): String {
        stringBuilder.deleteCharAt(stringBuilder.lastIndex)
        return stringBuilder.toString()
    }
}