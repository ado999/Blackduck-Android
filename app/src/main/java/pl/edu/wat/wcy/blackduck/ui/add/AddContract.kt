package pl.edu.wat.wcy.blackduck.ui.add

import pl.edu.wat.wcy.blackduck.data.request.FolderRequest
import pl.edu.wat.wcy.blackduck.data.request.PostRequest
import pl.edu.wat.wcy.blackduck.data.responses.FolderResponse
import pl.edu.wat.wcy.blackduck.ui.base.BaseContract

interface AddContract{

    interface View : BaseContract.View{
        fun onFoldersAvailable(folders: List<FolderResponse>)
        fun onError(msg: String?)
        fun onPostSent()
    }

    interface Presenter : BaseContract.Presenter<View>{
        fun fetchFolders()
        fun sendPost(postRequest: PostRequest, selectedFolder: FolderResponse?, folderName: String, folderDescription: String, folderPrivate: Boolean)
    }

}