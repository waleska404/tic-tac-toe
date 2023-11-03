package com.waleska404.tictactoe.ui.home

import androidx.lifecycle.ViewModel
import com.waleska404.tictactoe.data.network.FirebaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseService: FirebaseService
) : ViewModel() {
    fun onCreateGame() {
    }

    fun onJoinGame(id: String) {
    }
}