package lexer

import java.text.ParseException

class LexerException(msg: String, pos: Int): ParseException(msg, pos) {
    override val message: String
        get() = "(At position ${errorOffset}) " + super.message
}