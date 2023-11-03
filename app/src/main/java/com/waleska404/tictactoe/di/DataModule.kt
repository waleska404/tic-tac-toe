package com.waleska404.tictactoe.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.waleska404.tictactoe.data.network.FirebaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun providesDatabaseReference() = Firebase.database.reference

    @Singleton
    @Provides
    fun providesFirebaseService(databaseReference: DatabaseReference) =
        FirebaseService(databaseReference)
}