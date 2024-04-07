package farkhat.myrzabekov.shabyttan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artworks")
class ArtworkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var firestoreId: String = "",
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
    var viewDate: String? = null,

    var ownerId: String
) {
    constructor() : this(
        id = 0,
        firestoreId = "",
        title = "",
        titleRu = "",
        technique = "",
        techniqueRu = "",
        type = "",
        typeRu = "",
        description = "",
        descriptionRu = "",
        didYouKnow = "",
        didYouKnowRu = "",
        imageUrl = "",
        ownerId = "",
    )
}
