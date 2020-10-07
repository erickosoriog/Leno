package app.leno.domain.auth.Forgotpasswordinteractor

interface ForgotPasswordInteract {

    suspend fun sendPasswordResetEmail(email: String)
}