# Kotlin-JsonParser

Kotlin-JsonParser is a Kotlin library for JSON parsing. It can parse JSON string to Kotlin Object or reverse in a simple way.

#Feature
> One line parsing
* you can parse json to object or object to json in one line code
> Annotation Configuration
* Support annotation for enabling/disabling features (e.g. serialization), see also [here](#annotation)
> flexible custom class serialize and deserialize
* Support TypeAdapter for custom deserialize and serialize process, see also [here](#typadapter)

## Getting Started


### Installation
To use Kotlin-JsonParser in gradle implemented project, 
```
dependencies {
	...
	implementation 'com.tomatobean:jsonparser:1.0.5'
	...
}
```

### Usage

For parsing JSON String to Kotlin Object, it can be done by one line comment.

```kotlin
	val kotlinObject = jsonString.parseJson(KotlinObject::class)
```

P.S. For Json parsing, all immutable params must be in the Primary Constructor of Kotlin Object

```kotlin
    data class KotlinObject(
        val paramOne: String,
        val paramTwo, Int,
        ....
    )
```
And it is also easy for parsing kotlin Object to JSON String.

```kotlin
	val jsonString = kotlinObject.toJson()
```


#### Annotation
Using Annotation for config Json parsing process

```kotlin
data class ClassA(@JsonFormat(JsonName = "jsonNameString")
                   val stringA: String,
                   @JsonFormat(JsonName = "nonDeserializableString", Deserializable = false)
                   val stringB: String = "stringB",
                   @JsonFormat(JsonName = "nonDeserializableString", Serializable = false)
                   val stringC: String = "stringC")
                                           
```

JsonFormat class have the following params can config
> JsonName
* the json paring name tag for serialize and deserialize, variable name will be use when it is not set
> Serializable
* the variable will not parse to json when it set to false (default is true)
> Deserializable
* the variable will not parse from json when it set to false (default is true)


#### TypAdapter
For custom deserialize process, DeserializeAdapter and SerializeAdapter interface can implement for custom process

DeserializeAdapter usage demonstration can be found in /app module 
```kotlin
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
```

and create JsonFormatter instance by the following way
```kotlin
    val jsonFormatter = JsonFormatter(deserializeAdapterMap = hashMapOf(
                AbstractObject::class to abstractObjectDeserializer
        ))
```


For more usage example, you can reference to /app module

#### TypeToken
KotlinJsonParser also support List, Map class for parsering

```kotlin
    val kotlinObjectList = json.parserJson(object: TypeToke<List<KotlinObject>>(){})
    val json = kotlinObjectList.toJson()
```

For Generic Abstract class for parsering

```kotlin
    abstract class GenericDataSource<DATA>: TypeToken<DATA> {
        fun parserJson(json: String) {
	    json.parserJson(this)
	}
    }
````

## Authors

* **[Hin Lai](https://github.com/hinls1007)** - *Initial work*
* **[Wingy Ng](https://github.com/wingy26)** - *Initial work*

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
