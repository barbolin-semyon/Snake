package com.example.snake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.snake.data.GameEngine
import com.example.snake.ui.theme.SnakeTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.snake.data.SnakeDirection

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnakeTheme {
                val viewModel: GameEngine = viewModel()

                Column {
                    Board(viewModel = viewModel)
                    Arrows(viewModel = viewModel)
                }
                GameOverView(viewModel = viewModel)
                viewModel.start()
            }
        }
    }
}

@Composable
private fun Board(viewModel: GameEngine) {
    val state = viewModel.stateGame.observeAsState()

    BoxWithConstraints(Modifier.padding(16.dp)) {
        val itemSize = maxWidth / viewModel.BOARD_SIZE

        Box(
            Modifier
                .size(maxWidth)
                .border(BorderStroke(2.dp, Color.Gray), RectangleShape)
        ) {

            Food(itemSize = itemSize, food = state.value!!.food)
            Snake(snake = state.value!!.snake, itemSize = itemSize)
        }
    }

}

@Composable
private fun Food(itemSize: Dp, food: Pair<Int, Int>) {

    Box(
        Modifier
            .offset(
                x = itemSize * food.first,
                y = itemSize * food.second
            )
            .size(itemSize)
            .background(Color.Magenta, CircleShape)
    )
}

@Composable
private fun Snake(snake: List<Pair<Int, Int>>, itemSize: Dp) {
    snake.forEach { itemSnake ->
        Box(
            Modifier
                .offset(
                    x = itemSize * itemSnake.first,
                    y = itemSize * itemSnake.second
                )
                .size(itemSize)
                .background(Color.Green, RoundedCornerShape(4.dp))
        )
    }
}

@Composable
private fun Arrows(viewModel: GameEngine) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Arrow(R.drawable.ic_up_arrow) { viewModel.updateDirection(SnakeDirection.TOP) }
        Row {
            Arrow(R.drawable.ic_baseline_keyboard_arrow_left_24) {
                viewModel.updateDirection(
                    SnakeDirection.LEFT
                )
            }

            Arrow(painterId = R.drawable.ic_launcher_background) {}

            Arrow(R.drawable.ic_baseline_keyboard_arrow_right_24) {
                viewModel.updateDirection(
                    SnakeDirection.RIGHT
                )
            }
        }
        Arrow(R.drawable.ic_down_arrow) { viewModel.updateDirection(SnakeDirection.BOTTOM) }

    }
}

@Composable
private fun Arrow(painterId: Int, onClick: () -> Unit) {
    Button(onClick = { onClick() }, Modifier.padding(8.dp)) {
        Icon(
            painter = painterResource(id = painterId),
            contentDescription = "btn",
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
private fun GameOverView(viewModel: GameEngine) {
    val state = viewModel.gameIsOver.observeAsState()

    if (state.value!!) {
        Card(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            backgroundColor = (Color.Red)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Вы проиграли",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                TextButton(
                    onClick = { viewModel.restartGame() },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Попробовать снова", color = Color.White)
                }
            }
        }
    }
}