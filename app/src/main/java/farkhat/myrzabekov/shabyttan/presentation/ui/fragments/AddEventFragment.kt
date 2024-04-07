package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import farkhat.myrzabekov.shabyttan.data.remote.model.Event
import farkhat.myrzabekov.shabyttan.databinding.FragmentAddEventBinding
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID


class AddEventFragment : Fragment() {

    private var _binding: FragmentAddEventBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val storage = Firebase.storage
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEventBinding.inflate(inflater, container, false)

        binding.postImageView.setOnClickListener {
            openImagePicker()
//            showDateRangePicker()
        }

        binding.submit.setOnClickListener {
            uploadImageToStorage()
        }

        return binding.root
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { uri ->
                    imageUri = uri
                    binding.postImageView.setImageURI(imageUri)
                }
            }
        }

    private fun uploadImageToStorage() {
        imageUri?.let { uri ->
            val imageName = UUID.randomUUID().toString()
            val imageRef = storage.reference.child("images/$imageName")
            binding.progressBar.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val downloadUrl = uploadImage(uri, imageRef)
                    val title = binding.titleEditText.text.toString()
                    val description = binding.descriptionEditText.text.toString()
                    val isFree = binding.isFreeCheckBox.isChecked
                    val location = binding.locationEditText.text.toString()


                    withContext(Dispatchers.Main) {
                        saveArtworkToFirestore(
                            title = title,
                            description = description,
                            imageUrl = downloadUrl,
                            isFree = isFree,
                            location = location
                        )
                    }
                } catch (e: Exception) {

                    Log.e("AddArtworkFragment", "Failed to upload image: $e")
                } finally {
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private suspend fun uploadImage(uri: Uri, imageRef: StorageReference): String {
        return withContext(Dispatchers.IO) {
            imageRef.putFile(uri).await()
            return@withContext imageRef.downloadUrl.await()
                .toString()
        }
    }

    private fun saveArtworkToFirestore(
        title: String,
        location: String,
        description: String,
        imageUrl: String,
        isFree: Boolean
    ) {


        CoroutineScope(Dispatchers.IO).launch {
            val auth = FirebaseAuth.getInstance()
            val uid = auth.currentUser?.uid


            val event = Event(
                id = "123",
                title = title,
                location = location,
                imageUrl = imageUrl,
                description = description,
                free = isFree,
                creator = uid.toString(),
            )

            val firestore = FirebaseFirestore.getInstance()

            firestore.collection("events")
                .add(event)
        }


    }


}