package com.waleska404.tictactoe.data.network

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.snapshots
import com.waleska404.tictactoe.data.network.model.GameData
import com.waleska404.tictactoe.ui.model.GameUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirebaseService @Inject constructor(
    private val reference: DatabaseReference
) {
    companion object {
        private const val PATH = "games"
    }

    fun createGame(gameData: GameData): String {
        val gameReference: DatabaseReference = reference.child(PATH).push()
        val key = gameReference.key
        val newGame = gameData.copy(gameId = key)
        gameReference.setValue(newGame)
        return newGame.gameId.orEmpty()
    }

    fun joinToGame(gameId: String): Flow<GameUIModel?> {
        return reference.database.reference.child("$PATH/$gameId").snapshots.map { dataSnapshot ->
            dataSnapshot.getValue(GameData::class.java)?.toUIModel()
        }
    }

    fun updateGame(gameData: GameData) {
        if(gameData.gameId != null) {
            reference.child(PATH).child(gameData.gameId).setValue(gameData)
        }
    }
}