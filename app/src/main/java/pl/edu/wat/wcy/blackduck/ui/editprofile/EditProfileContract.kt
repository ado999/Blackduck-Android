package pl.edu.wat.wcy.blackduck.ui.editprofile

import android.widget.ImageView
import pl.edu.wat.wcy.blackduck.data.request.EditProfileRequest
import pl.edu.wat.wcy.blackduck.ui.base.BaseContract
import java.io.File

interface EditProfileContract {

    interface View: BaseContract.View {
        fun onError(message: String?)
        fun onProfileEdited()

    }

    interface Presenter: BaseContract.Presenter<View> {
        fun loadPicture(profileImage: ImageView?, src: String)
        fun sendEditProfile(profilePic: File?, bgPic: File?, editProfileRequest: EditProfileRequest)

    }

}