package pl.edu.wat.wcy.blackduck.ui.editprofile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.top_menu_edit_profile.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.request.EditProfileRequest
import java.io.File
import javax.inject.Inject

class EditProfileActivity : AppCompatActivity(), EditProfileContract.View {

    @Inject
    lateinit var presenter: EditProfileContract.Presenter

    @Inject
    lateinit var prefs: PrefsManager

    private var easyImage: EasyImage? = null

    private var newProfile: File? = null

    private var newBackground: File? = null

    private var choosing: Choosing? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BlackduckApplication.appComponent.inject(this)
        easyImage = EasyImage.Builder(this).build()
        presenter.attachView(this)
        setContentView(R.layout.activity_edit_profile)
        setupButtons()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage?.handleActivityResult(
            requestCode, resultCode, data, this, object : DefaultCallback() {
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    if (choosing == Choosing.PROFILE) {
                        newProfile = imageFiles[0].file
                        Picasso.get()
                            .load(newProfile!!)
                            .into(profile_image)
                    } else {
                        newBackground = imageFiles[0].file
                        Picasso.get()
                            .load(newBackground!!)
                            .into(bg_image)
                    }
                }

            }
        )
    }

    override fun onError(message: String?) {
        showToast(message!!, this)
    }

    override fun onProfileEdited() {
        progressBar3.visibility = View.GONE
        showToast("Zmiany zostały zapisane", this)
        finish()
    }


    private fun setupButtons() {
        val user = prefs.loadUser()
        btn_close.setOnClickListener { finish() }
        btn_save.setOnClickListener {
            progressBar3.visibility = View.VISIBLE
            presenter.sendEditProfile(
                newProfile,
                newBackground,
                EditProfileRequest(
                    "", "",
                    fName.text.toString(),
                    textInputEditText3.text.toString(),
                    textInputEditText4.text.toString()
                )
            )
        }
        profile_image.setOnClickListener {
            choosing = Choosing.PROFILE
            easyImage?.openGallery(this)
        }
        bg_image.setOnClickListener {
            choosing = Choosing.BACKGROUND
            easyImage?.openGallery(this)
        }
        presenter.loadPicture(profile_image, user.profilePhotoUrl)
        presenter.loadPicture(bg_image, user.profileBackgroundUrl)
        textInputEditText2.setText(user.displayName)
        fName.setText(user.fullname)
        textInputEditText3.setText(user.description)
        textInputEditText4.setText(user.email)
    }

    enum class Choosing {
        PROFILE, BACKGROUND
    }
}