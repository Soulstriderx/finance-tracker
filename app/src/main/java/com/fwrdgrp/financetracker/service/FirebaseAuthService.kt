package com.fwrdgrp.financetracker.service

import com.fwrdgrp.financetracker.data.model.main.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    init {
        fetchLoggedInUser()
    }

    suspend fun register(email: String, password: String): String {
        val result =
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            } catch (e: FirebaseAuthUserCollisionException) {
                throw IllegalStateException("Email in-use")
            } catch (e: Exception) {
                throw e
            }

        return result.user?.uid ?: throw IllegalStateException("User creation failed")
    }

    suspend fun login(email: String, password: String): FirebaseUser {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw IllegalStateException("User doesn't exist")

            fetchFromFirestore(firebaseUser.uid)

            return result.user ?: throw IllegalStateException("User doesn't exist")
    }

    fun getCurrentUser(): User? {
        return user.value
    }

    fun fetchFromFirestore(uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val userSnapshot = firestore.collection("users").document(uid).get().await()

            userSnapshot.toObject(User::class.java).let { user ->
                _user.update { user }
            }
        }
    }

    private fun fetchLoggedInUser() {
        val firebaseUser = firebaseAuth.currentUser ?: return
        fetchFromFirestore(firebaseUser.uid)
    }

    fun signout() {
        firebaseAuth.signOut()
    }
}