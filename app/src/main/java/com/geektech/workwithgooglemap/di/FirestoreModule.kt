package com.geektech.workwithgooglemap.di

import com.geektech.workwithgooglemap.data.remote.repositories.UserRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Provides
    fun provideFirestore() = Firebase.firestore

    @Provides
    fun provideUserTestRep() = UserRepository(provideFirestore())
}
