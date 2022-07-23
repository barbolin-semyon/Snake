package com.example.snake.data

data class State(
    val food: Pair<Int, Int>,
    val snake: List<Pair<Int, Int>>,
    var direction: SnakeDirection
)
