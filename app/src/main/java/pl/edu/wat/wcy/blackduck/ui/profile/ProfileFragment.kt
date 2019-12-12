package pl.edu.wat.wcy.blackduck.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.data.responses.UserShortResponse
import pl.edu.wat.wcy.blackduck.ui.editprofile.EditProfileActivity
import pl.edu.wat.wcy.blackduck.ui.main.MainActivity
import pl.edu.wat.wcy.blackduck.ui.main.SectionsStatePageAdapter
import javax.inject.Inject

class ProfileFragment : Fragment(), ProfileContract.View {

    @Inject
    lateinit var presenter: ProfileContract.Presenter

    @Inject
    lateinit var prefs: PrefsManager

    private val myPosts = ArrayList<PostResponse>()

    private val followers = ArrayList<UserShortResponse>()

    private val followedUsers = ArrayList<UserShortResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        BlackduckApplication.appComponent.inject(this)
        presenter.attachView(this)
        view.recycler_view_profile.layoutManager =
            GridLayoutManager(activity, 3)
        view.recycler_view_profile.adapter = ProfileAdapter(myPosts)
        
        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewCreated()
        setupButtons()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
        setupButtons()
    }

    override fun onError(msg: String?) {
        showToast(msg!!, context!!)
    }

    override fun onPostsAvailable(it: List<PostResponse>?) {
        myPosts.clear()
        myPosts.addAll(it ?: emptyList())
        textView6.text = myPosts.size.toString()
    }

    override fun onFollowersAvailable(it: List<UserShortResponse>?) {
        followers.clear()
        followers.addAll(it ?: emptyList())
        textView7.text = followers.size.toString()
    }

    override fun onFollowedUsersAvailable(it: List<UserShortResponse>?) {
        followedUsers.clear()
        followedUsers.addAll(it ?: emptyList())
        textView8.text = followedUsers.size.toString()
    }
    
    private fun setupButtons(){
        val user = prefs.loadUser()
        Picasso.get()
            .load(Configuration.apiFile + user.profilePhotoUrl)
            .fit()
            .centerCrop()
            .into(profile_image)
        Picasso.get()
            .load(Configuration.apiFile + user.profileBackgroundUrl)
            .fit()
            .centerCrop()
            .into(imageView7)
        tv_nickname.text = user.displayName
        if(user.displayName.isNullOrBlank()) textView33.visibility = View.GONE
        else textView33.text = user.description
        textView12.text = prefs.loadString(Key.FULL_NAME)

        btn_edit_profile?.setOnClickListener {
            val intent = Intent(it.context, EditProfileActivity::class.java)
            startActivity(intent)
        }
        btn_settings.setOnClickListener {
            (activity as MainActivity).setViewPager(SectionsStatePageAdapter.FragmentName.SETTINGS, true)
        }
    }
    
}