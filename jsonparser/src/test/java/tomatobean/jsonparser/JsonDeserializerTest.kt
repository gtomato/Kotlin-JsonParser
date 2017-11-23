package tomatobean.jsonparser

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass

class JsonDeserializerTest {

    internal lateinit var jsonDeserializer: JsonDeserializer
    val mTypeAdapterMap = linkedMapOf<KClass<*>, DeserializeAdapter<*>>().apply {
        putAll(JsonFormatter.DEFAULT_TypeAdapterMap)
    }
    val mConfig = JsonParserConfig()

    var mTestingString = "Testing String A"


    val classAJson = """{
  "stringA": "Testing String",
  "innerClassA": {
    "stringA": "Inner Class A String",
    "intA": 10,
    "booleanA": true,
    "doubleA": 0.017
  },
  "listOfInnerClassB": [
    {
      "listOfString": [
        "Inner Class B String 1",
        "Inner Class B String 2",
        "Inner Class B String 3",
        "Inner Class B String 4",
        "Inner Class B String 5"
      ],
      "listOfInt": [
        1,
        2,
        3,
        4,
        5,
        6,
        7
      ],
      "listOfBoolean": [
        true,
        true,
        true,
        false,
        true,
        true
      ],
      "listOfInnerClassC": [
        {
          "enumA": "EnumC",
          "listOfEnumA": [
            "EnumC",
            "EnumA",
            "EnumB",
            "EnumC",
            "EnumA",
            "EnumA",
            "EnumB"
          ]
        }
      ]
    },
    {
      "listOfString": [
        "Inner Class B String 6",
        "Inner Class B String 9",
        "Inner Class B String 10"
      ],
      "listOfInt": [
        8,
        11,
        54,
        465456,
        1243463,
        7654,
        234453
      ],
      "listOfBoolean": [
        true,
        false,
        true,
        true
      ],
      "listOfInnerClassC": [
        {
          "enumA": "EnumA",
          "listOfEnumA": [
            "EnumC",
            "EnumA",
            "EnumB",
            "EnumC",
            "EnumA",
            "EnumA",
            "EnumB",
            "EnumC",
            "EnumA"
          ]
        },
        {
          "enumA": "EnumB",
          "listOfEnumA": [
            "EnumC",
            "EnumA",
            "EnumC",
            "EnumA",
            "EnumA",
            "EnumB",
            "EnumC",
            "EnumA",
            "EnumA",
            "EnumB"
          ]
        }
      ]
    }
  ]
}"""
    val classAObj = JsonDeserializerTest.TestingClassA(
            "Testing String",
            JsonDeserializerTest.InnerClassA(
                    "Inner Class A String",
                    10,
                    true,
                    0.017
            ),
            arrayListOf(
                    JsonDeserializerTest.InnerClassB(
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
                                    JsonDeserializerTest.InnerClassC(
                                            JsonDeserializerTest.EnumA.EnumC,
                                            arrayListOf(
                                                    JsonDeserializerTest.EnumA.EnumC,
                                                    JsonDeserializerTest.EnumA.EnumA,
                                                    JsonDeserializerTest.EnumA.EnumB,
                                                    JsonDeserializerTest.EnumA.EnumC,
                                                    JsonDeserializerTest.EnumA.EnumA,
                                                    JsonDeserializerTest.EnumA.EnumA,
                                                    JsonDeserializerTest.EnumA.EnumB
                                            )
                                    )
                            )
                    ),
                    JsonDeserializerTest.InnerClassB(
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
                                    JsonDeserializerTest.InnerClassC(
                                            JsonDeserializerTest.EnumA.EnumA,
                                            arrayListOf(
                                                    JsonDeserializerTest.EnumA.EnumC,
                                                    JsonDeserializerTest.EnumA.EnumA,
                                                    JsonDeserializerTest.EnumA.EnumB,
                                                    JsonDeserializerTest.EnumA.EnumC,
                                                    JsonDeserializerTest.EnumA.EnumA,
                                                    JsonDeserializerTest.EnumA.EnumA,
                                                    JsonDeserializerTest.EnumA.EnumB,
                                                    JsonDeserializerTest.EnumA.EnumC,
                                                    JsonDeserializerTest.EnumA.EnumA
                                            )
                                    ),
                                    JsonDeserializerTest.InnerClassC(
                                            JsonDeserializerTest.EnumA.EnumB,
                                            arrayListOf(
                                                    JsonDeserializerTest.EnumA.EnumC,
                                                    JsonDeserializerTest.EnumA.EnumA,
                                                    JsonDeserializerTest.EnumA.EnumC,
                                                    JsonDeserializerTest.EnumA.EnumA,
                                                    JsonDeserializerTest.EnumA.EnumA,
                                                    JsonDeserializerTest.EnumA.EnumB,
                                                    JsonDeserializerTest.EnumA.EnumC,
                                                    JsonDeserializerTest.EnumA.EnumA,
                                                    JsonDeserializerTest.EnumA.EnumA,
                                                    JsonDeserializerTest.EnumA.EnumB
                                            )
                                    )
                            )
                    )
            )
    )


    val annotationClassAJson = """{
            "jsonNameString" : "JsonName",
            "nonDeserializableString": "nonDeserializableString",
            "InnerClassA": [
                {
                    "InnerStringA": "InnerStringAA",
                    "nonDeserializableString": "nonDeserializableStringAA",
                    "listOfString": ["String1", "String2", "String3"]
                },
                {
                    "InnerStringA": "InnerStringAB",
                    "nonDeserializableString": "nonDeserializableStringAB",
                    "listOfString": ["String1", "String2","String155"]
                },
                {
                    "InnerStringA": "InnerStringAC",
                    "nonDeserializableString": "nonDeserializableStringAC",
                    "listOfString": ["String1", "String2"]
                }
            ],
            "noAnnotationString": "noAnnotationString",
            "deserializableTrueString": "deserializableTrueString",
            "InnerClassANotDeserializable": [
                {
                    "InnerStringA": "InnerStringAA",
                    "nonDeserializableString": "nonDeserializableStringAA",
                    "listOfString": ["String1", "String2"]
                },
                {
                    "InnerStringA": "InnerStringAB",
                    "nonDeserializableString": "nonDeserializableStringAB",
                    "listOfString": ["String1", "String2"]
                }
            ]
        }
    """

    val annotationClassAObj = TestingClassWithAnnotationA(
            "JsonName",
            "stringB",
            arrayListOf(
                    TestingInnerClassWithAnnotationA(
                            stringA = "InnerStringAA",
                            stringArr = arrayListOf(
                                    "String1", "String2", "String3"
                            )
                    ),
                    TestingInnerClassWithAnnotationA(
                            stringA = "InnerStringAB",
                            stringArr = arrayListOf(
                                    "String1", "String2","String155"
                            )
                    ),
                    TestingInnerClassWithAnnotationA(
                            stringA = "InnerStringAC",
                            stringArr = arrayListOf(
                                    "String1", "String2"
                            )
                    )
            ),
            "noAnnotationString",
            "deserializableTrueString"
    )

    @Before
    fun setUp() {
        jsonDeserializer = JsonDeserializer()
    }

    @Test
    fun parseJsonTest() {
        assertEquals(jsonDeserializer.parseJson(mTestingString, String::class, mTypeAdapterMap, mConfig), mTestingString)
    }

    @Test
    fun parseSimpleJsonObject() {
        assertEquals(jsonDeserializer.parseJson("""{"stringA":"Testing Class B", "intA": 10} """, TestingClassB::class, mTypeAdapterMap, mConfig), TestingClassB("Testing Class B", 10))
    }

    @Test
    fun parseComplexJsonObject() {
        assertEquals(jsonDeserializer.parseJson(classAJson, TestingClassA::class, mTypeAdapterMap, mConfig), classAObj)
    }

    @Test
    fun parseComplexObjectWithAnnotation() {
        assertEquals(jsonDeserializer.parseJson(annotationClassAJson, TestingClassWithAnnotationA::class, mTypeAdapterMap, mConfig),
                annotationClassAObj)
    }
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
    data class TestingClassB(val stringA: String,
                             val intA: Int)

    data class TestingClassA(val stringA: String,
                             val innerClassA: InnerClassA,
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
}