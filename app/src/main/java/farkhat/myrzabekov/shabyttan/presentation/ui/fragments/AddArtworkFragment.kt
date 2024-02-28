package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.databinding.FragmentAddArtworkBinding
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import java.util.UUID
@AndroidEntryPoint
class AddArtworkFragment : Fragment() {
    private var _binding: FragmentAddArtworkBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val storage = Firebase.storage
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddArtworkBinding.inflate(inflater, container, false)

        binding.uploadImage.setOnClickListener {
            openImagePicker()
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
                    // Set the selected image to an ImageView or display it as needed
                }
            }
        }

    private fun uploadImageToStorage() {
        imageUri?.let { uri ->
            val imageName = UUID.randomUUID().toString() // Generate a unique name for the image
            val imageRef = storage.reference.child("images/$imageName")

            val uploadTask = imageRef.putFile(uri)

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val author = binding.author.text.toString()
                    val description = binding.description.text.toString()
                    // Once the image is uploaded, save the download URL along with other fields to Firestore
                    saveArtworkToFirestore(author, description, downloadUri.toString())
                } else {
                    // Handle errors
                }
            }
        }
    }

    private fun saveArtworkToFirestore(author: String, description: String, imageUrl: String) {
        // Implement saving to Firestore as shown in the previous example
        val artwork = ArtworkEntity(
            0,
            "123",
            "",
            "",
            author,
            author,
            "",
            "",
            "",
            "",
            "",
            "",
            description,
            description,
            "",
            "",
            imageUrl,
            ""
        )
        viewModel.addArtworkToFirestore(artwork)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}