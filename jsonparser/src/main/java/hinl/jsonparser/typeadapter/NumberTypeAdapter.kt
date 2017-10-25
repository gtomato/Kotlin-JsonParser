package hinl.jsonparser.typeadapter

import hinl.jsonparser.TypeAdapter
import org.json.JSONObject
import java.math.BigDecimal


class IntTypeAdapter: TypeAdapter<Int>() {

    override fun write(output: JSONObject, key: String, value: Int?): JSONObject {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(input: JSONObject, key: String): Int? {
        return input.getInt(key)
    }
}

class LongTypeAdapter: TypeAdapter<Long>() {

    override fun write(output: JSONObject, key: String, value: Long?): JSONObject {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(input: JSONObject, key: String): Long? {
        return input.getLong(key)
    }
}

class ShortTypeAdapter: TypeAdapter<Short>() {
    override fun write(output: JSONObject, key: String, value: Short?): JSONObject {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(input: JSONObject, key: String): Short? {
        return input.getInt(key).toShort()
    }
}

class DoubleTypeAdapter: TypeAdapter<Double>() {
    override fun write(output: JSONObject, key: String, value: Double?): JSONObject {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(input: JSONObject, key: String): Double? {
        return input.getDouble(key)
    }
}

class FloatTypeAdapter: TypeAdapter<Float>() {
    override fun write(output: JSONObject, key: String, value: Float?): JSONObject {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(input: JSONObject, key: String): Float? {
        return input.getDouble(key).toFloat()
    }
}

class BigDecimalTypeAdapter: TypeAdapter<BigDecimal>() {
    override fun write(output: JSONObject, key: String, value: BigDecimal?): JSONObject {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(input: JSONObject, key: String): BigDecimal? {
        return BigDecimal(input.get(key).toString())
    }
}