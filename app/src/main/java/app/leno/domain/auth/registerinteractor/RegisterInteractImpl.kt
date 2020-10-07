package app.leno.domain.auth.registerinteractor

import app.leno.presentation.auth.register.FirebaseExceptionsRegister
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RegisterInteractImpl : RegisterInteract {

    override suspend fun createUserWithEmailAndPassword(
        username: String,
        email: String,
        password: String
    ): Unit = suspendCancellableCoroutine { continuation ->
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    user?.sendEmailVerification()
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(FirebaseExceptionsRegister(it.exception?.message))
                }
            }
    }

}
