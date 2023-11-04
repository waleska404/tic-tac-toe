package com.waleska404.tictactoe.ui.model

data class GameUIModel(
    val board: List<PlayerType>,
    val player1: PlayerUIModel,
    val player2: PlayerUIModel?,
    val playerTurn: PlayerUIModel,
    val gameId: String
)

data class PlayerUIModel(
    val userId: String,
    val playerType: PlayerType
)


sealed class PlayerType(
    val id: Int,
    val symbol: String
) {
    object FirstPlayer: PlayerType(2, "X")
    object SecondPlayer: PlayerType(3, "O")
    object Empty: PlayerType(0, "")

    companion object {
        fun getPlayerById(id: Int?): PlayerType {
            return when (id) {
                FirstPlayer.id -> FirstPlayer
                SecondPlayer.id -> SecondPlayer
                else -> Empty
            }
        }
    }
}