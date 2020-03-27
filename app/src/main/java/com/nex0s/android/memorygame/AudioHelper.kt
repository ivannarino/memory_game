package com.nex0s.android.memorygame

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

class AudioHelper(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var backgroundMediaPlayer: MediaPlayer? = null

    fun playMatch() {
        playAudio(R.raw.smb3_match)
    }

    fun playMiss() {
        playAudio(R.raw.smb3_miss)
    }

    fun playFlip() {
        playAudio(R.raw.smb3_flip)
    }

    fun playBackground() {
        backgroundMediaPlayer = MediaPlayer.create(context, R.raw.smb3_bgm)
        backgroundMediaPlayer?.isLooping = true
        backgroundMediaPlayer?.start()
    }

    fun releaseResources() {
        mediaPlayer?.release()
        mediaPlayer = null
        backgroundMediaPlayer?.release()
        backgroundMediaPlayer = null
    }

    private fun playAudio(@RawRes media: Int) {
        mediaPlayer?.let {
            it.stop()
            it.release()
        }
        mediaPlayer = MediaPlayer.create(context, media)
        mediaPlayer?.start()
    }
}