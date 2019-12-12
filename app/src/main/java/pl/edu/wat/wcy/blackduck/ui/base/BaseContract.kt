package pl.edu.wat.wcy.blackduck.ui.base

import android.content.Context
import android.widget.Toast
import pl.edu.wat.wcy.blackduck.data.responses.ConversationResponse

interface BaseContract {



    interface View {
        fun showToast(str: String, context: Context){
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }

        fun showLongToast(str: String, context: Context){
            Toast.makeText(context, str, Toast.LENGTH_LONG).show()
        }
    }

    interface Presenter<in T> {

        fun attachView(view: T)
        fun onViewCreated()
        fun onViewDestroyed()
    }

}