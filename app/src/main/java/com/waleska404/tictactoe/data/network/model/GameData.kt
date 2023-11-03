package com.waleska404.tictactoe.data.network.model

import java.util.Calendar

data class GameData(
    val board: List<Int?>? = null,
    val gameId: String? = null,
    val player1: PlayerData? = null,
    val player2: PlayerData? = null,
    val playerTurn: PlayerData? = null,
)


data class PlayerData(
    val userId: String? = Calendar.getInstance().timeInMillis.hashCode().toString(),
    val playerType: Int? = null,
)