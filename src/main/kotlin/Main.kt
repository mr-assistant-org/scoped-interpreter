import interpreter.Interpreter
import parser.Parser
import kotlin.io.path.Path
import kotlin.io.path.readText

fun runScoped(program: CharSequence): String {
    val parser = Parser(program)
    val interpreter = Interpreter()
    val result = interpreter.interpret(parser.statements())
    return result
}

fun main() {
    println(runScoped(Path("example.txt").readText()))
}
