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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.databinding.FragmentAddArtworkBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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

        binding.postImageView.setOnClickListener {
            openImagePicker()
//            showDateRangePicker()
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

                    // Возвращаемся на главный поток для обновления UI
                    withContext(Dispatchers.Main) {
                        saveArtworkToFirestore(title, description, downloadUrl)
                    }
                } catch (e: Exception) {
                    // Обработка ошибок загрузки изображения
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
            imageRef.putFile(uri).await() // Загружаем изображение
            return@withContext imageRef.downloadUrl.await()
                .toString() // Получаем URL загруженного изображения
        }
    }

    private fun saveArtworkToFirestore(title: String, description: String, imageUrl: String) {
        try {
            val artwork = ArtworkEntity(
                0,
                "123",
                title,
                title,
                "",
                "",
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
                "24/03/2024",
                ownerId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            )
            viewModel.addArtworkToFirestore(artwork)
        } catch (e: Exception) {
            // Обработка ошибок сохранения данных в Firestore
            Log.e("AddArtworkFragment", "Failed to save artwork to Firestore: $e")
        }
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