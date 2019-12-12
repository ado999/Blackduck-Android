package pl.edu.wat.wcy.blackduck.data.network

object Configuration {

    private const val IP = "192.168.43.75" // redmi hotspot
//    private const val IP = "172.28.118.19" //akademik
//    private const val IP = "10.65.0.27" //95
//    private const val IP = "192.168.0.11" //madzia
//    private const val IP = "192.168.1.7" //teściowa
//    private const val IP = "192.168.43.218" //teściowa

    private const val PORT = "8080"

    private const val PROTOCOL = "http"

    const val BASE_URL = "$PROTOCOL://$IP:$PORT"

    const val apiLogin = "/login"

    const val apiSignUp = "/signup"

    const val apiPosts = "/posts"

    const val apiPost = "/posts/post"

    const val apiRate = "/rates"

    const val apiPutComment = "/comments"

    const val apiSendMessage = "/chat/sendMessage"

    const val apiConversations = "/chat/conversations"

    const val apiMessages = "/chat/messages"

    const val apiMyPosts = "/posts/my"

    const val apiForeignPosts = "/posts/user/{username}"

    const val apiMyFollowers = "/followers"

    const val apiMyFollowedUsers = "/followedUsers"

    const val apiFile = "$BASE_URL/file/"

    const val apiPutFile = "/file"

    const val apiMyFolders = "/folders"

    const val apiEditProfile = "/editProfile"

    const val apiSendPost = "$BASE_URL/posts/sendPostUF"

    const val apiSearchUsers = "/search/{text}"

    const val apiSearchPosts = "/posts/search/{text}"

    const val apiProfile = "/user/{username}"

    const val apiFollow = "/follow"

}