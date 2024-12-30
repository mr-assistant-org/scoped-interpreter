import interpreter.Evaluator
import parser.Parser
import kotlin.io.path.Path
import kotlin.io.path.readText

fun runScoped(program: CharSequence): String {
    val parser = Parser(program)
    val evaluator = Evaluator()
    val result = evaluator.interpret(parser.statements())
    return result
}

fun main() {
    println(runScoped(Path("example.txt").readText()))
}
