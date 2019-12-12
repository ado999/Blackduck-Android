package pl.edu.wat.wcy.blackduck.data.request

data class FolderRequest(
    val folderName: String,
    val description: String,
    val folderPrivate: Boolean
)