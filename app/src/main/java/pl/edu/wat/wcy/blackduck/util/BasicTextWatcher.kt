package pl.edu.wat.wcy.blackduck.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View

class BasicTextWatcher(val view: View) : TextWatcher {

    override fun afterTextChanged(s: Editable?){}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        if(s != null){
            view.isEnabled = s.isNotEmpty()
        }

    }
}