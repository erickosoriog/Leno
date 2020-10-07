package app.leno.domain.auth.registerinteractor

interface RegisterInteract {

    suspend fun createUserWithEmailAndPassword(username: String, email: String, password: String)
}