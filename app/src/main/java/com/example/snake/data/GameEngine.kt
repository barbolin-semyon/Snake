package com.example.snake.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameEngine : ViewModel() {
    final val BOARD_SIZE = 20

    private val _stateGame = MutableLiveData(
        State(
            food = getRandomPair(),
            snake = listOf(getRandomPair()),
            direction = SnakeDirection.RIGHT
        )
    )
    val stateGame: LiveData<State>
        get() = _stateGame

    private val _gameIsOver = MutableLiveData(false)
    val gameIsOver: LiveData<Boolean>
        get() = _gameIsOver

    fun start() = viewModelScope.launch {

        while (_gameIsOver.value!!.not()) {
            delay(500)
            var food = _stateGame.value!!.food
            val direction = _stateGame.value!!.direction
            val snake = _stateGame.value!!.snake.toMutableList()

            val header = getNewHeaderSnake(_stateGame.value!!.snake.first(), _stateGame.value!!.direction)

            if (header != _stateGame.value!!.food) {
                snake.remove(snake[snake.lastIndex])
            } else {
                food = getRandomPair()
            }

            snake.add(0, header)

            _stateGame.value = State(food, snake.toList(), direction)
            _gameIsOver.value = snake.count { header == it } > 1 || isHeaderInWall(header)
        }
        cancel()
    }

    fun updateDirection(direction: SnakeDirection) {
        _stateGame.value!!.direction = direction
    }

    fun restartGame() {
        _gameIsOver.value = false
        _stateGame.value = State(
            food = getRandomPair(),
            snake = listOf(getRandomPair()),
            direction = SnakeDirection.RIGHT
        )
        start()
    }


    private fun getNewHeaderSnake(
        previousHeader: Pair<Int, Int>,
        direction: SnakeDirection
    ): Pair<Int, Int> {

        return when(direction) {
            SnakeDirection.RIGHT -> Pair(previousHeader.first + 1, previousHeader.second)
            SnakeDirection.LEFT -> Pair(previousHeader.first - 1, previousHeader.second)
            SnakeDirection.TOP -> Pair(previousHeader.first, previousHeader.second - 1)
            else -> Pair(previousHeader.first, previousHeader.second + 1)
        }
    }

    private fun isHeaderInWall(header: Pair<Int, Int>): Boolean {
        return (header.first in 1 until BOARD_SIZE).not() || (header.second in 1 until BOARD_SIZE).not()
    }

    private fun getRandomPair(): Pair<Int, Int> {
        return Pair(getRandInt(), getRandInt())
    }

    private fun getRandInt(): Int {
        return (1 until BOARD_SIZE).random()
    }
}