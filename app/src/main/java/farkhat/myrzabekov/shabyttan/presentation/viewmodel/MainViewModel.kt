package farkhat.myrzabekov.shabyttan.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import farkhat.myrzabekov.shabyttan.data.local.entity.*
import farkhat.myrzabekov.shabyttan.presentation.usecase.artwork.*
import farkhat.myrzabekov.shabyttan.presentation.usecase.firestore.AddArtworkFirestoreUseCase
import farkhat.myrzabekov.shabyttan.presentation.usecase.firestore.GetArtworkByIdFirestoreUseCase
import farkhat.myrzabekov.shabyttan.presentation.usecase.user.*
import farkhat.myrzabekov.shabyttan.presentation.usecase.user.favorites.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserByUsernameUseCase: GetUserByUsernameUseCase,
    private val createArtworkUseCase: CreateArtworkUseCase,
    private val getAllArtworksUseCase: GetAllArtworksUseCase,
    private val getArtworkByIdUseCase: GetArtworkByIdUseCase,
    private val getArtworkByViewDateUseCase: GetArtworkByViewDateUseCase,
    private val addToUserFavoritesUseCase: AddToUserFavoritesUseCase,
    private val removeFromUserFavoritesUseCase: RemoveFromUserFavoritesUseCase,
    private val getUserFavoritesUseCase: GetUserFavoritesUseCase,
    private val checkArtworkInFavoritesUseCase: CheckArtworkInFavoritesUseCase,
    private val artworksLikedByUserUseCase: ArtworksLikedByUserUseCase,
    private val artworksByCreatorUseCase: ArtworksByCreatorUseCase,
    private val artworksSearchUseCase: ArtworksSearchUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val setUserIdUseCase: SetUserIdUseCase,
    private val setUserLanguageUseCase: SetUserLanguageUseCase,
    private val getUserLanguageUseCase: GetUserLanguageUseCase,
    private val getRecommendedArtworksUseCase: GetRecommendedArtworksUseCase,
    private val getGetLatestArtworksUseCase: GetLatestArtworksUseCase,
    private val getUserEmailUseCase: GetUserEmailUseCase,
    private val getUserUsername: GetUserUsernameUseCase,
    private val authorizeUserUseCase: AuthorizeUserUseCase,
    private val addArtworkFirestoreUseCase: AddArtworkFirestoreUseCase,
    private val getArtworkByIdFirestoreUseCase: GetArtworkByIdFirestoreUseCase,

    ) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val favoritesCollection = firestore.collection("favorites")
    private val artworksCollection = firestore.collection("artworks")


    private val _userData = MutableLiveData<UserEntity?>()
    val userData: LiveData<UserEntity?> get() = _userData

    private val _artworksData = MutableLiveData<List<ArtworkEntity>>()
    val artworksData: LiveData<List<ArtworkEntity>> get() = _artworksData

    private val _userFavoritesData = MutableLiveData<List<UserFavoritesEntity>>()
    val userFavoritesData: LiveData<List<UserFavoritesEntity>> get() = _userFavoritesData

    private val _todayArtworkLiveData = MutableLiveData<ArtworkEntity?>()
    val todayArtworkLiveData: LiveData<ArtworkEntity?> get() = _todayArtworkLiveData

    private val _artworkByIdLiveData = MutableLiveData<ArtworkEntity?>()
    val artworkByIdLiveData: LiveData<ArtworkEntity?> get() = _artworkByIdLiveData

    private val _isArtworkInFavoritesStateFlow = MutableStateFlow(false)
    val isArtworkInFavoritesStateFlow: StateFlow<Boolean>
        get() = _isArtworkInFavoritesStateFlow

    private val _artworksLikedByUserData = MutableStateFlow<List<ArtworkEntity>>(emptyList())
    val artworksLikedByUserData: StateFlow<List<ArtworkEntity>>
        get() = _artworksLikedByUserData

    private val _artworksByCreatorData = MutableLiveData<List<ArtworkEntity>>()
    val artworksByCreatorData: LiveData<List<ArtworkEntity>> get() = _artworksByCreatorData

    private val _searchArtworksData = MutableLiveData<List<ArtworkEntity>>()
    val searchArtworksData: LiveData<List<ArtworkEntity>> get() = _searchArtworksData

    private val _userIdLiveData = MutableLiveData<Long>()
    val userIdLiveData: LiveData<Long> get() = _userIdLiveData

    private val _languageStateFlow = MutableStateFlow<String>("")
    val languageStateFlow: StateFlow<String> get() = _languageStateFlow

    private val _recommendedArtworks = MutableLiveData<List<ArtworkEntity>>()
    val recommendedArtworks: LiveData<List<ArtworkEntity>> get() = _recommendedArtworks

    private val _latestArtworks = MutableLiveData<List<ArtworkEntity>>()
    val latestArtworks: LiveData<List<ArtworkEntity>> get() = _latestArtworks

    private val _userEmailLiveData = MutableLiveData<String>()
    val userEmailLiveData: LiveData<String> get() = _userEmailLiveData

    private val _userUsernameLiveData = MutableLiveData<String>()
    val userUsernameLiveData: LiveData<String> get() = _userUsernameLiveData

    private val _authorizeUserLiveData = MutableLiveData<Boolean>()
    val authorizeUserLiveData: LiveData<Boolean> get() = _authorizeUserLiveData

    private val _addArtworkResult = MutableLiveData<Boolean>()
    val addArtworkResult: LiveData<Boolean>
        get() = _addArtworkResult

    private val _artworkLiveData = MutableLiveData<ArtworkEntity?>()
    val artworkLiveData: LiveData<ArtworkEntity?> = _artworkLiveData


    init {
        observeFavoritesChanges()
    }

    fun createUser(user: UserEntity) {
        viewModelScope.launch {
            createUserUseCase(user)
        }
    }

    fun getUserByUsername(username: String) {
        viewModelScope.launch {
            val user = getUserByUsernameUseCase(username)
            _userData.postValue(user)
        }
    }

    fun createArtwork(artwork: ArtworkEntity) {
        viewModelScope.launch {
            createArtworkUseCase(artwork)
        }
    }

    fun getAllArtworks() {
        viewModelScope.launch {
            val artworks = getAllArtworksUseCase()
            _artworksData.postValue(artworks)
        }
    }

    fun getArtworkById(artworkId: Long) {
        viewModelScope.launch {
            val artwork = getArtworkByIdUseCase(artworkId)
            _artworkByIdLiveData.postValue(artwork)
        }
    }

    fun getArtworkByViewDate(viewDate: String) {
        viewModelScope.launch {
            artworksCollection
                .orderBy("viewDate", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val document = querySnapshot.documents[0]
                    val artworkEntity = document.toObject(ArtworkEntity::class.java)
                    artworkEntity?.firestoreId = document.id
                    _todayArtworkLiveData.postValue(artworkEntity)
                }
                .addOnFailureListener { e ->
                    Log.e("FavoritesViewModel", "Error getArtworkByViewDate", e)
                }

        }
    }


    fun addToUserFavorites(userFavorite: UserFavoritesEntity) {
        viewModelScope.launch {
            addToUserFavoritesUseCase(userFavorite)
        }
    }

    fun removeFromUserFavorites(userId: Long, artworkId: Long) {
        viewModelScope.launch {
            removeFromUserFavoritesUseCase(userId, artworkId)
        }
    }

    fun getUserFavorites(userId: Long) {
        viewModelScope.launch {
            val userFavorites = getUserFavoritesUseCase(userId)
            _userFavoritesData.postValue(userFavorites)
        }
    }

    fun checkArtworkInFavorites(artworkId: String) {
        viewModelScope.launch {
            try {
                val favoriteQuery = favoritesCollection
                    .whereEqualTo("uid", auth.currentUser?.uid)
                    .whereEqualTo("artworkId", artworkId.toString())
                    .get()
                    .await()

                val isArtworkInFavorites = !favoriteQuery.isEmpty
                _isArtworkInFavoritesStateFlow.value = isArtworkInFavorites
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Error checking artwork in favorites", e)
            }
        }
    }


    fun handleUserFavorites(userId: Long, artworkId: Long) {
        viewModelScope.launch {
            val isArtworkInFavorites =
                checkArtworkInFavoritesUseCase(userId, artworkId)
            _isArtworkInFavoritesStateFlow.value = !isArtworkInFavorites

            if (isArtworkInFavorites) {
                removeFromUserFavoritesUseCase(userId, artworkId)
            } else {
                addToUserFavoritesUseCase(
                    UserFavoritesEntity(
                        userId = userId,
                        artworkId = artworkId
                    )
                )
            }
        }
    }

    fun toggleFavorite(artworkId: String) {
        viewModelScope.launch {
            _isArtworkInFavoritesStateFlow.value = !_isArtworkInFavoritesStateFlow.value
            val currentUser = auth.currentUser ?: return@launch
            val uid = currentUser.uid
            val favoriteQuery = favoritesCollection
                .whereEqualTo("uid", uid)
                .whereEqualTo("artworkId", artworkId)

            try {
                val querySnapshot = favoriteQuery.get().await()

                if (querySnapshot.isEmpty) {
                    val addResult =
                        favoritesCollection.add(mapOf("uid" to uid, "artworkId" to artworkId))
                            .await()
                    Log.d("FavoritesViewModel", "Successfully added favorite: $addResult")
                } else {
                    querySnapshot.documents.forEach { document ->
                        val deleteResult = document.reference.delete().await()
                        Log.d("FavoritesViewModel", "Successfully deleted favorite: $deleteResult")
                    }
                }
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Error toggling favorite", e)
            }
        }
    }


    fun getArtworksLikedByUser() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch

            try {
                val favoriteQuery = favoritesCollection.whereEqualTo("uid", userId).get().await()
                val artworkIds = favoriteQuery.documents.mapNotNull { it.getString("artworkId") }
                val artworks = mutableListOf<ArtworkEntity>()

                for (artworkId in artworkIds) {
                    val artworkDocument = artworksCollection.document(artworkId).get().await()
                    artworkDocument.toObject(ArtworkEntity::class.java)?.let { artworks.add(it) }
                }

                _artworksLikedByUserData.value = artworks
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun observeFavoritesChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = auth.currentUser?.uid ?: return@launch

            favoritesCollection.whereEqualTo("uid", userId).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    return@addSnapshotListener
                }

                val artworkIds =
                    snapshot?.documents?.mapNotNull { it.getString("artworkId") } ?: emptyList()

                viewModelScope.launch(Dispatchers.IO) {
                    loadArtworks(artworkIds)
                }
            }
        }
    }


    private suspend fun loadArtworks(artworkIds: List<String>) {
        val artworks = mutableListOf<ArtworkEntity>()

        for (artworkId in artworkIds) {
            try {
                val artworkDocument = artworksCollection.document(artworkId).get().await()
                val artworkEntity = artworkDocument.toObject(ArtworkEntity::class.java)
                artworkEntity?.let { artworks.add(it) }
            } catch (e: Exception) {
                // Обработка ошибок, если что-то пошло не так при загрузке данных об искусстве
                e.printStackTrace()
            }
        }

        // Обновляем StateFlow с полученными данными об искусстве
        _artworksLikedByUserData.value = artworks
    }

    fun getArtworksByCreator(creator: String) {
        viewModelScope.launch {
            val artworks = artworksByCreatorUseCase(creator)
            _artworksByCreatorData.postValue(artworks)
        }
    }

    fun searchArtworks(keyword: String) {
        viewModelScope.launch {
            val searchResults = artworksSearchUseCase(keyword)
            _searchArtworksData.postValue(searchResults)
        }
    }

    fun getUserId() {
        viewModelScope.launch {
            val userId = getUserIdUseCase()
            _userIdLiveData.postValue(userId)
        }
    }

    fun setUserId(userId: Long) {
        viewModelScope.launch {
            setUserIdUseCase.setUserId(userId)
        }
    }

    fun setUserLanguage(language: String? = null) {
        viewModelScope.launch {
            if (_languageStateFlow.value == "") {
                setUserLanguageUseCase.setUserLanguage("")
                return@launch
            }
            if (language == null) {
                setUserLanguageUseCase.setUserLanguage(
                    if (_languageStateFlow.value == "ru")
                        "en" else "ru"
                )
            } else
                setUserLanguageUseCase.setUserLanguage(language)
        }
    }

    fun getUserLanguage() {
        viewModelScope.launch {
            val language = getUserLanguageUseCase.getUserLanguage()
            _languageStateFlow.value = language ?: ""
        }
    }

    fun getRecommendedArtworks() {
        viewModelScope.launch {
            val artworks = getRecommendedArtworksUseCase.invoke()
            _recommendedArtworks.value = artworks
        }
    }

    fun getLatestArtworks() {
        viewModelScope.launch {
            val artworks = getGetLatestArtworksUseCase.invoke()
            _latestArtworks.value = artworks
        }
    }

    fun getUserEmail() {
        viewModelScope.launch {
            val email = getUserEmailUseCase.invoke()
            _userEmailLiveData.value = email
        }
    }

    fun getUserUsername() {
        viewModelScope.launch {
            val username = getUserUsername.invoke()
            _userUsernameLiveData.value = username
        }
    }

    fun authorizeUser(username: String, password: String) {
        viewModelScope.launch {
            val isAccessGranted = authorizeUserUseCase.invoke(username, password)
            _authorizeUserLiveData.value = isAccessGranted
        }
    }

    fun addArtworkToFirestore(artwork: ArtworkEntity) {
        viewModelScope.launch {
            try {
                addArtworkFirestoreUseCase.invoke(artwork)
                _addArtworkResult.value = true
            } catch (e: Exception) {
                _addArtworkResult.value = false
                Log.e("MainViewModel", "Error adding artwork to Firestore", e)
            }
        }
    }

    fun getArtworkByIdFirestore(artworkId: String) {
        viewModelScope.launch {
            try {
                val artwork = getArtworkByIdFirestoreUseCase(artworkId)
                _artworkLiveData.value = artwork
            } catch (e: Exception) {
                // Handle error
                Log.e("YourViewModel", "Error getting artwork by ID", e)
            }
        }
    }

    fun isUserAuth(): Boolean {
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }
}
