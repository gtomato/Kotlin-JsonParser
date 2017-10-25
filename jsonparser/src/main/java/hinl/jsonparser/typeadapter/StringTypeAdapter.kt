package hinl.jsonparser.typeadapter

import hinl.jsonparser.TypeAdapter
import org.json.JSONObject


class CharSequenceTypeAdapter: TypeAdapter<CharSequence>() {
    override fun write(output: JSONObject, key: String, value: CharSequence?): JSONObject {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(input: JSONObject, key: String): CharSequence? {
        return input.getString(key)
    }
}

class StringTypeAdapter: TypeAdapter<String>(){
    override fun write(output: JSONObject, key: String, value: String?): JSONObject {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(input: JSONObject, key: String): String? {
        return input.getString(key)
    }
}

class CharTypeAdapter: TypeAdapter<Char>(){
    override fun write(output: JSONObject, key: String, value: Char?): JSONObject {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(input: JSONObject, key: String): Char? {
        return input.getString(key).single()
    }
}