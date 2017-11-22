package hinl.kotlin.jsonparser

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import hinl.jsonparser.*
import hinl.kotlin.jsonparser.example.model.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        abstractObjectParsingExample()
        simpleParsingExample()
    }


    private fun simpleParsingExample() {
        val json = getAssetsJson(this, "example/exampleJson.json")
        var time = System.currentTimeMillis()
        Log.d("JsonParser", "Start time: " + time.toString())
        val objectA = json?.parseJson(ExampleClassA::class)
        Log.d("JsonParser", (System.currentTimeMillis() - time).toString() + "ms, Deserialize Object A: " + objectA)
        time = System.currentTimeMillis()
        val serializeJson = objectA?.toJson()
        Log.d("JsonParser", (System.currentTimeMillis() - time).toString() + "ms, Serialize Object A json: " + serializeJson)
        time = System.currentTimeMillis()
        val deserializeObjectA = serializeJson?.parseJson(ExampleClassA::class)
        Log.d("JsonParser", (System.currentTimeMillis() - time).toString() + "ms, Deserialize Object A by serialize Json: " + deserializeObjectA)

    }

    private fun abstractObjectParsingExample() {
        val json = getAssetsJson(this, "example/abstractObjectExampleJson.json")
        var time = System.currentTimeMillis()
        val jsonFormatter = JsonFormatter(deserializeAdapterMap = hashMapOf(
                AbstractObject::class to abstractObjectDeserializer
        ))
        Log.d("JsonParser", "Start time: " + time.toString())
        if (json != null && json.isNotEmpty()) {
            val objectA = jsonFormatter.parseJson(json, AbstractObjectDemo::class)
            Log.d("JsonParser", (System.currentTimeMillis() - time).toString() + "ms, Deserialize Object A: " + objectA)
            time = System.currentTimeMillis()
            val serializeJson = objectA?.toJson()
            Log.d("JsonParser", (System.currentTimeMillis() - time).toString() + "ms, Serialize Object A json: " + serializeJson)
            time = System.currentTimeMillis()
            val deserializeObjectA = jsonFormatter.parseJson(serializeJson!!, AbstractObjectDemo::class)
            Log.d("JsonParser", (System.currentTimeMillis() - time).toString() + "ms, Deserialize Object A by serialize Json: " + deserializeObjectA)
        }
    }

    val abstractObjectDeserializer: DeserializeAdapter<AbstractObject> = object: DeserializeAdapter<AbstractObject> {

        override fun read(json: String, config: JsonParserConfig): AbstractObject? {
            val jsonObj = JSONObject(json)
            if (jsonObj.has("tag") && jsonObj.getString("tag").isNotEmpty()) {
                val classType = jsonObj.getString("tag")
                return switchJson(classType, json)
            }
            return null
        }

        private fun switchJson(classType: String, jsonString: String): AbstractObject? =
                when (classType) {
                    "classA" -> {
                        jsonString.parseJson(ChildClassA::class)
                    }
                    "classB" -> {
                        jsonString.parseJson(ChildClassB::class)
                    }
                    "classC" -> {
                        jsonString.parseJson(ChildClassC::class)
                    }
                    else -> null
                }
    }
}
