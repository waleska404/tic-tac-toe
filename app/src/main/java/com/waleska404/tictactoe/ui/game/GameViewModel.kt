package com.waleska404.tictactoe.ui.game

import android.util.Log
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

    private var _game = MutableStateFlow<GameUIModel?>(null)
    val game: StateFlow<GameUIModel?> = _game

    private var _winner = MutableStateFlow<PlayerType?>(null)
    val winner: StateFlow<PlayerType?> = _winner

    private var _invalidGameId = MutableStateFlow(false)
    val invalidGameId: StateFlow<Boolean> = _invalidGameId

    fun joinToGame(gameId: String, userId: String, owner: Boolean) {
        this.userId = userId
        if (owner) {
            joinGame(gameId)
        } else {
            joinGameAsGuest(gameId)
        }
    }

    private fun joinGameAsGuest(gameId: String) {
        viewModelScope.launch {
            firebaseService.joinToGame(gameId).take(1).collect { gameInfo ->
                if (gameInfo != null) {
                    val result =
                        gameInfo.copy(player2 = PlayerUIModel(userId, PlayerType.SecondPlayer))
                    firebaseService.updateGame(result.toDataModel())
                } else {
                    _invalidGameId.value = true
                    Log.e(TAG, "GAME INFO WAS NULL! INVALID GAME ID!")
                }
            }
            joinGame(gameId)
        }
    }

    private fun joinGame(gameId: String) {
        viewModelScope.launch {
            firebaseService.joinToGame(gameId).collect { gameInfo ->
                val result = gameInfo?.copy(
                    isGameReady = gameInfo.player2 != null,
                    isMyTurn = isMyTurn(gameInfo.playerTurn)
                )
                _game.value = result
                verifyWinner()
            }
        }
    }

    private fun isMyTurn(playerTurn: PlayerUIModel): Boolean {
        return playerTurn.userId == userId
    }

    fun onCellClicked(position: Int) {
        val currentGame = _game.value ?: return
        if (currentGame.isGameReady && currentGame.board[position] == PlayerType.Empty && isMyTurn(
                currentGame.playerTurn
            )
        ) {
            val newBoard = currentGame.board.toMutableList()
            newBoard[position] = getMyPlayerType()
            viewModelScope.launch {
                firebaseService.updateGame(
                    currentGame.copy(
                        board = newBoard,
                        playerTurn = getEnemyPlayer()!!
                    ).toDataModel()
                )
            }
        }
    }

    private fun verifyWinner() {
        val board = _game.value?.board
        if (board != null && board.size == 9) {
            when {
                playerWon(board, PlayerType.FirstPlayer) -> {
                    _winner.value = PlayerType.FirstPlayer
                }

                playerWon(board, PlayerType.SecondPlayer) -> {
                    _winner.value = PlayerType.SecondPlayer
                }
                else -> {
                    val boardComplete = board.count { it == PlayerType.FirstPlayer || it == PlayerType.SecondPlayer } == 9
                    if(boardComplete) {
                        _winner.value = PlayerType.Empty
                    }
                }
            }
        }
    }

    private fun playerWon(board: List<PlayerType>, playerType: PlayerType): Boolean {
        return when {
            //Row
            (board[0] == playerType && board[1] == playerType && board[2] == playerType) -> true
            (board[3] == playerType && board[4] == playerType && board[5] == playerType) -> true
            (board[6] == playerType && board[7] == playerType && board[8] == playerType) -> true

            //Column
            (board[0] == playerType && board[3] == playerType && board[6] == playerType) -> true
            (board[1] == playerType && board[4] == playerType && board[7] == playerType) -> true
            (board[2] == playerType && board[5] == playerType && board[8] == playerType) -> true

            //Diagonal
            (board[0] == playerType && board[4] == playerType && board[8] == playerType) -> true
            (board[2] == playerType && board[4] == playerType && board[6] == playerType) -> true

            else -> false
        }
    }

    fun getMyPlayerType(): PlayerType {
        return when {
            (game.value?.player1?.userId == userId) -> PlayerType.FirstPlayer
            (game.value?.player2?.userId == userId) -> PlayerType.SecondPlayer
            else -> PlayerType.Empty
        }
    }

    private fun getEnemyPlayer(): PlayerUIModel? {
        return if (game.value?.player1?.userId == userId) {
            game.value?.player2
        } else {
            game.value?.player1
        }
    }

    companion object {
        private val TAG = GameViewModel::class.qualifiedName
    }
}