package io.winapps.voizy.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.winapps.voizy.data.model.posts.CompletePost
import io.winapps.voizy.data.repository.PostsRepository
import io.winapps.voizy.data.repository.UsersRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {
    var searchText by mutableStateOf("")
        private set

    fun onSearchTextChanged(newValue: String) {
        searchText = newValue
    }

    var recommendedPosts by mutableStateOf<List<CompletePost>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)

    fun loadRecommendedPosts(userId: Long, apiKey: String, limit: Long = 50, page: Long = 1, excludeSeen: Boolean = false, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val cachedPosts = null

                if (cachedPosts != null) {
                    //
                } else {
                    val recommendedPostsResponse = postsRepository.getRecommendedFeed(
                        apiKey = apiKey,
                        userIdHeader = userId.toString(),
                        userId = userId,
                        limit = limit,
                        page = page,
                        excludeSeen = excludeSeen
                    )
                    val recommendedFeedPosts = recommendedPostsResponse.recommendedFeedPosts

                    val finalList = mutableListOf<CompletePost>()
                    for (post in recommendedFeedPosts) {
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
                            profilePicURL = post.profilePicURL,
                            totalComments = post.totalComments,
                            reactions = details.reactions,
                            hashtags = details.hashtags,
                            images = media.images.orEmpty(),
                            videos = media.videos.orEmpty()
                        )
                        finalList.add(complete)
                    }

                    recommendedPosts = finalList

                    // TODO: recommended posts cache here
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}