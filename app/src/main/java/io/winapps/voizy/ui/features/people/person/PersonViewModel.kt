package io.winapps.voizy.ui.features.people.person

import android.content.Context
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import io.winapps.voizy.data.model.posts.Comment
import io.winapps.voizy.data.model.posts.CompletePost
import io.winapps.voizy.data.model.posts.CreatePostRequest
import io.winapps.voizy.data.model.posts.GetBatchPresignedPutUrlRequest
import io.winapps.voizy.data.model.posts.ListPost
import io.winapps.voizy.data.model.posts.PresignedFile
import io.winapps.voizy.data.model.posts.PutPostCommentRequest
import io.winapps.voizy.data.model.posts.PutPostMediaRequest
import io.winapps.voizy.data.model.posts.PutPostReactionRequest
import io.winapps.voizy.data.model.posts.ReactionType
import io.winapps.voizy.data.model.users.Friend
import io.winapps.voizy.data.model.users.UserImage
import io.winapps.voizy.data.repository.PostsRepository
import io.winapps.voizy.data.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {
    var selectedPersonId by mutableLongStateOf(0)
        private set

    var username by mutableStateOf("")
        private set

    fun selectPerson(personId: Long, username: String) {
        selectedPersonId = personId
        this.username = username
    }

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var profilePicURL by mutableStateOf<String?>(null)
        private set

    var coverPicURL by mutableStateOf<String?>(null)
        private set

    var firstName by mutableStateOf<String?>(null)
        private set

    var lastName by mutableStateOf<String?>(null)
        private set

    var preferredName by mutableStateOf("")
        private set

    var birthDate by mutableStateOf<String?>(null)
        private set

    var cityOfResidence by mutableStateOf<String?>(null)
        private set

    var placeOfWork by mutableStateOf<String?>(null)
        private set

    var dateJoined by mutableStateOf<String?>(null)
        private set

    fun loadProfilePic(personId: Long, userId: Long, apiKey: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = usersRepository.getProfilePic(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = personId
                )
                profilePicURL = response.profilePicURL
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun loadCoverPic(personId: Long, userId: Long, apiKey: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = usersRepository.getCoverPic(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = personId
                )
                coverPicURL = response.coverPicURL
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun loadProfileInfo(personId: Long, userId: Long, apiKey: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = usersRepository.getProfileInfo(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = personId
                )
                firstName = response.firstName
                lastName = response.lastName
                preferredName = response.preferredName
                birthDate = response.birthDate
                cityOfResidence = response.cityOfResidence
                placeOfWork = response.placeOfWork
                dateJoined = response.dateJoined
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}

@HiltViewModel
class PersonPostsViewModel @Inject constructor(
    val postsRepository: PostsRepository,
    val usersRepository: UsersRepository
) : ViewModel() {
    var isLoadingTotalPosts by mutableStateOf(false)
        private set

    var totalPostsErrorMessage by mutableStateOf<String?>(null)
        private set

    var totalPosts by mutableLongStateOf(0)
        private set

    var isLoadingPosts by mutableStateOf(false)
        private set

    var postsErrorMessage by mutableStateOf<String?>(null)
        private set

    var completePosts by mutableStateOf(emptyList<CompletePost>())
        private set

    var posts by mutableStateOf(emptyList<ListPost>())
        private set

    var displayName by mutableStateOf<String?>(null)
        private set

    var selectedLocation by mutableStateOf<Location?>(null)
        private set

    var selectedImages by mutableStateOf<List<Uri>>(emptyList())
        private set

    var comments by mutableStateOf<List<Comment>?>(emptyList())
        private set

    var isLoadingComments by mutableStateOf(false)
        private set

    var commentText by mutableStateOf("")
        private set

    var isPuttingNewComment by mutableStateOf(false)
        private set

    var postText by mutableStateOf("")
        private set

    var isCreatingNewPost by mutableStateOf(false)
        private set

    var isSubmittingPost by mutableStateOf(false)
        private set

    var showCreatePostSuccessToast by mutableStateOf(false)
        private set

    fun onPostTextChanged(newValue: String) {
        postText = newValue
    }

    fun onOpenCreatePost() {
        isCreatingNewPost = true
    }

    fun onCloseCreatePost() {
        isCreatingNewPost = false
        postText = ""
        selectedImages = emptyList()
    }

    fun loadCompletePosts(personId: Long, userId: Long, apiKey: String, limit: Long = 20, page: Long = 1, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            isLoadingPosts = true
            postsErrorMessage = null

            try {
//                val cachedPosts = if (!forceRefresh) postsCache.getCachedPosts(userId, limit, page) else null
                val cachedPosts = null

                if (cachedPosts != null) {
//                    completePosts = cachedPosts
                    isLoadingPosts = false
                } else {
                    val listResponse = postsRepository.listPosts(
                        apiKey = apiKey,
                        userIdHeader = userId.toString(),
                        userId = personId,
                        limit = limit,
                        page = page
                    )
                    val rawPosts = listResponse.posts

                    val finalList = mutableListOf<CompletePost>()
                    for (post in rawPosts) {
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
                        val profilePicDeferred = async {
                            usersRepository.getProfilePic(
                                apiKey = apiKey,
                                userIdHeader = userId.toString(),
                                userId = post.userID
                            )
                        }
                        val details = detailDeferred.await()
                        val media = mediaDeferred.await()
                        val profilePicResponse = profilePicDeferred.await()
                        val profilePicURL = profilePicResponse.profilePicURL

                        val complete = CompletePost(
                            post = post,
                            profilePicURL = profilePicURL,
                            totalComments = post.totalComments,
                            reactions = details.reactions,
                            hashtags = details.hashtags,
                            images = media.images.orEmpty(),
                            videos = media.videos.orEmpty()
                        )
                        finalList.add(complete)
                    }

                    completePosts = finalList

//                    postsCache.cachePosts(userId, limit, page, finalList)
                }
            } catch (e: Exception) {
                postsErrorMessage = e.message
            } finally {
                isLoadingPosts = false
            }
        }
    }

    fun loadTotalPosts(personId: Long, userId: Long, apiKey: String) {
        viewModelScope.launch {
            isLoadingTotalPosts = true
            totalPostsErrorMessage = null

            try {
                val response = postsRepository.getTotalPosts(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = personId
                )
                totalPosts = response.totalPosts
            } catch (e: Exception) {
                totalPostsErrorMessage = e.message
            } finally {
                isLoadingTotalPosts = false
            }
        }
    }

    fun putPostReaction(postId: Long, userId: Long, apiKey: String, token: String, reactionType: ReactionType) {
        viewModelScope.launch {
            try {
                val response = postsRepository.putPostReaction(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    token = "Bearer $token",
                    putPostReactionRequest = PutPostReactionRequest(
                        postID = postId,
                        userID = userId,
                        reactionType = reactionType.toString(),
                    )
                )
            } catch (e: Exception) {
                //
            }
        }
    }

    fun addImage(uri: Uri) {
        if (selectedImages.size < 10) {
            selectedImages = selectedImages + uri
        }
    }

    fun addImages(uris: List<Uri>) {
        val total = selectedImages.size + uris.size
        val slice = if (total > 10) uris.take(10 - selectedImages.size) else uris
        selectedImages = selectedImages + slice
    }

    fun removeImage(uri: Uri) {
        selectedImages = selectedImages - uri
    }

    fun loadPostComments(userId: Long, apiKey: String, postId: Long, limit: Long = 20, page: Long = 1) {
        viewModelScope.launch {
            isLoadingComments = true

            try {
                val response = postsRepository.listComments(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    postId = postId,
                    limit = limit,
                    page = page,
                )
                comments = response.comments ?: emptyList()
            } catch (e: Exception) {
                //
            } finally {
                isLoadingComments = false
            }
        }
    }

    fun onChangeCommentText(newValue: String) {
        commentText = newValue
    }

    fun putPostComment(userId: Long, apiKey: String, token: String, postId: Long) {
        if (commentText.isEmpty() || commentText.isBlank()) {
            return
        }

        viewModelScope.launch {
            isPuttingNewComment = true

            try {
                val response = postsRepository.putPostComment(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    token = "Bearer $token",
                    putPostCommentRequest = PutPostCommentRequest(
                        postID = postId,
                        userID = userId,
                        contentText = commentText
                    )
                )
                if (response.success) {
                    loadPostComments(
                        userId = userId,
                        apiKey = apiKey,
                        postId = postId,
                        limit = 20,
                        page = 1,
                    )
                }
            } catch (e: Exception) {
                //
            } finally {
                isPuttingNewComment = false
                commentText = ""
            }
        }
    }

    fun submitPost(context: Context, personId: Long, userId: Long, apiKey: String, token: String, locationName: String?, locationLat: Double?, locationLong: Double?, hashtags: List<String> = emptyList()) {
        viewModelScope.launch {
            isSubmittingPost = true

            try {
                val response = postsRepository.createPost(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    token = "Bearer $token",
                    createPostRequest = CreatePostRequest(
                        userID = userId,
                        toUserID = personId,
                        originalPostID = null,
                        contentText = postText,
                        locationName = locationName,
                        locationLat = locationLat,
                        locationLong = locationLong,
                        images = emptyList<String>(),
                        hashtags = hashtags,
                        isPoll = false,
                        pollQuestion = null,
                        pollDurationType = null,
                        pollDurationLength = null,
                        pollOptions = emptyList<String>()
                    )
                )
                if (response.success && selectedImages.isNotEmpty()) {
                    val postID: Long = response.postID!!
                    val fileNames: List<String> = List(selectedImages.size) { index ->
                        "image${index + 1}.jpg"
                    }
                    val imagesResponse = postsRepository.getBatchPresignedPutUrls(
                        apiKey = apiKey,
                        userIdHeader = userId.toString(),
                        token = "Bearer $token",
                        getBatchPresignedPutUrlRequest = GetBatchPresignedPutUrlRequest(
                            postID = postID,
                            userID = userId, // TODO: figure out if I need to change this to personId after looking at server code
                            fileNames = fileNames,
                        )
                    )

                    uploadImagesToS3(
                        context,
                        selectedImages,
                        imagesResponse.images,
                    )

                    val images: List<String> = List(imagesResponse.images.size) { index ->
                        imagesResponse.images[index].finalURL
                    }
                    for (img in images) {
                        Log.d(null, "IMAGEEEEEEEEEEEEEEEE $img")
                    }
                    postsRepository.putPostMedia(
                        apiKey = apiKey,
                        userIdHeader = userId.toString(),
                        token = "Bearer $token",
                        putPostMediaRequest = PutPostMediaRequest(
                            postID = postID,
                            images = images
                        )
                    )
                }
                if (response.success) {
                    showCreatePostSuccessToast = true

//                    postsCache.invalidateUserCache(userId)

                    loadCompletePosts(personId, userId, apiKey)
                }
            } catch (e: Exception) {
                Log.d(e.message, "Create post exception: ${e.message}")
            } finally {
                isSubmittingPost = false
            }
        }
    }

    private suspend fun uploadImagesToS3(
        context: Context,
        selectedImages: List<Uri>,
        presignedFiles: List<PresignedFile>,
    ) {
        val count = minOf(selectedImages.size, presignedFiles.size)
        for (i in 0 until count) {
            val localUri = selectedImages[i]
            val presignedUrl = presignedFiles[i].presignedURL
            putImageToPresignedUrl(context, localUri, presignedUrl)
        }
    }

    suspend fun putImageToPresignedUrl(
        context: Context,
        localUri: Uri,
        presignedUrl: String
    ) {
        withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(localUri) ?: return@withContext

            val bytes = inputStream.readBytes()
            inputStream.close()

            val mimeType = "image/jpeg"

            val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            val request = Request.Builder()
                .url(presignedUrl)
                .put(requestBody)
                .build()

            val client = OkHttpClient()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Failed to upload to S3. HTTP ${response.code}")
                }
            }
        }
    }

    fun endShowCreatePostSuccessToast() {
        showCreatePostSuccessToast = false
    }
}

