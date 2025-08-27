package com.example.nutralis.ui.util

sealed class UiEvent {
    data class Navigate(val route: String): UiEvent()
}