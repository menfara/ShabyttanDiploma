package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.databinding.FragmentSettingsBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.getStringInLocale
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private var savedUserId: Long = -1
    private var savedLanguage: String = ""

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModelObservers()
        setupLanguageSelector()
        val user = auth.currentUser

        binding.notificationsTextView.setOnClickListener {
            val intent = Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            }
            startActivity(intent)
        }

        binding.logoutTextView.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            viewModel.setUserId(-1)
            recreateActivity()
        }

        binding.closeIconImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.changePasswordTextView.setOnClickListener {

            user?.email?.let { email ->
                resetPassword(email)
            }
        }

        user?.apply {
            displayName?.let { binding.usernameTextView.text = it }
            email?.let { binding.emailTextView.text = it }
        }

        binding.languageTextView.setOnClickListener {
            showLanguageMenu(it)
        }
    }

    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Password reset email sent",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to send reset email: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun recreateActivity() {
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }

    private fun initViewModelObservers() {
        viewModel.getUserId()
        viewModel.userIdLiveData.observe(viewLifecycleOwner) { userId ->
            savedUserId = userId ?: -1
        }

//        viewModel.getUserEmail()
//        viewModel.userEmailLiveData.observe(viewLifecycleOwner) { email ->
//            binding.emailTextView.text = email
//        }
//
//        viewModel.getUserUsername()
//        viewModel.userUsernameLiveData.observe(viewLifecycleOwner) { username ->
//            binding.usernameTextView.text = username
//        }

        viewModel.getUserLanguage()
        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            handleLanguageChange(language)
        }
    }

    private fun handleLanguageChange(language: String) {
        savedLanguage = language
//        binding.apply {
//            languageTextView.text = if (language == "ru") "Русский" else "English"
//            usernameLabelTextView.text =
//                requireContext().getStringInLocale(R.string.username, language)
//            emailLabelTextView.text = requireContext().getStringInLocale(R.string.email, language)
//            languageLabelTextView.text =
//                requireContext().getStringInLocale(R.string.language, language)
//        }
    }

    private fun setupLanguageSelector() {
//        binding.languageSelector.setOnClickListener { selectorView ->
//            showLanguageMenu(selectorView)
//        }
    }

    private fun showLanguageMenu(view: View) {

        val languageMap = mapOf(
            R.id.menu_item_language_1 to "en",
            R.id.menu_item_language_2 to "ru"
        )

        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.language_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            languageMap[menuItem.itemId]?.let { languageCode ->
                setLanguageAndNavigate(languageCode, view)
                true
            } ?: false
        }
        popupMenu.show()
    }

    private fun setLanguageAndNavigate(languageCode: String, view: View) {
        viewModel.setUserLanguage(languageCode)

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.profileFragment, true)
            .build()

        val bundle = Bundle().apply {
            putBoolean("isLanguageRefresh", true)
        }

        with(sharedPreferences.edit()) {
            putBoolean("IS_LANGUAGE_SET", false)
            apply()
        }

        view.findNavController().navigate(R.id.profileFragment, bundle, navOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
