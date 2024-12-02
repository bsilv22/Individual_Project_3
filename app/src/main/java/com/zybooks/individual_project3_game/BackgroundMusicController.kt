package com.zybooks.individual_project3_game

import android.content.Context
import android.media.MediaPlayer

class BackgroundMusicController(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.background_music)
            mediaPlayer?.apply {
                isLooping = true  // Makes the music loop
                setVolume(1f, 1f)  // Sets volume (0.0 to 1.0)
                start()
            }
        }
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }

    fun resumeMusic() {
        mediaPlayer?.start()
    }

    fun stopMusic() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }
}