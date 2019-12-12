package pl.edu.wat.wcy.blackduck.ui.comment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comment.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.responses.CommentResponse
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.util.BasicTextWatcher
import javax.inject.Inject

class CommentActivity: AppCompatActivity(), CommentContract.View, SwipeRefreshLayout.OnRefreshListener {
    @Inject
    lateinit var presenter: CommentContract.Presenter

    @Inject
    lateinit var prefs: PrefsManager

    @Inject
    lateinit var portalApi: PortalApi

    private var postId: Int = -1

    private var post: PostResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BlackduckApplication.appComponent.inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_comment)
        recycler_view_comment.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler_view_comment.adapter = CommentAdapter(post, this)
        swipe_container_comment.setOnRefreshListener(this)
        postId = intent.extras.getInt(Key.POST_ID.name)
        setupButtons()
        presenter.onViewCreated()
        setupView()
        onRefresh()
    }

    override fun onPostAvailable(postResponse: PostResponse) {
        post = postResponse
        recycler_view_comment.adapter = CommentAdapter(post, this)
        swipe_container_comment.isRefreshing = false
    }

    override fun onError(msg: String) {
        swipe_container_comment.isRefreshing = false
        showToast(msg, this)
    }

    override fun onPutCommentResponseSuccess(commentResponse: CommentResponse){
        post?.comments?.add(commentResponse)
        recycler_view_comment.adapter?.notifyDataSetChanged()
        recycler_view_comment.smoothScrollToPosition(post?.comments?.size!!)
    }

    override fun onRefresh() {
        swipe_container_comment.isRefreshing = true
        presenter.getPost(postId)
    }

    private fun setupButtons(){
        backArrowTV.setOnClickListener {
            finish()
        }
    }

    private fun setupView(){
        Picasso.get()
            .load(Configuration.apiFile + prefs.loadUser().profilePhotoUrl)
            .into(btn_author_avatar2)

        newcommentTIV.addTextChangedListener(BasicTextWatcher(publish_btn))

        newcommentTIV.setOnFocusChangeListener { v, hasFocus ->
            recycler_view_comment.smoothScrollToPosition(post?.comments?.size!!)
        }

        publish_btn.setOnClickListener {
            presenter.putComment(postId, newcommentTIV.text.toString())
            newcommentTIV.setText("")
        }
    }

}