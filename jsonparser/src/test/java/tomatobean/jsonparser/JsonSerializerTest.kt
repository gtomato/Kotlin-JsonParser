package tomatobean.jsonparser

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.reflect.KClass

/**
 * Created by wingy26 on 13/12/2017.
 */
class JsonSerializerTest {

    private val mTypeAdapterMap = hashMapOf<KClass<*>, SerializeAdapter<*>>().apply {
        putAll(JsonFormatter.DEFAULT_TypeAdapterMap)
    }
    private val mConfig = JsonParserConfig()

    @Test
    fun serialize() {
        val serializer = JsonSerializer()
        val result = serializer.serialize(classAObj, mTypeAdapterMap, mConfig)
        assertEquals(classAJson, result)
    }

    //    @Test
//    fun serializeWithAnnotation() {
//        val serializer = JsonSerializer()
//        assertEquals(classAJson, serializer.serialize(classAObj, mTypeAdapterMap, mConfig))
//    }
    data class TestingClassWithAnnotationA(@JsonFormat(JsonName = "jsonNameString")
                                           val stringA: String,
                                           @JsonFormat(JsonName = "nonDeserializableString", Deserializable = false)
                                           val stringB: String = "stringB",
                                           @JsonFormat(JsonName = "InnerClassA")
                                           val listOfInnerClassA: List<TestingInnerClassWithAnnotationA>,
                                           val noAnnotationString: String,
                                           @JsonFormat(Deserializable = true)
                                           val deserializableTrueString: String,
                                           @JsonFormat(JsonName = "InnerClassANotDeserializable", Deserializable = false)
                                           val listOfInnerClassANotDeserialize: List<TestingInnerClassWithAnnotationA> = ArrayList())

    data class TestingInnerClassWithAnnotationA(@JsonFormat(JsonName = "InnerStringA")
                                                val stringA: String,
                                                @JsonFormat(JsonName = "nonDeserializableString", Deserializable = false)
                                                val stringB: String = "stringB",
                                                @JsonFormat(JsonName = "listOfString") val stringArr: ArrayList<String>)

    data class TestEmptyObject(@JsonFormat(JsonName = "nonDeserializableString", Serializable = false)
                               val stringB: String = "stringB")

    data class TestingClassB(val stringA: String,
                             val intA: Int)

    data class TestingClassA(val stringA: String,
                             val innerClassA: InnerClassA,
                             val nullableWithNull: Int?,
                             val nullableWithNotNull: Int?,
                             val nullableListWithNull: List<Int>?,
                             val nullableListWithNotNull: List<Int>?,
                             val emptyObject: TestEmptyObject,
                             val listOfInnerClassB: List<InnerClassB>)

    data class InnerClassA(val stringA: String,
                           val intA: Int,
                           val booleanA: Boolean,
                           val doubleA: Double)

    data class InnerClassB(val listOfString: List<String>,
                           val listOfInt: List<Int>,
                           val listOfBoolean: List<Boolean>,
                           val listOfInnerClassC: List<InnerClassC>)

    data class InnerClassC(val enumA: EnumA,
                           val listOfEnumA: List<EnumA>)

    enum class EnumA {
        EnumA,
        EnumB,
        EnumC,
    }

    private val classAJson = """{"emptyObject":{},"innerClassA":{"booleanA":true,"doubleA":0.017,"intA":10,"stringA":"Inner Class A String"},"listOfInnerClassB":[{"listOfBoolean":[true,true,true,false,true,true],"listOfInnerClassC":[{"enumA":"EnumC","listOfEnumA":["EnumC","EnumA","EnumB","EnumC","EnumA","EnumA","EnumB"]}],"listOfInt":[1,2,3,4,5,6,7],"listOfString":["Inner Class B String 1","Inner Class B String 2","Inner Class B String 3","Inner Class B String 4","Inner Class B String 5"]},{"listOfBoolean":[true,false,true,true],"listOfInnerClassC":[{"enumA":"EnumA","listOfEnumA":["EnumC","EnumA","EnumB","EnumC","EnumA","EnumA","EnumB","EnumC","EnumA"]},{"enumA":"EnumB","listOfEnumA":["EnumC","EnumA","EnumC","EnumA","EnumA","EnumB","EnumC","EnumA","EnumA","EnumB"]}],"listOfInt":[8,11,54,465456,1243463,7654,234453],"listOfString":["Inner Class B String 6","Inner Class B String 9","Inner Class B String 10"]}],"nullableListWithNotNull":[],"nullableListWithNull":null,"nullableWithNotNull":3,"nullableWithNull":null,"stringA":"Testing String"}"""
    private val classAObj = TestingClassA(
            "Testing String",
            InnerClassA(
                    "Inner Class A String",
                    10,
                    true,
                    0.017
            ),
            null,
            3,
            null,
            listOf(),
            TestEmptyObject(),
            arrayListOf(
                    InnerClassB(
                            arrayListOf(
                                    "Inner Class B String 1",
                                    "Inner Class B String 2",
                                    "Inner Class B String 3",
                                    "Inner Class B String 4",
                                    "Inner Class B String 5"
                            ),
                            arrayListOf(
                                    1,
                                    2,
                                    3,
                                    4,
                                    5,
                                    6,
                                    7
                            ),
                            arrayListOf(
                                    true,
                                    true,
                                    true,
                                    false,
                                    true,
                                    true
                            ),
                            arrayListOf(
                                    InnerClassC(
                                            EnumA.EnumC,
                                            arrayListOf(
                                                    EnumA.EnumC,
                                                    EnumA.EnumA,
                                                    EnumA.EnumB,
                                                    EnumA.EnumC,
                                                    EnumA.EnumA,
                                                    EnumA.EnumA,
                                                    EnumA.EnumB
                                            )
                                    )
                            )
                    ),
                    InnerClassB(
                            arrayListOf(
                                    "Inner Class B String 6",
                                    "Inner Class B String 9",
                                    "Inner Class B String 10"
                            ),
                            arrayListOf(
                                    8,
                                    11,
                                    54,
                                    465456,
                                    1243463,
                                    7654,
                                    234453
                            ),
                            arrayListOf(
                                    true,
                                    false,
                                    true,
                                    true
                            ),
                            arrayListOf(
                                    InnerClassC(
                                            EnumA.EnumA,
                                            arrayListOf(
                                                    EnumA.EnumC,
                                                    EnumA.EnumA,
                                                    EnumA.EnumB,
                                                    EnumA.EnumC,
                                                    EnumA.EnumA,
                                                    EnumA.EnumA,
                                                    EnumA.EnumB,
                                                    EnumA.EnumC,
                                                    EnumA.EnumA
                                            )
                                    ),
                                    InnerClassC(
                                            EnumA.EnumB,
                                            arrayListOf(
                                                    EnumA.EnumC,
                                                    EnumA.EnumA,
                                                    EnumA.EnumC,
                                                    EnumA.EnumA,
                                                    EnumA.EnumA,
                                                    EnumA.EnumB,
                                                    EnumA.EnumC,
                                                    EnumA.EnumA,
                                                    EnumA.EnumA,
                                                    EnumA.EnumB
                                            )
                                    )
                            )
                    )
            )
    )
}