package app.leno.model

data class ModelData(

    val imageUrl: String? = null,
    val title: String = "",
    val date: String = "",
    val created: com.google.firebase.Timestamp? = null,
    val type: Long? = 0
)