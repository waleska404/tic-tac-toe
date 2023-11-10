package com.waleska404.tictactoe.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waleska404.tictactoe.R
import com.waleska404.tictactoe.ui.core.components.TicTacPrimaryButton
import com.waleska404.tictactoe.ui.theme.Background
import com.waleska404.tictactoe.ui.theme.LightGrey
import com.waleska404.tictactoe.ui.theme.PrimaryBlack
import com.waleska404.tictactoe.ui.theme.PrimaryGrey
import com.waleska404.tictactoe.ui.theme.SecondaryGrey

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToGame: (String, String, Boolean) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background)
    ) {
        Spacer(modifier = Modifier.weight(0.2f))
        Header()
        Spacer(modifier = Modifier.weight(0.2f))
        Body(
            onCreateGame = { homeViewModel.onCreateGame(navigateToGame) },
            onJoinGame = { gameId -> homeViewModel.onJoinGame(gameId, navigateToGame) }
        )
        Spacer(modifier = Modifier.weight(0.6f))
    }
}

@Composable
fun Header() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // logo
        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.logo_content_description),
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        // title
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 32.sp,
            color = PrimaryBlack,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun Body(
    onCreateGame: () -> Unit,
    onJoinGame: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var createGame by remember { mutableStateOf(true) }
        Switch(
            checked = createGame,
            onCheckedChange = { createGame = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = SecondaryGrey,
                checkedTrackColor = PrimaryGrey,
                uncheckedThumbColor = SecondaryGrey,
                uncheckedTrackColor = PrimaryGrey,
                uncheckedBorderColor = Color.Transparent
            ),
        )
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedContent(targetState = createGame, label = "") {
            when (it) {
                true -> CreateGame(onCreateGame)
                false -> JoinGame(onJoinGame)
            }
        }
    }
}

@Composable
fun CreateGame(
    onCreateGame: () -> Unit,
) {
    TicTacPrimaryButton(
        onClick = { onCreateGame() },
        text = stringResource(id = R.string.create_game)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinGame(
    onJoinGame: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            colors = TextFieldDefaults.textFieldColors(
                textColor = PrimaryBlack,
                cursorColor = PrimaryBlack,
                focusedIndicatorColor = SecondaryGrey,
                unfocusedIndicatorColor = PrimaryGrey,
                containerColor = LightGrey
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TicTacPrimaryButton(
            onClick = { onJoinGame(text) },
            text = stringResource(id = R.string.join_game),
            enabled = text.isNotEmpty(),
            textColor = if (text.isNotEmpty()) PrimaryBlack else LightGrey,
        )
    }
}