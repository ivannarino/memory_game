package com.nex0s.android.memorygame

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ViewSwitcher
import androidx.annotation.DrawableRes
import com.tekle.oss.android.animation.AnimationFactory

class GameCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val switcher: ViewSwitcher
    private val frontImage: ImageView
    private val backImage: ImageView

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.view_game_card, this, true)

        switcher = findViewById(R.id.switcher)
        frontImage = findViewById(R.id.frontImage)
        backImage = findViewById(R.id.backImage)

        switcher.inAnimation = AnimationUtils.loadAnimation(context, R.anim.nav_default_pop_enter_anim)
        switcher.outAnimation = AnimationUtils.loadAnimation(context, R.anim.nav_default_pop_exit_anim)
    }

    fun flipCard() {
        AnimationFactory.flipTransition(switcher, AnimationFactory.FlipDirection.LEFT_RIGHT)
    }

    fun reset() {
        if (switcher.displayedChild == 1) {
            AnimationFactory.flipTransition(switcher, AnimationFactory.FlipDirection.LEFT_RIGHT)
        }
    }

    fun bind(@DrawableRes frontDrawable: Int, @DrawableRes backDrawable: Int) {
        frontImage.setImageResource(frontDrawable)
        backImage.setImageResource(backDrawable)
    }

}