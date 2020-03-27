package com.nex0s.android.memorygame.ui

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.nex0s.android.memorygame.R
import com.nex0s.android.memorygame.databinding.FragmentLobbyBinding

class LobbyFragment : Fragment(), TextToSpeech.OnInitListener {
    private val TAG = "LobbyFragment"

    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textToSpeech = TextToSpeech(requireActivity().applicationContext, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLobbyBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_lobby, container, false)
        return binding.root
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.speak(getString(R.string.instructions), TextToSpeech.QUEUE_FLUSH, null, "")
        } else {
            Log.e(TAG, "Could not initiate TTS")
        }
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }
}
