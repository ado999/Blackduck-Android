package pl.edu.wat.wcy.blackduck.ui.comment

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_comment.view.*
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.responses.CommentResponse
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.util.DateUtils

class CommentAdapter(private val post: PostResponse?, context: Context) :
    RecyclerView.Adapter<CommentAdapter.CustomViewHolder>() {

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindContent(post?.comments?.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_row_comment, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount() = post?.comments?.size ?: 0


    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bindContent(comment: CommentResponse?) {
            view.commUsernameTV.text = comment?.author?.username
            view.commContentTV.text = comment?.content
            view.commTimeTV.text = DateUtils.getDateDiff(comment?.creationDate!!)
            Picasso.get()
                .load(Configuration.apiFile + comment.author.profilePhotoUrl)
                .into(view.btn_author_avatar3)
        }
    }

}
