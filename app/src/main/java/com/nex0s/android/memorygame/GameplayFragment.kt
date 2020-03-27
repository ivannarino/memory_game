package com.nex0s.android.memorygame

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nex0s.android.memorygame.model.Card
import com.nex0s.android.memorygame.model.Game
import com.nex0s.android.memorygame.model.first
import com.nex0s.android.memorygame.model.second
import kotlinx.android.synthetic.main.fragment_gameplay.*
import kotlinx.coroutines.*

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
                if (game.checkCards(selectedCards.first(), selectedCards.second())) {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(500)
                        withContext(Dispatchers.Main) {
                            audioHelper.playMatch()

                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(2_000)
                        withContext(Dispatchers.Main) {
                            selectedCards.clear()
                            selectedCardsView.clear()
                            if (game.hasWon()) {
                                audioHelper.stopBackground()
                                audioHelper.playVictory()
                                val action =
                                    GameplayFragmentDirections.actionGameplayFragmentToVictoryDialogFragment()
                                action.tries = game.tries
                                action.elapsedTime = game.elapsedTime
                                findNavController().navigate(action)
                            }
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
        }
    }
}
