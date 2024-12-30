package parser

import ast.Expression
import ast.NumberExpr
import ast.Print
import ast.Scope
import ast.Statement
import ast.VarDef
import ast.VarExpr
import lexer.Lexer
import lexer.Terminal.*

class Parser(input: CharSequence) {
    private val lexer: Lexer = Lexer(input)
    init {
        lexer.nextToken()
    }

    fun expression(): Expression {
        return when (lexer.curTerminal.terminal) {
            INT -> NumberExpr(lexer.curTerminal.value.toInt()).also {
                lexer.nextToken()
            }
            VAR -> VarExpr(lexer.curTerminal.value.toString()).also {
                lexer.nextToken()
            }
            else -> throw ParserException("Invalid terminal ${lexer.curTerminal}", lexer.curPos)
        }
    }

    fun varDef(): VarDef {
        return when (lexer.curTerminal.terminal) {
            VAR -> {
                val name = lexer.curTerminal.value
                lexer.nextToken()

                lexer.expect(EQUAL)

                val value = expression()
                VarDef(name, value)
            }
            else -> throw ParserException("Invalid terminal ${lexer.curTerminal}", lexer.curPos)
        }
    }

    fun print(): Print {
        return when (lexer.curTerminal.terminal) {
            PRINT -> {
                lexer.nextToken()
                val value = expression()
                Print(value)
            }
            else -> throw ParserException("Invalid terminal ${lexer.curTerminal}", lexer.curPos)
        }
    }

    fun scope(): Scope {
        return when (lexer.curTerminal.terminal) {
            SCOPE -> {
                lexer.nextToken()
                lexer.expect(LBRACKET)
                Scope(statements()).also {
                    lexer.expect(RBRACKET)
                }
            }
            else -> throw ParserException("Invalid terminal ${lexer.curTerminal}", lexer.curPos)
        }
    }

    fun statement(): Statement {
        return when (lexer.curTerminal.terminal) {
            VAR -> varDef()
            PRINT -> print()
            SCOPE -> scope()
            else -> throw ParserException("Invalid terminal ${lexer.curTerminal}", lexer.curPos)
        }
    }

    fun statements(): List<Statement> {
        return when (lexer.curTerminal.terminal) {
            in listOf(VAR, PRINT, SCOPE) -> {
                val statements = mutableListOf<Statement>()
                while (lexer.curTerminal.terminal in listOf(VAR, PRINT, SCOPE)) {
                    statements.add(statement())
                }
                statements
            }
            in listOf(END, RBRACKET) -> listOf()
            else -> throw ParserException("Invalid terminal ${lexer.curTerminal}", lexer.curPos)
        }
    }
}