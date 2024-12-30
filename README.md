# Scoped programming language interpreter

In the "Scoped" programming language there are the following operations allowed:

1. `x = 99` (syntax: \<name> = \<integer value>) — assign a variable to some integer value.
2. `x = y` (syntax: \<name> = \<another name>) — assign a variable to some other variable's value.
3. `scope {` — open a scope
4. `}` — exit the last opened scope.
5. `print x` (syntax: print \<variable name>) — prints the variable's name on the screen or prints "null" if the variable doesn't exist.

It is allowed to re-assign existing variables (you can write `x = 5`, and then `x = 7`).  All declarations and re-assignments of the variables inside any scope (including all nested scopes) disappear after you exit the scope.

---

This is the formal grammar of the Scoped language:
```
stmts -> stmt*
stmt -> varDef | print | scope
varDef -> VAR_ID '=' expr
expr -> NUMBER | VAR_ID
print -> PRINT VAR_ID
scope -> SCOPE '{' stmts '}'

PRINT -> 'print(?![A-Za-z_0-9])'
SCOPE -> 'scope(?![A-Za-z_0-9])'
VAR_ID -> '[a-z][A-Za-z_0-9]*'
NUMBER -> '[0-9]+'
WS -> '[ \t\r\n]+'
```

Whitespaces described by `WS` terminal are consumed implicitly after each terminal. Start non-terminal is `stmts`.

It is easy to prove formally that this grammar is LL(1). It means that pretty much every approach to parsing such language will work.

I reviewed 3 options for parser:
1. Usage of parser generator like ANTLR
2. Writing parser combinator
3. Writing simple recursive descent parser

I chose the third option because it allows to show coding skills better (as opposed to parser generator) and is more native for Kotlin than parser combinators that are usually used in functional programming languages.

## How to run code

Change `example.txt` as you wish. Run `MainKt.main` to interpret code in `example.txt`, result will be printed out in standard output. Or just run tests.

## Implementation details

The code is separated into 4 main parts:
* `ast` package contains description of abstract syntax tree of the Scoped language
* `lexer` package contains lexer that is used by parser
* `parser` package contains parser that accepts input as CharSequence and return AST
* `interpreter` package contains interpreter that accepts Scoped AST and returns String

## Implementation features and pitfalls

* Undefined variables are always null. That means that following program prints three nulls:
    ```
    x = x
    z = p
    print x
    print y
    print z
    ```
* Print statements can be used not only with variables but also with integer literals. The following code prints 3:
    ```
    print 3
    ```
* Statements of the language (prints, variable assignments and scopes) can be separated not only by newlines, but also by spaces, `\t` and `\r`. In other words by everything that is matched by regex `[ \t\r\n]+`. That means that following code compiles and prints `3\n2`:
    ```
    x = 3 print x scope { print 2 }
    ```
* Whitespaces can be used arbitrarily in Scoped source code. The following program prints `-2\n2\n34`:
    ```
    x=2   x=-2
    y            =         3
    z
    =
    34
    print    
    x
    scope{print 2}
    scope
    
    {
    
    print z}
    scope{scope {}}
    ```
* After keywords only symbols that are not matched by regex `[A-Za-z_0-9]` can be used. It means that the following code compiles:
    ```
    printa = 2
    scopea = 4
    scope{print scopea}
    ```
  But none of the following statements:
    ```
    print= 2
    scope= 4
    print1
    ```

## Possible improvements

* In parser, lexer and evaluator BufferedReader and BufferedWriter could be used instead of CharSequence and String for performance
* For the sake of performance some of the terminal patterns could be made into plain strings or even chars, as comparing sequence to have certain prefix is faster than matching a regex
* More tests could be written, especially auto generated integration tests
