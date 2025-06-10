
package interpreter

class EvalEnvironment {
    private val scopes: ArrayDeque<MutableMap<String, Int?>> = ArrayDeque(listOf())

    fun enterScope() = scopes.addLast(mutableMapOf())
    fun leaveScope() = scopes.removeLast()

    fun setVar(name: String, value: Int?) {
        val scope = scopes.last()
        scope[name] = value
    }

    fun getVar(name: String): Int? = scopes.findLast { it.containsKey(name) }?.let { it[name] }

    inline fun <T> inScope(crossinline f: () -> T): T {
        enterScope()
        return f().also {
            leaveScope()
        }
    }
}
