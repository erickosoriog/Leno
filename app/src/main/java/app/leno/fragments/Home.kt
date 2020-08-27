package app.leno.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.leno.R
import app.leno.Viewmodel.MainViewModel
import app.leno.notes.MainAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_and_fav.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var adapter: MainAdapter


class Home : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MainAdapter()
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = adapter
        observerData()
    }

    private fun observerData() {
        viewModel.fetchUserData().observe(viewLifecycleOwner, Observer {
            adapter.setListData(it)
            if (it.size > 0) {
                box.visibility = View.GONE
                text_home_fragment.visibility = View.GONE
            }
            adapter.notifyDataSetChanged()
        })
    }

}