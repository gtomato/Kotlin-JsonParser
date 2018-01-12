package tomatobean.jsonparser

import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure


class TypeNotSupportException private constructor(rawType: String?) :IllegalArgumentException("Parser Not Support This Type $rawType currently") {
    constructor(kClass: KClass<*>) : this(kClass.simpleName)
    constructor(kType: KType): this(kType.jvmErasure.simpleName)
    constructor(type: Type): this(type.toString())
}

class MissingParamException : IllegalArgumentException {

    constructor(kClass: KClass<*>, jsonKey: String): this(objectClass = kClass.simpleName, jsonKey = jsonKey)
    constructor(objectClass: String?, jsonKey: String) : super("Object Class: $objectClass\n Object in key: $jsonKey is null while variable defined is a non-nullable object")

    constructor(kClass: KClass<*>, jsonKeyList: ArrayList<String?>) : this(objectClass = kClass.simpleName, jsonKeyList = jsonKeyList)
    constructor(objectClass: String?, jsonKeyList: ArrayList<String?>) : super(getMissingListMsg(objectClass, jsonKeyList))
    constructor(kType: KType, index: Int): this(kType.jvmErasure, index)
    constructor(kClass: KClass<*>, index: Int) : super("Object ${kClass.simpleName} in index: $index is null while variable defined is a non-nullable object")

    companion object {
        private fun getMissingListMsg(objectClass: String?, jsonKeyList: ArrayList<String?>): String {
            val stringBuilder = StringBuilder("Object Class: $objectClass\n")
            jsonKeyList.forEach {
                stringBuilder.append("Param : $it\n")
            }
            stringBuilder.append("is(are) missing, please check json String and/or Object Structure.")
            return stringBuilder.toString()
        }
    }
}