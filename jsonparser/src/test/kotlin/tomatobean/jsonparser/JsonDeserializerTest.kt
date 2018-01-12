package tomatobean.jsonparser

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.starProjectedType
import kotlin.test.assertTrue

class JsonDeserializerTest {

    internal lateinit var jsonDeserializer: JsonDeserializer

    val dateFormat = "yyyy-MM-dd HH:mm:ss Z"
    val mTypeAdapterMap = linkedMapOf<KClass<*>, DeserializeAdapter<*>>().apply {
        putAll(JsonFormatter.DEFAULT_Deserialize_TypeAdapterMap)
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

    val hashMapTypeTokenJson = """
        {
            "mapA": {
                "stringA": "ObjectA",
                "hashMapA": {
                    "mapString1": "Map String 1",
                    "mapString2": "Map String 2"
                }
            },
            "mapB": {
                "stringA": "ObjectB",
                "hashMapA": {
                    "mapString1": "Map String 3",
                    "mapString2": "Map String 4",
                    "mapString3": "Map String 5"
                }
            },
            "mapC": {
                "stringA": "ObjectC",
                "hashMapA": {
                    "mapString1": "Map String 6",
                    "mapString2": "Map String 7"
                }
            }
        }
    """

    val hashMapTypeTokenObject = hashMapOf<String, TestTypeTokenClassA>(
            "mapA" to TestTypeTokenClassA(
                    "ObjectA",
                    hashMapOf<String, String>(
                            "mapString1" to "Map String 1",
                            "mapString2" to "Map String 2"
                    )
            ),
            "mapB" to TestTypeTokenClassA(
                    "ObjectB",
                    hashMapOf<String, String>(
                            "mapString1" to "Map String 3",
                            "mapString2" to "Map String 4",
                            "mapString3" to "Map String 5"
                    )
            ),
            "mapC" to TestTypeTokenClassA(
                    "ObjectC",
            hashMapOf<String, String>(
                    "mapString1" to "Map String 6",
                    "mapString2" to "Map String 7"
            )
    )
    )

    val listMapTypeTokenJson = """
        [
            {
                "stringA": "ObjectA",
                "hashMapA": {
                    "mapString1": "Map String 1",
                    "mapString2": "Map String 2"
                }
            },
            {
                "stringA": "ObjectB",
                "hashMapA": {
                    "mapString1": "Map String 3",
                    "mapString2": "Map String 4",
                    "mapString3": "Map String 5"
                }
            },
            {
                "stringA": "ObjectC",
                "hashMapA": {
                    "mapString1": "Map String 6",
                    "mapString2": "Map String 7"
                }
            }
        ]
    """

    val listMapTypeTokenObject = listOf<TestTypeTokenClassA>(
            TestTypeTokenClassA(
                    "ObjectA",
                    hashMapOf<String, String>(
                            "mapString1" to "Map String 1",
                            "mapString2" to "Map String 2"
                    )
            ),
            TestTypeTokenClassA(
                    "ObjectB",
                    hashMapOf<String, String>(
                            "mapString1" to "Map String 3",
                            "mapString2" to "Map String 4",
                            "mapString3" to "Map String 5"
                    )
            ),
            TestTypeTokenClassA(
                    "ObjectC",
                    hashMapOf<String, String>(
                            "mapString1" to "Map String 6",
                            "mapString2" to "Map String 7"
                    )
            )
    )

    val testNullMapJson = """
        {
            "mapB": {
                "stringA": "Map B String A"
            },
            "mapA": null
        }
    """

    val testNullListJson = """
        [
            {
                "stringA": "String A 1"
            },
            {
                "stringA": "String A 2"
            },
            null
            ,
            {
                "stringA": "String A 4"
            },
            {
                "stringA": "String A 5"
            }
        ]
    """

    val parseMapNullAndNotThrowExceptionObj = mapOf<String, TestNullClassA?>(
            "mapB" to TestNullClassA("Map B String A"),
            "mapA" to null
    )

    val parseListNullAndNotThrowExceptionObj = listOf<TestNullClassA?>(
            TestNullClassA("String A 1"),
            TestNullClassA("String A 2"),
            null,
            TestNullClassA("String A 4"),
            TestNullClassA("String A 5")
    )

    val parseMissingParamAndThroExceptionJson = """
        {
            "stringA": "StringA"
        }
    """

    val testNonConstructorParamJson = """
        {
            "constructorParam": "ConstructorParam",
            "nonConstructorParamA": "nonConstructorParamA",
            "nonConstructorParamB": "nonConstructorParamB",
            "nonConstructorParamC": "nonConstructorParamC"
        }
    """
    val testNonConstructorParamObj = TestNonConstructorParamClass("ConstructorParam").apply {
        nonConstructorParamA = "nonConstructorParamA"
        nonConstructorParamB = "nonConstructorParamB"
        nonConstructorParamC = "nonConstructorParamC"
    }

    val testNonConstructorParamWithNullJson = """
        {
            "constructorParam": "ConstructorParam",
            "nonConstructorParamA": "nonConstructorParamA",
            "nonConstructorParamC": "nonConstructorParamC"
        }
    """
    val testNonConstructorParamWithNullObj = TestNonConstructorParamClass("ConstructorParam").apply {
        nonConstructorParamA = "nonConstructorParamA"
        nonConstructorParamC = "nonConstructorParamC"
    }

    val arrayObjJson = """
        [
            {"arr": ["String 11", "String 12", "String 13"],
            "arrObj": [
                {
                    "stringA": "String A1",
                    "intB": 11
                },
                {
                    "stringA": "String A2",
                    "intB": 12
                },
                {
                    "stringA": "String A3",
                    "intB": 13
                },
                {
                    "stringA": "String A4",
                    "intB": 14
                }
            ]},
            {"arr": ["String 21", "String 22"],
            "arrObj": [
                {
                    "stringA": "String B1",
                    "intB": 21
                },
                {
                    "stringA": "String B2",
                    "intB": 22
                },
                {
                    "stringA": "String B3",
                    "intB": 23
                },
                {
                    "stringA": "String B4",
                    "intB": 24
                },
                {
                    "stringA": "String B5",
                    "intB": 25
                }
            ]},
            {"arr": ["String 31", "String 32", "String 33", "String 34"],
            "arrObj": [
                {
                    "stringA": "String C1",
                    "intB": 31
                },
                {
                    "stringA": "String C2",
                    "intB": 32
                },
                {
                    "stringA": "String C4",
                    "intB": 34
                }
            ]}
        ]
    """

    val arrayObject = arrayOf(
            TestArrayObject(
                    arrayOf("String 11", "String 12", "String 13"),
                    arrayOf(
                            ArrayInnerObject("String A1", 11),
                            ArrayInnerObject("String A2", 12),
                            ArrayInnerObject("String A3", 13),
                            ArrayInnerObject("String A4", 14)
                    )
            ),
            TestArrayObject(
                    arrayOf("String 21", "String 22"),
                    arrayOf(
                            ArrayInnerObject("String B1", 21),
                            ArrayInnerObject("String B2", 22),
                            ArrayInnerObject("String B3", 23),
                            ArrayInnerObject("String B4", 24),
                            ArrayInnerObject("String B5", 25)
                    )
            ),
            TestArrayObject(
                    arrayOf("String 31", "String 32", "String 33", "String 34"),
                    arrayOf(
                            ArrayInnerObject("String C1", 31),
                            ArrayInnerObject("String C2", 32),
                            ArrayInnerObject("String C4", 34)
                    )
            )
    )

    val mapOfMapJson = """
        {
            "OuterMapA": {
                "InnerMapA1": {
                    "stringA":"String AA1",
                    "intB":1
                },
                "InnerMapA2": {
                    "stringA":"String AA2",
                    "intB":2
                },
                "InnerMapA3": {
                    "stringA":"String AA3",
                    "intB":3
                },
                "InnerMapA4": {
                    "stringA":"String AA4",
                    "intB":4
                }
            },
            "OuterMapB": {
                "InnerMapB1": {
                    "stringA":"String BB1",
                    "intB":5
                },
                "InnerMapB2": {
                    "stringA":"String BB2",
                    "intB":6
                },
                "InnerMapB3": {
                    "stringA":"String BB3",
                    "intB":7
                },
                "InnerMapB4": {
                    "stringA":"String BB4",
                    "intB":8
                }
            }
        }
    """

    val mapOfMapObj = mapOf<String, Map<String, MapOfMapClass>>(
            "OuterMapA" to mapOf(
                    "InnerMapA1" to MapOfMapClass("String AA1", 1),
                    "InnerMapA2" to MapOfMapClass("String AA2", 2),
                    "InnerMapA3" to MapOfMapClass("String AA3", 3),
                    "InnerMapA4" to MapOfMapClass("String AA4", 4)
            ),
            "OuterMapB" to mapOf(
                    "InnerMapB1" to MapOfMapClass("String BB1",5),
                    "InnerMapB2" to MapOfMapClass("String BB2",6),
                    "InnerMapB3" to MapOfMapClass("String BB3",7),
                    "InnerMapB4" to MapOfMapClass("String BB4",8)
            )
    )

    val listOfListJson = """
        [
            [
                {
                    "booleanA": true,
                    "stringB": "String 11"
                },
                {
                    "booleanA": false,
                    "stringB": "String 12"
                },
                {
                    "booleanA": true,
                    "stringB": "String 13"
                },
                {
                    "booleanA": false,
                    "stringB": "String 14"
                },
                {
                    "booleanA": true,
                    "stringB": "String 15"
                },
                {
                    "booleanA": false,
                    "stringB": "String 16"
                }
            ],
            [
                {
                    "booleanA": true,
                    "stringB": "String 27"
                },
                {
                    "booleanA": false,
                    "stringB": "String 28"
                },
                {
                    "booleanA": true,
                    "stringB": "String 29"
                },
                {
                    "booleanA": false,
                    "stringB": "String 24"
                },
                {
                    "booleanA": false,
                    "stringB": "String 26"
                }
            ]
        ]
    """

    val listOfListObj = listOf<List<ListOfListClass>>(
            listOf(
                    ListOfListClass(true, "String 11"),
                    ListOfListClass(false, "String 12"),
                    ListOfListClass(true, "String 13"),
                    ListOfListClass(false, "String 14"),
                    ListOfListClass(true, "String 15"),
                    ListOfListClass(false, "String 16")
            ),
            listOf(
                    ListOfListClass(true, "String 27"),
                    ListOfListClass(false, "String 28"),
                    ListOfListClass(true, "String 29"),
                    ListOfListClass(false, "String 24"),
                    ListOfListClass(false, "String 26")
            )
    )

    val differentTypeJson = """
        {"date": "1988-09-02 10:20:56 +0800",
        "calendar": "1986-10-07 18:59:43 +0800",
        "enum": "EnumB",
        "int": 200000,
        "long": 909090090,
        "short": 20,
        "double": 2.0222,
        "float": 97004.02,
        "bigDecimal": "98482132138.13887873",
        "bigInteger": 231221121,
        "charSequence": "CharSequenceCharSequence",
        "string": "String String String String",
        "char": "Z",
        "boolean": true}
    """

    val differentTypeObj = DifferentTypeClass(
            SimpleDateFormat(dateFormat).parse("1988-09-02 10:20:56 +0800"),
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
            DifferentTypeEnum.EnumB,
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

    @Before
    fun setUp() {
        jsonDeserializer = JsonDeserializer()
    }

    @Test
    fun parseJsonTest() {
        assertEquals(mTestingString,
                jsonDeserializer.parseJson(mTestingString, String::class.starProjectedType, String::class, mTypeAdapterMap, mConfig))
    }

    @Test
    fun parseSimpleJsonObject() {
        assertEquals(TestingClassB("Testing Class B", 10),
                jsonDeserializer.parseJson("""{"stringA":"Testing Class B", "intA": 10} """, TestingClassB::class.starProjectedType, TestingClassB::class, mTypeAdapterMap, mConfig))
    }

    @Test
    fun parseComplexJsonObject() {
        assertEquals(classAObj,
                jsonDeserializer.parseJson(classAJson, TestingClassA::class.starProjectedType,  TestingClassA::class, mTypeAdapterMap, mConfig))
    }

    @Test
    fun parseComplexObjectWithAnnotation() {
        assertEquals(annotationClassAObj,
                jsonDeserializer.parseJson(annotationClassAJson, TestingClassWithAnnotationA::class.starProjectedType, TestingClassWithAnnotationA::class, mTypeAdapterMap, mConfig))
    }

    @Test
    fun parserMapTypeToken() {
        val typeToken = object : TypeToken<HashMap<String, TestTypeTokenClassA>>(){}
        assertEquals(hashMapTypeTokenObject, jsonDeserializer.parseJson(hashMapTypeTokenJson, typeToken, mTypeAdapterMap, mConfig))
    }

    @Test
    fun parserListTypeToken() {
        val typeToken = object : TypeToken<List<TestTypeTokenClassA>>(){}
        assertEquals(listMapTypeTokenObject, jsonDeserializer.parseJson(listMapTypeTokenJson, typeToken, mTypeAdapterMap, mConfig))
    }

    @Test
    fun parseCollectionNullAndThrowException() {
        val typeToken = object : TypeToken<List<TestNullClassA>>(){}
        val objectClass = TestNullClassA::class.simpleName
        val index = 2
        try {
            jsonDeserializer.parseJson(testNullListJson, typeToken, mTypeAdapterMap, mConfig)
        } catch (e: Exception) {
            assertTrue {
                e is MissingParamException
            }

            assertEquals(
                    "Object $objectClass in index: $index is null while variable defined is a non-nullable object",
                    e.message
            )
        }
    }

    @Test
    fun parseMapNullAndThrowException() {
        val typeToken = object : TypeToken<Map<String, TestNullClassA>>(){}
        val objectClass = TestNullClassA::class.simpleName
        val jsonKey = "mapA"
        try {
            jsonDeserializer.parseJson(testNullMapJson, typeToken, mTypeAdapterMap, mConfig)
        } catch (e: Exception) {
            assertTrue {
                e is MissingParamException
            }
            assertEquals(
                    "Object Class: $objectClass\n" +
                            " Object in key: $jsonKey is null while variable defined is a non-nullable object",
                    e.message
            )
        }
    }

    @Test
    fun parseMapNullAndNotThrowException() {
        val typeToken = object : TypeToken<Map<String, TestNullClassA?>>(){}
        assertEquals(parseMapNullAndNotThrowExceptionObj,
                jsonDeserializer.parseJson(testNullMapJson, typeToken, mTypeAdapterMap, mConfig))
    }

    @Test
    fun parseListNullAndNotThrowException() {
        val typeToken = object : TypeToken<List<TestNullClassA?>>(){}
        assertEquals(parseListNullAndNotThrowExceptionObj,
                jsonDeserializer.parseJson(testNullListJson, typeToken, mTypeAdapterMap, mConfig))
    }

    @Test
    fun parseMissingParamAndThrowException() {
        try {
            jsonDeserializer.parseJson(parseMissingParamAndThroExceptionJson, TestMissParamClass::class.starProjectedType, TestMissParamClass::class, mTypeAdapterMap, mConfig)
        } catch (e: Exception) {
            assertTrue {
                e is MissingParamException
            }
            val objectClass = TestMissParamClass::class.simpleName
            val parseMissingParamAndThrowExceptionMsg = "Object Class: $objectClass\n" +
                    "Param : missingParamA\n" +
                    "Param : missingParamB\n" +
                    "Param : missingParamC\n" +
                    "is(are) missing, please check json String and/or Object Structure."
            assertEquals(
                    parseMissingParamAndThrowExceptionMsg,
                    e.message
            )
        }

    }

    @Test
    fun testNonConstructorParam() {
        assertEquals(testNonConstructorParamObj, jsonDeserializer.parseJson(testNonConstructorParamJson, TestNonConstructorParamClass::class.starProjectedType, TestNonConstructorParamClass::class, mTypeAdapterMap, mConfig))
    }

    @Test
    fun testNonConstructorParamWithNull() {
        assertEquals(testNonConstructorParamWithNullObj, jsonDeserializer.parseJson(testNonConstructorParamWithNullJson, TestNonConstructorParamClass::class.starProjectedType, TestNonConstructorParamClass::class, mTypeAdapterMap, mConfig))
    }

    @Test
    fun parserArrayObject() {
        val typeToken = object : TypeToken<Array<TestArrayObject>>(){}
        assertEquals(arrayObject, jsonDeserializer.parseJson(arrayObjJson, typeToken, mTypeAdapterMap, mConfig))
    }

    @Test
    fun parseMapInMapObject() {
        val typeToken = object : TypeToken<Map<String, Map<String, MapOfMapClass>>>(){}
        assertEquals(mapOfMapObj, jsonDeserializer.parseJson(mapOfMapJson, typeToken, mTypeAdapterMap, mConfig))
    }

    @Test
    fun parseListOfListObject() {
        val typeToken = object : TypeToken<List<List<ListOfListClass>>>(){}
        assertEquals(listOfListObj, jsonDeserializer.parseJson(listOfListJson, typeToken, mTypeAdapterMap, mConfig))
    }

    @Test
    fun parseDifferentTypeObject() {
        /**
         * Limitation
         * For the BigDecimal Type, The JSONObject will easy overflow when it declare as number in Json String
         * To have better behavior, declare BigDecimal as String in Json String
         */
        val obj = differentTypeObj
        assertEquals(obj, jsonDeserializer.parseJson(differentTypeJson, DifferentTypeClass::class.starProjectedType, DifferentTypeClass::class, mTypeAdapterMap, mConfig))
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

    data class TestTypeTokenClassA(
            val stringA: String,
            val hashMapA: HashMap<String, String>
    )

    data class TestNullClassA(
            val stringA: String
    )

    data class TestMissParamClass(
            val stringA: String,
            val missingParamA: String,
            val missingParamB: String,
            val missingParamC: String
    )

    data class TestNonConstructorParamClass(
            val constructorParam: String
    ) {
        var nonConstructorParamA: String = "A"
        var nonConstructorParamB: String = "B"
        var nonConstructorParamC: String = "C"
    }

    data class TestArrayObject(
            val arr: Array<String>,
            val arrObj: Array<ArrayInnerObject>
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TestArrayObject

            if (!Arrays.equals(arr, other.arr)) return false
            if (!Arrays.equals(arrObj, other.arrObj)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = Arrays.hashCode(arr)
            result = 31 * result + Arrays.hashCode(arrObj)
            return result
        }
    }

    data class ArrayInnerObject(
            val stringA: String,
            val intB: Int
    )

    data class MapOfMapClass(
            val stringA: String,
            val intB: Int
    )

    data class ListOfListClass(
            val booleanA: Boolean,
            val stringB: String
    )

    data class DifferentTypeClass(
            val date: Date,
            val calendar: Calendar,
            val enum: DifferentTypeEnum,
            val int: Int,
            val long: Long,
            val short: Short,
            val double: Double,
            val float: Float,
            val bigDecimal: BigDecimal,
            val bigInteger: BigInteger,
            val charSequence: CharSequence,
            val string: String,
            val char: Char,
            val boolean: Boolean

    )

    enum class DifferentTypeEnum {
        EnumA,
        EnumB
    }
}