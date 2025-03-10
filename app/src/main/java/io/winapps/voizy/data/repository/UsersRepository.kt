package io.winapps.voizy.data.repository

//import io.winapps.voizy.data.local.SecureStorage
//import io.winapps.voizy.data.model.users.CreateAccountRequest
//import io.winapps.voizy.data.model.users.CreateAccountResponse
//import io.winapps.voizy.data.remote.users.UsersApi
//
//class UsersRepository(private val secureStorage: SecureStorage) {
//    private val usersService: UsersApi.service
//
//    suspend fun createAccount(email: String, username: String, preferredName: String, password: String): CreateAccountResponse {
//        val response = usersService.createAccount(CreateAccountRequest(email, username, preferredName, password))
//
//        secureStorage.saveApiKey(response.apiKey)
//        secureStorage.saveToken(response.token)
//
//        return response
//    }
//}