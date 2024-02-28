package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
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
        initViewModelObservers()
        setupLanguageSelector()
    }

    private fun initViewModelObservers() {
        viewModel.getUserId()
        viewModel.userIdLiveData.observe(viewLifecycleOwner) { userId ->
            savedUserId = userId ?: -1
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
            handleLanguageChange(language)
        }
    }

    private fun handleLanguageChange(language: String) {
        savedLanguage = language
        binding.apply {
            languageTextView.text = if (language == "ru") "Русский" else "English"
            usernameLabelTextView.text =
                requireContext().getStringInLocale(R.string.username, language)
            emailLabelTextView.text = requireContext().getStringInLocale(R.string.email, language)
            languageLabelTextView.text =
                requireContext().getStringInLocale(R.string.language, language)
        }
    }

    private fun setupLanguageSelector() {
        binding.languageSelector.setOnClickListener { selectorView ->
            showLanguageMenu(selectorView)
        }
    }

    private fun showLanguageMenu(view: View) {
//        val artwork = ArtworkEntity(
//            title = "Starry Night",
//            titleRu = "Звездная ночь",
//
//            creator = "Vincent van Gogh",
//            creatorRu = "Винсент ван Гог",
//
//            creationDate = "1889",
//            creationDateRu = "1889 год",
//
//            technique = "Oil on Canvas",
//            techniqueRu = "Масло, холст",
//
//            type = "Post-Impressionism",
//            typeRu = "Постимпрессионизм",
//
//            description = "Starry Night is one of Vincent van Gogh's most famous paintings...",
//            descriptionRu = "«Звездная ночь» — одна из самых известных картин Винсента ван Гога...",
//
//            didYouKnow = "Did you know that Starry Night depicts the view from the east-facing window of Van Gogh's asylum room...",
//            didYouKnowRu = "Знали ли вы, что «Звездная ночь» изображает вид из окна психиатрической больницы, в которой находился Ван Гог?",
//
//            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Van_Gogh_-_Starry_Night_-_Google_Art_Project.jpg/798px-Van_Gogh_-_Starry_Night_-_Google_Art_Project.jpg",
//
//            viewDate = null
//        )
//        viewModel.addArtworkToFirestore(artwork)


        viewModel.getArtworkByIdFirestore("CUMS8Hb1TqFDwQels13J")

        viewModel.artworkLiveData.observe(requireActivity()) { artwork ->
            artwork?.let {
                Log.d(
                    "Artwork",
                    "Title: ${it.title}, Creator: ${it.creator}, Description: ${it.description}"
                )
            } ?: Log.d("Artwork", "Artwork is null")
        }

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
