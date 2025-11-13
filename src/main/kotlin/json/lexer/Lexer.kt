package org.example.json.lexer

class Lexer {
    private var line = 0
    private var linePos = 0
    private var startingPos = 0;
    private var pos = 0
    private var state = State.Init
    private val currentValue = StringBuilder()
    private val result = mutableListOf<Token>()

    fun parse(str: String): List<Token> {

        while(pos < str.length) {
            val char = str[pos]
            if (char == '\n') {
                space()
                line++
                linePos = 0
                pos++
                continue
            }

            if (char.isWhitespace() && state != State.String) {
                space()
            } else if (char == '{') {
                space()
                result.add(
                    Token.LbraceToken(line, linePos)
                )
            } else if (char == '}') {
                space()
                result.add(
                    Token.RbraceToken(line, linePos)
                )
            } else if (char == '[') {
                space()
                result.add(
                    Token.LSquareToken(line, linePos)
                )
            } else if (char == ']') {
                space()
                result.add(
                    Token.RSquareToken(line, linePos)
                )
            } else if (char == ':') {
                space()
                result.add(
                    Token.ColonToken(line, linePos)
                )
            } else if (char == ',') {
                space()
                result.add(
                    Token.CommaToken(line, linePos)
                )
            } else if (char == '"') {
                if (state == State.Space) {
                    startString()
                } else if (state == State.String) {
                    parseString()
                } else {
                    throw IllegalStateException("Unexpected \" at $line:$linePos")
                }
            } else {
                if (state == State.Space) {
                    val lowCaseChar = char.lowercaseChar()
                    if (lowCaseChar == 't' || lowCaseChar == 'f') {
                        startBoolean(char)
                    } else if (lowCaseChar == 'n') {
                        startNull(char)
                    } else if (char.isDigit()) {
                        startNumber(char)
                    } else {
                        throw IllegalStateException("Unexpected symbol at $line:$linePos")
                    }
                } else if (state == State.String) {
                    currentValue.append(char)
                } else if (state == State.Number) {
                    if (char.isDigit() || char == '.') {
                        currentValue.append(char)
                    } else {
                        throw IllegalStateException("Unexpected symbol at $line:$linePos")
                    }
                } else { // boolean or null
                    val allowedChars = "truefalsnl"
                    if (allowedChars.contains(char)) {
                        currentValue.append(char)
                    } else {
                        throw IllegalStateException("Unexpected symbol at $line:$linePos")
                    }
                }
            }

            pos++
            linePos++
        }

        return result
    }

    private fun space() {
        when(state) {
            State.String -> {
                throw IllegalStateException("Unexpected line break at $line:$linePos")
            }
            State.Number -> parseNumber()
            State.Boolean -> parseBoolean()
            State.Null -> parseNull()
            else -> {}
        }
        state = State.Space
    }

    private fun parseNumber() {
        state = State.Space
        val s = currentValue.toString()
        if (s.contains('.')) {
            val double = s.toDoubleOrNull()
            if (double != null) {
                result.add(
                    Token.DoubleToken(double, line, startingPos)
                )
            } else {
                throw IllegalStateException("Bad numeric value at $line:$linePos")
            }
        } else {
            val long = s.toLongOrNull()
            if (long != null) {
                result.add(
                    Token.LongToken(long, line, startingPos)
                )
            } else {
                throw IllegalStateException("Bad numeric value at $line:$linePos")
            }
        }
    }

    private fun parseBoolean() {
        state = State.Space
        val s = currentValue.toString().lowercase()
        if (s == "true") {
            result.add(
                Token.BoolToken(true, line, startingPos)
            )
        } else if (s == "false") {
            result.add(
                Token.BoolToken(false, line, startingPos)
            )
        } else {
            throw IllegalStateException("Unknown token ($s) at $line:$startingPos")
        }
    }

    private fun parseNull() {
        state = State.Space
        val s = currentValue.toString().lowercase()
        if (s == "null") {
            result.add(
                Token.NullToken(line, startingPos)
            )
        } else {
            throw IllegalStateException("Unknown token ($s) at $line:$startingPos")
        }
    }

    private fun parseString() {
        state = State.Space
        val s = currentValue.toString()
        result.add(
            Token.StringToken(s, line, startingPos)
        )
    }

    private fun startString() {
        state = State.String
        currentValue.clear()
        startingPos = linePos
    }

    private fun startNumber(char: Char) {
        state = State.Number
        currentValue.clear()
        currentValue.append(char)
        startingPos = linePos
    }

    private fun startBoolean(char: Char) {
        state = State.Boolean
        currentValue.clear()
        currentValue.append(char)
        startingPos = linePos
    }

    private fun startNull(char: Char) {
        state = State.Null
        currentValue.clear()
        currentValue.append(char)
        startingPos = linePos
    }

    private enum class State {
        Init,
        String,
        Number,
        Boolean,
        Null,
        Space
    }
}