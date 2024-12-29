import ast.NumberExpr
import ast.Print
import ast.Scope
import ast.VarExpr
import ast.VarDef
import parser.Parser
import kotlin.test.Test
import kotlin.test.assertEquals

class CommonTests {
    @Test
    fun testPrint() {
        val example = Scope(
            listOf(
                VarDef("x", NumberExpr(1)),
                Print(VarExpr("x")),
                Scope(listOf(
                    VarDef("x", NumberExpr(2)),
                    Print(VarExpr("x")),
                ))
            )
        )
        println(example)
    }

    @Test
    fun varDef() {
        val input = """
            x = 2
        """.trimIndent()
        val parser = Parser(input)
        println(parser.statements())
    }

    @Test
    fun printVar() {
        val input = """
            print x
        """.trimIndent()
        val parser = Parser(input)
        println(parser.statements())
    }

    @Test
    fun printInt() {
        val input = """
            print 2
        """.trimIndent()
        val parser = Parser(input)
        println(parser.statements())
    }

    @Test
    fun several() {
        val input = """
            print 1
            x = 2
            print x
        """.trimIndent()
        val parser = Parser(input)
        println(parser.statements())
    }

    @Test
    fun scope() {
        val input = """
            scope {
                print x
                print 2
            }
        """.trimIndent()
        val parser = Parser(input)
        println(parser.statements())
    }

    @Test
    fun nestedScopes() {
        val input = """
            x = 2
            scope {
                z = 3
                print 2
                scope {
                    print z
                }
                y = 5
                scope {
                    print y
                    scope {
                        print x
                    }
                }
                print x
                print 2
            }
        """.trimIndent()
        val parser = Parser(input)
        println(parser.statements())
    }

    @Test
    fun complex() {
        val input = """
            x = 1
            print x
            scope {
                x = 2
                print x
                scope {
                    x = 3
                    y = x
                    print x
                    print y
                }
                print x
                print y
            }
            print x
        """.trimIndent()
        val parser = Parser(input)
        val actual = parser.statements()
        val expected = scoped {
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
        assertEquals(expected, actual)
    }

    @Test
    fun variableNames() {
        val input = """
            xxx = 1
            jjadsjio = 3
            jJD12 = 3
            zprint = 4
            sscope = 5
        """.trimIndent()
        val parser = Parser(input)
        println(parser.statements())
    }

    @Test
    fun problem() {
        val input = """
            scopea = 2
        """.trimIndent()
        val parser = Parser(input)
        println(parser.statements())
    }
}
