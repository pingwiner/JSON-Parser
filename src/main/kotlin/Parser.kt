package org.example

import org.example.JsonValue.*
import java.lang.IllegalStateException

class Parser {
    val result = mutableMapOf<String, JsonValue>()
    private var state = State.Init
    lateinit var iterator: ListIterator<Token>
    lateinit var currentKey: Token.StringToken

    fun parse(tokens: List<Token>): JsonObject {
        iterator = tokens.listIterator()
        return parse(iterator)
    }

    private fun parse(it: ListIterator<Token>, initialState: State = State.Init): JsonObject {
        iterator = it
        state = initialState
        while(iterator.hasNext()) {
            val token = iterator.next()
            when(state) {
                State.Init -> {
                    if (token is Token.LbraceToken) {
                        state = State.WaitingForKey
                    } else {
                        reportIllegalToken(token)
                    }
                }
                State.WaitingForKey -> {
                    if (token is Token.StringToken) {
                        currentKey = token
                        state = State.WaitingForColon
                    } else if (token is Token.RbraceToken) {
                        return JsonObject(result)
                    } else {
                        reportIllegalToken(token)
                    }
                }
                State.WaitingForColon -> {
                    if (token is Token.ColonToken) {
                        state = State.WaitingForValue
                    } else {
                        reportIllegalToken(token)
                    }
                }
                State.WaitingForValue -> {
                    val jsonValue = parseValue(token)
                    result[currentKey.value] = jsonValue
                    state = State.WaitingForCommaOrEnd
                }
                State.WaitingForCommaOrEnd -> {
                    if (token is Token.CommaToken) {
                        state = State.WaitingForKey
                    } else if (token is Token.RbraceToken) {
                        return JsonObject(result)
                    } else {
                        reportIllegalToken(token)
                    }
                }
            }
        }
        throw IllegalStateException("Unexpected end of JSON")
    }

    private fun reportIllegalToken(token: Token) {
        throw IllegalStateException("Illegal token at ${token.line}:${token.pos}")
    }

    private fun parseValue(token: Token): JsonValue {
        return when(token) {
            is Token.StringToken -> StringValue(token.value)
            is Token.LongToken -> LongValue(token.value)
            is Token.DoubleToken -> DoubleValue(token.value)
            is Token.BoolToken -> BooleanValue(token.value)
            is Token.NullToken -> NullValue
            is Token.LSquareToken -> parseArray()
            is Token.LbraceToken -> {
                val parser = Parser()
                ObjectValue(parser.parse(iterator, State.WaitingForKey))
            }
            else -> {
                throw IllegalStateException("Illegal token at ${token.line}:${token.pos}")
            }
        }
    }

    private fun parseArray(): ArrayValue {
        val result = mutableListOf<JsonValue>()
        var waitingForComma = false
        while(iterator.hasNext()) {
            val token = iterator.next()
            if (waitingForComma) {
                if (token is Token.CommaToken) {
                    waitingForComma = false
                    continue
                } else if (token is Token.RSquareToken) {
                    return ArrayValue(result)
                } else {
                    reportIllegalToken(token)
                }
            }
            when(token) {
                is Token.LongToken -> {
                    result.add(LongValue(token.value))
                    waitingForComma = true
                }
                is Token.BoolToken -> {
                    result.add(BooleanValue(token.value))
                    waitingForComma = true
                }
                is Token.DoubleToken -> {
                    result.add(DoubleValue(token.value))
                    waitingForComma = true
                }
                is Token.NullToken -> {
                    result.add(NullValue)
                    waitingForComma = true
                }
                is Token.StringToken -> {
                    result.add(StringValue(token.value))
                    waitingForComma = true
                }
                is Token.LbraceToken -> {
                    val parser = Parser()
                    result.add(ObjectValue(parser.parse(iterator, State.WaitingForKey)))
                    waitingForComma = true
                }
                is Token.LSquareToken -> {
                    result.add(parseArray())
                }
                is Token.CommaToken -> {
                    //unexpected comma, skip it
                }
                is Token.RSquareToken -> {
                    return ArrayValue(result)
                }
                else -> {
                    reportIllegalToken(token)
                }
            }
        }
        throw IllegalStateException("Unexpected end of JSON")
    }

    private enum class State {
        Init,
        WaitingForKey,
        WaitingForColon,
        WaitingForValue,
        WaitingForCommaOrEnd
    }
}

fun JsonObject.Companion.fromString(json: String): JsonObject {
    val lexer = Lexer()
    val tokens = lexer.parse(json)
    val parser = Parser()
    return parser.parse(tokens)
}