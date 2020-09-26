package app.leno.ui.fragments

import android.os.Bundle
import android.view.View
import app.leno.R
import app.leno.ui.bases.BaseFragment
import kotlinx.android.synthetic.main.fragment_favorites.*

class Favorite : BaseFragment() {
    override fun getLayout(): Int {
        return R.layout.fragment_favorites
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        box_text_empty.visibility = View.VISIBLE
    }


}