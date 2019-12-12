package pl.edu.wat.wcy.blackduck.ui.message

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_messages.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.responses.ConversationResponse
import javax.inject.Inject

class MessageActivity : AppCompatActivity(), MessageContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var prefsManager: PrefsManager

    @Inject
    lateinit var presenter: MessageContract.Presenter

    private var conversations = ArrayList<ConversationResponse>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        BlackduckApplication.appComponent.inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_messages)
        recycler_view_message.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler_view_message.adapter = MessagesAdapter(conversations, this)
        onRefresh()
        setupListeners()
    }

    override fun onError(msg: String?) {
        showToast(msg!!, this)
        swipe_container_messages.isRefreshing = false
    }

    override fun onConversationsAvailable(conversations: List<ConversationResponse>?) {
        this.conversations.clear()
        this.conversations.addAll(conversations!!)
        recycler_view_message.adapter?.notifyDataSetChanged()
        swipe_container_messages.isRefreshing = false
    }

    override fun onRefresh() {
        presenter.onConversationsRefresh()
    }

    private fun setupListeners(){
        btn_back.setOnClickListener { finish() }
        swipe_container_messages.setOnRefreshListener(this)
    }

}