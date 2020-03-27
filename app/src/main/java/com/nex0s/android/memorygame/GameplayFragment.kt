package com.nex0s.android.memorygame

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nex0s.android.memorygame.model.Card
import com.nex0s.android.memorygame.model.Game
import com.nex0s.android.memorygame.model.first
import com.nex0s.android.memorygame.model.second
import kotlinx.android.synthetic.main.fragment_gameplay.*
import kotlinx.coroutines.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

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

        gameContainer.doOnPreDraw {
            val cardHeight = it.height / maxOf(args.size.verticalCount, args.size.horizontalCount)
            val cardWidth = it.width / minOf(args.size.verticalCount, args.size.horizontalCount)
            var index = 0
            for (x in 0 until maxOf(args.size.verticalCount, args.size.horizontalCount)) {
                val ll = LinearLayout(context)
                for (y in 0 until minOf(args.size.verticalCount, args.size.horizontalCount)) {
                    val gameCardView = GameCardView(requireContext())
                    gameCardView.bind(shuffledCards[index])
                    gameCardView.setSize(height = cardHeight, width = cardWidth)
                    gameCardView.setOnCardClickListener(this)
                    ll.addView(gameCardView)
                    index++
                }
                ll.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                gameContainer.addView(ll)
            }
        }
    }

    override fun onDestroy() {
        selectedCardsView.clear()
        super.onDestroy()
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
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
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

                handleTwoCardsRevealed()
            }
        }
    }

    private fun handleTwoCardsRevealed() {
        val isMatch = game.checkCards(selectedCards.first(), selectedCards.second())
        if (isMatch) audioHelper.playMatch() else audioHelper.playMiss()

        doDelayed {
            when {
                !isMatch -> selectedCardsView.forEach { it.flipCard() }
                game.hasWon() -> showVictory()
            }

            selectedCards.clear()
            selectedCardsView.clear()
        }
    }

    private fun doDelayed(function: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(2_000)
            withContext(Dispatchers.Main) {
                function()
            }
        }
    }
}
