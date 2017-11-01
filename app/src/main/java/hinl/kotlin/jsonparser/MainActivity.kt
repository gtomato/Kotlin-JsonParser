package hinl.kotlin.jsonparser

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import hinl.jsonparser.parseJson
import hinl.jsonparser.toJson
import hinl.kotlin.jsonparser.testing.model.ObjectA
import hinl.kotlin.jsonparser.testing.model.getAssetsJson

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val json = getAssetsJson(this, "testing/testingJson.json")
        val objectA = json?.parseJson(ObjectA::class)
        Log.d("JsonParser", "Deserialize Object A: " + objectA)
        val serializeJson = objectA?.toJson()
        Log.d("JsonParser", "Serialize Object A json: " + serializeJson)
        val deserializeObjectA = serializeJson?.parseJson(ObjectA::class)
        Log.d("JsonParser", "Deserialize Object A by serialize Json: " + deserializeObjectA)

    }
}
