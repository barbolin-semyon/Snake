package com.example.snake.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameEngine : ViewModel() {
    final val BOARD_SIZE = 10

    private val _stateGame = MutableStateFlow(
        State(
            food = Pair(3, 3),
            snake = listOf(Pair(6, 6)),
            direction = SnakeDirection.RIGHT
        )
    )
    val stateGame: StateFlow<State>
        get() = _stateGame

    private val _gameIsOver = MutableStateFlow(false)
    val gameIsOver: StateFlow<Boolean>
        get() = _gameIsOver

    fun start() = viewModelScope.launch {
        delay(150)

        var food = _stateGame.value.food
        val direction = _stateGame.value.direction
        val snake = _stateGame.value.snake.toMutableList()

        val header = getNewHeaderSnake(_stateGame.value.snake.first(), _stateGame.value.direction)

        if (header != _stateGame.value.food) {
            snake.remove(snake[snake.lastIndex])
        } else {
            food = getRandomPair()
        }

        _stateGame.update { state ->
            return@update State(food, snake.toList(), direction)
        }
        _gameIsOver.value = snake.contains(header) && isHeaderInWall(header)
    }


    private fun getNewHeaderSnake(
        previousHeader: Pair<Int, Int>,
        direction: SnakeDirection
    ): Pair<Int, Int> {

        return when(direction) {
            SnakeDirection.RIGHT -> Pair(previousHeader.first + 1, previousHeader.second)
            SnakeDirection.LEFT -> Pair(previousHeader.first - 1, previousHeader.second)
            SnakeDirection.TOP -> Pair(previousHeader.first, previousHeader.second + 1)
            else -> Pair(previousHeader.first, previousHeader.second - 1)
        }
    }

    private fun isHeaderInWall(header: Pair<Int, Int>): Boolean {
        return header.first in 0..BOARD_SIZE && header.second in 0..BOARD_SIZE
    }

    private fun getRandomPair(): Pair<Int, Int> {
        return Pair(getRandInt(), getRandInt())
    }

    private fun getRandInt(): Int {
        return (1 until BOARD_SIZE).random()
    }
}