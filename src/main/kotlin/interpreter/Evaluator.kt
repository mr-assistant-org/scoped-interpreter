package interpreter

import ast.*

class Evaluator {
    private val env = EvalEnvironment()

    fun interpret(statements: List<Statement>): String = interpret(Scope(statements))

    fun interpret(statement: Statement): String = when (statement) {
        is Print -> interpret(statement.argument).toString() + "\n"
        is Scope -> env.inScope { statement.statements.joinToString(transform = ::interpret, separator = "") }
        is VarDef -> env.setVar(statement.name, interpret(statement.value)).let { "" }
    }

    private fun interpret(expression: Expression): Int? = when (expression) {
        is NumberExpr -> expression.value
        is VarExpr -> env.getVar(expression.name)
    }
}