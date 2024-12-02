package com.zybooks.individual_project3_game


import android.app.Application

class GameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize database
        GameDatabase(applicationContext)
    }
}