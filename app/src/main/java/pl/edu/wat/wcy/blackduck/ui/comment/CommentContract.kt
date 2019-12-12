package pl.edu.wat.wcy.blackduck.ui.comment

import pl.edu.wat.wcy.blackduck.data.responses.CommentResponse
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface CommentContract {

    interface Presenter : BaseContract.Presenter<View>{
        fun getPost(postId: Int)
        fun putComment(postId: Int, toString: String)

    }

    interface View : BaseContract.View{
        fun onPostAvailable(postResponse: PostResponse)
        fun onError(msg: String)
        fun onPutCommentResponseSuccess(commentResponse: CommentResponse)

    }
}