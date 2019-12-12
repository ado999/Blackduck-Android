package pl.edu.wat.wcy.blackduck.ui.foreignProfile

import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.data.responses.UserResponse
import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface ForeignProfileContract {

    interface View: BaseContract.View{
        fun onProfileReady(userResponse: UserResponse?)
        fun onError(message: String?)
        fun onPostsAvailable(posts: List<PostResponse>)

    }

    interface Presenter: BaseContract.Presenter<View>{
        fun fetchUser(username: String)
        fun fetchPosts(username: String)
        fun setFollow(displayName: String, checked: Boolean)

    }

}