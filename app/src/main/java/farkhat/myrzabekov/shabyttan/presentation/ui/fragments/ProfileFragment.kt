package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.databinding.FragmentProfileBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.ProfilePagerAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.getStringInLocale
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var savedLanguage: String = "en"
    private var savedUserId: Long = -1
    private lateinit var sharedPreferences: SharedPreferences

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: ProfilePagerAdapter
    private lateinit var tabLayout: TabLayout

    private val imagePickCode = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("StringFormatInvalid")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupObservers()
        setupViewPagerAndTabs()
        handleLanguageRefresh()
        displayAvatarIfExists()
        setClickListeners()
    }

    private fun initViews() {
        viewPager = binding.viewPager
        pagerAdapter = ProfilePagerAdapter(requireActivity())
        tabLayout = binding.tabLayout
    }

    private fun setupObservers() {
        viewModel.userIdLiveData.observe(viewLifecycleOwner) { userId ->
            if (userId != null) {
                savedUserId = userId
                viewModel.getArtworksLikedByUser(userId)
            }
        }

        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            Log.d(">>> YourFragment", "User language: $language")
            savedLanguage = language
            updateTabTitles()
        }
    }

    private fun setupViewPagerAndTabs() {
        pagerAdapter.addFragment(FavoritesFragment())
        pagerAdapter.addFragment(SettingsFragment())
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }

    private fun updateTabTitles() {
        tabLayout.getTabAt(0)?.text = requireContext().getStringInLocale(R.string.favorites, savedLanguage)
        tabLayout.getTabAt(1)?.text = requireContext().getStringInLocale(R.string.settings, savedLanguage)
    }

    private fun handleLanguageRefresh() {
        val isLanguageSet = sharedPreferences.getBoolean("IS_LANGUAGE_SET", false)

        val receivedArguments = arguments
        if (receivedArguments != null && !isLanguageSet) {
            val isLanguageRefresh = receivedArguments.getBoolean("isLanguageRefresh")
            if (isLanguageRefresh) {
                Handler(Looper.getMainLooper()).postDelayed({
                    viewPager.setCurrentItem(1, true)
                    with(sharedPreferences.edit()) {
                        putBoolean("IS_LANGUAGE_SET", true)
                        apply()
                    }
                }, 100)
            }
        }
    }

    private fun setClickListeners() {
        binding.avatarImageView.setOnClickListener {
            openImagePicker()
        }

        binding.logoutImageView.setOnClickListener {
            viewModel.setUserId(-1)
            recreateActivity()
        }
    }

    private fun recreateActivity() {
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }

    private fun openImagePicker() {
        val intent = Intent(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            MediaStore.ACTION_PICK_IMAGES
        } else {
            Intent.ACTION_PICK
        })

        startActivityForResult(intent, imagePickCode)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imagePickCode && resultCode == Activity.RESULT_OK) {
            handleImagePickerResult(data)
        }
    }

    @SuppressLint("Recycle")
    private fun handleImagePickerResult(data: Intent?) {
        val imageUri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            data?.clipData?.getItemAt(0)?.uri
        } else {
            data?.data
        }

        imageUri?.let { uri ->
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().filesDir, "avatar.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)

            binding.avatarImageView.setImageURI(Uri.fromFile(file))
        }
    }

    private fun displayAvatarIfExists() {
        val file = File(requireContext().filesDir, "avatar.jpg")
        if (file.exists()) {
            val imageUri = Uri.fromFile(file)
            binding.avatarImageView.setImageURI(imageUri)
        } else {
            binding.avatarImageView.setImageResource(R.drawable.avatar)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
