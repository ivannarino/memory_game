package com.nex0s.android.memorygame

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nex0s.android.memorygame.model.Card
import com.nex0s.android.memorygame.model.Game
import kotlinx.android.synthetic.main.fragment_gameplay.*
import kotlinx.coroutines.*
import nl.dionsegijn.konfetti.models.Shape

class GameplayFragment : Fragment(), CardBoardView.OnTwoCardsReveal, CardBoardView.OnCardReveal {

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
        gameContainer.bind(shuffledCards, args.size)
        gameContainer.setOnCardReveal(this)
        gameContainer.setOnTwoCardReveal(this)
    }

    private fun showVictory() {
        audioHelper.stopBackground()
        audioHelper.playVictory()
        val action =
            GameplayFragmentDirections.actionGameplayFragmentToVictoryDialogFragment()
        action.tries = game.tries
        action.elapsedTime = game.elapsedTime
        findNavController().navigate(action)

        viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .addShapes(Shape.Square, Shape.Circle)
            .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
    }

    private fun doDelayed(function: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1_000)
            withContext(Dispatchers.Main) {
                function()
            }
        }
    }

    override fun onTwoCardsReveal(first: Card, second: Card) {
        val isMatch = game.checkCards(first, second)
        if (isMatch) audioHelper.playMatch() else audioHelper.playMiss()

        doDelayed {
            when {
                isMatch && game.hasWon() -> {
                    gameContainer.leaveRevealedCards()
                    showVictory()
                }
                isMatch && !game.hasWon() -> gameContainer.leaveRevealedCards()
                !isMatch -> gameContainer.flipRevealedCards()
            }
        }
    }

    override fun onCardReveal() {
        audioHelper.playFlip()
    }
}
