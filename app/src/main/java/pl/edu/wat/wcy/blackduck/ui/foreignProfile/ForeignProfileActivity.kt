package pl.edu.wat.wcy.blackduck.ui.foreignProfile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_foreign_profile.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.data.responses.UserResponse
import pl.edu.wat.wcy.blackduck.ui.conversation.ConversationActivity
import pl.edu.wat.wcy.blackduck.ui.profile.ProfileAdapter
import pl.edu.wat.wcy.blackduck.util.DateUtils
import javax.inject.Inject

class ForeignProfileActivity: AppCompatActivity(), ForeignProfileContract.View {

    @Inject
    lateinit var presenter: ForeignProfileContract.Presenter

    private var username: String? = null

    private var userResponse: UserResponse? = null

    private val posts = ArrayList<PostResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BlackduckApplication.appComponent.inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_foreign_profile)
        username = intent.extras.getString(Key.USERNAME.name)
        presenter.fetchUser(username!!)
        presenter.fetchPosts(username!!)
        recyclerViewForeign.layoutManager =
            GridLayoutManager(this, 3)
        recyclerViewForeign.adapter = ProfileAdapter(posts)
    }

    override fun onProfileReady(userResponse: UserResponse?) {
        this.userResponse = userResponse
        bindFields()
        bindPhotos()
        setupListeners()
    }

    override fun onPostsAvailable(posts: List<PostResponse>) {
        this.posts.clear()
        this.posts.addAll(posts)
        recyclerViewForeign.adapter?.notifyDataSetChanged()
    }

    override fun onError(message: String?) {
        showToast(message!!, this)
    }

    private fun bindFields(){
        textView34.text = userResponse?.displayName
        textView35.text = userResponse?.fullName
        if(userResponse?.description.isNullOrBlank()){
            textView36.visibility = View.GONE
        } else {
            textView36.text = userResponse?.description
        }
        toggleButton.isChecked = userResponse?.followed!!
    }

    private fun bindPhotos(){
        Picasso.get()
            .load(Configuration.apiFile + userResponse?.profileBackgroundUrl)
            .fit()
            .centerCrop()
            .into(imageView11)
        Picasso.get()
            .load(Configuration.apiFile + userResponse?.profileThumbnail)
            .fit()
            .centerCrop()
            .placeholder(R.drawable.progress_animation)
            .into(profile_image)
    }

    private fun setupListeners(){
        imageButton5.setOnClickListener {
            val text = DateUtils.getDateDiff(userResponse?.lastActivity!!)
            val bundle = Bundle()
            bundle.putString(Key.MESSAGE_USERNAME.name, userResponse?.displayName)
            bundle.putString(Key.MESSAGE_PROFILE_URL.name, userResponse?.profilePhotoUrl)
            bundle.putString(Key.MESSAGE_DATE.name, text)
            val intent = Intent(this, ConversationActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        toggleButton.setOnCheckedChangeListener {
                _, isChecked ->
            presenter.setFollow(userResponse?.displayName!!, isChecked)
        }
    }

}