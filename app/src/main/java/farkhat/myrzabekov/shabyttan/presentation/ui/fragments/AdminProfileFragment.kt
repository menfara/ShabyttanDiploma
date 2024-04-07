package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.databinding.FragmentAdminProfileBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.AdminProfilePagerAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.ProfilePagerAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.getStringInLocale
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel
import java.io.File


class AdminProfileFragment : Fragment() {

    private var _binding: FragmentAdminProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private var savedLanguage: String = "en"
    private var savedUserId: Long = -1

    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: AdminProfilePagerAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminProfileBinding.inflate(inflater, container, false)

        viewPager = binding.viewPager
        pagerAdapter = AdminProfilePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        pagerAdapter.addFragment(FavoritesFragment())
//        pagerAdapter.addFragment(SettingsFragment())


        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            savedLanguage = language
            tabLayout.getTabAt(0)?.text =
                requireContext().getStringInLocale(R.string.posts, savedLanguage)
            tabLayout.getTabAt(1)?.text =
                requireContext().getStringInLocale(R.string.favorites, savedLanguage)
        }


        tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

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
//            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        binding.addPostImageView.setOnClickListener {
//            findNavController().navigate(R.id.action_profileFragment_to_addArtworkFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!viewModel.isUserAuth()) {
//            findNavController().navigate(R.id.action_profileFragment_to_signUpFragment)
        }
        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            Log.d(">>> YourFragment", "User language: $language")
            savedLanguage = language
        }

        binding.viewPager

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

    private fun openImagePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
            startActivityForResult(intent, AdminProfileFragment.IMAGE_PICK_CODE)
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, AdminProfileFragment.IMAGE_PICK_CODE)
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

}