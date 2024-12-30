import Scoped.Companion.scoped
import ast.Statement
import interpreter.Interpreter
import kotlin.test.Test
import kotlin.test.assertEquals

class InterpreterTest {
    @Test
    fun `example from test task`() {
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
        makeTest(input, expected)
    }

    @Test
    fun `uninitialized variables are null`() {
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
        makeTest(input, expected)
    }

    @Test
    fun scopes() {
        val input = scoped {
            "x" -= 1
            scope {
                "x" -= 2
                scope {
                    "x" -= 4
                    print("x")
                }
                "y" -= 3
                print("x")
            }
            print("x")
            print("y")
        }
        val expected = """
            4
            2
            1
            null
            
        """.trimIndent()
        makeTest(input, expected)
    }

    private fun makeTest(input: MutableList<Statement>, expected: String) {
        val actual = Interpreter().interpret(input)
        assertEquals(expected, actual)
    }
}


