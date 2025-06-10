import Scoped.Companion.scoped
import ast.Statement
import parser.Parser
import kotlin.test.Test
import kotlin.test.assertEquals

class ParserTest {
    @Test
    fun `variable definition`() {
        val input = """
            x = 2
        """.trimIndent()
        val expected = scoped {
            "x" -= 2
        }
        makeTest(input, expected)
    }

    @Test
    fun `print variable`() {
        val input = """
            print x
        """.trimIndent()
        val expected = scoped {
            print("x")
        }
        makeTest(input, expected)
    }

    @Test
    fun `print integer`() {
        val input = """
            print 2
        """.trimIndent()
        val expected = scoped {
            print(2)
        }
        makeTest(input, expected)
    }

    @Test
    fun `several statements`() {
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
    fun `nested scopes`() {
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
    fun `example from test task`() {
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
    fun `complex variables names`() {
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

    @Test
    fun `complex variables values`() {
        val input = """
            x = 12312523
            y = -6567567
            z = 0
        """.trimIndent()
        val expected = scoped {
            "x" -= 12312523
            "y" -= -6567567
            "z" -= 0
        }
        makeTest(input, expected)
    }

    @Test
    fun `statements can be separated with any whitespace`() {
        val input = """
            x = 1 y = 3 ${"\t"} print y 
                scope { print x } z = 56 print z
        """.trimIndent()
        val expected = scoped {
            "x" -= 1
            "y" -= 3
            print("y")
            scope {
                print("x")
            }
            "z" -= 56
            print("z")
        }
        makeTest(input, expected)
    }

    @Test
    fun `inconsistent whitespaces`() {
        val input = """
            x=2
            x=-2
            y            =         3
            z
            ${"\r"}
            =
            
                34
                print    
                    x
            scope{print 2}
            scope
            
            {
            z = 3
            
            print z}
            scope{scope {}}
        """.trimIndent()
        val expected = scoped {
            "x" -= 2
            "x" -= -2
            "y" -= 3
            "z" -= 34
            print("x")
            scope {
                print(2)
            }
            scope {
                "z" -= 3
                print("z")
            }
            scope { scope {} }
        }
        makeTest(input, expected)
    }

    private fun makeTest(input: String, expected: MutableList<Statement>) {
        val actual = Parser(input).statements()
        assertEquals(expected, actual)
    }
}


