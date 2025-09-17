package com.ofa.nutralis.di

import com.ofa.nutralis.data.repository.AuthRepository
import com.ofa.nutralis.data.remote.ApiService
import com.ofa.nutralis.data.repository.ProductRepository
import com.ofa.nutralis.data.repository.ScannedProductRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.net/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideProductRepository(api: ApiService): ProductRepository =
        ProductRepository(api)

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideScannedProductRepository(firestore: FirebaseFirestore): ScannedProductRepository =
        ScannedProductRepository(firestore)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository =
        AuthRepository(firebaseAuth, firestore)
}