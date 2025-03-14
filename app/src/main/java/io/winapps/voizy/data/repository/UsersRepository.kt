package io.winapps.voizy.data.repository

import io.winapps.voizy.data.local.SecureStorage
import io.winapps.voizy.data.model.users.CreateAccountRequest
import io.winapps.voizy.data.model.users.CreateAccountResponse
import io.winapps.voizy.data.model.users.GetProfilePicResponse
import io.winapps.voizy.data.model.users.GetProfileResponse
import io.winapps.voizy.data.model.users.GetTotalFriendsResponse
import io.winapps.voizy.data.model.users.GetTotalUserImagesResponse
import io.winapps.voizy.data.model.users.ListFriendshipsResponse
import io.winapps.voizy.data.model.users.ListUserImagesResponse
import io.winapps.voizy.data.remote.users.UsersApi
import io.winapps.voizy.data.remote.users.UsersService
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val secureStorage: SecureStorage
) {
    private val usersService: UsersService = UsersApi.service

    suspend fun createAccount(email: String, username: String, preferredName: String, password: String): CreateAccountResponse {
        val response = usersService.createAccount(CreateAccountRequest(email, username, preferredName, password))

        secureStorage.saveApiKey(response.apiKey)
        secureStorage.saveToken(response.token)

        return response
    }

    suspend fun getProfileInfo(apiKey: String, userIdHeader: String, userId: Long): GetProfileResponse {
        return usersService.getProfileInfo(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            userId = userId,
        )
    }

    suspend fun getTotalFriends(apiKey: String, userIdHeader: String, userId: Long): GetTotalFriendsResponse {
        return usersService.getTotalFriends(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            userId = userId,
        )
    }

    suspend fun listFriendships(apiKey: String, userIdHeader: String, userId: Long, limit: Long, page: Long): ListFriendshipsResponse {
        return usersService.listFriendships(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            userId = userId,
            limit = limit,
            page = page,
        )
    }

    suspend fun getTotalImages(apiKey: String, userIdHeader: String, userId: Long): GetTotalUserImagesResponse {
        return usersService.getTotalImages(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            userId = userId,
        )
    }

    suspend fun getProfilePic(apiKey: String, userIdHeader: String, userId: Long): GetProfilePicResponse {
        return usersService.getProfilePic(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            userId = userId,
        )
    }

    suspend fun listImages(apiKey: String, userIdHeader: String, userId: Long, limit: Long, page: Long): ListUserImagesResponse {
        return usersService.listImages(
            apiKey = apiKey,
            userIdHeader = userIdHeader,
            userId = userId,
            limit = limit,
            page = page,
        )
    }
}