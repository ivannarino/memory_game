package com.nex0s.android.memorygame

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ViewSwitcher
import com.nex0s.android.memorygame.model.Card
import com.tekle.oss.android.animation.AnimationFactory

class GameCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    interface OnCardReveal {
        fun onCardReveal(gameCardView: GameCardView, card: Card)
    }

    private val switcher: ViewSwitcher
    private val frontImage: ImageView
    private val backImage: ImageView
    private var clickListener: OnCardReveal? = null
    private var card: Card? = null

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.view_game_card, this, true)

        switcher = findViewById(R.id.switcher)
        frontImage = findViewById(R.id.frontImage)
        backImage = findViewById(R.id.backImage)

        switcher.setOnClickListener {
            card?.let {
                if (switcher.displayedChild == 0) {
                    clickListener?.onCardReveal(this, it)
                }
            }
        }
    }

    fun flipCard() {
        AnimationFactory.flipTransition(switcher, AnimationFactory.FlipDirection.LEFT_RIGHT)
    }

    fun setOnCardClickListener(listener: OnCardReveal) {
        this.clickListener = listener
    }

    fun reset() {
        if (switcher.displayedChild == 1) {
            AnimationFactory.flipTransition(switcher, AnimationFactory.FlipDirection.LEFT_RIGHT)
        }
    }

    fun bind(card: Card) {
        this.card = card
        frontImage.setImageResource(card.frontImage)
        backImage.setImageResource(card.backImage)
    }
}