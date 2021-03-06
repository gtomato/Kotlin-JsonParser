package tomatobean.jsonparser

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

/**
 * Created by wingy26 on 13/12/2017.
 */
class JsonSerializerTest {

    private val mTypeAdapterMap = hashMapOf<KClass<*>, SerializeAdapter<*>>().apply {
        putAll(JsonFormatter.DEFAULT_Serialize_TypeAdapterMap)
    }
    private val mConfig = JsonParserConfig()

    @Test
    fun serializeStringWithReturn() {

    }

    @Test
    fun serializeStringWithReturnAnd() {

    }

    @Test
    fun serializeDoubleQuoteString() {
        val serializer = JsonSerializer()
        val result = serializer.serialize(doubleQuoteStringObject, mTypeAdapterMap, mConfig)
        assertEquals(doubleQuoteStringJson, result)
    }

    @Test
    fun serialize() {
        val serializer = JsonSerializer()
        val result = serializer.serialize(classAObj, mTypeAdapterMap, mConfig)
        assertEquals(classAJson, result)
    }

    @Test
    fun serializeArray() {
        val array = arrayOf("Hall", "Good", "Thank You")

        val serializer = JsonSerializer()
        val result = serializer.serialize(array, mTypeAdapterMap, mConfig)
        assertEquals("""["Hall","Good","Thank You"]""", result)
    }

    @Test
    fun serializeArrayOfObject() {
        val array = arrayOf(
                InnerClassA(
                        "a",
                        1,
                        true,
                        3.7
                ),
                InnerClassA(
                        "b",
                        755,
                        false,
                        6.9678
                ),
                InnerClassA(
                        "234546",
                        124321,
                        false,
                        645.7643
                )
        )

        val serializer = JsonSerializer()
        val result = serializer.serialize(array, mTypeAdapterMap, mConfig)
        assertEquals("""[{"booleanA":true,"doubleA":3.7,"intA":1,"stringA":"a"},{"booleanA":false,"doubleA":6.9678,"intA":755,"stringA":"b"},{"booleanA":false,"doubleA":645.7643,"intA":124321,"stringA":"234546"}]""", result)
    }

    @Test
    fun serializeList() {
        val list = listOf("Hall", "Good", "Thank You")

        val serializer = JsonSerializer()
        val result = serializer.serialize(list, mTypeAdapterMap, mConfig)
        assertEquals("""["Hall","Good","Thank You"]""", result)
    }

    @Test
    fun serializeMap() {
        val map = mapOf("First" to "fi", "Second" to "se", "Third" to "th", "Forth" to "fo")

        val serializer = JsonSerializer()
        val result = serializer.serialize(map, mTypeAdapterMap, mConfig)
        assertEquals("""{"First":"fi","Second":"se","Third":"th","Forth":"fo"}""", result)
    }

    @Test
    fun serializeMapOfMap() {
        val mapOfMap : Map<String, Map<String, String>> = hashMapOf("first" to hashMapOf("second" to "Hello", "third" to "Nooo"))

        val serializer = JsonSerializer()
        val result = serializer.serialize(mapOfMap, mTypeAdapterMap, mConfig)
        assertEquals("""{"first":{"third":"Nooo","second":"Hello"}}""", result)
    }

    @Test
    fun serializeMapOfMapDataClass() {
        val mapOfMap : Map<String, Map<String, String>> = hashMapOf("first" to hashMapOf("second" to "Hello", "third" to "Nooo"))
        val mapOfMapDataClass = MapOfMap(mapOfMap)

        val serializer = JsonSerializer()
        val result = serializer.serialize(mapOfMapDataClass, mTypeAdapterMap, mConfig)
        assertEquals("""{"map":{"first":{"third":"Nooo","second":"Hello"}}}""", result)
    }

    @Test
    fun serializeListOfMapOfMap() {
        val mapOfMap : Map<String, Map<String, String>> = hashMapOf("first" to hashMapOf("second" to "Hello", "third" to "Nooo"))
        val listOfMapOfMap = listOf(mapOfMap)

        val serializer = JsonSerializer()
        val result = serializer.serialize(listOfMapOfMap, mTypeAdapterMap, mConfig)
        assertEquals("""[{"first":{"third":"Nooo","second":"Hello"}}]""", result)
    }

