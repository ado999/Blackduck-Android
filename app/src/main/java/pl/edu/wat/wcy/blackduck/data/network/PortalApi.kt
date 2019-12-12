package pl.edu.wat.wcy.blackduck.data.network

import io.reactivex.Observable
import okhttp3.MultipartBody
import pl.edu.wat.wcy.blackduck.data.request.*
import pl.edu.wat.wcy.blackduck.data.responses.*
import retrofit2.http.*

interface PortalApi {

    @POST(Configuration.apiLogin)
    fun login(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Body loginRequest: LoginRequest
    ): Observable<LoginResponse>

    @POST(Configuration.apiSignUp)
    fun signup(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Body loginRequest: SignupRequest
    ): Observable<SignupResponse>

    @GET(Configuration.apiPosts)
    fun getPosts(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Query(Constants.PAGE) page: Int
    ): Observable<List<PostResponse>>

    @GET(Configuration.apiForeignPosts)
    fun getForeignPosts(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Path("username") username: String
    ): Observable<List<PostResponse>>

    @GET(Configuration.apiPost)
    fun getPost(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Query(Constants.POST_ID_QUERY) id: Int
    ): Observable<PostResponse>

    @POST(Configuration.apiRate)
    fun rate(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Body rateRequest: RateRequest
    ): Observable<Double>

    @POST(Configuration.apiPutComment)
    fun comment(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Body commentRequest: CommentRequest
    ): Observable<CommentResponse>

    @GET(Configuration.apiConversations)
    fun getConversations(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String
    ): Observable<List<ConversationResponse>>

    @POST(Configuration.apiMessages)
    fun getMessages(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Body getMessageRequest: GetMessagesRequest
    ): Observable<List<MessageResponse>>

    @POST(Configuration.apiSendMessage)
    fun sendMessage(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Body getMessageRequest: MessageRequest
    ): Observable<MessageResponse>

    @GET(Configuration.apiMyPosts)
    fun myPosts(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String
    ): Observable<List<PostResponse>>

    @GET(Configuration.apiMyFollowers)
    fun myFollowers(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String
    ): Observable<List<UserShortResponse>>

    @GET(Configuration.apiMyFollowedUsers)
    fun myFollowedUsers(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String
    ): Observable<List<UserShortResponse>>

    @GET(Configuration.apiMyFolders)
    fun myFolders(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String
    ): Observable<List<FolderResponse>>

    @POST(Configuration.apiMyFolders)
    fun addFolder(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Body folderRequest: FolderRequest
    ): Observable<Int>

    @POST(Configuration.apiSendPost)
    fun sendPost(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Body postRequestUF: PostRequestUF
    ): Observable<Boolean>

    @Multipart
    @POST(Configuration.apiPutFile)
    fun sendFile(
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Part file: MultipartBody.Part
    ): Observable<FilenameResponse>

    @POST(Configuration.apiEditProfile)
    fun editProfile(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Body editProfileRequest: EditProfileRequest
    ): Observable<UserResponse>

    @GET(Configuration.apiSearchUsers)
    fun searchUsers(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Path("text") text: String
    ): Observable<List<UserShortResponse>>

    @GET(Configuration.apiSearchPosts)
    fun searchPosts(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Path("text") text: String
    ): Observable<List<PostResponse>>

    @GET(Configuration.apiProfile)
    fun getProfile(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Path("username") username: String
    ): Observable<UserResponse>

    @GET(Configuration.apiFollow)
    fun setFollow(
        @Header(Constants.ACCEPT) consumes: String,
        @Header(Constants.CONTENT_TYPE) produces: String,
        @Header(Constants.AUTHORIZATION) authorisation: String,
        @Query("username") username: String,
        @Query("follow") follow: Boolean
    ): Observable<Boolean>

}
