package pl.edu.wat.wcy.blackduck.ui.add

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.util.Util
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.fragment_add.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.data.request.FolderRequest
import pl.edu.wat.wcy.blackduck.data.request.PostRequest
import pl.edu.wat.wcy.blackduck.data.responses.FolderResponse
import pl.edu.wat.wcy.blackduck.ui.main.MainActivity
import pl.edu.wat.wcy.blackduck.ui.main.SectionsStatePageAdapter
import pl.edu.wat.wcy.blackduck.util.BasicTextWatcher
import java.io.File
import javax.inject.Inject

class AddFragment: Fragment(), AddContract.View {

    @Inject
    lateinit var presenter: AddContract.Presenter

    @Inject
    lateinit var prefs: PrefsManager

    private var folders = ArrayList<FolderResponse>()

    private var selectedFolder: FolderResponse? = null

    private var easyImage: EasyImage? = null

    private var file: File? = null

    private var textWatcher: BasicTextWatcher? = null

    private var player: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        BlackduckApplication.appComponent.inject(this)
        presenter.attachView(this)
        val view: View = inflater.inflate(R.layout.fragment_add, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        easyImage = EasyImage.Builder(context!!)
            .allowMultiple(false)
            .build()
        presenter.fetchFolders()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onViewDestroyed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage?.handleActivityResult(
            requestCode, resultCode, data, activity as MainActivity, object: DefaultCallback(){
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    file = imageFiles[0].file
                    player?.release()
                    when (source){
                        MediaSource.CAMERA_VIDEO -> {
                            playerView2.visibility = View.VISIBLE
                            imageView5.visibility = View.GONE

                            player = ExoPlayerFactory.newSimpleInstance(context)
                            playerView2.player = player
                            val dataSpec = DataSpec(Uri.fromFile(file))
                            val fileDataSource = FileDataSource()
                            fileDataSource.open(dataSpec)
                            val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "BlackduckApplication"))
                            val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                                .createMediaSource(Uri.fromFile(file))
                            Log.e("FILE", file?.absolutePath.toString())
                            playerView2.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                            player?.prepare(videoSource)
                            player?.playWhenReady = true
                            player?.repeatMode = Player.REPEAT_MODE_ALL
                        }
                        else -> {
                            playerView2.visibility = View.GONE
                            imageView5.visibility = View.VISIBLE
                            Picasso.get()
                                .load(file!!)
                                .fit()
                                .centerCrop()
                                .placeholder(R.drawable.progress_animation)
                                .into(imageView5)
                        }
                    }
                    if(selectedFolder != null){
                        button5.isEnabled = true
                    }
                }

            }
        )
    }

    override fun onFoldersAvailable(folders: List<FolderResponse>) {
        this.folders.clear()
        this.folders.addAll(folders)
        val arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, folders.map { it.title }.plus("Dodaj nowy folder..."))
        spinner.adapter = arrayAdapter
        setupListeners()
    }

    override fun onPostSent() {
        progressBar2.visibility = View.GONE
        player?.release()
        (activity as MainActivity).setViewPager(SectionsStatePageAdapter.FragmentName.HOME, true)
        showToast("Post został dodany", context!!)
    }

    override fun onError(msg: String?) {
        showToast(msg!!, context!!)
    }

    private fun setupListeners(){
        button2.setOnClickListener {
            player?.release()
            easyImage?.openChooser(this)
        }
        button3.setOnClickListener {
            if (PermissionChecker.checkSelfPermission(
                    context!!,
                    Manifest.permission.CAMERA
                ) != PermissionChecker.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
            } else {
                easyImage?.openCameraForImage(this)
            }
            player?.release()
        }
        button4.setOnClickListener {
            if (PermissionChecker.checkSelfPermission(
                    context!!,
                    Manifest.permission.CAMERA
                ) != PermissionChecker.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
            } else {
                easyImage?.openCameraForVideo(this)
            }
            player?.release()
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position < folders.size){
                    selectedFolder = folders[position]
                    folderDescription.text?.clear()
                    folderName.text?.clear()
                    folderDescription.visibility = View.GONE
                    folderName.visibility = View.GONE
                    checkBox.visibility = View.GONE
                    folderName.removeTextChangedListener(textWatcher)
                    if(file != null){
                        button5.isEnabled = true
                    }
                } else {
                    folderDescription.visibility = View.VISIBLE
                    folderName.visibility = View.VISIBLE
                    checkBox.visibility = View.VISIBLE
                    button5.isEnabled = false
                    textWatcher = BasicTextWatcher(button5)
                    folderName.addTextChangedListener(textWatcher)
                    selectedFolder = null
                }
            }

        }
        button5.setOnClickListener {
            progressBar2.visibility = View.VISIBLE
            sendPost()
        }
    }

    private fun sendPost(){
        val folderId: Int? = selectedFolder?.id
        val post = PostRequest(textInputEditText8.text.toString(), file!!, null, folderId, textInputEditText7.text.toString())
        presenter.sendPost(post, selectedFolder, folderName.text.toString(), folderDescription.text.toString(), checkBox.isChecked)
    }

    override fun onPause() {
        super.onPause()
        player?.release()
    }

}