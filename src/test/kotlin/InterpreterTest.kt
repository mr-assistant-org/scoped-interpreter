import Scoped.Companion.scoped
import ast.Scope
import interpreter.Evaluator
import kotlin.test.Test
import kotlin.test.assertEquals

class InterpreterTest {
    @Test
    fun varDef() {
        val input = scoped {
            "x" -= 1
            print("x")
            scope {
                "x" -= 2
                print("x")
                scope {
                    "x" -= 3
                    "y" -= "x"
                    print("x")
                    print("y")
                }
                print("x")
                print("y")
            }
            print("x")
        }
        val expected = """
            1
            2
            3
            3
            2
            null
            1
            
        """.trimIndent()
        val actual = Evaluator().interpret(Scope(input))
        assertEquals(expected, actual)
    }

    @Test
    fun nullVars() {
        val input = scoped {
            "x" -= "x"
            print("x")
            "y" -= "z"
            print("z")
            print("p")
        }
        val expected = """
            null
            null
            null
            
        """.trimIndent()
        val actual = Evaluator().interpret(Scope(input))
        assertEquals(expected, actual)
    }

    @Test
    fun scopes() {
        val input = scoped {
            "x" -= 1
            scope {
                "x" -= 2
                "y" -= 3
            }
            print("x")
            print("y")
        }
        val expected = """
            1
            null
            
        """.trimIndent()
        val actual = Evaluator().interpret(Scope(input))
        assertEquals(expected, actual)
    }
}


