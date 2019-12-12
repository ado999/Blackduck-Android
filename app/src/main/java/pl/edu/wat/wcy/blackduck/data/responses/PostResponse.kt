package pl.edu.wat.wcy.blackduck.data.responses

import java.util.*
import kotlin.collections.ArrayList

data class PostResponse(
    val id: Int,
    val title: String,
    val contentUrl: String,
    val vidPhotoUrl: String,
    val thumbnail: String,
    val contentType: ContentTypeResponse,
    val author: UserShortResponse,
    val creationDate: Date,
    val description: String,
    val rootFolder: FolderResponse,
    val rate: Double,
    val comments: ArrayList<CommentResponse>,
    val rates: ArrayList<RateResponse>
    )