enum class FriendStatus(val label: String) {
    IDLE("idle"),
    PENDING("pending"),
    ACCEPTED("accepted"),
    BLOCKED("blocked");

    companion object {
        val default = IDLE
    }
}

@HiltViewModel
class PersonFriendsViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {
    var friendStatus by mutableStateOf<FriendStatus>(FriendStatus.IDLE)
        private set

    var friends by mutableStateOf(emptyList<Friend>())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var totalFriends by mutableLongStateOf(0)
        private set

    var isLoadingTotalFriends by mutableStateOf(false)
        private set

    var totalFriendsErrorMessage by mutableStateOf<String?>(null)
        private set

    var searchText by mutableStateOf("")
        private set

    fun loadFriendStatus(personId: Long, userId: Long, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = usersRepository.getFriendStatus(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = userId,
                    friendId = personId
                )
                friendStatus = when(response.status) {
                    "pending" -> {
                        FriendStatus.PENDING
                    }
                    "accepted" -> {
                        FriendStatus.ACCEPTED
                    }
                    "blocked" -> {
                        FriendStatus.BLOCKED
                    }
                    else -> {
                        FriendStatus.IDLE
                    }
                }
            } catch (e: Exception) {
                //
            }
        }
    }

    fun loadFriends(personId: Long, userId: Long, apiKey: String, limit: Long = 50, page: Long = 1) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = usersRepository.listFriendships(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = personId,
                    limit = limit,
                    page = page,
                )
                friends = if (!response.friends.isNullOrEmpty()) response.friends else emptyList()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun loadTotalFriends(personId: Long, userId: Long, apiKey: String) {
        viewModelScope.launch {
            isLoadingTotalFriends = true
            totalFriendsErrorMessage = null

            try {
                val response = usersRepository.getTotalFriends(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = personId
                )
                totalFriends = response.totalFriends
            } catch (e: Exception) {
                totalFriendsErrorMessage = e.message
            } finally {
                isLoadingTotalFriends = false
            }
        }
    }

    fun onChangeSearchText(newValue: String) {
        searchText = newValue
    }
}

