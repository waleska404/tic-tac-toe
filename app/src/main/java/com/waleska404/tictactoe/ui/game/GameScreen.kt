package com.waleska404.tictactoe.ui.game

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waleska404.tictactoe.ui.model.GameUIModel
import com.waleska404.tictactoe.ui.model.PlayerType
import com.waleska404.tictactoe.ui.theme.Background
import com.waleska404.tictactoe.ui.theme.BlueLink
import com.waleska404.tictactoe.ui.theme.PrimaryBlack
import com.waleska404.tictactoe.ui.theme.PrimaryGrey
import com.waleska404.tictactoe.ui.theme.SecondaryGrey

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    gameId: String,
    userId: String,
    owner: Boolean,
    navigateToHome: () -> Unit,
) {
    LaunchedEffect(true) {
        gameViewModel.joinToGame(gameId, userId, owner)
    }

    val game: GameUIModel? by gameViewModel.game.collectAsState()
    val winner: PlayerType? by gameViewModel.winner.collectAsState()

    if (winner != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PrimaryGrey,
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(text = "¡FELICIDADES!", color = PrimaryBlack, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    val currentWinner = if (winner == PlayerType.FirstPlayer) {
                        "player 1"
                    } else {
                        "player 2"
                    }
                    Text(text = "Ha ganado el jugador:", fontSize = 22.sp, color = PrimaryBlack)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = currentWinner, fontSize = 26.sp, color = PrimaryBlack, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { navigateToHome() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryGrey,
                        ),
                    ) {
                        Text(
                            text = "Back Home",
                            color = PrimaryBlack,
                            fontSize = 18.sp
                        )
                    }
                }
            }
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

    val clipboard: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = game.gameId,
            color = BlueLink,
            modifier = Modifier
                .padding(24.dp)
                .clickable {
                    clipboard.setText(AnnotatedString(game.gameId))
                    Toast
                        .makeText(context, "Copiado", Toast.LENGTH_SHORT)
                        .show()
                }
        )

        val status = if (game.isGameReady) {
            if (game.isMyTurn) {
                "Your turn"
            } else {
                "Opponent's turn"
            }
        } else {
            "Esperando Jugador 2"
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = status,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlack
            )
            Spacer(modifier = Modifier.width(6.dp))
            if (!game.isMyTurn || !game.isGameReady) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = PrimaryBlack,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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
            .border(BorderStroke(2.dp, SecondaryGrey))
            .clickable { onCellClicked() },
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = playerType.symbol, label = "") {
            Text(
                text = it,
                color = if (playerType is PlayerType.FirstPlayer) PrimaryGrey else PrimaryBlack,
                fontSize = 22.sp
            )
        }
    }
}