    @Test
    fun serializeListOfMapOfMapInDataClass() {
        val mapOfMap : Map<String, Map<String, String>> = hashMapOf("first" to hashMapOf("second" to "Hello", "third" to "Nooo"))
        val listOfMapOfMap = listOf(mapOfMap)
        val listOfMapOfMapInDataClass = ListOfMapOfMapData(listOfMapOfMap)

        val serializer = JsonSerializer()
        val result = serializer.serialize(listOfMapOfMapInDataClass, mTypeAdapterMap, mConfig)
        assertEquals("""{"list":[{"first":{"third":"Nooo","second":"Hello"}}]}""", result)
    }

    @Test
    fun serializeListOfMapOfMapData() {
        val mapOfMap : Map<String, Map<String, String>> = hashMapOf("first" to hashMapOf("second" to "Hello", "third" to "Nooo"))
        val mapOfMapDataClass = MapOfMap(mapOfMap)
        val listOfMapOfMap = listOf(mapOfMapDataClass)

        val serializer = JsonSerializer()
        val result = serializer.serialize(listOfMapOfMap, mTypeAdapterMap, mConfig)
        assertEquals("""[{"map":{"first":{"third":"Nooo","second":"Hello"}}}]""", result)
    }
    val differentTypeJson = """
        {"bigDecimal":98482132138.13887873,"bigInteger":231221121,"boolean":true,"calendar":"1986-10-07 18:59:43 +0800","char":"Z","charSequence":"CharSequenceCharSequence","date":"1988-09-02 10:20:56 +0800","double":2.0222,"enum":"EnumB","float":97004.02,"int":200000,"long":909090090,"short":20,"string":"String String String String"}
    """.trimIndent()

    val differentTypeObj = JsonDeserializerTest.DifferentTypeClass(
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse("1988-09-02 10:20:56 +0800"),
            Calendar.getInstance(TimeZone.getTimeZone("Asia/Hong_Kong")).apply {
                set(Calendar.YEAR, 1986)
                set(Calendar.MONTH, 9)
                set(Calendar.DATE, 7)
                set(Calendar.HOUR_OF_DAY, 18)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 43)
                set(Calendar.MILLISECOND, 0)
                setTime(this.getTime())
            },
            JsonDeserializerTest.DifferentTypeEnum.EnumB,
            200000,
            909090090,
            20,
            2.0222,
            97004.02f,
            BigDecimal("98482132138.13887873"),
            BigInteger.valueOf(231221121),
            "CharSequenceCharSequence",
            "String String String String",
            'Z',
            true
    )
    @Test
    fun serializeDifferentTypeObject() {
        assertEquals(differentTypeJson, JsonSerializer().serialize(differentTypeObj, mTypeAdapterMap, mConfig))
    }

    @Test
    fun serializeListOfMapOfMapDataInDataClass() {
        val mapOfMap : Map<String, Map<String, String>> = hashMapOf("first" to hashMapOf("second" to "Hello", "third" to "Nooo"))
        val mapOfMapDataClass = MapOfMap(mapOfMap)
        val listOfMapOfMap = listOf(mapOfMapDataClass)
        val listOfMapOfMapInDataClass = ListOfMapOfMapDataDataClass(listOfMapOfMap)

        val serializer = JsonSerializer()
        val result = serializer.serialize(listOfMapOfMapInDataClass, mTypeAdapterMap, mConfig)
        assertEquals("""{"list":[{"map":{"first":{"third":"Nooo","second":"Hello"}}}]}""", result)
    }

    data class MapOfMap(
            val map: Map<String, Map<String, String>>
    )

    data class ListOfMapOfMapData(
            val list: List<Map<String, Map<String, String>>>
    )

    data class ListOfMapOfMapDataDataClass(
            val list: List<MapOfMap>
    )

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

    private val doubleQuoteStringJson = """{"stringA":"\"Testing String"}"""
    private data class DoubleQuoteString(val stringA: String)
    private val doubleQuoteStringObject = DoubleQuoteString("\"Testing String")
}