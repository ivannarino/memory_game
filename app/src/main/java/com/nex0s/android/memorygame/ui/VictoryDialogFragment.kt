package com.nex0s.android.memorygame.ui

import android.app.Dialog
import android.os.Bundle
import android.text.format.DateUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class VictoryDialogFragment : DialogFragment() {

    private val args: VictoryDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return AlertDialog.Builder(requireActivity())
            .setTitle("Congratulations")
            .setMessage("You tried ${args.tries} times, in ${DateUtils.formatElapsedTime(args.elapsedTime / 1000)}")
            .setCancelable(false)
            .setPositiveButton("Return to lobby") { _, _ ->
                val action =
                    VictoryDialogFragmentDirections.actionVictoryDialogFragmentToLobbyFragment()
                findNavController().navigate(action)
            }
            .create()
    }
}