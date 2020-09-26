package app.leno.ui.bases

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.leno.R

abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity?.window?.statusBarColor = resources.getColor(R.color.white, activity?.theme)
            activity?.window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        return inflater.inflate(getLayout(), container, false)
    }

    abstract fun getLayout(): Int


}