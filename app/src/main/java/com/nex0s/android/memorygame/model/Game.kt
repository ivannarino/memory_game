package com.nex0s.android.memorygame.model

import androidx.annotation.DrawableRes
import com.nex0s.android.memorygame.R
import java.lang.IllegalStateException

enum class Size(val horizontalCount: Int, val verticalCount: Int) {
    SIZE_3x4(3, 4),
    SIZE_5x2(5, 2),
    SIZE_4x4(4, 4),
    SIZE_4x5(4, 5)
}

val cardDeck = arrayOf(
    Card(R.drawable.memory_bat, R.drawable.all_card_backs),
    Card(R.drawable.memory_cat, R.drawable.all_card_backs),
    Card(R.drawable.memory_cow, R.drawable.all_card_backs),
    Card(R.drawable.memory_dragon, R.drawable.all_card_backs),
    Card(R.drawable.memory_garbage_man, R.drawable.all_card_backs),
    Card(R.drawable.memory_ghost_dog, R.drawable.all_card_backs),
    Card(R.drawable.memory_hen, R.drawable.all_card_backs),
    Card(R.drawable.memory_horse, R.drawable.all_card_backs),
    Card(R.drawable.memory_pig, R.drawable.all_card_backs),
    Card(R.drawable.memory_spider, R.drawable.all_card_backs)
)

class Game(private val size: Size) {

    private val foundPairs = mutableListOf<Card>()

    fun shuffle(): List<Card> {
        val deckCards = cardDeck.toMutableList()

        val totalCards = size.horizontalCount * size.verticalCount
        val cardPairs = totalCards / 2

        val pickedCards = mutableListOf<Card>()

        for (i in 0 until cardPairs) {
            val pickedCard = deckCards.random()
            deckCards.remove(pickedCard)

            pickedCards.add(pickedCard)
            pickedCards.add(pickedCard)
        }

        pickedCards.shuffle()

        return pickedCards
    }

    fun isPairFound(card: Card): Boolean {
        return foundPairs.contains(card)
    }

    fun addPairFound(card1: Card, card2: Card) {
        if (foundPairs.contains(card1) || foundPairs.contains(card2)) {
            throw IllegalStateException("Cannot add a pair if the pair has already been found")
        }

        foundPairs.add(card1)
        foundPairs.add(card2)
    }
}

data class Card(@DrawableRes val frontImage: Int, @DrawableRes val backImage: Int)