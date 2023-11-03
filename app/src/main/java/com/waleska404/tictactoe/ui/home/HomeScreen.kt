package com.waleska404.tictactoe.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(2f))
        CreateGame()
        Spacer(modifier = Modifier.weight(1f))
        Divider(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        JoinGame()
        Spacer(modifier = Modifier.weight(2f))
    }
}

@Composable
fun CreateGame() {
    Button(onClick = { /*TODO*/ }) {
        Text(text = "Create game")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinGame() {
    var text by remember { mutableStateOf("") }

    TextField(value = text, onValueChange = {text = it})

    Button(
        onClick = { /*TODO*/ },
        enabled = text.isNotEmpty(),
    ) {
        Text(text = "Join game")
    }
}