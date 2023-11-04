package com.waleska404.tictactoe.data.network.model

import com.waleska404.tictactoe.ui.model.GameUIModel
import com.waleska404.tictactoe.ui.model.PlayerType
import com.waleska404.tictactoe.ui.model.PlayerUIModel
import java.util.Calendar

data class GameData(
    val board: List<Int?>? = null,
    val gameId: String? = null,
    val player1: PlayerData? = null,
    val player2: PlayerData? = null,
    val playerTurn: PlayerData? = null,
) {
    fun toUIModel(): GameUIModel {
        return GameUIModel(
            board = board?.map { PlayerType.getPlayerById(it) } ?: mutableListOf(),
            gameId = gameId.orEmpty(),
            player1 = player1!!.toUIModel(),
            player2 = player2?.toUIModel(),
            playerTurn = playerTurn!!.toUIModel(),
        )
    }
}


data class PlayerData(
    val userId: String? = Calendar.getInstance().timeInMillis.hashCode().toString(),
    val playerType: Int? = null,
) {
    fun toUIModel(): PlayerUIModel {
        return PlayerUIModel(
            userId = userId.orEmpty(),
            playerType = PlayerType.getPlayerById(playerType),
        )
    }
}