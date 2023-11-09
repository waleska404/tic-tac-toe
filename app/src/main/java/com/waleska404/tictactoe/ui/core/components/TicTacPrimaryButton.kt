package com.waleska404.tictactoe.ui.core.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.waleska404.tictactoe.ui.theme.PrimaryBlack
import com.waleska404.tictactoe.ui.theme.PrimaryGrey

@Composable
fun TicTacPrimaryButton(
    onClick: () -> Unit,
    text: String,
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryGrey,
        ),
    ) {
        Text(
            text = text,
            color = PrimaryBlack,
            fontSize = 18.sp
        )
    }
}