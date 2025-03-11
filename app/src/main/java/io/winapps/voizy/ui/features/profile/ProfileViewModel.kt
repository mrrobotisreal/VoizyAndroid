package io.winapps.voizy.ui.features.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.data.model.posts.ListPost
import io.winapps.voizy.data.repository.PostsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ProfileTab {
    POSTS, PHOTOS, ABOUT, FRIENDS
}

class ProfileViewModel {
}

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {
    var posts by mutableStateOf(emptyList<ListPost>())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadPosts(userId: Long, apiKey: String, limit: Long = 20, page: Long = 1) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = postsRepository.listPosts(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = userId,
                    limit = limit,
                    page = page
                )
                posts = response.posts
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}