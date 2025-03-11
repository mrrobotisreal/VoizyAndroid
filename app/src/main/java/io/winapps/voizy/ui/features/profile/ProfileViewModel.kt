package io.winapps.voizy.ui.features.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.winapps.voizy.SessionViewModel
import io.winapps.voizy.data.model.posts.CompletePost
import io.winapps.voizy.data.model.posts.ListPost
import io.winapps.voizy.data.repository.PostsRepository
import kotlinx.coroutines.async
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
    var completePosts by mutableStateOf(emptyList<CompletePost>())
        private set

    var posts by mutableStateOf(emptyList<ListPost>())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var totalPosts by mutableLongStateOf(0)
        private set

    var isLoadingTotalPosts by mutableStateOf(false)
        private set

    var totalPostsErrorMessage by mutableStateOf<String?>(null)
        private set

    var isLoadingPostDetails by mutableStateOf(false)
        private set

    var postDetailsErrorMessage by mutableStateOf<String?>(null)
        private set

    var isLoadingPostMedia by mutableStateOf(false)
        private set

    var postMediaErrorMessage by mutableStateOf<String?>(null)
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

    fun loadCompletePosts(userId: Long, apiKey: String, limit: Long = 20, page: Long = 1) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val listResponse = postsRepository.listPosts(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = userId,
                    limit = limit,
                    page = page
                )
                val rawPosts = listResponse.posts

                val finalList = mutableListOf<CompletePost>()
                for (post in rawPosts) {
//                    val details = postsRepository.getPostDetails(
//                            apiKey = apiKey,
//                            userIdHeader = userId.toString(),
//                            postId = post.postID
//                        )
//                    val media = postsRepository.getPostMedia(
//                            apiKey = apiKey,
//                            userIdHeader = userId.toString(),
//                            postId = post.postID
//                        )
                    val detailDeferred = async {
                        postsRepository.getPostDetails(
                            apiKey = apiKey,
                            userIdHeader = userId.toString(),
                            postId = post.postID
                        )
                    }
                    val mediaDeferred = async {
                        postsRepository.getPostMedia(
                            apiKey = apiKey,
                            userIdHeader = userId.toString(),
                            postId = post.postID
                        )
                    }
                    val details = detailDeferred.await()
                    val media = mediaDeferred.await()

                    val complete = CompletePost(
                        post = post,
                        reactions = details.reactions,
                        hashtags = details.hashtags,
                        images = media.images.orEmpty(),
                        videos = media.videos.orEmpty()
                    )
                    finalList.add(complete)
                }

                completePosts = finalList
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun loadTotalPosts(userId: Long, apiKey: String) {
        viewModelScope.launch {
            isLoadingTotalPosts = true
            totalPostsErrorMessage = null

            try {
                val response = postsRepository.getTotalPosts(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = userId
                )
                totalPosts = response.totalPosts
            } catch (e: Exception) {
                totalPostsErrorMessage = e.message
            } finally {
                isLoadingTotalPosts = false
            }
        }
    }

    fun loadPostDetails(userId: Long, postId: Long, apiKey: String) {
        viewModelScope.launch {
            isLoadingPostDetails = true
            postDetailsErrorMessage = null

            try {
                val response = postsRepository.getPostDetails(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    postId = postId
                )
            } catch (e: Exception) {
                postDetailsErrorMessage = e.message
            } finally {
                isLoadingPostDetails = false
            }
        }
    }

    fun loadPostMedia(userId: Long, postId: Long, apiKey: String) {
        viewModelScope.launch {
            isLoadingPostMedia = true
            postMediaErrorMessage = null

            try {
                val response = postsRepository.getPostMedia(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    postId = postId
                )
            } catch (e: Exception) {
                postMediaErrorMessage = e.message
            } finally {
                isLoadingPostMedia = false
            }
        }
    }
}