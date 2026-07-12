package com.yatzee.app.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yatzee.app.model.Category
import com.yatzee.app.model.PlayerData
import com.yatzee.app.model.SummaryRow

@Composable
fun ScoreSheetScreen(
    players: List<PlayerData>
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedPlayerIndex by remember { mutableStateOf(-1) }
    var inputValue by remember { mutableStateOf("") }

    val scrollStateH = rememberScrollState()
    val scrollStateV = rememberScrollState()

    val headerWidth = 140.dp
    val cellWidth = 72.dp
    val cellHeight = 44.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Text(
            text = "YATZEE-Checker",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .width(headerWidth)
                    .verticalScroll(scrollStateV)
            ) {
                HeaderCell("Category", headerWidth, 48.dp, bg = MaterialTheme.colorScheme.primaryContainer)

                for (cat in Category.values()) {
                    HeaderCell(cat.displayName, headerWidth, cellHeight)
                }

                Divider()
                for (row in SummaryRow.values()) {
                    HeaderCell(row.displayName, headerWidth, cellHeight,
                        bg = MaterialTheme.colorScheme.secondaryContainer)
                }
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(scrollStateH)
            ) {
                for ((pIdx, player) in players.withIndex()) {
                    Column(
                        modifier = Modifier
                            .width(cellWidth)
                            .verticalScroll(scrollStateV)
                    ) {
                        HeaderCell(player.name, cellWidth, 48.dp, bg = MaterialTheme.colorScheme.primaryContainer)

                        for (cat in Category.values()) {
                            val score = player.scores[cat]
                            ScoreCell(
                                text = score?.toString() ?: "",
                                width = cellWidth,
                                height = cellHeight,
                                onClick = {
                                    selectedPlayerIndex = pIdx
                                    selectedCategory = cat
                                    inputValue = score?.toString() ?: ""
                                    showDialog = true
                                }
                            )
                        }

                        Divider()

                        ScoreCell(
                            text = player.upperSum.toString(),
                            width = cellWidth,
                            height = cellHeight,
                            bg = MaterialTheme.colorScheme.secondaryContainer,
                            bold = true
                        )
                        ScoreCell(
                            text = player.bonus.toString(),
                            width = cellWidth,
                            height = cellHeight,
                            bg = MaterialTheme.colorScheme.secondaryContainer,
                            bold = true
                        )
                        ScoreCell(
                            text = player.lowerSum.toString(),
                            width = cellWidth,
                            height = cellHeight,
                            bg = MaterialTheme.colorScheme.secondaryContainer,
                            bold = true
                        )
                        ScoreCell(
                            text = player.total.toString(),
                            width = cellWidth,
                            height = cellHeight,
                            bg = MaterialTheme.colorScheme.tertiaryContainer,
                            bold = true,
                            larger = true
                        )
                    }
                }
            }
        }
    }

    if (showDialog && selectedCategory != null && selectedPlayerIndex >= 0) {
        ScoreInputDialog(
            title = "${selectedCategory!!.displayName} - ${players[selectedPlayerIndex].name}",
            initialValue = inputValue,
            onDismiss = { showDialog = false },
            onConfirm = { value ->
                val parsed = value.toIntOrNull()
                players[selectedPlayerIndex].scores[selectedCategory!!] = parsed
                showDialog = false
            }
        )
    }
}

@Composable
private fun HeaderCell(
    text: String,
    width: Dp,
    height: Dp,
    bg: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(bg)
            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = if (width < 80.dp) 11.sp else 13.sp,
            fontWeight = if (width >= 140.dp) FontWeight.Medium else FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun ScoreCell(
    text: String,
    width: Dp,
    height: Dp,
    bg: Color = MaterialTheme.colorScheme.surface,
    bold: Boolean = false,
    larger: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(bg)
            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
            .then(
                if (onClick != null) Modifier.clickable { onClick() }
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = if (larger) 16.sp else 14.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ScoreInputDialog(
    title: String,
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var value by remember { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                label = { Text("Score") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(value) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
