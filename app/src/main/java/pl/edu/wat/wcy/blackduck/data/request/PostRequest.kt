package pl.edu.wat.wcy.blackduck.data.request

import java.io.File

data class PostRequest(
    val title: String,
    val file: File,
    val vidPhoto: File? = null,
    val folderId: Int?,
    val description: String
)