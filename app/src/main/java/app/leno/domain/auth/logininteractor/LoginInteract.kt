package app.leno.domain.auth.logininteractor

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface LoginInteract {

    suspend fun signInWithEmailAndPassword(email: String, password: String)
    suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount)
}