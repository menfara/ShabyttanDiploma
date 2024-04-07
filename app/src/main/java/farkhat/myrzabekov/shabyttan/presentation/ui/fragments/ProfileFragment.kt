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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.databinding.FragmentProfileBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.AdminProfilePagerAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.ProfilePagerAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.getStringInLocale
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var savedLanguage: String = "en"
    private var savedUserId: Long = -1
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()


    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: ProfilePagerAdapter
    private lateinit var pagerAdminAdapter: AdminProfilePagerAdapter
    private lateinit var tabLayout: TabLayout
    private var isAdmin = false

    private var isLanguageSet: Boolean = false
    private lateinit var sharedPreferences: SharedPreferences
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


        val args = arguments
        if (args != null) {
            isAdmin = args.getBoolean("isAdmin", false)
        }


        if (!viewModel.isUserAuth()) {
            findNavController().navigate(R.id.action_profileFragment_to_signUpFragment)
        }


        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        if (isAdmin) {
            pagerAdminAdapter = AdminProfilePagerAdapter(requireActivity())
            viewPager.adapter = pagerAdminAdapter
        } else {
            pagerAdapter = ProfilePagerAdapter(requireActivity())
            viewPager.adapter = pagerAdapter
        }

        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        tabLayout.getTabAt(0)?.text =
            requireContext().getStringInLocale(
                if (isAdmin) R.string.events else R.string.posts,
                savedLanguage
            )
        tabLayout.getTabAt(1)?.text =
            requireContext().getStringInLocale(
                if (isAdmin) R.string.history else R.string.favorites,
                savedLanguage
            )




        viewModel.getArtworksLikedByUser()


        viewModel.getUserLanguage()
        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            Log.d(">>> YourFragment", "User language: $language")
            savedLanguage = language
            tabLayout.getTabAt(0)?.text =
                requireContext().getStringInLocale(
                    if (isAdmin) R.string.events else R.string.posts,
                    savedLanguage
                )
            tabLayout.getTabAt(1)?.text =
                requireContext().getStringInLocale(
                    if (isAdmin) R.string.history else R.string.favorites,
                    savedLanguage
                )
        }


        setupObservers()
        setupRecyclerView()

        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            savedLanguage = language
            tabLayout.getTabAt(0)?.text =
                requireContext().getStringInLocale(
                    if (isAdmin) R.string.events else R.string.posts,
                    savedLanguage
                )
            tabLayout.getTabAt(1)?.text =
                requireContext().getStringInLocale(
                    if (isAdmin) R.string.history else R.string.favorites,
                    savedLanguage
                )
        }


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

        displayAvatarIfExists()
        binding.avatarImageView.setOnClickListener {
            openImagePicker()
        }


//        binding.logoutImageView.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//
//            viewModel.setUserId(-1)
//            recreateActivity()
//        }


        binding.bannerImageView.setOnClickListener {

        }

        binding.settingsImageView.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        binding.addPostImageView.setOnClickListener {
            if (!isAdmin)
                findNavController().navigate(R.id.action_profileFragment_to_addArtworkFragment)
            else
                findNavController().navigate(R.id.action_profileFragment_to_addEventFragment)
        }


    }

    private fun recreateActivity() {
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }


    private fun setupObservers() {
//        viewModel.artworksLikedByUserData.asLiveData()
//            .observe(viewLifecycleOwner) { userFavorites ->
//                binding.historyRecyclerView.adapter =
//                    HistoryRecyclerViewAdapter(userFavorites, this, savedLanguage)
//            }
    }

    private fun setupRecyclerView() {
//        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openImagePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val clipData = data?.clipData
                clipData?.getItemAt(0)?.uri
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


    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

}