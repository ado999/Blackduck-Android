package pl.edu.wat.wcy.blackduck.ui.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_messages.view.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.responses.ConversationResponse
import pl.edu.wat.wcy.blackduck.data.responses.UserShortResponse
import pl.edu.wat.wcy.blackduck.ui.conversation.ConversationActivity
import pl.edu.wat.wcy.blackduck.util.DateUtils
import javax.inject.Inject

class MessagesAdapter(
    private val conversations: List<ConversationResponse>?,
    private var context: Context?
) : RecyclerView.Adapter<MessagesAdapter.CustomViewHolder>() {

    @Inject
    lateinit var prefs: PrefsManager

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CustomViewHolder {
        BlackduckApplication.appComponent.inject(this)
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_row_messages, parent, false)
        return CustomViewHolder(cellForRow, prefs, context)
    }

    override fun getItemCount(): Int {
        return conversations?.size ?: 0
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindContent(conversations?.get(position))
    }


    class CustomViewHolder(val view: View, val prefs: PrefsManager, val context: Context?) :
        RecyclerView.ViewHolder(view) {

        fun bindContent(conversation: ConversationResponse?) {

            val oppositeUser: UserShortResponse? =
                if (conversation?.user1?.username != prefs.loadString(Key.DISPLAY_NAME)) {
                    conversation?.user1
                } else {
                    conversation?.user2
                }

            Picasso.get()
                .load(Configuration.apiFile + oppositeUser?.profilePhotoUrl)
                .into(view.profile_image)

            view.nickname.text = oppositeUser?.username

            val text = DateUtils.getDateDiff(oppositeUser?.lastActivity!!)

            view.active.text = "Aktywny(a) $text"

            view.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(Key.MESSAGE_USERNAME.name, oppositeUser.username)
                bundle.putString(Key.MESSAGE_PROFILE_URL.name, oppositeUser.profilePhotoUrl)
                bundle.putString(Key.MESSAGE_DATE.name, text)
                val intent = Intent(context, ConversationActivity::class.java)
                intent.putExtras(bundle)
                context?.startActivity(intent)
            }
        }
    }

}
