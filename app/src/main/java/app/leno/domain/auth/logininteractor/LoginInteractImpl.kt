package app.leno.domain.auth.logininteractor

import android.content.ContentValues.TAG
import android.util.Log
import app.leno.data.network.user.UserRepoInteractImpl
import app.leno.presentation.auth.login.FirebaseExceptionsLogin
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LoginInteractImpl : LoginInteract {

    private val userRepoInteract = UserRepoInteractImpl()

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Unit =

        suspendCancellableCoroutine { continuation ->
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val displayName = it.result?.user?.displayName!!
                        userRepoInteract.createUserDB(displayName, email)
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(FirebaseExceptionsLogin(it.exception?.message))
                    }
                }
        }

    override suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount): Unit =
        suspendCancellableCoroutine { continuation ->
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    userRepoInteract.createUserDB(account.displayName!!, account.email!!)
                    continuation.resume(Unit)
                    Log.d(TAG, "signInWithGoogle: success")
                } else {
                    continuation.resumeWithException(FirebaseExceptionsLogin(it.exception?.message))
                    Log.w(TAG, "signInWithGoogle: failure", it.exception)

                }
            }
        }
}


