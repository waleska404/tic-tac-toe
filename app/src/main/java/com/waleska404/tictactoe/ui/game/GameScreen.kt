package com.waleska404.tictactoe.ui.game

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waleska404.tictactoe.R
import com.waleska404.tictactoe.ui.core.components.TicTacPrimaryButton
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
    val invalidGameID: Boolean by gameViewModel.invalidGameId.collectAsState()

    if (invalidGameID) {
        InvalidGameIDDialog(navigateToHome)
    }

    if (winner != null) {
        WinnerView(
            winner = winner,
            navigateToHome = navigateToHome
        )
    } else {
        GameView(
            game = game,
            onCellClicked = { position -> gameViewModel.onCellClicked(position) },
            myPlayerType = { gameViewModel.getMyPlayerType() }
        )
    }
}

@Composable
fun InvalidGameIDDialog(
    navigateToHome: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navigateToHome() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = PrimaryGrey,
                    containerColor = SecondaryGrey
                )
            ) {
                Text(text = stringResource(id = R.string.go_to_home_screen))
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.invalid_game_id),
                modifier = Modifier.fillMaxWidth(),
                color = PrimaryBlack
            )

        },
        containerColor = PrimaryGrey
    )
}

@Composable
fun WinnerView(
    winner: PlayerType?,
    navigateToHome: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(PrimaryGrey)
                .padding(24.dp),
        ) {
            val congratulationsText = if (winner == PlayerType.Empty) {
                stringResource(id = R.string.draw)
            } else {
                stringResource(id = R.string.congratulations)
            }
            Text(
                text = congratulationsText,
                color = PrimaryBlack,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            if (winner != PlayerType.Empty) {
                Spacer(modifier = Modifier.height(8.dp))
                val currentWinner = if (winner == PlayerType.FirstPlayer) {
                    stringResource(id = R.string.player_1)
                } else {
                    stringResource(id = R.string.player_2)
                }
                Text(
                    text = stringResource(id = R.string.winner_is),
                    fontSize = 22.sp,
                    color = PrimaryBlack
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = currentWinner,
                    fontSize = 26.sp,
                    color = PrimaryBlack,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            TicTacPrimaryButton(
                onClick = { navigateToHome() },
                text = stringResource(id = R.string.go_to_home_screen),
                backgroundColor = Background
            )
        }
    }
}

@Composable
fun GameView(
    game: GameUIModel?,
    onCellClicked: (Int) -> Unit,
    myPlayerType: () -> PlayerType,
) {
    if (game == null) return
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(0.2f))
        GameTitle()
        Spacer(modifier = Modifier.weight(0.3f))
        TurnTitle(game = game)
        Spacer(modifier = Modifier.weight(0.1f))
        Board(
            game = game,
            onCellClicked = onCellClicked,
            myPlayerType = myPlayerType
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ClickableGameID(game = game)
        }
        Spacer(modifier = Modifier.weight(0.2f))
    }
}

@Composable
fun GameTitle() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.copy_content_description),
            )
        }
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 26.sp,
            color = PrimaryBlack,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun Board(
    game: GameUIModel,
    onCellClicked: (Int) -> Unit,
    myPlayerType: () -> PlayerType
) {
    Row {
        GameItem(game.board[0], myPlayerType) { onCellClicked(0) }
        GameItem(game.board[1], myPlayerType) { onCellClicked(1) }
        GameItem(game.board[2], myPlayerType) { onCellClicked(2) }
    }
    Row {
        GameItem(game.board[3], myPlayerType) { onCellClicked(3) }
        GameItem(game.board[4], myPlayerType) { onCellClicked(4) }
        GameItem(game.board[5], myPlayerType) { onCellClicked(5) }
    }
    Row {
        GameItem(game.board[6], myPlayerType) { onCellClicked(6) }
        GameItem(game.board[7], myPlayerType) { onCellClicked(7) }
        GameItem(game.board[8], myPlayerType) { onCellClicked(8) }
    }
}

@Composable
fun ClickableGameID(
    game: GameUIModel,
) {
    val clipboard: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val toastText = stringResource(id = R.string.copied_to_clipboard)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                clipboard.setText(AnnotatedString(game.gameId))
                Toast
                    .makeText(context, toastText, Toast.LENGTH_SHORT)
                    .show()
            }
            .background(
                color = PrimaryGrey,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier.size(30.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.copy),
                contentDescription = stringResource(id = R.string.copy_content_description),
                tint = BlueLink,
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = game.gameId,
            color = PrimaryBlack,
        )
    }

}

@Composable
fun TurnTitle(
    game: GameUIModel,
) {
    val status = if (game.isGameReady) {
        if (game.isMyTurn) {
            stringResource(id = R.string.your_turn)
        } else {
            stringResource(id = R.string.opponents_turn)
        }
    } else {
        stringResource(id = R.string.waiting_player_2)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = status,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = SecondaryGrey
        )
        Spacer(modifier = Modifier.width(6.dp))
        if (!game.isMyTurn || !game.isGameReady) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = SecondaryGrey,
                strokeWidth = 3.dp
            )
        }
    }
}

@Composable
fun GameItem(
    playerType: PlayerType,
    getMyPlayerType: () -> PlayerType,
    onCellClicked: () -> Unit,
) {
    val myPlayerType = getMyPlayerType()
    Box(
        modifier = Modifier
            .padding(6.dp)
            .size(90.dp)
            .border(BorderStroke(4.dp, SecondaryGrey), RoundedCornerShape(12.dp))
            .clickable { onCellClicked() },
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = playerType.symbol, label = "") {
            Text(
                text = it,
                color = if (playerType == myPlayerType) PrimaryBlack else SecondaryGrey,
                fontSize = 40.sp
            )
        }
    }
}