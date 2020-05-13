package com.example.suggestivekeyboardapp.machinelearning.nlp

interface RankingModel {
    fun rank(
        languageModel: LanguageModel,
        history: String,
        modelOrder: Int,
        order: Int
    ): Map<Char, Float>
}