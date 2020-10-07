package app.leno.presentation.fragments.profile

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import app.leno.R
import app.leno.bases.BaseFragment
import app.leno.databinding.FragmentProfileBinding
import app.leno.presentation.welcome.Welcome

class Profile : BaseFragment(), ProfileContract.ProfileView {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ProfilePresenter()
        presenter.attachView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profile()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayout(container: ViewGroup?): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.LogOut.setOnClickListener {
            showDialog()
        }
    }

    override fun showDialog() {

        val alertDialogBuilder = AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme)
        alertDialogBuilder.setTitle("Log out")
        alertDialogBuilder.setMessage("Are you sure you want to leave?")

        alertDialogBuilder.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.cancel()
        }
        alertDialogBuilder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            presenter.signOut()
        }

        alertDialogBuilder.show()
        return
    }

    override fun navigateToWelcome() {
        val intent = Intent(activity, Welcome::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

