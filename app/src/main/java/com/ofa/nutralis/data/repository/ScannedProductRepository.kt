package com.ofa.nutralis.data.repository

import com.ofa.nutralis.data.model.ScannedProduct
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ScannedProductRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun addScannedProduct(scannedProduct: ScannedProduct){
        val docId = "${scannedProduct.user_id}_${scannedProduct.code}"
        firestore.collection("scanned_products")
            .document(docId)
            .set(scannedProduct)
            .await()
    }

    suspend fun getScannedProducts(userId: String): List<ScannedProduct> {
        return try {
            firestore.collection("scanned_products")
                .whereEqualTo("user_id", userId)
                .get()
                .await()
                .toObjects(ScannedProduct::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}