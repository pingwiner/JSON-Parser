package org.example

import org.example.json.JsonObject
import org.example.json.JsonValue
import org.example.json.*

fun main() {
    val json = """
        {
            "width" : 640,
            "height" : 480,
            "alpha": 0.48,
            "enabled" : true ,
            "active":false,
            "ints" : [1, 2, 3],
            "strings": ["abc", "cde"],
            "child" : {
                "item": "itemValue",
            },
            "objArray": [
                {
                    "field1": 1,
                    "field2": 2.0
                }
                ,{
                    "field1" : "asdasd",
                    "field2" : null,
                }
            ],
            "arrayOfArrays": [
                [1, 2, 3],
                ["a", "b", "c"],
                [true, false, null]
            ]
        }
    """
    try {
        val jsonObject = JsonObject.fromString(json)
        println("object: " + jsonObject)

        assert(jsonObject.keys == listOf("width", "height", "alpha", "enabled", "active", "ints", "strings", "child", "objArray", "arrayOfArrays"))
        assert(jsonObject["width"].asInt() == 640)
        assert(jsonObject["height"].asLong() == 480L)
        assert(jsonObject["alpha"].asDouble() == 0.48)
        assert(jsonObject["enabled"].asBoolean() == true)
        assert(jsonObject["active"].asBoolean() == false)
        assert(jsonObject["ints"].asIntArray().contentEquals(intArrayOf(1, 2, 3)))
        assert(jsonObject["strings"].asStringList() == listOf("abs", "cde"))
        assert(jsonObject["child"]["item"].asString() == "itemValue")
        assert(jsonObject["objArray"][0]["field1"].asInt() == 1)
        assert(jsonObject["arrayOfArrays"][1][1].asString() == "b")
        assert(jsonObject["arrayOfArrays"][2][2] is JsonValue.NullValue)
    } catch(e: Exception) {
        e.printStackTrace()
    }
}


