package app.leno.ui.fragments

import android.os.Bundle
import android.view.View
import app.leno.R
import app.leno.ui.bases.BaseFragmentProfile

class Profile : BaseFragmentProfile() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logOut()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_profile
    }
}

