package pl.edu.wat.wcy.blackduck.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import pl.edu.wat.wcy.blackduck.BlackduckApplication
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.ui.main.MainActivity
import pl.edu.wat.wcy.blackduck.ui.main.SectionsStatePageAdapter
import javax.inject.Inject

class RegisterFragment : Fragment(), RegisterContract.View {

    @Inject
    lateinit var presenter: RegisterContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BlackduckApplication.appComponent.inject(this)
        presenter.attachView(this)
        setupListeners()
    }

    override fun onRegisterSucces() {
        (activity as MainActivity).setViewPager(SectionsStatePageAdapter.FragmentName.LOGIN, true)
        newusername.setText("")
        newfullname.setText("")
        newemail.setText("")
        newpassword1.setText("")
        newpassword2.setText("")
        showToast("Pomyślnie utworzono konto", context!!)
    }

    override fun onRegisterFailied(message: String?) {
        if (message == null)
            showToast("Coś poszło źle", context!!)
        else
            showToast(message, context!!)
    }

    private fun checkButtonAvailability() {
        registerbtn.isEnabled =
            !hasUsernameError() &&
                    !hasFullnameError() &&
                    !hasEmailError() &&
                    !hasPassword1Error() &&
                    !hasPassword2Error()
    }

    private fun hasUsernameError(): Boolean {
        val text = newusername.text.toString()
        when {
            text.length < 3 -> {
                newusername_err.text = "Nazwa użytkownika jest za krótka"
                newusername_err.visibility = View.VISIBLE
            }
            text.contains(" ") -> {
                newusername_err.text = "Nazwa użytkownika nie może zawierać spacji"
                newusername_err.visibility = View.VISIBLE
            }
            text.length > 60 -> {
                newusername_err.text = "Nazwa użytkownika jest za długa"
                newusername_err.visibility = View.VISIBLE
            }
            else -> {
                newusername_err.visibility = View.INVISIBLE
                return false
            }
        }
        return true
    }

    private fun hasFullnameError(): Boolean {
        val text = newfullname.text.toString()
        return if (!text.matches(Regex("\\w{2,9}\\s\\w{2,12}"))) {
            newfullname_err.visibility = View.VISIBLE
            true
        } else {
            newfullname_err.visibility = View.INVISIBLE
            false
        }
    }

    private fun hasEmailError(): Boolean {
        val text = newemail.text.toString()
        return if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            newemail_err.visibility = View.VISIBLE
            true
        } else {
            newemail_err.visibility = View.INVISIBLE
            false
        }
    }

    private fun hasPassword1Error(): Boolean {
        val text = newpassword1.text.toString()
        return if (text.length < 6) {
            newpassword1_err.visibility = View.VISIBLE
            true
        } else {
            newpassword1_err.visibility = View.INVISIBLE
            false
        }
    }

    private fun hasPassword2Error(): Boolean {
        return if (newpassword1.text.toString() != newpassword2.text.toString()) {
            newpassword2_err.visibility = View.VISIBLE
            true
        } else {
            newpassword2_err.visibility = View.INVISIBLE
            false
        }
    }

    fun setupListeners() {
        newusername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                hasUsernameError()
                checkButtonAvailability()
            }
        })
        newfullname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                hasFullnameError()
                checkButtonAvailability()
            }
        })
        newemail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                hasEmailError()
                checkButtonAvailability()
            }
        })
        newpassword1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                hasPassword1Error()
                checkButtonAvailability()
            }
        })
        newpassword2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                hasPassword2Error()
                checkButtonAvailability()
            }
        })
        registerbtn.setOnClickListener {
            presenter.onUserRegisterClick(
                username = newusername.text.toString(),
                fullName = newfullname.text.toString(),
                email = newemail.text.toString(),
                password = newpassword1.text.toString()
            )
        }

    }


}