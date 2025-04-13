package io.winapps.voizy.data.local

import io.winapps.voizy.data.model.posts.CompletePost
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsCache @Inject constructor() {
    private val completePosts = ConcurrentHashMap<String, List<CompletePost>>()
    private val mutex = Mutex()

    private fun getCacheKey(userId: Long, limit: Long, page: Long): String {
        return "${userId}_${limit}_${page}"
    }

    suspend fun getCachedPosts(userId: Long, limit: Long, page: Long): List<CompletePost>? {
        val key = getCacheKey(userId, limit, page)
        return completePosts[key]
    }

    suspend fun cachePosts(userId: Long, limit: Long, page: Long, posts: List<CompletePost>) {
        val key = getCacheKey(userId, limit, page)
        mutex.withLock {
            completePosts[key] = posts
        }
    }

    suspend fun clearCache() {
        mutex.withLock {
            completePosts.clear()
        }
    }

    suspend fun invalidateUserCache(userId: Long) {
        mutex.withLock {
            completePosts.keys.filter { it.startsWith("${userId}_") }
                .forEach { completePosts.remove(it) }
        }
    }
}