package com.baksha97.sdui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform