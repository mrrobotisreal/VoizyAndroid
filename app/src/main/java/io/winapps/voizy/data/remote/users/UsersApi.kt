package io.winapps.voizy.data.remote.users

import io.winapps.voizy.data.remote.ApiClient

object UsersApi {
    val service: UsersService by lazy {
        ApiClient.retrofit.create(UsersService::class.java)
    }
}