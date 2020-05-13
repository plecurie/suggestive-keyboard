package com.example.suggestivekeyboardapp.di

import com.example.suggestivekeyboardapp.machinelearning.nlp.LanguageModel
import com.example.suggestivekeyboardapp.machinelearning.nlp.NGrams
import com.example.suggestivekeyboardapp.machinelearning.nlp.RankingModel
import com.example.suggestivekeyboardapp.machinelearning.nlp.StupidBackoffRanking
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import java.io.ObjectInputStream


val predictModule = module {
    single {
        val fileName = "saved_model"

        val fileDescriptor = androidApplication().assets.open(fileName)

         ObjectInputStream(fileDescriptor).use { ois ->
            @Suppress("UNCHECKED_CAST")
            ois.readObject() as LanguageModel
        }
    }

    single {
        NGrams(get())
    }

    single {
        StupidBackoffRanking() as RankingModel
    }
}