package tomatobean.jsonparser

import java.util.*

class JsonWriter {
    companion object {
        const val ARRAY_BEGIN = "["
        const val ARRAY_END = "]"
        const val OBJECT_BEGIN = "{"
        const val OBJECT_END = "}"
        const val SEPERATOR = ":"
        const val COMMA = ","
        const val NULL = "null"

    }

    private val stringBuilder = StringBuilder()
    private val currentStateStack by lazy {
        Stack<JsonState>().apply {
            add(JsonState.EMPTY)
        }
    }

    fun beginArray() {
        checkLastElement()
        currentStateStack.add(JsonState.EMPTY_ARRAY)
        stringBuilder.append(ARRAY_BEGIN)
    }

    fun nullValue() {
        checkLastElement()
        stringBuilder.append(NULL)
        close(JsonElementType.KEY_VALUE)
    }

    fun endArray() {
//        checkLastElement()
        close(JsonElementType.ARRAY)
    }

    fun beginObject() {
        checkLastElement()
        currentStateStack.add(JsonState.EMPTY_OBJECT)
        stringBuilder.append(OBJECT_BEGIN)
    }

    fun endObject() {
//        checkLastElement()
        close(JsonElementType.OBJECT)
    }

    fun name(name: String) {
        checkLastElement()
        currentStateStack.add(JsonState.KEY)
        stringBuilder.append("\"$name\"")
    }

    fun value(any: Any?): JsonWriter {
        if (any == null) {
            nullValue()
        } else {
            checkLastElement()
            when (any) {
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
                    stringBuilder.append("\"$any\"")
                }
            }
            close(JsonElementType.KEY_VALUE)
        }
        return this
    }

    private fun checkLastElement() {
        
        val lastElement = currentStateStack.last() ?: throw IllegalStateException("No element")
        when (lastElement) {
            JsonState.KEY -> {
                stringBuilder.append(SEPERATOR)
            }
            JsonState.FILLED_ARRAY -> {
                stringBuilder.append(COMMA)
            }
            JsonState.EMPTY_ARRAY -> {
                currentStateStack[currentStateStack.lastIndex] = JsonState.FILLED_ARRAY
            }
            JsonState.FILLED_OBJECT -> {
                stringBuilder.append(COMMA)
            }
            JsonState.EMPTY_OBJECT -> {
                currentStateStack[currentStateStack.lastIndex] = JsonState.FILLED_OBJECT
            }
            JsonState.EMPTY -> {
                currentStateStack[currentStateStack.lastIndex] = JsonState.ENDED
            }
            JsonState.ENDED -> {
                throw IllegalStateException("The json has ended. No other element can add")
            }
        }
    }

    private fun close(type: JsonElementType) {
        
        when (type) {
            JsonElementType.KEY_VALUE -> {
                val lastElement = currentStateStack.last() ?: throw IllegalStateException("No element")
                if (lastElement == JsonState.KEY) {
                    currentStateStack.removeAt(currentStateStack.lastIndex)
                }
            }
            JsonElementType.ARRAY -> {
                val lastElement = currentStateStack.last() ?: throw IllegalStateException("No element")
                if (lastElement in arrayOf(JsonState.EMPTY_ARRAY, JsonState.FILLED_ARRAY)) {
                    currentStateStack.removeAt(currentStateStack.lastIndex)
                }
                close(JsonElementType.KEY_VALUE)
                stringBuilder.append(ARRAY_END)
            }
            JsonElementType.OBJECT -> {
                val lastElement = currentStateStack.last() ?: throw IllegalStateException("No element")
                if (lastElement in arrayOf(JsonState.EMPTY_OBJECT, JsonState.FILLED_OBJECT)) {
                    currentStateStack.removeAt(currentStateStack.lastIndex)
                }
                close(JsonElementType.KEY_VALUE)
                stringBuilder.append(OBJECT_END)
            }
        }
    }

    override fun toString(): String {
        return stringBuilder.toString()
    }
}

private enum class JsonState {
    KEY,
    FILLED_ARRAY,
    EMPTY_ARRAY,
    FILLED_OBJECT,
    EMPTY_OBJECT,
    EMPTY,
    ENDED
}
private enum class JsonElementType {
    KEY_VALUE,
    ARRAY,
    OBJECT,
}
