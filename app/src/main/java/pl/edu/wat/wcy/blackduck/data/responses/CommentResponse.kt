package pl.edu.wat.wcy.blackduck.data.responses

import java.util.Date

data class CommentResponse(
    val author: UserShortResponse,
    val content: String,
    val creationDate: Date,
    val videoTime: Long
)