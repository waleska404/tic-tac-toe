package com.waleska404.tictactoe.data.network

import com.google.firebase.database.DatabaseReference
import com.waleska404.tictactoe.data.network.model.GameData
import javax.inject.Inject

class FirebaseService @Inject constructor(
    private val reference: DatabaseReference
) {
    companion object {
        private const val PATH = "games"
    }

    fun createGame(gameData: GameData) {
        val gameReference: DatabaseReference = reference.child(PATH).push()
        gameReference.setValue(gameData)
    }
}