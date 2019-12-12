package pl.edu.wat.wcy.blackduck.ui.home

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.top_menu_main.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.responses.PostResponse
import pl.edu.wat.wcy.blackduck.ui.message.MessageActivity
import javax.inject.Inject


class HomeFragment : Fragment(), HomeContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var presenter: HomeContract.Presenter

    @Inject
    lateinit var prefs: SharedPreferences

    private var recyclerViewContent = ArrayList<PostResponse>()

    private var isDownloadingItems = false

    override fun onPostsAvailable(posts: List<PostResponse>) {
        recyclerViewContent.addAll(posts)
        view?.recycler_view_main?.adapter?.notifyDataSetChanged()
        view?.swipe_container?.isRefreshing = false
    }

    override fun onPostsAdded(downloadedPosts: List<PostResponse>) {
        recyclerViewContent.addAll(downloadedPosts)
        view?.recycler_view_main?.adapter?.notifyDataSetChanged()
        isDownloadingItems = false
    }

    override fun onNoPostsAvailable() {
        view?.swipe_container?.isRefreshing = false
        showLongToast("Brak postów do wyświetlenia", context!!)
    }

    override fun onError(msg: String) {
        showToast(msg, context!!)
        view?.swipe_container!!.isRefreshing = false
    }

    override fun onRefresh() {
        recyclerViewContent.clear()
        isDownloadingItems = false
        presenter.refreshPosts()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        BlackduckApplication.appComponent.inject(this)
        presenter.attachView(this)
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        view.recycler_view_main.layoutManager = LinearLayoutManager(activity)
        view.swipe_container.setOnRefreshListener(this)
        view.recycler_view_main.post {
            view.swipe_container.isRefreshing = true
            onRefresh()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recycler_view_main?.adapter = HomeAdapter(recyclerViewContent, context)
        setupButtons()
        presenter.onViewCreated()
        view.recycler_view_main.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.e("COUNT", "${recyclerViewContent.size}")
                val lastVisible = (view.recycler_view_main.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if(lastVisible == recyclerViewContent.size - 1 && !isDownloadingItems && recyclerViewContent.size > 5){
                    Log.e("DOWNLOAD", "downloading")
                    presenter.onBottomAchieved()
                    isDownloadingItems = true
                }
            }
        })
    }

    private fun setupButtons() {
        btn_messages.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    MessageActivity::class.java
                )
            )
        }
        camera_btn.setOnClickListener {
            if (checkSelfPermission(
                    context!!,
                    Manifest.permission.CAMERA
                ) != PermissionChecker.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
            } else {
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 102)
            }
        }
    }
}