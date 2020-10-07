package app.leno.domain.auth.Forgotpasswordinteractor

import app.leno.presentation.auth.forgotPassword.FirebaseExceptionsPassword
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ForgotPasswordImpl : ForgotPasswordInteract {

    override suspend fun sendPasswordResetEmail(email: String): Unit =
        suspendCancellableCoroutine { continuation ->
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(FirebaseExceptionsPassword(it.exception?.message))
                }
            }
        }
}