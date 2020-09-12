package app.leno.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.leno.R
import app.leno.adapter.MainAdapter
import app.leno.data.Resource
import app.leno.domain.UseCaseImpl
import app.leno.model.ModelData
import app.leno.repo.RepoImpl
import app.leno.ui.activitys.NoteLayout
import app.leno.ui.bases.BaseFragment
import app.leno.viewmodel.DataVMFactory
import app.leno.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*


class Home : BaseFragment(), MainAdapter.OnItemClickListener {

    private lateinit var adapter: MainAdapter

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            DataVMFactory(UseCaseImpl(RepoImpl()))
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        baseFragment()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MainAdapter(this)
        rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv.adapter = adapter
        observerData()

    }

    private fun observerData() {
        viewModel.fetchUserData.observe(viewLifecycleOwner, { result ->
            when (result) {

                is Resource.Loading -> {

                    progress_circular.visibility = View.VISIBLE
                }

                is Resource.Success -> {

                    adapter.submitList(result.data)

                    if (adapter.currentList.isNotEmpty() && result.data.size > 0) {
                        box_text_empty.visibility = View.GONE
                        rv.visibility = View.VISIBLE
                    } else {
                        rv.visibility = View.GONE
                        box_text_empty.visibility = View.VISIBLE
                        progress_circular.visibility = View.GONE
                    }
                }

                is Resource.Failure -> {
                    Toast.makeText(
                        activity,
                        "An error has occurred:${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })

    }

    override fun onClick(adapterPosition: ModelData) {
        val intent = Intent(activity, NoteLayout::class.java)
        intent.putExtra(NoteLayout.DATA_TEXT, adapterPosition)
        startActivity(intent)
    }
}