package com.zybooks.individual_project3_game


import android.app.Application
import android.content.Context

class GlobalApplication : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}