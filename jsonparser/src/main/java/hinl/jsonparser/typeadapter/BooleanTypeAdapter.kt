package hinl.jsonparser.typeadapter

import hinl.jsonparser.TypeAdapter
import org.json.JSONObject


class BooleanTypeAdapter: TypeAdapter<Boolean>(){
    override fun write(output: JSONObject, key: String, value: Boolean?): JSONObject {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(input: JSONObject, key: String): Boolean? {
        return input.getBoolean(key)
    }
}