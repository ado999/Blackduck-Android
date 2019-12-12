package pl.edu.wat.wcy.blackduck.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_row_post.view.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.responses.ContentTypeResponse
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.ui.post.PostActivity

class SearchPostAdapter(private val items: ArrayList<PostResponse>) :
    RecyclerView.Adapter<SearchPostAdapter.CustomViewHolder>() {

    var context: Context? = null

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindContent(items[position], context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        BlackduckApplication.appComponent.inject(this)
        this.context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.search_row_post, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CustomViewHolder(
        val view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bindContent(post: PostResponse, context: Context?) {
            view.textView13.text = post.author.username
            view.textView17.text = post.title
            view.textView18.text = post.description
            when (post.contentType){
                ContentTypeResponse.PHOTO -> {
                    Picasso.get()
                        .load(Configuration.apiFile + post.thumbnail)
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation)
                        .into(view.imageView8)
                }
                ContentTypeResponse.VIDEO -> {
                    Picasso.get()
                        .load(Configuration.apiFile + post.thumbnail)
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation)
                        .into(view.imageView8)
                }
            }
            view.view_post_row.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(Key.POST_ID.name, post.id)
                val intent = Intent(context, PostActivity::class.java)
                intent.putExtras(bundle)
                context?.startActivity(intent)
            }
        }


    }

}
