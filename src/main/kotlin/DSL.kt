import ast.NumberExpr
import ast.Print
import ast.Scope
import ast.Statement
import ast.VarDef
import ast.VarExpr

class Scoped {
    private val statements = mutableListOf<Statement>()

    fun scope(block: Scoped.() -> Unit) {
        statements += Scope(Scoped().apply(block).statements)
    }

    operator fun String.minusAssign(value: String) {
        statements += VarDef(this, VarExpr(value))
    }

    operator fun String.minusAssign(value: Int) {
        statements += VarDef(this, NumberExpr(value))
    }

    fun print(value: Int) {
        statements += Print(NumberExpr(value))
    }

    fun print(name: String) {
        statements += Print(VarExpr(name))
    }

    companion object {
        fun scoped(block: Scoped.() -> Unit) = Scoped().also(block).statements
    }
}

