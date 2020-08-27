package app.leno.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import app.leno.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

private lateinit var auth: FirebaseAuth
private lateinit var db: FirebaseFirestore
private lateinit var googleSignInClient: GoogleSignInClient
private lateinit var google_login: Button
private var firebaseUserID: String = ""
private lateinit var register: Button

class Register : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        register = findViewById(R.id.register_user)
        register.setOnClickListener {
            registerUser()
        }

        // Google login configuration

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()

        google_login = findViewById(R.id.google_sign_btn)
        google_login.setOnClickListener {
            googleLogin()
        }

    }

    private fun registerUser() {

        // Fields

        val username: String = username.editText?.text.toString()
        val email: String = email_register.editText?.text.toString()
        val password: String = password_register.editText?.text.toString()

        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_LONG).show()
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show()
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show()

        }

        if (username.isEmpty() && email.isEmpty() && password.isEmpty()) {

            Toast.makeText(this, "Fields can´t be empty", Toast.LENGTH_LONG).show()

        }

        if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        firebaseUserID = auth.currentUser!!.uid
                        val documentUserMap = HashMap<String, Any>()
                        documentUserMap["created"] = Timestamp.now()

                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"] = firebaseUserID
                        userHashMap["username"] = username
                        userHashMap["email"] = email
                        userHashMap["registered"] = Timestamp.now()

                        db.collection("Users").document(firebaseUserID).set(userHashMap)
                        db.collection("UsersNotes").document(firebaseUserID).set(documentUserMap)
                        sendEMailVerification()

                    } else {

                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()

                    }

                }
        }
    }

    private fun sendEMailVerification() {

        val user: FirebaseUser? = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email verify send", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
            }
    }

    private fun googleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, Login.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Login.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("GoogleLogin", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)

                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("GoogleLogin", "Google sign in failed", e)

                }
            } else {
                Log.w("GoogleLogin", task.exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("GoogleLogin", "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    updateUI(currentUser = null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        } else {
            Toast.makeText(this, "Please verify your email address", Toast.LENGTH_LONG).show()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }

}