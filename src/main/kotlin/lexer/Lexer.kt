package lexer

import lexer.Terminal.*

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
        Terminal.entries.forEach { rule ->
            val match = rule.pattern?.matchAt(input, curPos) ?: return@forEach
            curPos += match.range.length()
            curTerminal = TermWithValue(rule, match.value)
            skipWhitespaces()
            return
        }
        throw LexerException("Unknown token starting from `${currentPrefix()}...`", curPos)
    }

    fun expect(terminal: Terminal) {
        if (terminal != curTerminal.terminal) {
            throw LexerException(
                "Invalid terminal. Expected terminal `$terminal`, " +
                        "actual `${curTerminal.terminal}` starting from `${currentPrefix()}...`", curPos
            )
        }
        nextToken()
    }

    private fun skipWhitespaces() {
        while (curTerminal.terminal == WS) nextToken()
    }

    fun currentPrefix(length: Int = 15): CharSequence =
        input.subSequence(curPos, minOf(curPos + length, input.length))

    private fun IntRange.length() = last - first + 1
}
