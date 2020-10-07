package app.leno.domain.welcome

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface WelcomeInteract {

    suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount)
}