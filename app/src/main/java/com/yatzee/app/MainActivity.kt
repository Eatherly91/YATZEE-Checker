package com.yatzee.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.yatzee.app.model.PlayerData
import com.yatzee.app.ui.PlayerSetupScreen
import com.yatzee.app.ui.ScoreSheetScreen
import com.yatzee.app.ui.theme.YATZEETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YATZEETheme {
                var players by remember { mutableStateOf<List<PlayerData>?>(null) }

                if (players == null) {
                    PlayerSetupScreen(
                        onStartGame = { playerList ->
                            players = playerList
                        }
                    )
                } else {
                    ScoreSheetScreen(players = players!!)
                }
            }
        }
    }
}
