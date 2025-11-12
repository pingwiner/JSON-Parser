package org.example

@JvmInline
value class JsonObject(val value: Map<String, JsonValue>) {

    fun getLong(key: String): Long? = when(val entry = value[key]) {
        is JsonValue.LongValue -> entry.value
        else -> null
    }

    fun getDouble(key: String): Double? = when(val entry = value[key]) {
        is JsonValue.DoubleValue -> entry.value
        else -> null
    }

    fun getString(key: String): String? = when(val entry = value[key]) {
        is JsonValue.StringValue -> entry.value
        else -> null
    }

    fun getBoolean(key: String): Boolean? = when(val entry = value[key]) {
        is JsonValue.BooleanValue -> entry.value
        else -> null
    }

    fun getList(key: String): List<JsonValue>? = when(val entry = value[key]) {
        is JsonValue.ArrayValue -> entry.value
        else -> null
    }

    fun getObject(key: String): JsonObject? = when(val entry = value[key]) {
        is JsonValue.ObjectValue -> entry.value
        else -> null
    }

    fun hasField(key: String): Boolean = value.contains(key)

    fun getLongArray(key: String): LongArray? {
        if (!hasField(key)) return null
        val objList = getList(key) ?: return null
        if (objList.all { it is JsonValue.LongValue }) {
            return objList.map { (it as JsonValue.LongValue).value }.toLongArray()
        }
        return null
    }

    fun getDoubleArray(key: String): DoubleArray? {
        if (!hasField(key)) return null
        val objList = getList(key) ?: return null
        if (objList.all { it is JsonValue.DoubleValue }) {
            return objList.map { (it as JsonValue.DoubleValue).value }.toDoubleArray()
        }
        return null
    }

    fun getStringList(key: String): List<String>? {
        if (!hasField(key)) return null
        val objList = getList(key) ?: return null
        if (objList.all { it is JsonValue.StringValue }) {
            return objList.map { (it as JsonValue.StringValue).value }
        }
        return null
    }

    companion object

}