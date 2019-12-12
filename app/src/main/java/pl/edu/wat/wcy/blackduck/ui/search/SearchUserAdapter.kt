package pl.edu.wat.wcy.blackduck.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_row_user.view.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.responses.UserShortResponse
import pl.edu.wat.wcy.blackduck.ui.foreignProfile.ForeignProfileActivity
import pl.edu.wat.wcy.blackduck.ui.main.MainActivity
import pl.edu.wat.wcy.blackduck.ui.main.SectionsStatePageAdapter
import javax.inject.Inject

class SearchUserAdapter(private val items: ArrayList<UserShortResponse>) :
    RecyclerView.Adapter<SearchUserAdapter.CustomViewHolder>() {

    @Inject
    lateinit var prefs: PrefsManager

    @Inject
    lateinit var portalApi: PortalApi

    var context: Context? = null

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindContent(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        BlackduckApplication.appComponent.inject(this)
        this.context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.search_row_user, parent, false)
        return CustomViewHolder(cellForRow, prefs, context!!)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CustomViewHolder(
        val view: View,
        val prefs: PrefsManager,
        val context: Context
    ) : RecyclerView.ViewHolder(view) {

        fun bindContent(user: UserShortResponse) {
            view.textView31.text = user.username
            view.textView32.text = user.fullName
            Picasso.get()
                .load(Configuration.apiFile + user.profileThumbnail)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(view.imageView9)
            view.constraintLayout100.setOnClickListener {
                view.textView31.text = "${user.username}!"
            }
            view.constraintLayout100.setOnClickListener {
                if(user.username == prefs.loadUser().displayName){
                    (context as MainActivity).setViewPager(SectionsStatePageAdapter.FragmentName.PROFILE, false)
                } else {
                    val bundle = Bundle()
                    bundle.putString(Key.USERNAME.name, user.username)
                    val intent = Intent(context, ForeignProfileActivity::class.java)
                    intent.putExtras(bundle)
                    context.startActivity(intent)
                }
            }
        }


    }

}