package pl.edu.wat.wcy.blackduck.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.top_menu_search.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.data.responses.UserShortResponse
import javax.inject.Inject

class SearchFragment : Fragment(), SearchContract.View {

    @Inject
    lateinit var presenter: SearchContract.Presenter

    private var mode = Mode.USER

    private val posts = ArrayList<PostResponse>()

    private val users = ArrayList<UserShortResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        BlackduckApplication.appComponent.inject(this)
        presenter.attachView(this)
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        view.recycler_view_search.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view_search.adapter = SearchUserAdapter(users)
        setupListeners()
    }

    override fun onPostsSearched(res: List<PostResponse>) {
        posts.clear()
        posts.addAll(res)
        recycler_view_search?.adapter?.notifyDataSetChanged()
    }

    override fun onUsersSearched(res: List<UserShortResponse>) {
        users.clear()
        users.addAll(res)
        recycler_view_search?.adapter?.notifyDataSetChanged()
    }

    override fun onSearchError(msg: String) {
        showToast(msg, context!!)
    }

    private fun setupListeners(){
        view?.button6?.setOnClickListener {
            when (mode){
                Mode.USER -> {
                    mode = Mode.POST
                    view?.button6?.text = "Posty"
                    recycler_view_search.adapter = SearchPostAdapter(posts)
                }
                Mode.POST -> {
                    mode = Mode.USER
                    view?.button6?.text = "Osoby"
                    recycler_view_search.adapter = SearchUserAdapter(users)
                }
            }
            reloadSearch()
        }
        view?.tv_top_menu_search?.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                reloadSearch()
            }

        })
    }

    private fun reloadSearch(){
        if (view?.tv_top_menu_search?.text.toString().replace(" ", "") == ""){
            posts.clear()
            users.clear()
            view?.recycler_view_search?.adapter?.notifyDataSetChanged()
        } else {
            when (mode){
                Mode.USER -> {
                    presenter.searchInUsers(view?.tv_top_menu_search?.text.toString())
                }
                Mode.POST -> {
                    presenter.searchInPosts(view?.tv_top_menu_search?.text.toString())
                }
            }
        }
    }

    enum class Mode {
        USER,
        POST
    }

}