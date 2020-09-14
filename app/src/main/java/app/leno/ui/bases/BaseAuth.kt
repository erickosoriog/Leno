package app.leno.ui.bases

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.leno.R
import app.leno.ui.activity.Login
import app.leno.ui.activity.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.CoroutineContext

abstract class BaseAuth : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var db: FirebaseFirestore
    private var firebaseUserID: String = ""

    companion object {
        const val RC_SIGN_IN = 17
    }

    suspend fun registerUser() {

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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

        if (email.isEmpty() && password.isEmpty()) {

            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_LONG).show()

        }

        if (username.isEmpty() && email.isEmpty() && password.isEmpty()) {

            Toast.makeText(this, "Fields can´t be empty", Toast.LENGTH_LONG).show()

        }

        if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {

            try {

                auth.createUserWithEmailAndPassword(email, password)
                    .await()

                createdUserDB()
                sendEMailVerification()

            } catch (e: Exception) {

                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun createdUserDB() {

        val username: String = username.editText?.text.toString()
        val email: String = email_register.editText?.text.toString()

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
    }

    private suspend fun sendEMailVerification() {

        auth = FirebaseAuth.getInstance()

        try {

            val user: FirebaseUser? = auth.currentUser
            user?.sendEmailVerification()
                ?.await()

            Toast.makeText(this, "Email verify send", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()

        } catch (e: Exception) {

            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun loginUser() {

        auth = FirebaseAuth.getInstance()

        val email: String = email_login.editText?.text.toString()
        val password: String = password_login.editText?.text.toString()

        if (email.isEmpty()) {

            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show()

        }

        if (password.isEmpty()) {

            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show()

        }

        if (email.isEmpty() && password.isEmpty()) {

            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_LONG).show()

        }

        if (email.isNotEmpty() && password.isNotEmpty()) {

            try {

                auth.signInWithEmailAndPassword(email, password)
                    .await()

                val user: FirebaseUser? = auth.currentUser
                updateUiLogin(user)

            } catch (e: Exception) {

                updateUiLogin(null)
            }
        }
        return
    }

    private fun updateUiLogin(currentUser: FirebaseUser?) {
        if (currentUser != null) {

            if (currentUser.isEmailVerified) {

                Log.d("loginWithEmail", "signInWithEmail:success")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Welcome ${currentUser.email}", Toast.LENGTH_SHORT).show()


            } else {

                Log.d("loginWithEmail", "signInWithEmail:failed")
                Toast.makeText(this, "Please verify your email address", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun googleLogin() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    suspend fun firebaseAuthWithGoogle(idToken: String) {

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        try {
            auth.signInWithCredential(credential)
                .await()

            Log.d("loginWithGoogle", "signInWithEmail:success")
            val user: FirebaseUser? = auth.currentUser
            updateUiGoogle(user)


        } catch (e: Exception) {

            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            updateUiGoogle(currentUser = null)
        }

    }

    private fun updateUiGoogle(currentUser: FirebaseUser?) {
        if (currentUser != null) {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

            Toast.makeText(this, "Welcome ${currentUser.email}", Toast.LENGTH_SHORT).show()

        } else {

            Toast.makeText(this, "Please verify your email address", Toast.LENGTH_LONG).show()
        }
    }

}