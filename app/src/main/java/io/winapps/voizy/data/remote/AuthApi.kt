package io.winapps.voizy.data.remote

object AuthApi {
    val service: AuthService by lazy {
        ApiClient.retrofit.create(AuthService::class.java)
    }
}