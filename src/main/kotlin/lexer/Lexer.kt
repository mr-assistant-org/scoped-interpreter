package lexer

import lexer.Term.*

open class Lexer(private val input: CharSequence) {
    var curPos = 0
        private set

    var curTerminal: TermWithValue = TermWithValue(END, "")
        private set

    fun nextToken() {
        if (curPos >= input.length) {
            curTerminal = TermWithValue(END, "")
            return
        }
        Term.entries.forEach { rule ->
            val match = rule.pattern?.matchAt(input, curPos) ?: return@forEach
            curPos += match.range.length()
            curTerminal = TermWithValue(rule, match.value)
            skipWhitespaces()
            return
        }
        throw LexerException(
            "Unknown token starting from `${input.subSequence(curPos, minOf(curPos + 10, input.length))}...`", curPos
        )
    }

    fun expect(terminal: Term) {
        if (terminal != curTerminal.terminal) {
            throw LexerException(
                "Invalid terminal. Expected terminal with `$terminal`, actual `${curTerminal.terminal}`", curPos
            )
        }
    }

    fun skipWhitespaces() {
        while (curTerminal.terminal == WS) nextToken()
    }

    private fun IntRange.length() = last - first + 1
}
