package com.iyannah.ivy

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : androidx.activity.ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var winingPlayer by remember {
                mutableStateOf<Player?>(null)
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TicTacToe(onPlayerWin = {
                    winingPlayer = it
                }) {
                    winingPlayer = null
                }
                Column(
                    Modifier
                        .padding(20.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = "Winning Player is ${winingPlayer?.symbol ?: ""}",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TicTacToe(
    onPlayerWin: (Player) -> Unit,
    onNewRound: () -> Unit
) {

    val scope = rememberCoroutineScope()

    var currentPlayer by remember {
        mutableStateOf<Player>(Player.X)
    }
    var isGameRunning by remember {
        mutableStateOf(true)
    }

    var gameState by remember {
        mutableStateOf(emptyState())
    }

    var animations = remember {
        animationState()
    }

    Canvas(
        modifier = Modifier
            .size(300.dp)
            .pointerInput(true) {
                detectTapGestures {
                    if (!isGameRunning) {
                        return@detectTapGestures
                    }
                    when {
                        it.x < size.width / 3f && it.y < size.height / 3f -> {

                            if (gameState[0][0] == 'E') {
                                gameState = updateGameState(currentPlayer.symbol, 0, 0, gameState)
                                scope.animateFloatToOne(animations[0][0])
                                currentPlayer = !currentPlayer
                            }
                        }

                        it.x in size.width / 3f..size.width * 2 / 3f && it.y < size.height / 3f -> {
                            if (gameState[1][0] == 'E') {
                                gameState = updateGameState(currentPlayer.symbol, 1, 0, gameState)
                                scope.animateFloatToOne(animations[1][0])
                                currentPlayer = !currentPlayer
                            }
                        }

                        it.x > size.width * 2 / 3f && it.y < size.height / 3f -> {
                            if (gameState[2][0] == 'E') {
                                gameState = updateGameState(currentPlayer.symbol, 2, 0, gameState)
                                scope.animateFloatToOne(animations[2][0])
                                currentPlayer = !currentPlayer
                            }
                        }

                        it.x < size.width / 3f && it.y in size.height / 3f..size.height * 2 / 3f -> {

                            if (gameState[0][1] == 'E') {
                                gameState = updateGameState(currentPlayer.symbol, 0, 1, gameState)
                                scope.animateFloatToOne(animations[0][1])
                                currentPlayer = !currentPlayer
                            }
                        }

                        it.x in size.width / 3f..size.width * 2 / 3f && it.y in size.height / 3f..size.height * 2 / 3f -> {
                            if (gameState[1][1] == 'E') {
                                gameState = updateGameState(currentPlayer.symbol, 1, 1, gameState)
                                scope.animateFloatToOne(animations[1][1])
                                currentPlayer = !currentPlayer
                            }
                        }

                        it.x > size.width * 2 / 3f && it.y in size.height / 3f..size.height * 2 / 3f -> {
                            if (gameState[2][1] == 'E') {
                                gameState = updateGameState(currentPlayer.symbol, 2, 1, gameState)
                                scope.animateFloatToOne(animations[2][1])
                                currentPlayer = !currentPlayer
                            }
                        }

                        it.x < size.width / 3f && it.y > size.height * 2 / 3f -> {

                            if (gameState[0][2] == 'E') {
                                gameState = updateGameState(currentPlayer.symbol, 0, 2, gameState)
                                scope.animateFloatToOne(animations[0][2])
                                currentPlayer = !currentPlayer
                            }
                        }

                        it.x in size.width / 3f..size.width * 2 / 3f && it.y > size.height * 2 / 3f -> {
                            if (gameState[1][2] == 'E') {
                                gameState = updateGameState(currentPlayer.symbol, 1, 2, gameState)
                                scope.animateFloatToOne(animations[1][2])
                                currentPlayer = !currentPlayer
                            }
                        }

                        it.x > size.width * 2 / 3f && it.y > size.height * 2 / 3f -> {
                            if (gameState[2][2] == 'E') {
                                gameState = updateGameState(currentPlayer.symbol, 2, 2, gameState)
                                scope.animateFloatToOne(animations[2][2])
                                currentPlayer = !currentPlayer
                            }
                        }
                    }

                    val isFilled = gameState.all { row -> row.all { it != 'E' } }
                    val hasPlayerXWon = hasPlayerWin(gameState, Player.X)
                    val hasPlayerOWon = hasPlayerWin(gameState, Player.O)
                    if (hasPlayerOWon) {
                        onPlayerWin(Player.O)
                    } else if (hasPlayerXWon) {
                        onPlayerWin(Player.X)
                    }

                    if (isFilled || hasPlayerXWon || hasPlayerOWon) {
                        scope.launch {
                            isGameRunning = false
                            delay(5000L)
                            isGameRunning = true
                            gameState = emptyState()
                            animations = animationState()
                            onNewRound()
                        }
                    }

                }
            }
    ) {
        drawLine(
            color = Color.Black,
            strokeWidth = 5.dp.toPx(),
            start = Offset(0f, size.height * 1 / 3),
            end = Offset(size.width, size.height * 1 / 3),
            cap = StrokeCap.Round,
        )
        drawLine(
            color = Color.Black,
            strokeWidth = 5.dp.toPx(),
            start = Offset(0f, size.height * 2 / 3),
            end = Offset(size.width, size.height * 2 / 3),
            cap = StrokeCap.Round,
        )

        drawLine(
            color = Color.Black,
            strokeWidth = 5.dp.toPx(),
            start = Offset(size.width * 1 / 3, 0f),
            end = Offset(size.width * 1 / 3, size.height),
            cap = StrokeCap.Round,
        )
        drawLine(
            color = Color.Black,
            strokeWidth = 5.dp.toPx(),
            start = Offset(size.width * 2 / 3, 0f),
            end = Offset(size.width * 2 / 3, size.height),
            cap = StrokeCap.Round,
        )

        gameState.forEachIndexed { i, row ->
            row.forEachIndexed { j, sym ->
                if (sym == Player.X.symbol) {
                    val path1 = Path().apply {
                        moveTo(
                            x = i * size.width * 1 / 3 + size.width * 1 / 6 - 50f,
                            y = j * size.height * 1 / 3 + size.height * 1 / 6 - 50f
                        )
                        lineTo(
                            x = i * size.width * 1 / 3 + size.width * 1 / 6 + 50f,
                            y = j * size.height * 1 / 3 + size.height * 1 / 6 + 50f
                        )
                    }

                    val path2 = Path().apply {
                        moveTo(
                            x = i * size.width * 1 / 3 + size.width * 1 / 6 - 50f,
                            y = j * size.height * 1 / 3 + size.height * 1 / 6 + 50f
                        )
                        lineTo(
                            x = i * size.width * 1 / 3 + size.width * 1 / 6 + 50f,
                            y = j * size.height * 1 / 3 + size.height * 1 / 6 - 50f
                        )
                    }

                    val outputPat1 = Path()
                    PathMeasure().apply {
                        setPath(path1, false)
                        getSegment(0f, animations[i][j].value * length, outputPat1)
                    }
                    val outputPat2 = Path()
                    PathMeasure().apply {
                        setPath(path2, false)
                        getSegment(0f, animations[i][j].value * length, outputPat2)
                    }

                    drawPath(
                        outputPat1,
                        color = Color.Green,
                        style = Stroke(
                            cap = StrokeCap.Round,
                            width = 4.dp.toPx(),
                        )
                    )

                    drawPath(
                        outputPat2,
                        color = Color.Green,
                        style = Stroke(
                            cap = StrokeCap.Round,
                            width = 4.dp.toPx(),
                        )
                    )

                } else if (sym == Player.O.symbol) {
                    drawArc(
                        color = Color.Yellow,
                        startAngle = 0f,
                        sweepAngle = animations[i][j].value * 360f,
                        size = Size(100f, 100f),
                        useCenter = false,
                        style = Stroke(
                            cap = StrokeCap.Round,
                            width = 4.dp.toPx()
                        ),
                        topLeft = Offset(
                            i * size.width * 1 / 3 + size.width * 1 / 6 - 50f,
                            j * size.height * 1 / 3 + size.height * 1 / 6 - 50f
                        )
                    )
                }
            }
        }
    }
}

sealed class Player(val symbol: Char) {
    data object X : Player('X')
    data object O : Player('O')

    operator fun not(): Player = if (this is X) O else X
}


private fun emptyState(): Array<CharArray> {
    return arrayOf(
        charArrayOf('E', 'E', 'E'),
        charArrayOf('E', 'E', 'E'),
        charArrayOf('E', 'E', 'E'),
    )
}

private fun animationState(): ArrayList<ArrayList<Animatable<Float, AnimationVector1D>>> {
    val animationList = arrayListOf<ArrayList<Animatable<Float, AnimationVector1D>>>()
    for (i in 0..2) {
        animationList.add(arrayListOf())
        for (j in 0..2) {
            animationList[i].add(Animatable(0f))
        }
    }
    return animationList
}

private fun updateGameState(
    sym: Char,
    i: Int,
    j: Int,
    gameState: Array<CharArray>
): Array<CharArray> {
    val state = gameState.copyOf()
    state[i][j] = sym
    return state
}

fun CoroutineScope.animateFloatToOne(animatable: Animatable<Float, AnimationVector1D>) {
    launch {
        animatable.animateTo(
            animationSpec = tween(
                delayMillis = 500
            ),
            targetValue = 1f
        )
    }
}

private fun hasPlayerWin(gameState: Array<CharArray>, player: Player): Boolean {
    val firstRowFull = gameState[0][0] == gameState[1][0] &&
            gameState[1][0] == gameState[2][0] && gameState[0][0] == player.symbol
    val secondRowFull = gameState[0][1] == gameState[1][1] &&
            gameState[1][1] == gameState[2][1] && gameState[0][1] == player.symbol
    val thirdRowFull = gameState[0][2] == gameState[1][2] &&
            gameState[1][2] == gameState[2][2] && gameState[0][2] == player.symbol

    val firstColFull = gameState[0][0] == gameState[0][1] &&
            gameState[0][1] == gameState[0][2] && gameState[0][0] == player.symbol
    val secondColFull = gameState[1][0] == gameState[1][1] &&
            gameState[1][1] == gameState[1][2] && gameState[1][0] == player.symbol
    val thirdColFull = gameState[2][0] == gameState[2][1] &&
            gameState[2][1] == gameState[2][2] && gameState[2][0] == player.symbol

    val firstDiagonalFull = gameState[0][0] == gameState[1][1] &&
            gameState[1][1] == gameState[2][2] && gameState[0][0] == player.symbol
    val secondDiagonalFull = gameState[0][2] == gameState[1][1] &&
            gameState[1][1] == gameState[2][0] && gameState[0][2] == player.symbol

    return firstRowFull || secondRowFull || thirdRowFull || firstColFull ||
            secondColFull || thirdColFull || firstDiagonalFull || secondDiagonalFull
}

