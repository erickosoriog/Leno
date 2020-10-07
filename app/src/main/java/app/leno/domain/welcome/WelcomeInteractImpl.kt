package app.leno.domain.welcome

import android.content.ContentValues
import android.util.Log
import app.leno.data.network.user.UserRepoInteractImpl
import app.leno.presentation.welcome.FirebaseExceptionsWelcome
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class WelcomeInteractImpl : WelcomeInteract {

    private val userRepoInteract = UserRepoInteractImpl()

    override suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount): Unit =
        suspendCancellableCoroutine { continuation ->
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        userRepoInteract.createUserDB(account.displayName!!, account.email!!)
                        continuation.resume(Unit)
                        Log.d(ContentValues.TAG, "signInWithGoogle: success")
                    } else {
                        continuation.resumeWithException(FirebaseExceptionsWelcome(it.exception?.message))
                        Log.w(ContentValues.TAG, "signInWithGoogle: failure", it.exception)
                    }
                }

        }
}