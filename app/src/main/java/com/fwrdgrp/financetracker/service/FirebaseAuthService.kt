package com.fwrdgrp.financetracker.service

import android.util.Log
import com.fwrdgrp.financetracker.data.model.main.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var listenerRegistration: ListenerRegistration? = null
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

    fun fetchUserFlowFromFirestore(uid: String): Flow<User?> = callbackFlow {
        val listener = firestore.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Listening failed", error)
                    close(error)
                    return@addSnapshotListener
                }

                val user = snapshot?.data?.let { User.fromMap(it) }
                trySend(user)
            }
        listenerRegistration = listener
        awaitClose {
            listener.remove()
            listenerRegistration?.remove()
        }
    }

    private fun fetchFromFirestore(uid: String) {
        listenerRegistration?.remove()
        serviceScope.launch {
            fetchUserFlowFromFirestore(uid).collect { user ->
                _user.update { user }
            }
        }
    }

    private fun fetchLoggedInUser() {
        val firebaseUser = firebaseAuth.currentUser ?: return
        fetchFromFirestore(firebaseUser.uid)
    }

    fun signout() {
        listenerRegistration?.remove()
        listenerRegistration = null
        _user.update { null }
        firebaseAuth.signOut()
    }
}