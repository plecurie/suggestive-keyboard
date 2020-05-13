package com.example.suggestivekeyboardapp

import android.app.Application
import com.example.suggestivekeyboardapp.di.predictModule
import org.koin.android.ext.android.startKoin

class SmartKeyboardApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // start Koin!
        startKoin(this, listOf(predictModule))
    }
}
