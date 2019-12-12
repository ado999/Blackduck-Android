package pl.edu.wat.wcy.blackduck.data.responses

data class FolderResponse(
    val id: Int,
    val author: UserShortResponse,
    val title: String,
    val description: String,
    val files: ArrayList<PostResponse>
)