package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.databinding.FragmentSearchBinding
import farkhat.myrzabekov.shabyttan.databinding.FragmentSearchResultsBinding
import farkhat.myrzabekov.shabyttan.databinding.FragmentSettingsBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.getStringInLocale
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private var savedUserId: Long = -1
    private var savedLanguage: String = ""

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserId()
        viewModel.userIdLiveData.observe(viewLifecycleOwner) { userId ->
            if (userId != null) {
                savedUserId = userId
            }
        }

        viewModel.getUserEmail()
        viewModel.userEmailLiveData.observe(viewLifecycleOwner) { email ->
            binding.emailTextView.text = email
        }

        viewModel.getUserUsername()
        viewModel.userUsernameLiveData.observe(viewLifecycleOwner) { username ->
            binding.usernameTextView.text = username
        }

        viewModel.getUserLanguage()
        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            Log.d(">>> YourFragment", "User language: $language")
            savedLanguage = language

            binding.apply {
                languageTextView.text = if (language == "ru") "Русский" else "English"


                usernameLabelTextView.text =
                    requireContext().getStringInLocale(R.string.username, language)
                emailLabelTextView.text =
                    requireContext().getStringInLocale(R.string.email, language)
                languageLabelTextView.text =
                    requireContext().getStringInLocale(R.string.language, language)


            }
        }



        binding.languageSelector.setOnClickListener { selectorView ->
            showLanguageMenu(selectorView)
        }


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

