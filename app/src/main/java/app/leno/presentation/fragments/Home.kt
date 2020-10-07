package app.leno.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.leno.R
import app.leno.adapter.MainAdapter
import app.leno.bases.BaseFragment
import app.leno.data.model.ModelData
import app.leno.data.network.repo.RepoImpl
import app.leno.data.vo.Resource
import app.leno.databinding.FragmentHomeBinding
import app.leno.domain.repo.UseCaseImpl
import app.leno.presentation.note.Note
import app.leno.presentation.viewmodel.MainViewModel
import app.leno.presentation.viewmodel.MainViewModelFactory


class Home : BaseFragment(), MainAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MainAdapter
    private lateinit var navController: NavController

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(UseCaseImpl(RepoImpl()))
        ).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        base()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayout(container: ViewGroup?): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)

        adapter = MainAdapter(requireContext(), this)
        val recyclerView = binding.rv

        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(itemDecorator)
        recyclerView.itemAnimator
        recyclerView.adapter = adapter
        observerData()


    }

    private fun observerData() {
        viewModel.fetchUserData.observe(viewLifecycleOwner, { result ->
            when (result) {

                is Resource.Loading -> {

                    binding.progressCircular.visibility = View.VISIBLE
                }

                is Resource.Success -> {

                    adapter.submitList(result.data)

                    if (adapter.currentList.isNotEmpty() && result.data.size > 0) {
                        binding.boxTextEmpty.visibility = View.GONE
                        binding.rv.visibility = View.VISIBLE
                    } else {
                        binding.rv.visibility = View.GONE
                        binding.boxTextEmpty.visibility = View.VISIBLE
                        binding.progressCircular.visibility = View.GONE
                    }
                }

                is Resource.Failure -> {
                    toast(activity, "An error has occurred:${result.exception}")
                }
            }

        })

    }

    override fun onClickNote(item: ModelData) {
        val intent = Intent(activity, Note::class.java)
        intent.putExtra(Note.DATA_TEXT, item)
        startActivity(intent)
        activity?.finish()
    }

    override fun onclickFolder(item: ModelData) {
        navController.navigate(R.id.Folder)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}