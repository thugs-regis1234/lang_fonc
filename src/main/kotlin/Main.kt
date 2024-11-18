package org.example

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.io.File

fun interpOpen(args: Array<String>) {
    if (args.isEmpty() || args[0] == "") {
        while (true) {
            println("Welcome to Stellar! Look at the sky with us")
            val input = readLine()
            if (input != null && input.isNotEmpty()) {
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



fun main(args:Array<String>) {
    interpOpen(args)
    println("success")
}

