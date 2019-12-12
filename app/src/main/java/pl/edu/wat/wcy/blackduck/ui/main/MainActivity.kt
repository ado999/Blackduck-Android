package pl.edu.wat.wcy.blackduck.ui.main

import android.app.Activity
import android.app.Notification
import android.os.Bundle
import android.util.Log
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.preference.Key
import pl.edu.wat.wcy.blackduck.data.preference.PrefsManager
import pl.edu.wat.wcy.blackduck.ui.add.AddFragment
import pl.edu.wat.wcy.blackduck.ui.home.HomeFragment
import pl.edu.wat.wcy.blackduck.ui.login.LoginFragment
import pl.edu.wat.wcy.blackduck.ui.profile.ProfileFragment
import pl.edu.wat.wcy.blackduck.ui.register.RegisterFragment
import pl.edu.wat.wcy.blackduck.ui.search.SearchFragment
import pl.edu.wat.wcy.blackduck.ui.settings.SettingsFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject
    lateinit var prefs: PrefsManager

    private var sectionsStatePageAdapter: SectionsStatePageAdapter? = null

    private var viewPager: ViewPager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BlackduckApplication.appComponent.inject(this)
        setContentView(R.layout.activity_main)
        sectionsStatePageAdapter =
            SectionsStatePageAdapter(supportFragmentManager)
        viewPager = view_pager
        if(isUserLoggedId())
            enterLoggedInMode()
        else
            enterLoginMode()
        if(intent.hasExtra(Key.USERNAME.name)){
            setViewPager(SectionsStatePageAdapter.FragmentName.PROFILE, false)
        }
    }

    private fun isUserLoggedId() = prefs.loadString(Key.TOKEN) != ""

    fun enterLoginMode() {
        bottom_menu.visibility = View.GONE
        val adapter = SectionsStatePageAdapter(supportFragmentManager)
        adapter.addItem(SectionsStatePageAdapter.FragmentName.LOGIN, LoginFragment())
        adapter.addItem(SectionsStatePageAdapter.FragmentName.REGISTER, RegisterFragment())
        viewPager?.adapter = adapter
    }

    fun enterLoggedInMode() {
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/${prefs.loadUser().uuid}")
        Log.e("TAG", prefs.loadUser().uuid)
        bottom_menu.visibility = View.VISIBLE
        val adapter = SectionsStatePageAdapter(supportFragmentManager)
        adapter.addItem(SectionsStatePageAdapter.FragmentName.HOME, HomeFragment())
        adapter.addItem(SectionsStatePageAdapter.FragmentName.SEARCH, SearchFragment())
        adapter.addItem(SectionsStatePageAdapter.FragmentName.ADD, AddFragment())
        adapter.addItem(SectionsStatePageAdapter.FragmentName.PROFILE, ProfileFragment())
        adapter.addItem(SectionsStatePageAdapter.FragmentName.SETTINGS, SettingsFragment())
        viewPager?.adapter = adapter
        setupButtons()
    }

    fun setViewPager(fragmentName: SectionsStatePageAdapter.FragmentName, smoothScroll: Boolean) {
        viewPager?.setCurrentItem((viewPager?.adapter as SectionsStatePageAdapter).getItemIndex(fragmentName), smoothScroll)
    }

    fun hideKeyboard(){
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null){
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupButtons() {
        bottom_menu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.btn_menu_home -> setViewPager(SectionsStatePageAdapter.FragmentName.HOME, true)
                R.id.btn_menu_search -> setViewPager(SectionsStatePageAdapter.FragmentName.SEARCH, true)
                R.id.btn_menu_add -> setViewPager(SectionsStatePageAdapter.FragmentName.ADD, true)
                R.id.btn_menu_person -> setViewPager(SectionsStatePageAdapter.FragmentName.PROFILE, true)
            }
            true
        }
    }

}
