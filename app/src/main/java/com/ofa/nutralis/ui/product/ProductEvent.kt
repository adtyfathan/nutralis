package com.ofa.nutralis.ui.product

sealed class ProductEvent {
    data class OnSubmit(val barcode: String): ProductEvent()
}