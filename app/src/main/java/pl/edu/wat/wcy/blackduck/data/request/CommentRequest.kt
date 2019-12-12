package pl.edu.wat.wcy.blackduck.data.request

data class CommentRequest(val postId: Int, val content: String, val videoTime: Long)