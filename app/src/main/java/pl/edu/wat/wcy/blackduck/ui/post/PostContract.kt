package pl.edu.wat.wcy.blackduck.ui.post

import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface PostContract {

    interface View: BaseContract.View {
        fun onPostAvailable(postResponse: PostResponse)
        fun onError(msg: String?)
        fun onRateSuccess(userRate: Int, rate: Double?)
        fun onCommentAdded()

    }

    interface Presenter: BaseContract.Presenter<View> {
        fun fetchPost(postId: Int)
        fun rate(rate: Int, postId: Int)
        fun addComment(toString: String, id: Int, videoTime: Long)
    }

}