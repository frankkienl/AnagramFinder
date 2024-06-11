package nl.frankkie

import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

lateinit var listOfWords: Array<String>

fun main() {
    listOfWords = readFileToStringArray()
    println("ready for action")
    println("number of words: ${listOfWords.size}")
    while (true) {
        println("Enter letters: ")
        val input = readlnOrNull()
        if (input.isNullOrEmpty()) {
            println("Exiting...")
            break
        }
        findMatchingWords(input)
    }
}

fun findMatchingWords(inputString: String) {
    val results = mutableListOf<String>()
    val maxLetters = inputString.length

    val inputLetterCounts = mutableMapOf<Char, Int>()
    inputString.forEach { c ->
        inputLetterCounts[c] = inputLetterCounts.getOrDefault(c, 0) + 1
    }
    val letterCounts = inputLetterCounts.toMap()

    for (someWord in listOfWords) {
        if (someWord.length > maxLetters) {
            continue //too long
        }
        if (someWord.length < 4) {
            continue //too short
        }
        if (!letterCounts.keys.containsAll(someWord.toCharArray().toSet())) {
            continue //contains letters, not in the set.
        }
        if (checkSomeWord(someWord, letterCounts)) {
            results.add(someWord)
        }
    }


    //Show results
    results.sortBy { it.length }
    for (resultString in results) {
        println(resultString)
    }
    println("no. of results: ${results.count()}")
}

fun checkSomeWord(someWord: String, availableLetters: Map<Char, Int>): Boolean {
    val mutableAvailableLetters = availableLetters.toMutableMap()
    for (c in someWord) {
        //Get current amount of available letters, for this specific letter
        //If not in the set, return -1, to signal none are available.
        var countAvailable = mutableAvailableLetters.getOrDefault(c, -1)
        countAvailable -= 1 //use one, so decrease by one, one less available for next iteration.
        if (countAvailable <= -1) { //if below zero, we now used more than available.
            return false //using non-available letter, so return false
        }
        mutableAvailableLetters[c] = countAvailable //write back new value for next iteration.
    }
    //If we haven't used any unavailable letters
    // (we still have letters available, or used exactly the correct amount)
    //Then the word is a possible anagram, return true
    return true
}

fun readFileToStringArray(): Array<String> {
    val fileName = "words_alpha.txt"
    val inputStream = object {}.javaClass.classLoader.getResourceAsStream(fileName)
    if (inputStream != null) {
        val content = InputStreamReader(inputStream, StandardCharsets.UTF_8).use { it.readText() }
        val lines = content.split("\r\n")
        return lines.toTypedArray()
    } else {
        println("File not found!")
    }
    return emptyArray()
}