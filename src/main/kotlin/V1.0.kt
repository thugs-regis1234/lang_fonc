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


fun lexer(source: String): List<Token> {
    fun process(index: Int, line: Int, tokens: List<Token>): List<Token> {
        if (index >= source.length) return tokens + Token(TokenType.EOF, "", line) // Fin du traitement

        val char = source[index]
        val (newTokens, newIndex, newLine) = when (char) {
            '(' -> listOf(Token(TokenType.LEFT_PAREN, "(", line)) to (index + 1) to line
            ')' -> listOf(Token(TokenType.RIGHT_PAREN, ")", line)) to (index + 1) to line
            '+' -> listOf(Token(TokenType.PLUS, "+", line)) to (index + 1) to line
            '-' -> listOf(Token(TokenType.MINUS, "-", line)) to (index + 1) to line
            '*' -> listOf(Token(TokenType.STAR, "*", line)) to (index + 1) to line
            '/' -> listOf(Token(TokenType.SLASH, "/", line)) to (index + 1) to line
            '{' -> listOf(Token(TokenType.LEFT_BRACE, "{", line)) to (index + 1) to line
            '}' -> listOf(Token(TokenType.RIGHT_BRACE, "}", line)) to (index + 1) to line
            '\n' -> emptyList<Token>() to (index + 1) to (line + 1) // Incrémente la ligne pour les sauts de ligne
            else -> when {
                char.isWhitespace() -> emptyList<Token>() to (index + 1) to line
                char.isDigit() || char == '.' -> {
                    val startIndex = index
                    val number = source.substring(startIndex, source.length).takeWhile { it.isDigit() || it == '.' }
                    listOf(Token(TokenType.NUMBER, number, line)) to (startIndex + number.length) to line
                }
                char.isLetter() -> {
                    val startIndex = index
                    val identifier = source.substring(startIndex, source.length).takeWhile { it.isLetterOrDigit() }
                    val tokenType = when (identifier) {
                        "if" -> TokenType.IF
                        "else" -> TokenType.ELSE
                        "while" -> TokenType.WHILE
                        else -> TokenType.IDENTIFIER
                    }
                    listOf(Token(tokenType, identifier, line)) to (startIndex + identifier.length) to line
                }
                char == '=' -> {
                    if (index + 1 < source.length && source[index + 1] == '=') {
                        listOf(Token(TokenType.EQUAL_EQUAL, "==", line)) to (index + 2) to line
                    } else {
                        listOf(Token(TokenType.EQUAL, "=", line)) to (index + 1) to line
                    }
                }
                char == '!' -> {
                    if (index + 1 < source.length && source[index + 1] == '=') {
                        listOf(Token(TokenType.BANG_EQUAL, "!=", line)) to (index + 2) to line
                    } else {
                        listOf(Token(TokenType.BANG, "!", line)) to (index + 1) to line
                    }
                }
                else -> throw IllegalArgumentException("Unexpected character: $char at line $line")
            }
        }


        return process(newIndex, newLine, tokens + newTokens)
    }

    return process(0, 1, emptyList())
}
