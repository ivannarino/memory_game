package com.nex0s.android.memorygame

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*

class AudioHelper(private val context: Context) : LifecycleObserver {
    private var mediaPlayer: MediaPlayer? = null
    private var backgroundMediaPlayer: MediaPlayer? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        backgroundMediaPlayer = MediaPlayer.create(context, R.raw.smb3_bgm)
        backgroundMediaPlayer?.isLooping = true
        backgroundMediaPlayer?.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        backgroundMediaPlayer?.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        backgroundMediaPlayer?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun releaseResources() {
        mediaPlayer?.release()
        mediaPlayer = null
        backgroundMediaPlayer?.release()
        backgroundMediaPlayer = null
    }

    fun playMatch() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            withContext(Dispatchers.Main) {
                playAudio(R.raw.smb3_match)
            }
        }
    }

    fun playMiss() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            withContext(Dispatchers.Main) {
                playAudio(R.raw.smb3_miss)
            }
        }
    }

    fun playFlip() {
        playAudio(R.raw.smb3_flip)
    }

    fun playVictory() {
        playAudio(R.raw.smb3_1up)
    }

    fun stopBackground() {
        backgroundMediaPlayer?.pause()
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