import kotlin.system.measureTimeMillis
import kotlin.test.Test
import kotlin.test.assertEquals

class PerformanceTest {
    private fun makeDeep(depth: Int): String {
        return """
            x = 1
            scope {
                y = 2
                
        """.trimIndent().repeat(depth) +
                "print x" +
                """
                
                        print y
                    }
                """.trimIndent().repeat(depth)
    }

    @Test
    fun `deep scopes`() {
        val depth = 500
        val program = makeDeep(depth)
        val expected = "1\n" + "2\n".repeat(depth)
        var actual: String
        val time = measureTimeMillis {
            actual = runScoped(program)
        }
        println("Elapsed time: $time ms")
        assertEquals(expected, actual)
    }

    @Test
    fun `long program`() {
        val length = 100000
        val program = """
            x = 2
            scope {
                print x
            }
            
        """.trimIndent().repeat(length)
        val expected = "2\n".repeat(length)
        var actual: String
        val time = measureTimeMillis {
            actual = runScoped(program)
        }
        println("Elapsed time: $time ms")
        assertEquals(expected, actual)
    }

}


