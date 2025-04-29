package com.gayatri.e_commerce_application.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger. Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax. inject.Singleton

@Module
@InstallIn(SingletonComponent :: class)
object DataModule {
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth. getInstance()
    }
    @Singleton
    @Provides
    fun provideFirbaseFirestore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
}