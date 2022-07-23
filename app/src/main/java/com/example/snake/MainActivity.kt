package com.example.snake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.snake.data.GameEngine
import com.example.snake.ui.theme.SnakeTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnakeTheme {
                val viewModel: GameEngine = viewModel()
                Board(viewModel = viewModel)
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
                .border(BorderStroke(2.dp, Color.Gray), RectangleShape)) {
            Box(
                Modifier
                    .offset(
                        x = itemSize * state.value!!.food.first,
                        y = itemSize * state.value!!.food.second
                    )
                    .size(itemSize)
                    .background(Color.Magenta, CircleShape)
            )

            state.value!!.snake.forEach { itemSnake ->
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
    }

}

