package org.example.json

import org.example.json.JsonValue.ArrayValue
import org.example.json.JsonValue.BooleanValue
import org.example.json.JsonValue.DoubleValue
import org.example.json.JsonValue.LongValue
import org.example.json.JsonValue.NullValue
import org.example.json.JsonValue.ObjectValue
import org.example.json.JsonValue.StringValue

sealed class JsonValue {
    class LongValue(val value: Long) : JsonValue()
    class DoubleValue(val value: Double) : JsonValue()
    class StringValue(val value: String) : JsonValue()
    class BooleanValue(val value: Boolean) : JsonValue()
    class ArrayValue(val value: List<JsonValue>) : JsonValue()
    class ObjectValue(val value: JsonObject) : JsonValue()
    data object NullValue : JsonValue()
}

fun JsonValue?.asLong(): Long? = (this as? LongValue)?.value
fun JsonValue?.isLong(): Boolean = this is LongValue
fun JsonValue?.asInt(): Int? = asLong()?.let { longToInt(it) }
fun JsonValue?.isInt(): Boolean = asInt() != null
fun JsonValue?.asDouble(): Double? = (this as? DoubleValue)?.value
fun JsonValue?.isDouble(): Boolean = this is DoubleValue
fun JsonValue?.asString(): String? = (this as? StringValue)?.value
fun JsonValue?.isString(): Boolean = this is StringValue
fun JsonValue?.asFloat(): Float? = asDouble()?.let { doubleToFloat(it) }
fun JsonValue?.isFloat(): Boolean = asFloat() != null
fun JsonValue?.asBoolean(): Boolean? = (this as? BooleanValue)?.value
fun JsonValue?.isBoolean(): Boolean = this is BooleanValue
fun JsonValue?.asList(): List<JsonValue>? = (this as? ArrayValue)?.value
fun JsonValue?.isList(): Boolean = this is ArrayValue

fun JsonValue?.asLongArray(): LongArray? {
    val list = asList() ?: return null
    val result = LongArray(list.size)
    var i = 0
    list.forEach {
        result[i++] = it.asLong() ?: return null
    }
    return result
}

fun JsonValue?.asIntArray(): IntArray? {
    val list = asList() ?: return null
    val result = IntArray(list.size)
    var i = 0
    list.forEach {
        result[i++] = it.asInt() ?: return null
    }
    return result
}

fun JsonValue?.asStringList(): List<String>? {
    val list = asList() ?: return null
    val result = mutableListOf<String>()
    list.forEach {
        result.add(it.asString() ?: return null)
    }
    return result
}

fun JsonValue?.asBooleanArray(): BooleanArray? {
    val list = asList() ?: return null
    val result = BooleanArray(list.size)
    var i = 0
    list.forEach {
        result[i++] = it.asBoolean() ?: return null
    }
    return result
}

fun JsonValue?.asDoubleArray(): DoubleArray? {
    val list = asList() ?: return null
    val result = DoubleArray(list.size)
    var i = 0
    list.forEach {
        result[i++] = it.asDouble() ?: return null
    }
    return result
}

fun JsonValue?.asFloatArray(): FloatArray? {
    val list = asList() ?: return null
    val result = FloatArray(list.size)
    var i = 0
    list.forEach {
        result[i++] = it.asFloat() ?: return null
    }
    return result
}

operator fun List<JsonValue>?.get(index: Int): JsonValue? {
    return this?.let {
        if (it.size > index) {
            return it[index]
        }
        return null
    }
}

operator fun JsonValue?.get(index: Int): JsonValue? {
    return this?.asList()[index]
}

operator fun JsonValue?.get(key: String): JsonValue? {
    return this?.asObject()[key]
}

fun JsonValue?.asObject(): JsonObject? = (this as? ObjectValue)?.value
fun JsonValue?.isObject(): Boolean = this is ObjectValue
fun JsonValue?.isNull(): Boolean = (this is NullValue)
