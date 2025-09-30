package com.ofa.nutralis.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofa.nutralis.data.remote.ProductItem
import com.ofa.nutralis.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _categories = listOf("snacks", "beverages", "dairies", "desserts", "breakfasts", "milks")
    val categories: List<String> get() = _categories

    private val _selectedCategory = MutableStateFlow(_categories.first())
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _products = MutableStateFlow<List<ProductItem>>(emptyList())
    val products: StateFlow<List<ProductItem>> = _products

    init {
        loadProducts(_selectedCategory.value)
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        loadProducts(category)
    }

    private fun loadProducts(category: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = productRepository.searchProducts(query = category, page = 1)
                _products.value = response.products.take(6)
            } catch (e: Exception) {
                _products.value = emptyList()
            }
            finally {
                _isLoading.value = false
            }
        }
    }
}