package com.ofa.nutralis.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofa.nutralis.data.remote.ProductItem
import com.ofa.nutralis.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus

data class SearchUiState(
    val products: List<ProductItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    val lastQuery: String = ""
)

@HiltViewModel
class ProductCompareViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {
    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState

    private val _selectedProduct1 = MutableStateFlow<ProductItem?>(null)
    val selectedProduct1: StateFlow<ProductItem?> = _selectedProduct1

    private val _selectedProduct2 = MutableStateFlow<ProductItem?>(null)
    val selectedProduct2: StateFlow<ProductItem?> = _selectedProduct2

    fun searchProducts(query: String, reset: Boolean = true) {
        viewModelScope.launch {
            try {
                val page = if (reset) 1 else _searchUiState.value.currentPage + 1

                _searchUiState.value = _searchUiState.value.copy(isLoading = true, error = null)

                val response = productRepository.searchProducts(query, page)

                _searchUiState.value = _searchUiState.value.copy(
                    products = if (reset) response.products else _searchUiState.value.products + response.products,
                    isLoading = false,
                    currentPage = response.page,
                    hasMore = (_searchUiState.value.products.size + response.products.size) < response.count,
                    lastQuery = query
                )

            } catch (e: Exception) {
                _searchUiState.value = _searchUiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun selectProduct(slot: Int, product: ProductItem) {
        if (slot == 1) _selectedProduct1.value = product
        else _selectedProduct2.value = product
    }

    fun clearSearchResults() {
        _searchUiState.value = _searchUiState.value.copy(
            products = emptyList(),
            currentPage = 1,
            hasMore = true,
            lastQuery = ""
        )
    }

}