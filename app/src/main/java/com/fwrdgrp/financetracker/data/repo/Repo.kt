package com.fwrdgrp.financetracker.data.repo

import com.fwrdgrp.financetracker.data.model.main.User
import com.fwrdgrp.financetracker.data.model.request.LoginReq
import com.fwrdgrp.financetracker.data.model.request.RegisterReq

interface Repo {
    suspend fun register(user: RegisterReq)
    suspend fun login(req: LoginReq): User
}