@HiltViewModel
class PersonPhotosViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {
    var userImages by mutableStateOf(emptyList<UserImage>())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)

    var totalImages by mutableLongStateOf(0)
        private set

    var isLoadingTotalImages by mutableStateOf(false)
        private set

    var totalImagesErrorMessage by mutableStateOf<String?>(null)
        private set

    fun loadUserImages(personId: Long, userId: Long, apiKey: String, limit: Long = 40, page: Long = 1) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = usersRepository.listImages(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = personId,
                    limit = limit,
                    page = page,
                )
                userImages = response.images
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun loadTotalImages(personId: Long, userId: Long, apiKey: String) {
        viewModelScope.launch {
            isLoadingTotalImages = true
            totalImagesErrorMessage = null

            try {
                val response = usersRepository.getTotalImages(
                    apiKey = apiKey,
                    userIdHeader = userId.toString(),
                    userId = personId
                )
                totalImages = response.totalImages
            } catch (e: Exception) {
                totalImagesErrorMessage = e.message
            } finally {
                isLoadingTotalImages = false
            }
        }
    }
}

@HiltViewModel
class PersonPlayerViewModel @Inject constructor() : ViewModel() {
    var isLoadingSong by mutableStateOf(false)
        private set

    var songErrorMessage by mutableStateOf<String?>(null)
        private set

