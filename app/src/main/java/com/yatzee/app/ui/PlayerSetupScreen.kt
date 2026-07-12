package com.yatzee.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yatzee.app.model.PlayerData

@Composable
fun PlayerSetupScreen(
    onStartGame: (List<PlayerData>) -> Unit
) {
    var playerCount by remember { mutableStateOf(2) }
    var playerNames by remember {
        mutableStateOf(List(6) { i -> "Player ${i + 1}" })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "YATZEE-Checker",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Score Sheet",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "Number of players:",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalButton(
                onClick = { if (playerCount > 2) playerCount-- },
                enabled = playerCount > 2
            ) {
                Text("-", fontSize = 20.sp)
            }

            Text(
                text = "$playerCount",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(48.dp),
                textAlign = TextAlign.Center
            )

            FilledTonalButton(
                onClick = { if (playerCount < 6) playerCount++ },
                enabled = playerCount < 6
            ) {
                Text("+", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Player names:",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in 0 until playerCount) {
                OutlinedTextField(
                    value = playerNames[i],
                    onValueChange = { newName ->
                        val list = playerNames.toMutableList()
                        list[i] = newName
                        playerNames = list
                    },
                    label = { Text("Player ${i + 1}") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(
            onClick = {
                val players = playerNames.take(playerCount)
                    .map { PlayerData(it.ifBlank { "Player ${playerNames.indexOf(it) + 1}" }) }
                onStartGame(players)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Start Game", fontSize = 18.sp)
        }
    }
}
