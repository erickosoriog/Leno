package app.leno.bases

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.leno.R

abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return getLayout(container)
    }

    abstract fun getLayout(container: ViewGroup?): View

    fun toast(
        context: Context? = activity?.applicationContext,
        message: String?,
        duration: Int = Toast.LENGTH_LONG
    ) {
        Toast.makeText(context, message, duration).show()
    }

    fun base() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity?.window?.statusBarColor = resources.getColor(R.color.white, activity?.theme)
            activity?.window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    fun profile() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            activity?.window?.statusBarColor =
                resources.getColor(R.color.colorPrimary, activity?.theme)

            activity?.window?.decorView?.systemUiVisibility =
                0 or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }


}