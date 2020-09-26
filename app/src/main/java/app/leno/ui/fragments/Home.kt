package app.leno.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.leno.R
import app.leno.adapter.MainAdapter
import app.leno.data.Resource
import app.leno.data.model.ModelData
import app.leno.data.repo.RepoImpl
import app.leno.domain.UseCaseImpl
import app.leno.ui.activity.NoteLayout
import app.leno.ui.bases.BaseFragment
import app.leno.viewmodel.MainViewModel
import app.leno.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*


class Home : BaseFragment(), MainAdapter.OnItemClickListener {

    private lateinit var adapter: MainAdapter

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(UseCaseImpl(RepoImpl()))
        ).get(MainViewModel::class.java)
    }

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)

        adapter = MainAdapter(this)
        rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rv.addItemDecoration(itemDecorator)
        rv.itemAnimator
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

    override fun onClick(item: ModelData) {
        val intent = Intent(activity, NoteLayout::class.java)
        intent.putExtra(NoteLayout.DATA_TEXT, item)
        startActivity(intent)
        activity?.finish()
    }
}