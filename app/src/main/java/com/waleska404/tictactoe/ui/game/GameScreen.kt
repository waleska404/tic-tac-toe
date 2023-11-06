package com.waleska404.tictactoe.ui.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waleska404.tictactoe.ui.model.GameUIModel
import com.waleska404.tictactoe.ui.model.PlayerType

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    gameId: String,
    userId: String,
    owner: Boolean,
) {
    LaunchedEffect(true) {
        gameViewModel.joinToGame(gameId, userId, owner)
    }

    val game: GameUIModel? by gameViewModel.game.collectAsState()
    val winner: PlayerType? by gameViewModel.winner.collectAsState()

    if(winner != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            val currentWinner = if(winner == PlayerType.FirstPlayer) {
                "player 1"
            } else {
                "player 2"
            }
            Text(text = "Ha ganado el jugador: $currentWinner")
        }
    } else {
        Board(
            game = game,
            onCellClicked = { position -> gameViewModel.onCellClicked(position) },
        )
    }
}

@Composable
fun Board(
    game: GameUIModel?,
    onCellClicked: (Int) -> Unit,
) {
    if (game == null) return
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = game.gameId)

        val status = if (game.isGameReady) {
            if (game.isMyTurn) {
                "Your turn"
            } else {
                "Opponent's turn"
            }
        } else {
            "Esperando Jugador 2"
        }

        Text(text = status)

        Row {
            GameItem(game.board[0]) { onCellClicked(0) }
            GameItem(game.board[1]) { onCellClicked(1) }
            GameItem(game.board[2]) { onCellClicked(2) }
        }
        Row {
            GameItem(game.board[3]) { onCellClicked(3) }
            GameItem(game.board[4]) { onCellClicked(4) }
            GameItem(game.board[5]) { onCellClicked(5) }
        }
        Row {
            GameItem(game.board[6]) { onCellClicked(6) }
            GameItem(game.board[7]) { onCellClicked(7) }
            GameItem(game.board[8]) { onCellClicked(8) }
        }
    }
}

@Composable
fun GameItem(
    playerType: PlayerType,
    onCellClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(12.dp)
            .size(64.dp)
            .border(BorderStroke(2.dp, Color.Black))
            .clickable { onCellClicked() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = playerType.symbol)
    }
}