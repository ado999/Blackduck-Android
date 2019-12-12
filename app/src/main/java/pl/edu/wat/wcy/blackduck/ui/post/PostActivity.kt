package pl.edu.wat.wcy.blackduck.ui.post

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.responses.ContentTypeResponse
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.ui.comment.CommentActivity
import pl.edu.wat.wcy.blackduck.ui.conversation.ConversationActivity
import pl.edu.wat.wcy.blackduck.ui.foreignProfile.ForeignProfileActivity
import pl.edu.wat.wcy.blackduck.ui.main.MainActivity
import pl.edu.wat.wcy.blackduck.util.BasicTextWatcher
import pl.edu.wat.wcy.blackduck.util.DateUtils
import pl.edu.wat.wcy.blackduck.util.StarBinder
import java.io.File
import javax.inject.Inject

class PostActivity : AppCompatActivity(), PostContract.View {

    @Inject
    lateinit var presenter: PostContract.Presenter

    @Inject
    lateinit var prefs: PrefsManager

    private var postId: Int = -1

    private var player: ExoPlayer? = null

    lateinit var postResponse: PostResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BlackduckApplication.appComponent.inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_post)
        postId = intent.extras.getInt(Key.POST_ID.name)
        presenter.fetchPost(postId)
    }

    override fun onResume() {
        super.onResume()
        player?.retry()
    }

    override fun onPause() {
        super.onPause()
        player?.stop()
    }

    override fun onPostAvailable(postResponse: PostResponse) {
        this.postResponse = postResponse
        bindFields()
        bindImages()
        setupListeners()
    }

    override fun onRateSuccess(userRate: Int, rate: Double?) {
        StarBinder.bindStars(emptyList(), userRate, prefs.loadUser().displayName, listOf(star1, star2, star3, star4, star5))
        tv_likes.text = "%.2f".format(rate)
    }

    override fun onCommentAdded() {
        showToast("Komentarz został dodany", this)
        textInputEditText.text?.clear()
        textInputEditText.clearFocus()
        hideKeyboard()
    }

    override fun onError(msg: String?) {
        showToast(msg ?: "Wystąpił błąd", this)
    }

    private fun bindFields(){
        tv_author_nickname.text = postResponse.author.username
        tv_comment_count.text = postResponse.comments.size.toString()
        tv_likes.text = "%.2f".format(postResponse.rate)
    }

    private fun bindImages(){
        Picasso.get()
            .load(Configuration.apiFile + postResponse.author.profilePhotoUrl)
            .fit()
            .centerCrop()
            .into(btn_author_avatar)
        Picasso.get()
            .load(Configuration.apiFile + prefs.loadUser().profilePhotoUrl)
            .fit()
            .centerCrop()
            .into(userProfilePicture)
        StarBinder.bindStars(postResponse.rates, null, prefs.loadUser().displayName, listOf(star1, star2, star3, star4, star5))
        if (postResponse.contentType == ContentTypeResponse.PHOTO){
            playerView.visibility = View.GONE
            iv_image.visibility = View.VISIBLE
            Picasso.get()
                .load(Configuration.apiFile + postResponse.contentUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(iv_image)
        } else {
            player = ExoPlayerFactory.newSimpleInstance(this)
            playerView.player = player
            val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "BlackduckApplication"))
            val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(Configuration.apiFile + postResponse.contentUrl))
            playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            player?.prepare(videoSource)
            player?.playWhenReady = true
            player?.repeatMode = Player.REPEAT_MODE_ONE
            for(comment in postResponse.comments){
                if(comment.videoTime > 0){
                    val target =
                        PlayerMessage.Target { _, payload ->
                            if((payload as String) == ""){
                                textView38.text = comment.author.username
                                textView39.text = comment.content
                                cl_comm_time.visibility = View.VISIBLE
                            } else if ((payload as String) == (textView38.text.toString() + textView39.text.toString())) {
                                cl_comm_time.visibility = View.INVISIBLE
                            }
                        }
                    player?.createMessage(target)
                        ?.setHandler(Handler())
                        ?.setPosition(comment.videoTime)
                        ?.setPayload("")
                        ?.setDeleteAfterDelivery(false)
                        ?.send()
                    player?.createMessage(target)
                        ?.setHandler(Handler())
                        ?.setPosition(comment.videoTime + 3000)
                        ?.setPayload(comment.author.username + comment.content)
                        ?.setDeleteAfterDelivery(false)
                        ?.send()
                }
            }
        }
    }

    private fun setupListeners(){
        btn_send.setOnClickListener {
            val text = DateUtils.getDateDiff(postResponse.author.lastActivity)
            val bundle = Bundle()
            bundle.putString(Key.MESSAGE_USERNAME.name, postResponse.author.username)
            bundle.putString(Key.MESSAGE_PROFILE_URL.name, postResponse.author.profilePhotoUrl)
            bundle.putString(Key.MESSAGE_DATE.name, text)
            val intent = Intent(this, ConversationActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        star1.setOnClickListener {
            presenter.rate(1, postResponse.id)
        }
        star2.setOnClickListener {
            presenter.rate(2, postResponse.id)
        }
        star3.setOnClickListener {
            presenter.rate(3, postResponse.id)
        }
        star4.setOnClickListener {
            presenter.rate(4, postResponse.id)
        }
        star5.setOnClickListener {
            presenter.rate(5, postResponse.id)
        }
        textInputEditText.addTextChangedListener(BasicTextWatcher(publish_btn))
        publish_btn.setOnClickListener {
            if(postResponse.contentType == ContentTypeResponse.PHOTO){
                presenter.addComment(textInputEditText.text.toString(), postResponse.id, 0)
            } else {
                presenter.addComment(
                    textInputEditText.text.toString(),
                    postResponse.id,
                    player?.currentPosition ?: 0
                    )
            }
        }
        btn_comment.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(Key.POST_ID.name, postResponse.id)
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtras(bundle)
            this.startActivity(intent)
        }
        btn_author_avatar.setOnClickListener {
            if(postResponse.author.username == prefs.loadUser().displayName){
                val bundle = Bundle()
                bundle.putString(Key.USERNAME.name, prefs.loadUser().displayName)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            } else {
                val bundle = Bundle()
                bundle.putString(Key.USERNAME.name, postResponse.author.username)
                val intent = Intent(this, ForeignProfileActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    fun hideKeyboard(){
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null){
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }

}