package com.waleska404.tictactoe.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waleska404.tictactoe.data.network.FirebaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val firebaseService: FirebaseService
) : ViewModel() {

    private lateinit var userId: String

    fun joinToGame(gameId: String, userId: String, owner: Boolean) {
        this.userId = userId
        if(owner) {
            joinGameAsOwner(gameId)
        } else {
            joinGameAsGuest(gameId)
        }
    }

    private fun joinGameAsOwner(gameId: String) {
        viewModelScope.launch {
            firebaseService.joinToGame(gameId).collect { gameInfo ->
                Log.i("MYTAG", gameInfo.toString())
            }
        }
    }

    private fun joinGameAsGuest(gameId: String) {
        TODO("Not yet implemented")
    }
}