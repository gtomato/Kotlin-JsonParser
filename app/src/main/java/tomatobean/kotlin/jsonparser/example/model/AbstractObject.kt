package tomatobean.kotlin.jsonparser.example.model

import tomatobean.jsonparser.JsonFormat


abstract class AbstractObject {
    abstract fun getName(): String
}

data class ChildClassA(
        val tag: String,
        @JsonFormat(JsonName = "ClassA")
        val nameA: String,
        val booleanA: Boolean
): AbstractObject() {
    override fun getName(): String = nameA
}

data class ChildClassB(
        val tag: String,
        val bName: String,
        val integerB: Int
): AbstractObject() {
    override fun getName(): String = bName
}

data class ChildClassC(
        val tag: String,
        val objectC: String,
        val listOfString: List<String>
): AbstractObject() {
    override fun getName(): String = objectC
}

data class AbstractObjectDemo(
        val listOfAbstractObject: List<AbstractObject>,
        val id: Int
)