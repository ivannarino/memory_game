package com.nex0s.android.memorygame.ui

import android.app.Dialog
import android.os.Bundle
import android.text.format.DateUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nex0s.android.memorygame.R

class VictoryDialogFragment : DialogFragment() {

    private val args: VictoryDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.victory_title))
            .setMessage(getString(R.string.victory_msg, args.tries, DateUtils.formatElapsedTime(args.elapsedTime / 1000)))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.victory_return_to_lobby)) { _, _ ->
                val action =
                    VictoryDialogFragmentDirections.actionVictoryDialogFragmentToLobbyFragment()
                findNavController().navigate(action)
            }
            .create()
    }
}