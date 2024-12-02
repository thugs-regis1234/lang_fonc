package org.example

import java.io.File

fun interpret1(args: Array<String>) {
    when {
        args.isEmpty() || args[0].isEmpty() -> processInput()
        args.size == 1 -> handleSingleArgument(args[0])
        else -> println("Multiple arguments provided: ${args.joinToString(", ")}")
    }
}

fun processInput() {
    generateSequence {
        println("Welcome to Stellar! Look at the sky with us")
        readlnOrNull()
    }.takeWhile { !it.isNullOrEmpty() }
        .map { input -> "You entered: $input" }
        .toList() // Force evaluation of the sequence
        .joinToString("\n")
        .let { result -> println(result) }
}

fun handleSingleArgument(fileName: String) {
    runCatching {
        File(fileName).readText()
    }.onSuccess { fileContent ->
        println("File content of $fileName:\n$fileContent")
    }.onFailure { e ->
        println("Error opening file: ${e.message}")
    }
}


fun lexer(source: String): List<Token>? {
    val tokens = mutableListOf<Token>()
    val char = source.toCharArray()
    return null

}
