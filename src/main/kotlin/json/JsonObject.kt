package org.example.json

import org.example.json.lexer.Lexer
import org.example.json.parser.Parser

@JvmInline
value class JsonObject(val value: Map<String, JsonValue>) {
    val keys: Set<String>
        get() = value.keys

    fun hasField(key: String): Boolean = value.contains(key)

    companion object {
        fun fromString(json: String): JsonObject {
            val lexer = Lexer()
            val tokens = lexer.parse(json)
            val parser = Parser()
            return parser.parse(tokens)
        }
    }
}

operator fun JsonObject?.get(key: String): JsonValue? = this?.value[key]