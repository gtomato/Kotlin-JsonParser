package hinl.jsonparser.typeadapter

import android.util.JsonWriter
import hinl.jsonparser.JsonParserConfig
import hinl.jsonparser.TypeAdapter
import org.json.JSONObject
import java.math.BigDecimal
import java.math.BigInteger


class IntTypeAdapter: TypeAdapter<Int>() {
    override fun write(output: JsonWriter, value: Int?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Int? {
        if (!input.has(key)) {
            return null
        }
        return input.getInt(key)
    }

    override fun read(json: String): Int? {
        return json.toInt()
    }
}

class LongTypeAdapter: TypeAdapter<Long>() {
    override fun write(output: JsonWriter, value: Long?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Long? {
        if (!input.has(key)) {
            return null
        }
        return input.getLong(key)
    }

    override fun read(json: String): Long? {
        return json.toLong()
    }
}

class ShortTypeAdapter: TypeAdapter<Short>() {
    override fun write(output: JsonWriter, value: Short?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Short? {
        if (!input.has(key)) {
            return null
        }
        return input.getInt(key).toShort()
    }

    override fun read(json: String): Short? {
        return json.toShort()
    }
}

class DoubleTypeAdapter: TypeAdapter<Double>() {
    override fun write(output: JsonWriter, value: Double?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Double? {
        if (!input.has(key)) {
            return null
        }
        return input.getDouble(key)
    }

    override fun read(json: String): Double? {
        return json.toDouble()
    }
}

class FloatTypeAdapter: TypeAdapter<Float>() {
    override fun write(output: JsonWriter, value: Float?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Float? {
        if (!input.has(key)) {
            return null
        }
        return input.getDouble(key).toFloat()
    }

    override fun read(json: String): Float? {
        return json.toFloat()
    }
}

class BigDecimalTypeAdapter: TypeAdapter<BigDecimal>() {
    override fun write(output: JsonWriter, value: BigDecimal?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): BigDecimal? {
        if (!input.has(key)) {
            return null
        }
        return BigDecimal(input.get(key).toString())
    }

    override fun read(json: String): BigDecimal? {
        return BigDecimal(json)
    }
}

class BigIntegerTypeAdapter: TypeAdapter<BigInteger>() {
    override fun write(output: JsonWriter, value: BigInteger?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): BigInteger? {
        return BigInteger(input.get(key).toString())
    }
}