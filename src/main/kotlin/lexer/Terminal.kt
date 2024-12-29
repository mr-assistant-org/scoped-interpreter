package lexer

enum class Term(val pattern: Regex) {
    PRINT("print[^A-Za-z_0-9]".toRegex()), // TODO Add Keyword function?
    SCOPE("scope[^A-Za-z_0-9]".toRegex()),
    EQUAL("=".toRegex()),
    LBRACKET("\\{".toRegex()),
    RBRACKET("}".toRegex()),
    VAR("[a-z][A-Za-z_0-9]*".toRegex()),
    INT("[0-9]+".toRegex()),
    WS("[ \\t\\r\\n]+".toRegex()),
    END("".toRegex()),
    EPS("".toRegex()),
}

data class TermWithValue(val terminal: Term, val value: String)