package io.winapps.voizy.data.remote.users

object UsersApi {
    val service: UsersService by lazy {
        UsersApiClient.retrofit.create(UsersService::class.java)
    }
}