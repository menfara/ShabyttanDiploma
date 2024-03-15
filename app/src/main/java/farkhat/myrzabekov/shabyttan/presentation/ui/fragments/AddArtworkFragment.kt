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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.databinding.FragmentAddArtworkBinding
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
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
//            openImagePicker()
            showDateRangePicker()
        }

        binding.submit.setOnClickListener {
            uploadImageToStorage()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
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
                }
            }
        }

    private fun uploadImageToStorage() {
        imageUri?.let { uri ->
            val imageName = UUID.randomUUID().toString()
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
                    val author = binding.textInputAuthor.text.toString()
                    val description = binding.textInputDescription.text.toString()

                    saveArtworkToFirestore(author, description, downloadUri.toString())
                } else {
                    // Handle errors
                }
            }
        }
    }

    private fun saveArtworkToFirestore(author: String, description: String, imageUrl: String) {

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

    private fun showDateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()

        // Установка начальной и конечной даты по умолчанию (опционально)
        val constraintsBuilder = Calendar.getInstance()
        val startDate = constraintsBuilder.timeInMillis
        constraintsBuilder.add(Calendar.MONTH, 1)
        val endDate = constraintsBuilder.timeInMillis
        builder.setTheme(com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
        builder.setSelection(androidx.core.util.Pair(startDate, endDate))

        val picker = builder.build()
        picker.show(parentFragmentManager, picker.toString())

        // Обработка выбора диапазона дат
        picker.addOnPositiveButtonClickListener { selection ->

            Log.d(
                "AddArtworkFragment",
                "${formatDate(selection.first)} - ${formatDate(selection.second)}"
            )
        }
    }

    private fun formatDate(milliseconds: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = milliseconds
        }
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}