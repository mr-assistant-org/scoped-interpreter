import ast.Statement
import parser.Parser
import kotlin.test.Test
import kotlin.test.assertEquals

class ParserTest {
    @Test
    fun varDef() {
        val input = """
            x = 2
        """.trimIndent()
        val expected = scoped {
            "x" -= 2
        }
        makeTest(input, expected)
    }

    @Test
    fun printVar() {
        val input = """
            print x
        """.trimIndent()
        val expected = scoped {
            print("x")
        }
        makeTest(input, expected)
    }

    @Test
    fun printInt() {
        val input = """
            print 2
        """.trimIndent()
        val expected = scoped {
            print(2)
        }
        makeTest(input, expected)
    }

    @Test
    fun severalStatements() {
        val input = """
            print 1
            x = 2
            print x
        """.trimIndent()
        val expected = scoped {
            print(1)
            "x" -= 2
            print("x")
        }
        makeTest(input, expected)
    }

    @Test
    fun scope() {
        val input = """
            scope {
                print x
                print 2
            }
        """.trimIndent()
        val expected = scoped {
            scope {
                print("x")
                print(2)
            }
        }
        makeTest(input, expected)
    }

    @Test
    fun nestedScopes() {
        val input = """
            x = 1
            scope {
                x = 2
                scope {
                    x = 3
                    scope {
                        x = 4
                    }
                }
                scope {
                    x = 5
                    scope {
                        x = 6
                    }
                }
                print x
            }
            print x
        """.trimIndent()
        val expected = scoped {
            "x" -= 1
            scope {
                "x" -= 2
                scope {
                    "x" -= 3
                    scope {
                        "x" -= 4
                    }
                }
                scope {
                    "x" -= 5
                    scope {
                        "x" -= 6
                    }
                }
                print("x")
            }
            print("x")
        }
        makeTest(input, expected)
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
        makeTest(input, expected)
    }

    @Test
    fun variableNames() {
        val input = """
            xxx = 1
            jjads_jio = 2
            jJD12 = 3
            zprint = 4
            sscope = 5
            scopeL = 2
            println = 2
        """.trimIndent()
        val expected = scoped {
            "xxx" -= 1
            "jjads_jio" -= 2
            "jJD12" -= 3
            "zprint" -= 4
            "sscope" -= 5
            "scopeL" -= 2
            "println" -= 2
        }
        makeTest(input, expected)
    }

    private fun makeTest(input: String, expected: MutableList<Statement>) {
        val actual = Parser(input).statements()
        assertEquals(expected, actual)
    }
}


