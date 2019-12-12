package pl.edu.wat.wcy.blackduck.ui.home

import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface HomeContract {

    interface View: BaseContract.View {
        fun onPostsAvailable(posts: List<PostResponse>)
        fun onError(msg: String)
        fun onNoPostsAvailable()
        fun onPostsAdded(downloadedPosts: List<PostResponse>)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun fetchPosts()
        fun onBottomAchieved()
        fun refreshPosts()
    }
}