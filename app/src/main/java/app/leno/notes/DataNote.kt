package app.leno.notes

data class DataNote(
    val imageUrl: String = "DEFAULT_URL",
    val title: String = "DEFAULT_TITLE",
    val date: String = "DEFAULT_DATE",
    val type: Long? = 0
)