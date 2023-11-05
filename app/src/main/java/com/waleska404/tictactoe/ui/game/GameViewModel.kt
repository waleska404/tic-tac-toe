package com.waleska404.tictactoe.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waleska404.tictactoe.data.network.FirebaseService
import com.waleska404.tictactoe.ui.model.GameUIModel
import com.waleska404.tictactoe.ui.model.PlayerType
import com.waleska404.tictactoe.ui.model.PlayerUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val firebaseService: FirebaseService
) : ViewModel() {

    private lateinit var userId: String

    private var  _game = MutableStateFlow<GameUIModel?>(null)
    val game: StateFlow<GameUIModel?> = _game

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
                val result = gameInfo?.copy(isGameReady = gameInfo.player2 != null)
                _game.value = result
            }
        }
    }

    private fun joinGameAsGuest(gameId: String) {
        viewModelScope.launch {
            firebaseService.joinToGame(gameId).take(1).collect { gameInfo ->
                if(gameInfo != null) {
                    val result = gameInfo.copy(player2 = PlayerUIModel(userId, PlayerType.SecondPlayer))
                    firebaseService.updateGame(result.toDataModel())
                }
            }
            viewModelScope.launch {
                firebaseService.joinToGame(gameId).collect { gameInfo ->
                    val result = gameInfo?.copy(isGameReady = gameInfo.player2 != null)
                    _game.value = result
                }
            }
        }
    }
}