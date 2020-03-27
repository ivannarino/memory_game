package com.nex0s.android.memorygame

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.ViewSwitcher
import androidx.core.view.setPadding
import com.nex0s.android.memorygame.model.Card
import com.tekle.oss.android.animation.AnimationFactory

class GameCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewSwitcher(context, attrs) {

    interface OnCardReveal {
        fun onCardReveal(gameCardView: GameCardView, card: Card)
    }

    private val frontImage: ImageView
    private val backImage: ImageView
    private var clickListener: OnCardReveal? = null
    private var card: Card? = null

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.view_game_card, this, true)

        frontImage = findViewById(R.id.frontImage)
        backImage = findViewById(R.id.backImage)

        setOnClickListener {
            card?.let {
                if (displayedChild == 0) {
                    clickListener?.onCardReveal(this, it)
                }
            }
        }
    }

    fun flipCard() {
        AnimationFactory.flipTransition(this, AnimationFactory.FlipDirection.LEFT_RIGHT)
    }

    fun setOnCardClickListener(listener: OnCardReveal) {
        this.clickListener = listener
    }

    fun bind(card: Card) {
        this.card = card
        frontImage.setImageResource(card.frontImage)
        backImage.setImageResource(card.backImage)
    }

    fun setSize(width: Int = WRAP_CONTENT, height: Int = WRAP_CONTENT) {
        layoutParams = LayoutParams(width, height, Gravity.CENTER)
        setPadding(resources.getDimensionPixelSize(R.dimen.mat_4dp))
    }
}