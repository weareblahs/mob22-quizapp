package com.quizapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp: Application() {
    @Inject
    override fun onCreate() {
        super.onCreate()
    }
}