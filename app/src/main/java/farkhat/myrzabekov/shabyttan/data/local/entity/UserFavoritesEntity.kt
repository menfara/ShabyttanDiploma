package farkhat.myrzabekov.shabyttan.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_favorites",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ArtworkEntity::class,
            parentColumns = ["id"],
            childColumns = ["artworkId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserFavoritesEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val artworkId: Long
)
