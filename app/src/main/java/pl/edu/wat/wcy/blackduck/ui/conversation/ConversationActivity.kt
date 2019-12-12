package pl.edu.wat.wcy.blackduck.ui.conversation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_conversation.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.request.MessageRequest
import pl.edu.wat.wcy.blackduck.data.responses.MessageResponse
import pl.edu.wat.wcy.blackduck.util.BasicTextWatcher
import javax.inject.Inject


class ConversationActivity: AppCompatActivity(), ConversationContract.View {

    @Inject
    lateinit var presenter: ConversationContract.Presenter

    private var messageUsername: String? = null
    private var messageProfileUrl: String? = null
    private var messageLastDate: String? = null

    private val messages = ArrayList<MessageResponse>()

    private val messageReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val gb = GsonBuilder()
            gb.setDateFormat("dd.MM.yyyy, HH:mm:ss")
            val messageResponse = gb.create().fromJson(intent?.extras?.getString("message"), MessageResponse::class.java)
            if (messageResponse.fromUser.username == messageUsername) {
                messages.add(messageResponse)
                recycler_view_conversation.adapter?.notifyDataSetChanged()
                recycler_view_conversation.smoothScrollToPosition(messages.size)
                Log.e(javaClass.simpleName, "rec")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        BlackduckApplication.appComponent.inject(this)
        presenter.attachView(this)
        setContentView(pl.edu.wat.wcy.blackduck.R.layout.activity_conversation)
        recycler_view_conversation.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler_view_conversation.adapter = ConversationAdapter(messages, this)
        messageUsername = intent.extras.getString(Key.MESSAGE_USERNAME.name)
        messageProfileUrl = intent.extras.getString(Key.MESSAGE_PROFILE_URL.name)
        messageLastDate = intent.extras.getString(Key.MESSAGE_DATE.name)
        presenter.fetchMessages(messageUsername)

        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, IntentFilter("MessageResponse"))
        setupView()
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
    }

    override fun onMessagesAvailable(messages: List<MessageResponse>?) {
        this.messages.clear()
        this.messages.addAll(messages ?: emptyList())
        this.recycler_view_conversation.adapter?.notifyDataSetChanged()
    }

    override fun onMessageSent(sentMessage: MessageResponse?) {
        this.messages.add(sentMessage!!)
        textInput.text?.clear()
        recycler_view_conversation.smoothScrollToPosition(messages.size)
        recycler_view_conversation.adapter?.notifyDataSetChanged()
    }

    override fun onError(msg: String?) {
        if(msg != null) showToast(msg, this)
    }

    private fun setupView() {
        Picasso.get()
            .load(Configuration.apiFile + messageProfileUrl)
            .into(btn_author_avatar)
        textView25.text = messageUsername
        textView28.text = messageLastDate
        if(messageLastDate!!.endsWith("chwilą")) textView29.visibility = View.INVISIBLE
        imageButton.setOnClickListener {
            finish()
        }
        textInput.setOnClickListener {
            recycler_view_conversation.smoothScrollToPosition(messages.size)
        }
        textInput.addTextChangedListener(BasicTextWatcher(sendMessage))
        sendMessage.setOnClickListener {
            presenter.sendMessage(MessageRequest(textInput.text.toString(), messageUsername!!))
        }
    }

}