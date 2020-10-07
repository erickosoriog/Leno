package app.leno.presentation.auth.forgotPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import app.leno.R
import app.leno.bases.BaseActivity
import app.leno.databinding.ActivityForgotPasswordBinding
import app.leno.domain.auth.Forgotpasswordinteractor.ForgotPasswordImpl
import app.leno.presentation.auth.login.Login

class ForgotPassword : BaseActivity(), ForgotPasswordContract.ForgotView, View.OnClickListener {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var presenter: ForgotPasswordPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ForgotPasswordPresenter(ForgotPasswordImpl())
    }

    override fun getLayout(): View {
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onClick(item: View?) {
        when (item?.id) {
            R.id.resetPassword -> forgotPassword()
        }
    }

    override fun showError(errorMsg: String?) {
        toast(this, errorMsg)
    }

    override fun navigateToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        toast(this, "retrieve password e-mail sent")
        finish()
    }

    override fun forgotPassword() {
        val email: String = binding.emailResetPassword.editText?.text.toString().trim()
        if (email.isNotEmpty())
            presenter.sendPasswordResetEmail(email)
        else
            toast(this, "Please enter Email")
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

        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }


}