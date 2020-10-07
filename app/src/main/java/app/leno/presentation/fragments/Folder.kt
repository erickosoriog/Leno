package app.leno.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import app.leno.R
import app.leno.bases.BaseFragment
import app.leno.databinding.FragmentFolderBinding
import com.ismaeldivita.chipnavigation.ChipNavigationBar


class Folder : BaseFragment() {

    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!
    private lateinit var navView: ChipNavigationBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar!!.title = "Hello"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        navView = container?.rootView?.findViewById(R.id.navView)!!
        navView.visibility = View.GONE

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayout(container: ViewGroup?): View {
        _binding = FragmentFolderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.boxTextEmpty.visibility = View.VISIBLE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        navView.visibility = View.VISIBLE
    }
}