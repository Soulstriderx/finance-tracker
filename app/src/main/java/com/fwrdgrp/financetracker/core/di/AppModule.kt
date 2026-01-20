package com.fwrdgrp.financetracker.core.di

import com.fwrdgrp.financetracker.data.repo.Repo
import com.fwrdgrp.financetracker.data.repo.RepoImpl
import com.fwrdgrp.financetracker.service.FirebaseAuthService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseAuthService(firebaseAuth: FirebaseAuth): FirebaseAuthService {
        return FirebaseAuthService(firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesAuthRepo(authService: FirebaseAuthService, firestore: FirebaseFirestore): Repo {
        return RepoImpl(authService, firestore)
    }
}