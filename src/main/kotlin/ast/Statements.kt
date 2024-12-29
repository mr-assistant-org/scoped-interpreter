package ast

sealed interface Statement

data class Print(val argument: Expression) : Statement

data class Scope(val statements: List<Statement>) : Statement

data class VarDef(val name: String, val value: Expression) : Statement
