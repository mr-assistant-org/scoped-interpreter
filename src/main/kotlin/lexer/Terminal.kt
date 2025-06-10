package lexer

enum class Terminal(val pattern: Regex? = null) {
    PRINT(keyword("print")),
    SCOPE(keyword("scope")),
    EQUAL("=".toRegex()),
    LBRACKET("\\{".toRegex()),
    RBRACKET("}".toRegex()),
    VAR("[a-z][A-Za-z_0-9]*".toRegex()),
    INT("-?[0-9]+".toRegex()),
    WS("[ \\t\\r\\n]+".toRegex()),
    END(),
    EPS(),
}

private fun keyword(keyword: String): Regex = "$keyword(?![A-Za-z_0-9])".toRegex()

data class TermWithValue(val terminal: Terminal, val value: String)