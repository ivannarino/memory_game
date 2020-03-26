package com.nex0s.android.memorygame

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.nex0s.android.memorygame.model.Game
import kotlinx.android.synthetic.main.fragment_gameplay.*

class GameplayFragment : Fragment() {

    private val args: GameplayFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gameplay, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.back_nav_button)
            setDisplayHomeAsUpEnabled(true)
            title = "${args.size.horizontalCount} x ${args.size.verticalCount} Game"
        }

        button5.setOnClickListener {
            applyFunctionToCards {
                it.reset()
            }
        }

        val game = Game(args.size)
        val shuffledCards = game.shuffle()

        var index = 0
        for (x in 0 until args.size.verticalCount) {
            val ll = LinearLayout(context)
            for (y in 0 until args.size.horizontalCount) {
                val gameCardView = GameCardView(requireContext())
                gameCardView.bind(shuffledCards[index].image, R.drawable.all_card_backs)
                gameCardView.setOnClickListener {
                    (it as GameCardView).flipCard()
                }
                ll.addView(gameCardView)

                index++
            }
            ll.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            gameContainer.addView(ll)
        }
    }

    private fun applyFunctionToCards(function: (GameCardView) -> Unit) {
        for (x in 0 until args.size.verticalCount) {
            val row = gameContainer.getChildAt(x) as ViewGroup
            for (y in 0 until args.size.horizontalCount) {
                function(row.getChildAt(y) as GameCardView)
            }
        }
    }
}
