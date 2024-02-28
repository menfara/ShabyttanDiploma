package farkhat.myrzabekov.shabyttan.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import farkhat.myrzabekov.shabyttan.data.local.ArtworkDao
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.domain.repository.FirestoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepositoryImpl @Inject constructor(
    private val artworkDao: ArtworkDao
) : FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val artworkCollection = db.collection("artworks")

    override fun addArtwork(artwork: ArtworkEntity) {
        artworkCollection.add(artwork)
            .addOnSuccessListener { documentReference ->
                Log.d("FirestoreRepository", "Artwork added with ID: ${documentReference.id}")
                CoroutineScope(Dispatchers.IO).launch {
                    artwork.viewDate = "2024/02/25"
                    saveArtworkToLocalDatabase(artwork)
                }
            }
            .addOnFailureListener { e ->
                Log.w("FirestoreRepository", "Error adding artwork", e)
            }
    }


    override suspend fun getArtworkById(artworkId: String): ArtworkEntity? {
        return withContext(Dispatchers.IO) {
            artworkDao.getArtworkByFirestoreId(artworkId)
                ?: try {
                    val document = artworkCollection.document(artworkId).get().await()
                    val artwork = document.toObject<ArtworkEntity>()
                    if (artwork != null) {
                        artwork.firestoreId = document.id
                    }
                    if (artwork != null) {
                        saveArtworkToLocalDatabase(artwork)
                    }
                    artwork
                } catch (e: Exception) {
                    null
                }
        }
    }


    private suspend fun saveArtworkToLocalDatabase(artwork: ArtworkEntity) {
        withContext(Dispatchers.IO) {
            artworkDao.insertArtwork(artwork)
        }
    }
}
