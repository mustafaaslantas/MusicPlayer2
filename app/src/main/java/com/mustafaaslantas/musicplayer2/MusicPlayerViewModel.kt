package com.mustafaaslantas.musicplayer2

import android.content.res.AssetFileDescriptor
import android.content.res.Resources
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MusicPlayerViewModel : ViewModel() {
    private lateinit var mediaPlayer: MediaPlayer

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    init {
        initializeMediaPlayer()
    }

    fun initializeMediaPlayer() {
        mediaPlayer = MediaPlayer()

        val rawFileNames = Resources.getSystem().assets.list("raw")
        rawFileNames?.forEach { fileName ->
            mediaPlayer.apply {
                reset()
                val descriptor: AssetFileDescriptor = Resources.getSystem().assets.openFd("R/raw/$fileName")
                setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
                prepare()
            }
        }
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value!!

        if (_isPlaying.value == false) {
            mediaPlayer.start()
        } else {
            mediaPlayer.pause()
        }
    }

    override fun onCleared() {
        super.onCleared()
        releaseMediaPlayer()
    }

    private fun releaseMediaPlayer() {
        mediaPlayer.release()
    }
}
