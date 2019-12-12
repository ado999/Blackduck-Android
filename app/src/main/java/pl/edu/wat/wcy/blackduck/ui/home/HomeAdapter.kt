package pl.edu.wat.wcy.blackduck.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comment.view.*
import kotlinx.android.synthetic.main.recycler_row_home.view.*
import kotlinx.android.synthetic.main.recycler_row_home.view.publish_btn
import kotlinx.android.synthetic.main.recycler_row_messages.view.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.network.Constants
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.request.CommentRequest
import pl.edu.wat.wcy.blackduck.data.request.RateRequest
import pl.edu.wat.wcy.blackduck.data.responses.ContentTypeResponse
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.data.responses.RateResponse
import pl.edu.wat.wcy.blackduck.ui.comment.CommentActivity
import pl.edu.wat.wcy.blackduck.ui.conversation.ConversationActivity
import pl.edu.wat.wcy.blackduck.ui.foreignProfile.ForeignProfileActivity
import pl.edu.wat.wcy.blackduck.ui.main.MainActivity
import pl.edu.wat.wcy.blackduck.ui.main.SectionsStatePageAdapter
import pl.edu.wat.wcy.blackduck.ui.post.PostActivity
import pl.edu.wat.wcy.blackduck.util.BasicTextWatcher
import pl.edu.wat.wcy.blackduck.util.DateUtils
import pl.edu.wat.wcy.blackduck.util.StarBinder
import javax.inject.Inject

class HomeAdapter(private val items: ArrayList<PostResponse>, val context: Context?) :
    RecyclerView.Adapter<HomeAdapter.CustomViewHolder>() {

    @Inject
    lateinit var prefs: PrefsManager

    @Inject
    lateinit var portalApi: PortalApi

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindContent(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        BlackduckApplication.appComponent.inject(this)
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_row_home, parent, false)
        return CustomViewHolder(cellForRow, prefs, portalApi, context, this)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CustomViewHolder(
        val view: View,
        val prefs: PrefsManager,
        val api: PortalApi,
        val context: Context?,
        val adapter: RecyclerView.Adapter<CustomViewHolder>
    ) : RecyclerView.ViewHolder(view) {

        fun bindContent(post: PostResponse) {

            view.postIdTV.text = post.id.toString()
            view.tv_author_nickname.text = post.author.username
            view.tv_comment_count.text = post.comments.size.toString()
            view.tv_likes.text = "%.2f".format(post.rate)
            Picasso.get()
                .load(Configuration.apiFile + post.author.profileThumbnail)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(view.btn_author_avatar)
            Picasso.get()
                .load(Configuration.apiFile + prefs.loadUser().profilePhotoUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .into(view.userProfilePicture)
            if(post.contentType == ContentTypeResponse.PHOTO){
                Picasso.get()
                    .load(Configuration.apiFile + post.thumbnail)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .into(view.iv_image)
                //gone play
                view.imageView10.visibility = View.GONE
            } else {
                Picasso.get()
                    .load(Configuration.apiFile + post.thumbnail)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .into(view.iv_image)
                //visible play
                view.imageView10.visibility = View.VISIBLE
            }
            StarBinder.bindStars(post.rates, null, prefs.loadUser().displayName, listOf(view.star1, view.star2, view.star3, view.star4, view.star5))
            setupListeners(post)
        }



        private fun setupListeners(post: PostResponse) {
            view.btn_comment.setOnClickListener {
                showComments(view.postIdTV.text.toString().toInt())
            }
            view.star1.setOnClickListener {
                rate(1, view.postIdTV.text.toString().toInt())
            }
            view.star2.setOnClickListener {
                rate(2, view.postIdTV.text.toString().toInt())
            }
            view.star3.setOnClickListener {
                rate(3, view.postIdTV.text.toString().toInt())
            }
            view.star4.setOnClickListener {
                rate(4, view.postIdTV.text.toString().toInt())
            }
            view.star5.setOnClickListener {
                rate(5, view.postIdTV.text.toString().toInt())
            }

            view.textInputEditText.addTextChangedListener(BasicTextWatcher(view.publish_btn))
            view.publish_btn.setOnClickListener {
                api.comment(
                    Constants.APPLICATION_JSON,
                    Constants.APPLICATION_JSON,
                    prefs.loadString(Key.TOKEN),
                    CommentRequest(view.postIdTV.text.toString().toInt(), view.textInputEditText.text.toString(), 0L)
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            post.comments.add(it)
                            view.textInputEditText.text?.clear()
                            view.textInputEditText.clearFocus()
                            (context as MainActivity).hideKeyboard()
                            adapter.notifyDataSetChanged()
                            Toast.makeText(
                                context,
                                "Komentarz został dodany",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        {
                            Toast.makeText(
                                context,
                                "Coś poszło nie po naszej myśli",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
            }
            view.btn_send.setOnClickListener {
                val text = DateUtils.getDateDiff(post.author.lastActivity)
                val bundle = Bundle()
                bundle.putString(Key.MESSAGE_USERNAME.name, post.author.username)
                bundle.putString(Key.MESSAGE_PROFILE_URL.name, post.author.profilePhotoUrl)
                bundle.putString(Key.MESSAGE_DATE.name, text)
                val intent = Intent(context, ConversationActivity::class.java)
                intent.putExtras(bundle)
                context?.startActivity(intent)
            }
            view.userProfilePicture.setOnClickListener {
                (context as MainActivity).setViewPager(SectionsStatePageAdapter.FragmentName.PROFILE, true)
            }
            view.iv_image.setOnClickListener {
                val intent = Intent(context!!, PostActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(Key.POST_ID.name, view.postIdTV.text.toString().toInt())
                intent.putExtras(bundle)
                context.startActivity(intent)
            }
            view.btn_author_avatar.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(Key.USERNAME.name, post.author.username)
                val intent = Intent(context, ForeignProfileActivity::class.java)
                intent.putExtras(bundle)
                context?.startActivity(intent)
            }
        }

        @SuppressLint("CheckResult")
        private fun rate(rate: Int, postId: Int) {
            api.rate(
                Constants.APPLICATION_JSON,
                Constants.APPLICATION_JSON,
                prefs.loadString(Key.TOKEN),
                RateRequest(rate, postId)
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        StarBinder.bindStars(listOf(), rate, prefs.loadUser().displayName, listOf(view.star1, view.star2, view.star3, view.star4, view.star5))
                        view.tv_likes.text = "%.2f".format(it)
                    },
                    {
                        Toast.makeText(
                            context,
                            "Coś poszło nie po naszej myśli",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
        }

        private fun showComments(postId: Int) {
            val bundle = Bundle()
            bundle.putInt(Key.POST_ID.name, postId)
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtras(bundle)
            context?.startActivity(intent)
        }


    }

}


