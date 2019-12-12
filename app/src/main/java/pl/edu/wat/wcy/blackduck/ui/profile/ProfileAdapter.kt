package pl.edu.wat.wcy.blackduck.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_grid_photo.view.*
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.responses.ContentTypeResponse
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.ui.post.PostActivity

class ProfileAdapter(private val posts:List<PostResponse>): RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>() {

    var context: Context? = null

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindContent(posts[position], context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        this.context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_grid_photo, parent, false)
        return CustomViewHolder(cellForRow)
    }
    override fun getItemCount(): Int {
        return posts.size
    }

    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bindContent(
            post: PostResponse,
            context: Context?
        ){

            if(post.contentType == ContentTypeResponse.PHOTO){
                Picasso.get()
                    .load(Configuration.apiFile + post.thumbnail)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .into(view.iv_1)
            } else {
                Picasso.get()
                    .load(Configuration.apiFile + post.thumbnail)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .into(view.iv_1)
                view.imageView6.visibility = View.VISIBLE
            }
            view.iv_1.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(Key.POST_ID.name, post.id)
                val intent = Intent(context, PostActivity::class.java)
                intent.putExtras(bundle)
                context?.startActivity(intent)
            }
        }

    }

}