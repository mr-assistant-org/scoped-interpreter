package ast

sealed interface Expression

data class NumberExpr(val value: Int) : Expression

data class VarExpr(val name: String) : Expression
