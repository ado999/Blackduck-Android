package pl.edu.wat.wcy.blackduck.ui.add

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.request.FolderRequest
import pl.edu.wat.wcy.blackduck.data.request.PostRequest
import pl.edu.wat.wcy.blackduck.data.request.PostRequestUF
import pl.edu.wat.wcy.blackduck.data.responses.FolderResponse
import java.io.File
import javax.inject.Inject

class AddPresenter : AddContract.Presenter {

    @Inject
    lateinit var portalApi: PortalApi

    @Inject
    lateinit var prefs: PrefsManager

    lateinit var view: AddContract.View

    var subscription: Disposable? = null

    override fun fetchFolders() {
        subscription = portalApi
            .myFolders(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadString(Key.TOKEN)
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onFoldersAvailable(it)
                },
                {
                    view.onError(it.message)
                }
            )
    }

    override fun sendPost(
        postRequest: PostRequest,
        selectedFolder: FolderResponse?,
        folderName: String,
        folderDescription: String,
        folderPrivate: Boolean
    ) {
        if (selectedFolder == null) {
            createFolder(FolderRequest(folderName, folderDescription, folderPrivate), postRequest)
        } else {
            onFolderReady(selectedFolder.id, postRequest)
        }
    }

    private fun createFolder(folderRequest: FolderRequest, postRequest: PostRequest) {
        subscription = portalApi
            .addFolder(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadUser().token,
                folderRequest
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                onFolderReady(it, postRequest)
            }
    }

    private fun sendPostUF(
        fileName: String,
        folderId: Int,
        postRequest: PostRequest
    ) {
        val postRequestUF = PostRequestUF(
            postRequest.title,
            fileName,
            folderId,
            postRequest.description
        )
        subscription = portalApi
            .sendPost(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadUser().token,
                postRequestUF
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    view.onPostSent()
                }, {
                    view.onError(it.message + " sendPostUF")
                }
            )
    }

    private fun onFolderReady(folderId: Int, postRequest: PostRequest) {
        sendFile(postRequest.file, folderId, postRequest)
    }

    private fun sendFile(file: File, folderId: Int, postRequest: PostRequest) {
        val mimeType = getMimeType(file)
        val requestFile = RequestBody.create(MediaType.parse(mimeType), file)
        val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestFile)
        subscription = portalApi.sendFile(
            prefs.loadUser().token,
            multipartBody
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    sendPostUF(it.filename, folderId, postRequest)
                }, {
                    view.onError(it.message + " sendFile")
                }
            )
    }

    private fun getMimeType(file: File): String {
        return when {
            file.extension == "jpg" -> "image/jpeg"
            file.extension == "png" -> "image/png"
            file.extension == "mp4" -> "video/mp4"
            file.extension == "mov" -> "video/quicktime"
            else -> ""
        }
    }

    override fun attachView(view: AddContract.View) {
        this.view = view
        BlackduckApplication.appComponent.inject(this)
    }

    override fun onViewCreated() {
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }
}