package pl.edu.wat.wcy.blackduck.ui.profile

import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.data.responses.UserShortResponse
import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface ProfileContract {

    interface View: BaseContract.View {
        fun onError(msg: String?)
        fun onPostsAvailable(it: List<PostResponse>?)
        fun onFollowersAvailable(it: List<UserShortResponse>?)
        fun onFollowedUsersAvailable(it: List<UserShortResponse>?)

    }

    interface Presenter: BaseContract.Presenter<View> {

    }
}