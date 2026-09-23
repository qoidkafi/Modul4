package com.example.phoneandtablet

import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

class Repo {
    companion object {
        suspend fun getData() : Int {
            delay(2.seconds) // simulating network
            return Random.nextInt(100, 1000)
        }
    }
}
