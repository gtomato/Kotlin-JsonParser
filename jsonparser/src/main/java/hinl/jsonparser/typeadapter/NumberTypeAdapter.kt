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

    override fun read(json: String, config: JsonParserConfig): Int? {
        return json.toInt()
    }
}

class LongTypeAdapter: TypeAdapter<Long>() {
    override fun write(output: JsonWriter, value: Long?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(json: String, config: JsonParserConfig): Long? {
        return json.toLong()
    }
}

class ShortTypeAdapter: TypeAdapter<Short>() {
    override fun write(output: JsonWriter, value: Short?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(json: String, config: JsonParserConfig): Short? {
        return json.toShort()
    }
}

class DoubleTypeAdapter: TypeAdapter<Double>() {
    override fun write(output: JsonWriter, value: Double?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(json: String, config: JsonParserConfig): Double? {
        return json.toDouble()
    }
}

class FloatTypeAdapter: TypeAdapter<Float>() {
    override fun write(output: JsonWriter, value: Float?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(json: String, config: JsonParserConfig): Float? {
        return json.toFloat()
    }
}

class BigDecimalTypeAdapter: TypeAdapter<BigDecimal>() {
    override fun write(output: JsonWriter, value: BigDecimal?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(json: String, config: JsonParserConfig): BigDecimal? {
        return BigDecimal(json)
    }
}

class BigIntegerTypeAdapter: TypeAdapter<BigInteger>() {

    override fun write(output: JsonWriter, value: BigInteger?, config: JsonParserConfig): JsonWriter {
        return output.value(value)
    }

    override fun read(json: String, config: JsonParserConfig): BigInteger? {
        return BigInteger(json)
    }
}