    var songURL by mutableStateOf<String?>(null)
        private set

    fun loadSongURL(personId: Long, userId: Long, apiKey: String) {
        viewModelScope.launch {
            isLoadingSong = true
            songErrorMessage = null

            try {
                // load song here
            } catch (e: Exception) {
                songErrorMessage = e.message
            } finally {
                isLoadingSong = false
            }
        }
    }

    private var _exoPlayer: ExoPlayer? = null
    var isPlaying by mutableStateOf(false)
        private set

    var currentPosition by mutableLongStateOf(0L)
        private set

    var duration by mutableLongStateOf(0L)
        private set

    fun getExoPlayer(context: Context): ExoPlayer {
        if (_exoPlayer == null) {
            _exoPlayer = ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.fromUri("https://voizy-app.s3.us-west-2.amazonaws.com/default/music/ES_Push+and+Pull+-+TAGE.mp3")
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = false

                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(playing: Boolean) {
                        this@PersonPlayerViewModel.isPlaying = playing
                    }
                })
            }

            // Start position tracking
            viewModelScope.launch {
                while (true) {
                    if (isPlaying) {
                        _exoPlayer?.let {
                            currentPosition = it.currentPosition
                            if (duration <= 0 && it.duration > 0) {
                                duration = it.duration
                            }
                        }
                    }
                    delay(500)
                }
            }
        }
        return _exoPlayer!!
    }

    override fun onCleared() {
        _exoPlayer?.release()
        _exoPlayer = null
        super.onCleared()
    }
}