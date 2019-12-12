package pl.edu.wat.wcy.blackduck.ui.search

import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.data.responses.UserShortResponse
import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface SearchContract {

    interface View: BaseContract.View {
        fun onUsersSearched(res: List<UserShortResponse>)
        fun onPostsSearched(res: List<PostResponse>)
        fun onSearchError(msg: String)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun searchInUsers(text: String)
        fun searchInPosts(text: String)

    }
}