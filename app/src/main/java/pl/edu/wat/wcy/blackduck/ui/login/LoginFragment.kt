package pl.edu.wat.wcy.blackduck.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_login.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.ui.main.MainActivity
import pl.edu.wat.wcy.blackduck.ui.main.SectionsStatePageAdapter
import javax.inject.Inject

class LoginFragment : Fragment(), LoginContract.View {

    @Inject
    lateinit var presenter: LoginContract.Presenter

    val TAG = "LoginFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (BlackduckApplication).appComponent.inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun onLoginSuccess() {
        progressBar.visibility = View.INVISIBLE
        (activity as MainActivity).enterLoggedInMode()
    }

    override fun onLoginFailied(msg: String?) {
        progressBar.visibility = View.INVISIBLE
        if( msg == null){
            showToast("Coś poszło nie tak", context!!)
        } else {
            showToast(msg, context!!)
        }
    }

    fun hasUsernameError() : Boolean {
        val text = usernameTV.text.toString()
        return if (text.length < 2) {
            usernameerr.visibility = View.VISIBLE
            true
        } else {
            usernameerr.visibility = View.INVISIBLE
            false
        }
    }

    fun hasPasswordError() : Boolean {
        val text = passwordTV.text.toString()
        return if (text.length < 3) {
            fullname_err.visibility = View.VISIBLE
            true
        } else {
            fullname_err.visibility = View.INVISIBLE
            false
        }
    }

    fun checkButtonAvailability() {
        loginBTN.isEnabled =
            !hasUsernameError() &&
                    !hasPasswordError()
    }

    fun setupListeners() {
        loginBTN.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            (activity as MainActivity).hideKeyboard()
            presenter.requestLogin(
                usernameTV.text.toString(),
                passwordTV.text.toString()
            )
        }
        goregisterTV.setOnClickListener {
            (activity as MainActivity).setViewPager(SectionsStatePageAdapter.FragmentName.REGISTER, true)
        }
        usernameTV.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                hasUsernameError()
                checkButtonAvailability()
            }
        })
        passwordTV.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                hasPasswordError()
                checkButtonAvailability()
            }
        })
    }

}