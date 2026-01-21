package com.fwrdgrp.financetracker.data.repo

import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.request.LoginReq
import com.fwrdgrp.financetracker.data.model.request.RegisterReq
import com.fwrdgrp.financetracker.service.FirebaseAuthService
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class RepoImpl @Inject constructor(
    private val authService: FirebaseAuthService,
    firestore: FirebaseFirestore
) : Repo {
    private val dbRef = firestore.collection("users")

    override suspend fun register(user: RegisterReq) {
        val userUid = authService.register(user.email, user.password)
        val userWithUid = user.copy(uid = userUid)
        dbRef.document(userUid).set(userWithUid.toMap()).await()
    }

    override suspend fun login(req: LoginReq): User {
        return authService.login(req.email, req.password).let { user ->
            dbRef.document(user.uid)
                .get()
                .await()
                .toObject(User::class.java)
                ?: throw IllegalStateException("User doesn't exist")
        }
    }

    override suspend fun fetchUser(uid: String): User {
        val snapshot = dbRef.document(uid).get().await()

        return snapshot.toObject(User::class.java)
            ?: throw java.lang.IllegalStateException("User doesn't exist")
    }
}