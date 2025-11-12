package org.example

sealed interface JsonValue {
    @JvmInline
    value class LongValue(val value: Long) : JsonValue

    @JvmInline
    value class DoubleValue(val value: Double) : JsonValue

    @JvmInline
    value class StringValue(val value: String) : JsonValue

    @JvmInline
    value class BooleanValue(val value: Boolean) : JsonValue

    @JvmInline
    value class ArrayValue(val value: List<JsonValue>) : JsonValue

    @JvmInline
    value class ObjectValue(val value: JsonObject) : JsonValue

    data object NullValue : JsonValue
}