package io.winapps.voizy.ui.features.feed

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

enum class FeedTab(val label: String) {
    FOR_YOU("For You"),
    POPULAR("Popular"),
    GROUPS("Groups"),
    FRIENDS("Friends");

    companion object {
        val default = FOR_YOU
    }
}

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {
    var searchText by mutableStateOf("")
        private set

    var showFiltersDialog by mutableStateOf(false)
        private set

    var selectedFilter by mutableStateOf<FeedTab>(FeedTab.FOR_YOU)
        private set

    var selectedFilterLabel by mutableStateOf("For You feed")
        private set

    fun onSearchTextChanged(newValue: String) {
        searchText = newValue
    }

    fun onOpenFiltersDialog() {
        showFiltersDialog = true
    }

    fun onCloseFiltersDialog() {
        showFiltersDialog = false
    }

    fun onSelectFilter(newFilter: FeedTab) {
        selectedFilter = newFilter
        selectedFilterLabel = when(newFilter) {
            FeedTab.FOR_YOU -> "For You feed"
            FeedTab.POPULAR -> "Popular feed"
            FeedTab.GROUPS -> "Groups feed"
            FeedTab.FRIENDS -> "Friends feed"
        }
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

    var popularPosts by mutableStateOf<List<CompletePost>>(emptyList())
        private set

    fun loadPopularPosts(userId: Long, apiKey: String, limit: Long = 50, page: Long = 1, days: Long = 30, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val cachedPosts = null

                if (cachedPosts != null) {
                    //
                } else {
                    val popularPostsResponse = postsRepository.getPopularPosts(
                        apiKey = apiKey,
                        userIdHeader = userId.toString(),
                        userId = userId,
                        limit = limit,
                        page = page,
                        days = days
                    )
                    val popularFeedPosts = popularPostsResponse.popularPosts

                    val finalList = mutableListOf<CompletePost>()
                    for (post in popularFeedPosts) {
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

                    popularPosts = finalList

                    // TODO: recommended posts cache here
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    var friendFeed by mutableStateOf<List<CompletePost>>(emptyList())
        private set

    fun loadFriendFeed(userId: Long, apiKey: String, limit: Long = 50, page: Long = 1, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val cachedPosts = null

                if (cachedPosts != null) {
                    //
                } else {
                    val popularPostsResponse = postsRepository.getFriendFeed(
                        apiKey = apiKey,
                        userIdHeader = userId.toString(),
                        userId = userId,
                        limit = limit,
                        page = page,
                    )
                    val friendFeedPosts = popularPostsResponse.friendPosts

                    val finalList = mutableListOf<CompletePost>()
                    for (post in friendFeedPosts) {
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

                    friendFeed = finalList

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