package app.leno.presentation.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import app.leno.R
import app.leno.bases.BaseActivity
import app.leno.databinding.ActivityRegisterBinding
import app.leno.domain.auth.registerinteractor.RegisterInteractImpl
import app.leno.presentation.auth.login.Login

class Register : BaseActivity(), RegisterContract.RegisterView, View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var presenter: RegisterPresenter
    private lateinit var progressBar: ViewStub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = RegisterPresenter(RegisterInteractImpl())
        presenter.attachView(this)
        progressBar = findViewById(R.id.progress_register)
    }

    override fun getLayout(): View {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onClick(item: View?) {
        when (item?.id) {
            R.id.register_user -> signUp()
            R.id.googleButton -> {
            }
        }
    }

    override fun showError(errorMsg: String?) {
        toast(this, errorMsg)
    }

    override fun showProgressBar() {
        binding.progressRegister.visibility = View.VISIBLE
        binding.registerUser.visibility = View.GONE
    }

    override fun hideProgressBar() {
        binding.progressRegister.visibility = View.GONE
        binding.registerUser.visibility = View.VISIBLE
    }

    override fun navigationToSignIn() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        toast(this, "verification email sent")
        finish()
    }

    override fun signUp() {

        val username: String = binding.username.editText?.text.toString().trim()
        val email: String = binding.emailRegister.editText?.text.toString().trim()
        val password: String = binding.passwordRegister.editText?.text.toString().trim()
        if (presenter.checkFields(username, email, password))
            toast(this, "Fields can´t be empty")
        else
            presenter.createUserWithEmailAndPassword(username, email, password)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onDetachView()
        presenter.onDetachJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetachView()
        presenter.onDetachJob()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )

    }

}