package app.leno.domain.main

interface MainInteract {

    suspend fun createFolderInDB(title: String)
}