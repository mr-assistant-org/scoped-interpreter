package parser

import java.text.ParseException

class ParserException constructor(msg: String, pos: Int): ParseException(msg, pos) {
    override val message: String
        get() = "(At position ${errorOffset}) " + super.message
}