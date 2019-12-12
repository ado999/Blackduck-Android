package pl.edu.wat.wcy.blackduck.ui.conversation

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_conversation.view.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.responses.MessageResponse
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs

class ConversationAdapter(
    private val messages: List<MessageResponse>?,
    private var context: Context?
) : androidx.recyclerview.widget.RecyclerView.Adapter<ConversationAdapter.CustomViewHolder>() {

    @Inject
    lateinit var prefs: PrefsManager

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CustomViewHolder {
        BlackduckApplication.appComponent.inject(this)
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_row_conversation, parent, false)
        return CustomViewHolder(cellForRow, prefs, context)
    }

    override fun getItemCount(): Int {
        return messages?.size ?: 0
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindContent(messages?.get(position))
    }


    class CustomViewHolder(val view: View, val prefs: PrefsManager, val context: Context?) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        fun bindContent(message: MessageResponse?) {

            val myUsername = prefs.loadString(Key.DISPLAY_NAME)

            when (message?.fromUser?.username) {
                myUsername -> {
                    //ja wysłałem
                    view.constraintLayout14.visibility = View.VISIBLE
                    view.btn_author_avatar.visibility = View.GONE
                    view.constraintLayout15.visibility = View.GONE

                    view.msg_own.text = message?.message
                }
                else -> {
                    //ktoś wysłał
                    view.constraintLayout14.visibility = View.GONE
                    view.btn_author_avatar.visibility = View.VISIBLE
                    view.constraintLayout15.visibility = View.VISIBLE

                    view.msg_opposite.text = message?.message

                    Picasso.get()
                        .load(Configuration.apiFile + message?.fromUser?.profilePhotoUrl)
                        .into(view.btn_author_avatar)
                }
            }

            view.textView30.text = getTimeText(message?.date!!)
        }

        private fun getTimeText(date: Date): String{
            return when {
                DateFormat.format("dd.MM.yyyy", date) == DateFormat.format("dd.MM.yyyy", Date())
                -> "${DateFormat.format("hh:mm", date)}"
                else -> "${DateFormat.format("dd.MM.yyyy", date)} o ${DateFormat.format("hh:mm", date)}"
            }
        }
    }

}
