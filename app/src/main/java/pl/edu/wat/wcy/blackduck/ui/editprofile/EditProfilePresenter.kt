package pl.edu.wat.wcy.blackduck.ui.editprofile

import android.widget.ImageView
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.request.EditProfileRequest
import java.io.File
import javax.inject.Inject

class EditProfilePresenter : EditProfileContract.Presenter {

    @Inject
    lateinit var portalApi: PortalApi

    @Inject
    lateinit var prefs: PrefsManager

    lateinit var view: EditProfileContract.View

    private var subscription: Disposable? = null

    override fun sendEditProfile(
        profilePic: File?,
        bgPic: File?,
        editProfileRequest: EditProfileRequest
    ) {
        sendProfilePic(profilePic, bgPic, editProfileRequest)
    }

    private fun sendProfilePic(
        profilePic: File?,
        bgPic: File?,
        editProfileRequest: EditProfileRequest
    ) {
        if (profilePic == null) {
            sendBgPic("", bgPic, editProfileRequest)
            return
        }
        val mimeType = getMimeType(profilePic)
        val requestFile = RequestBody.create(MediaType.parse(mimeType), profilePic)
        val multipartBody = MultipartBody.Part.createFormData("file", profilePic.name, requestFile)
        subscription = portalApi.sendFile(
            prefs.loadUser().token,
            multipartBody
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    sendBgPic(it.filename, bgPic, editProfileRequest)
                }, {
                    view.onError(it.message)
                }
            )
    }

    private fun sendBgPic(
        profileName: String,
        bgPic: File?,
        editProfileRequest: EditProfileRequest
    ) {
        if (bgPic == null) {
            editProfileRequest.profilePicture = profileName
            editProfileRequest.backgroundPicture = ""
            sendEditProfileReq(editProfileRequest)
            return
        }
        val mimeType = getMimeType(bgPic)
        val requestFile = RequestBody.create(MediaType.parse(mimeType), bgPic)
        val multipartBody = MultipartBody.Part.createFormData("file", bgPic.name, requestFile)
        subscription = portalApi.sendFile(
            prefs.loadUser().token,
            multipartBody
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    editProfileRequest.profilePicture = profileName
                    editProfileRequest.backgroundPicture = it.filename
                    sendEditProfileReq(editProfileRequest)
                }, {
                    view.onError(it.message)
                }
            )
    }

    private fun sendEditProfileReq(editProfileRequest: EditProfileRequest) {
        subscription = portalApi
            .editProfile(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadUser().token,
                editProfileRequest
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    makeChanges(editProfileRequest)
                    view.onProfileEdited()
                }, {
                    view.onError(it.message)
                }
            )
    }

    private fun makeChanges(request: EditProfileRequest) {
        val user = prefs.loadUser()
        if (!request.profilePicture.isBlank())
            user.profilePhotoUrl = request.profilePicture
        if (!request.backgroundPicture.isBlank())
            user.profileBackgroundUrl = request.backgroundPicture
        user.fullname = request.fullName
        user.description = request.description
        user.email = request.email
        prefs.storeUser(user)
    }

    override fun loadPicture(profileImage: ImageView?, src: String) {
        Picasso.get()
            .load(Configuration.apiFile + src)
            .resize(200, 200)
            .centerCrop()
            .placeholder(R.drawable.loading)
            .error(R.drawable.placeholder)
            .into(profileImage)
    }

    override fun attachView(view: EditProfileContract.View) {
        BlackduckApplication.appComponent.inject(this)
        this.view = view
    }

    override fun onViewCreated() {
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }

    private fun getMimeType(file: File): String {
        return when {
            file.extension == "jpg" -> "image/jpeg"
            file.extension == "png" -> "image/png"
            else -> ""
        }
    }

}