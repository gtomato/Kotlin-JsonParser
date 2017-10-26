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
        return input.getInt(key)
    }
}

class LongTypeAdapter: TypeAdapter<Long>() {
    override fun write(output: JsonWriter, value: Long?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Long? {
        return input.getLong(key)
    }
}

class ShortTypeAdapter: TypeAdapter<Short>() {
    override fun write(output: JsonWriter, value: Short?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Short? {
        return input.getInt(key).toShort()
    }
}

class DoubleTypeAdapter: TypeAdapter<Double>() {
    override fun write(output: JsonWriter, value: Double?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Double? {
        return input.getDouble(key)
    }
}

class FloatTypeAdapter: TypeAdapter<Float>() {
    override fun write(output: JsonWriter, value: Float?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): Float? {
        return input.getDouble(key).toFloat()
    }
}

class BigDecimalTypeAdapter: TypeAdapter<BigDecimal>() {
    override fun write(output: JsonWriter, value: BigDecimal?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(input: JSONObject, key: String, config: JsonParserConfig): BigDecimal? {
        return BigDecimal(input.get(key).toString())
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