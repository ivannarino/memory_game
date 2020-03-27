package com.nex0s.android.memorygame

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.nex0s.android.memorygame.model.Card
import com.nex0s.android.memorygame.model.Game
import kotlinx.android.synthetic.main.fragment_gameplay.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class GameplayFragment : Fragment(), GameCardView.OnCardReveal {

    private val args: GameplayFragmentArgs by navArgs()
    private lateinit var game: Game
    private lateinit var audioHelper: AudioHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.back_nav_button)
            setDisplayHomeAsUpEnabled(true)
            title = "${args.size.horizontalCount} x ${args.size.verticalCount} Game"
        }

        audioHelper = AudioHelper(context)
        lifecycle.addObserver(audioHelper)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gameplay, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button5.setOnClickListener {
            applyFunctionToCards {
                it.reset()
            }

            selectedCards.clear()
            selectedCardsView.clear()
        }

        game = Game(args.size)
        val shuffledCards = game.shuffle()

        var index = 0
        for (x in 0 until args.size.verticalCount) {
            val ll = LinearLayout(context)
            for (y in 0 until args.size.horizontalCount) {
                val gameCardView = GameCardView(requireContext())
                gameCardView.bind(shuffledCards[index])
                gameCardView.setOnCardClickListener(this)
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

    private val selectedCards = mutableListOf<Card>()
    private val selectedCardsView = mutableListOf<GameCardView>()
    override fun onCardReveal(gameCardView: GameCardView, card: Card) {
        when (selectedCards.size) {
            0 -> {
                audioHelper.playFlip()
                selectedCardsView.add(gameCardView)
                selectedCards.add(card)
                gameCardView.flipCard()
            }
            1 -> {
                audioHelper.playFlip()
                selectedCardsView.add(gameCardView)
                selectedCards.add(card)
                gameCardView.flipCard()
                if (selectedCards[0] == selectedCards[1]) {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(500)
                        withContext(Dispatchers.Main) {
                            audioHelper.playMatch()
                            game.addPairFound(selectedCards[0], selectedCards[1])
                            selectedCards.clear()
                            selectedCardsView.clear()
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(500)
                        withContext(Dispatchers.Main) {
                            audioHelper.playMiss()

                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(2_000)
                        withContext(Dispatchers.Main) {
                            selectedCardsView.forEach {
                                it.flipCard()
                            }
                            selectedCards.clear()
                            selectedCardsView.clear()
                        }
                    }
                }
            }
            else -> {
                //throw IllegalStateException("Only two cards can be compared at the same time")
            }
        }
    }
}
