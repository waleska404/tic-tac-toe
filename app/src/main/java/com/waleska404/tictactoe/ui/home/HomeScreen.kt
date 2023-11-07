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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waleska404.tictactoe.R
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
        Header()
        Body(
            onCreateGame = { homeViewModel.onCreateGame(navigateToGame) },
            onJoinGame = { gameId -> homeViewModel.onJoinGame(gameId, navigateToGame) }
        )
    }
}

@Composable
fun Header() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(50.dp))
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
                contentDescription = "tic tac toe app logo",
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        // title
        Text(
            text = "Tic Tac Toe",
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
    Spacer(modifier = Modifier.height(50.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(24.dp),
        colors = cardColors(
            containerColor = Background,
        ),
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
                when(it) {
                    true -> CreateGame(onCreateGame)
                    false -> JoinGame(onJoinGame)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CreateGame(
    onCreateGame: () -> Unit,
) {
    Button(
        onClick = { onCreateGame() },
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryGrey,
        ),
    ) {
        Text(
            text = "Create Game",
            color = PrimaryBlack,
            fontSize = 18.sp
        )
    }
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
        Button(
            onClick = { onJoinGame(text) },
            enabled = text.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGrey,
                disabledContainerColor = PrimaryGrey
            )
        ) {
            Text(
                text = "Join Game",
                color = if(text.isNotEmpty()) PrimaryBlack else LightGrey,
                fontSize = 18.sp
            )
        }
    }
}