package farkhat.myrzabekov.shabyttan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artworks")
class ArtworkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val titleRu: String,

    val creator: String? = null,
    val creatorRu: String? = null,

    val creationDate: String? = null,
    val creationDateRu: String? = null,

    val technique: String,
    val techniqueRu: String,

    val type: String,
    val typeRu: String,

    val description: String,
    val descriptionRu: String,

    val didYouKnow: String,
    val didYouKnowRu: String,

    val imageUrl: String,
    var viewDate: String? = null
)
