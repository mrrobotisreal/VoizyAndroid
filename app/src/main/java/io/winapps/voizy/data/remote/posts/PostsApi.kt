package io.winapps.voizy.data.remote.posts

import io.winapps.voizy.data.remote.ApiClient

object PostsApi {
    val service: PostsService by lazy {
        ApiClient.retrofit.create(PostsService::class.java)
    }
}