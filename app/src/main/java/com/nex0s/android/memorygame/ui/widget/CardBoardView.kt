package com.nex0s.android.memorygame.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.core.view.doOnPreDraw
import com.nex0s.android.memorygame.business.Card
import com.nex0s.android.memorygame.business.Size
import com.nex0s.android.memorygame.business.first
import com.nex0s.android.memorygame.business.second
import java.lang.ref.WeakReference

class CardBoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs), GameCardView.OnCardReveal {

    private val revealedCards = mutableListOf<Card>()
    private val revealedCardsView = mutableListOf<WeakReference<GameCardView>>()

    interface OnCardReveal {
        fun onCardReveal()
    }

    interface OnTwoCardsReveal {
        fun onTwoCardsReveal(first: Card, second :Card)
    }

    private var onCardRevealListener : OnCardReveal? = null
    private var onTwoCardRevealListener : OnTwoCardsReveal? = null

    fun setOnCardReveal(onCardRevealListener : OnCardReveal) {
        this.onCardRevealListener = onCardRevealListener
    }

    fun setOnTwoCardReveal(onTwoCardRevealListener : OnTwoCardsReveal) {
        this.onTwoCardRevealListener = onTwoCardRevealListener
    }

    fun bind(cards: List<Card>, size: Size) {
        doOnPreDraw {
            // Settings optimal orientation
            val cardHeight = height / maxOf(size.verticalCount, size.horizontalCount)
            val cardWidth = width / minOf(size.verticalCount, size.horizontalCount)
            var index = 0
            for (x in 0 until maxOf(size.verticalCount, size.horizontalCount)) {
                val ll = LinearLayout(context)
                for (y in 0 until minOf(size.verticalCount, size.horizontalCount)) {
                    val gameCardView = GameCardView(context)
                    gameCardView.bind(cards[index])
                    gameCardView.setSize(height = cardHeight, width = cardWidth)
                    gameCardView.setOnCardClickListener(this)
                    ll.addView(gameCardView)
                    index++
                }
                ll.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                addView(ll)
            }
        }
    }

    fun flipRevealedCards() {
        revealedCardsView.forEach {
            it.get()?.flipCard()
        }

        revealedCards.clear()
        revealedCardsView.clear()
    }

    fun leaveRevealedCards() {
        revealedCards.clear()
        revealedCardsView.clear()
    }

    override fun onCardReveal(gameCardView: GameCardView, card: Card) {
        when (revealedCards.size) {
            0 -> {
                onCardRevealListener?.onCardReveal()
                revealedCardsView.add(WeakReference(gameCardView))
                revealedCards.add(card)
                gameCardView.flipCard()
            }
            1 -> {
                onCardRevealListener?.onCardReveal()
                revealedCardsView.add(WeakReference(gameCardView))
                revealedCards.add(card)
                gameCardView.flipCard()

                onTwoCardRevealListener?.onTwoCardsReveal(revealedCards.first(), revealedCards.second())
            }
        }
    }
}