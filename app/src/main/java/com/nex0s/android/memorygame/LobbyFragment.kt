package com.nex0s.android.memorygame

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.QUEUE_ADD
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.nex0s.android.memorygame.databinding.FragmentLobbyBinding
import com.nex0s.android.memorygame.model.Size
import java.util.*

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
