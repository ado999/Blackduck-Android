package pl.edu.wat.wcy.blackduck.data.request

data class PostRequestUF(
    val title: String,
    val file: String,
    val folderId: Int?,
    val description: String
)