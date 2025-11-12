package org.example

sealed class Token(val line: Int, val pos: Int) {
    class LongToken(val value: Long, line: Int, pos: Int) : Token(line, pos) {
        override fun toString() : String = "Long($value)"
    }
    class DoubleToken(val value: Double, line: Int, pos: Int) : Token(line, pos) {
        override fun toString() : String = "Double($value)"
    }
    class StringToken(val value: String, line: Int, pos: Int) : Token(line, pos) {
        override fun toString() : String = "String($value)"
    }
    class BoolToken(val value: Boolean, line: Int, pos: Int) : Token(line, pos) {
        override fun toString() : String = "Bool($value)"
    }
    class NullToken(line: Int, pos: Int) : Token(line, pos) {
        override fun toString() : String = "Null"
    }
    class LbraceToken(line: Int, pos: Int) : Token(line, pos) {
        override fun toString() : String = "LBrace"
    }
    class RbraceToken(line: Int, pos: Int) : Token(line, pos) {
        override fun toString() : String = "RBrace"
    }
    class LSquareToken(line: Int, pos: Int) : Token(line, pos) {
        override fun toString() : String = "LSquare"
    }
    class RSquareToken(line: Int, pos: Int) : Token(line, pos) {
        override fun toString() : String = "RSquare"
    }
    class ColonToken(line: Int, pos: Int) : Token(line, pos) {
        override fun toString() : String = "Colon"
    }
    class CommaToken(line: Int, pos: Int) : Token(line, pos) {
        override fun toString() : String = "Comma"
    }
}