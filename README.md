# Kotlin-JsonParser

Kotlin-JsonParser is a Kotlin library for JSON parsing. It can parse JSON string to Kotlin Object or reverse in a simple way.

## Getting Started


### Installation
To use Kotlin-JsonParser in gradle implemented project, 
```
dependencies {
	...
	implementation 'hinl:jsonparser:1.0'
	...
}
```

### Usage

For parsing JSON String to Kotlin Object, it can be done by one line comment.

```
	val kotlinObject = jsonString.parseJson(KotlinObject::class)
```

And it is also easy for parsing kotlin Object to JSON String.

```
	val jsonString = kotlinObject.toJson()
```

## Authors

* **Hin Lai** - *Initial work*
* **Wingy Ng** - *Initial work*

See also the list of [contributors]() who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
