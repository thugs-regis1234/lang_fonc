package org.example

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.io.File


// Definit le type TOKEN

enum class TokenType {
    //Single-character tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, COMMA, DOT, MINUS, PLUS, STAR,

    // One or two character tokens
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals
    IDENTIFIER, STRING, NUMBER,

    // Keywords
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR, PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE, EOF
}



data class Token(
    val type: TokenType,
    val lexeme: String,
    val literal: Any? = null,
    val line: Int
) {
    // etendre la fonction tostring
    override fun toString(): String {
        return "Token(type=$type, lexeme='$lexeme', literal=$literal, line=$line)"
    }
}


//Opener de l'interpreteur

fun interpret(args: Array<String>) {
    if (args.isEmpty() || args[0] == "") {
        while (true) {
            println("Welcome to Stellar! Look at the sky with us")
            val input = readlnOrNull()
            if (!input.isNullOrEmpty()) {
                println("You entered: $input")
            }
        }
    } else {
        if (args.size == 1) {
            val fileName = args[0]
            try {
                val fileContent = File(fileName).readText()
                println("File content of $fileName:\n$fileContent")
            } catch (e: Exception) {
                println("Error opening file: ${e.message}")
            }
        } else {
            println("Multiple arguments provided: ${args.joinToString(", ")}")
        }
    }
}

fun sentSplitter(sentence: String): Array<String> {
    // Vérifier si la phrase n'est pas vide ou nulle
    if (sentence.isBlank()) {
        return arrayOf() // Retourne un tableau vide si la chaîne est vide ou composée d'espaces
    }

    // Diviser la phrase en utilisant un ou plusieurs espaces comme séparateurs
    return sentence.split("\\s+".toRegex()).toTypedArray()
}

fun String.isNumeric(): Boolean {
    return this.toDoubleOrNull() != null
}

fun tokenize(source: String): List<Token> {
    val tokens = mutableListOf<Token>()
    var line = 1
    val chars = source.toCharArray()
    var index = 0

    while (index < chars.size) {
        when (val char = chars[index]) {
            '(' -> tokens.add(Token(TokenType.LEFT_PAREN, "(", line = line))
            ')' -> tokens.add(Token(TokenType.RIGHT_PAREN, ")", line = line))
            '+' -> tokens.add(Token(TokenType.PLUS, "+", line = line))
            '-' -> tokens.add(Token(TokenType.MINUS, "-", line = line))
            '*' -> tokens.add(Token(TokenType.STAR, "*", line = line))
            //'/' -> tokens.add(Token(TokenType.SLASH, "/", line = line))
            '{' -> tokens.add(Token(TokenType.LEFT_BRACE, "{", line = line))
            '}' -> tokens.add(Token(TokenType.RIGHT_BRACE, "}", line = line))
            '\n' -> line++ // Gérer les sauts de ligne
            else -> if (char.isWhitespace()) {
                // Ignorer les espaces
            } else {
                // Identifier un identifiant, mot-clé ou littéral

                val start = index
                while (index < chars.size && !chars[index].isWhitespace() && chars[index] !in "(){}") {
                    index++
                }
                val text = source.substring(start, index)

                val tokenType = when {
                    text.isNumeric() -> TokenType.NUMBER
                    else -> TokenType.IDENTIFIER
                }

                tokens.add(Token(tokenType, text, line = line))
                index--
            }
        }
        index++
    }

    // Ajouter un token EOF à la fin
    tokens.add(Token(TokenType.EOF, "", line = line))
    return tokens
}







fun main(args:Array<String>) {
    interpret(args)
    println("success")
    val words = sentSplitter("pas de caviard  pour toi")
    println(words.joinToString(", "))
    val source = "( a + b ) - 123"
    val tokens = tokenize(source)
    tokens.forEach { println(it) }
}

