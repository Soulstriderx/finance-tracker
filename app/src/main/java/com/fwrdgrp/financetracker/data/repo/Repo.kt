package com.fwrdgrp.financetracker.data.repo

import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.request.LoginReq
import com.fwrdgrp.financetracker.data.model.request.RegisterReq

interface Repo {
    //AuthRepo
    suspend fun register(user: RegisterReq)
    suspend fun login(req: LoginReq): User

    //UserRepo
    suspend fun fetchUser(uid: String): User
}