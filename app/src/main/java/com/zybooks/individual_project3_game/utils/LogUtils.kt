package com.zybooks.individual_project3_game.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream

object LogUtils {
    fun writeGameProgressToFile(
        context: Context,
        currentLevel: Int,
        totalScore: Int,
        timeLeft: Int,
        status: String
    ) {
        val logFileName = "game_progress_log.txt"
        val logData = "Level: $currentLevel, Score: $totalScore, Time Left: $timeLeft seconds, Status: $status\n"

        try {
            val logFile = File(context.filesDir, logFileName)
            val fileOutputStream = FileOutputStream(logFile, true) // Append mode
            fileOutputStream.write(logData.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}