package org.example

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
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
            ]
        }
    """
    try {
        val jsonObject = JsonObject.fromString(json)
        println("object: " + jsonObject)
    } catch(e: Exception) {
        e.printStackTrace()
    }
}


