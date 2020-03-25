package com.nex0s.android.memorygame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.nex0s.android.memorygame.databinding.FragmentLobbyBinding
import com.nex0s.android.memorygame.model.Size

class LobbyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentLobbyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lobby, container, false)
        return binding.root
    }
}
