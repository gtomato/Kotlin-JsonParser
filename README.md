# Kotlin-JsonParser

Kotlin-JsonParser is a Kotlin library for JSON parsing. It can parse JSON string to Kotlin Object or reverse in a simple way.

#Feature
> One line parsing
* you can parse json to object or object to json in one line code
> Annotation Configuration
* Annotation can be add for switching feature
> flexible custom class serialize and deserialize
* TypeAdapter can be add for custom deserialize and serialize process

## Getting Started


### Installation
To use Kotlin-JsonParser in gradle implemented project, 
```
dependencies {
	...
	implementation 'com.tomatobean:jsonparser:1.0.1'
	...
}
```

### Usage

For parsing JSON String to Kotlin Object, it can be done by one line comment.

```kotlin
	val kotlinObject = jsonString.parseJson(KotlinObject::class)
```

P.S. For Json parsing, all params must be in the Primary Constructor of Kotlin Object

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

## Authors

* **Hin Lai** - *Initial work*
* **Wingy Ng** - *Initial work*

See also the list of [contributors](CONTRIBUTORS.md) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
