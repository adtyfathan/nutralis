package com.ofa.nutralis.ui.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofa.nutralis.data.model.ScannedProduct
import com.ofa.nutralis.data.repository.ProductRepository
import com.ofa.nutralis.data.repository.ScannedProductRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val scannedProductRepository: ScannedProductRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    var state by mutableStateOf(ProductState())
        private set

    fun fetchProduct(barcode: String){
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, product = null)
            try {
                val response = productRepository.getProduct(barcode)

                val userId = auth.currentUser?.uid

                if(userId.isNullOrBlank()){
                    state = state.copy(error = "User not logged in", isLoading = false)
                    return@launch
                }

                val scannedProduct = ScannedProduct(
                    product_name = response.product.product_name,
                    product_type = response.product.product_type,
                    image_url = response.product.image_url,
                    nutriscore_grade = response.product.nutriscore_grade,
                    nutriscore_score = response.product.nutriscore_score,
                    code = response.code,
                    user_id = userId
                )

                scannedProductRepository.addScannedProduct(scannedProduct)

                state = state.copy(product = response.product, isLoading = false)
            } catch (e: Exception){
                state = state.copy(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }
}