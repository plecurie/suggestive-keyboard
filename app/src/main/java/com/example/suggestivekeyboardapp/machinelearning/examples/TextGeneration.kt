package com.example.suggestivekeyboardapp.machinelearning.examples

import arrow.core.Try
import arrow.core.getOrElse
import com.example.suggestivekeyboardapp.machinelearning.nlp.*
import java.io.*

suspend fun main(args: Array<String>) {
    if(args.isEmpty()){
        println("Usage: <text_file_in_resources> <save_dir> <ngram_order> [evaluate]")
        println("Example: './app/src/main/assets/test.txt' './app/src/main/assets/saved_model' 4 true")
        return
    }

    val data = File(args[0]).readText()
    val modelFile = args[1]
    val order = args[2].toInt()
    val evaluate = args.getOrElse(3) { "false" }.toBoolean()
    val processedData = object : DataPreprocessor {}.processData(order, data)

    val nGrams = NGrams(StupidBackoffRanking())

    // Load or train the model
    val lm = loadModel(modelFile).getOrElse {
        val model = trainModel(nGrams, processedData, order)
        saveModel(model, modelFile)
        model
    }

    val size = lm.filterKeys { it.length == order }.size
    println("Model numbers:")
    println("$order-ngrams -> $size")

    if (evaluate) {
        val lm2 = trainModel(nGrams, processedData, order - 1)
        val lm3 = trainModel(nGrams, processedData, order + 1)
        println("Perplexity ${perplexity(
            lm,
            "life: three stories of love, lust, and liberation",
            order
        )}")
        println("Perplexity model ${order - 1} ${perplexity(
            lm2,
            "life: three stories of love, lust, and liberation",
            order
        )}")
        println("Perplexity model ${order + 1} ${perplexity(
            lm3,
            "life: three stories of love, lust, and liberation",
            order
        )}")
        println()
    }

    // Inference
    val nLetters = 100
    println(nGrams.generateText(lm, order, nLetters, "l"))
    println("########")
    println(nGrams.generateText(lm, order, nLetters, "st"))
    println("########")
    println(nGrams.generateText(lm, order, nLetters, "la "))
    println("########")
    println(nGrams.generateText(lm, order, nLetters, "cha"))
    println("########")
    println(nGrams.generateText(lm, order, nLetters, "c"))
    println("########")

    println(nGrams.generateText(lm, order, nLetters, "sta".toLowerCase()))
    println("########")
    println(nGrams.generateText(lm, order, nLetters, "sta".toLowerCase()))
    println("########")
    println(nGrams.generateText(lm, order, nLetters, "sta".toLowerCase()))
    println("########")
    println(nGrams.generateText(lm, order, nLetters, "sta".toLowerCase()))
    println("########")
    println(nGrams.generateText(lm, order, nLetters, "sta".toLowerCase()))
}

private suspend fun trainModel(nGrams: NGrams, data: String, order: Int) = nGrams.train(data, order)

private fun saveModel(languageModel: LanguageModel, modelFile: String) {
    ObjectOutputStream(FileOutputStream(modelFile)).use { it -> it.writeObject(languageModel) }
}

@Suppress("UNCHECKED_CAST")
private fun loadModel(file: String): Try<LanguageModel> {
    return Try {
        ObjectInputStream(FileInputStream(file)).use { it ->
            it.readObject() as LanguageModel
        }
    }
}
