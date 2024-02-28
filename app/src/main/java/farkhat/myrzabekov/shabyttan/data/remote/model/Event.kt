package farkhat.myrzabekov.shabyttan.data.remote.model

data class Event(
    val id: String,
    val title: String,
    val location: String,
    val imageUrl: String,
    val description: String,
    val isFree: Boolean
)
