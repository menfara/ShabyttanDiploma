package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

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
            savedUserId = userId
        }


        viewModel.getUserLanguage()
        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            Log.d(">>> YourFragment", "User language: $language")
            savedLanguage = language
            binding.apply {
                val localizedString =
                    requireContext().getStringInLocale(R.string.favorites, language)

//                favoriteTextView.text = localizedString
            }
        }


        binding.languageSelector.setOnClickListener { view ->
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.inflate(R.menu.language_menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_item_language_1 -> {
                        viewModel.setUserLanguage("en")
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.profileFragment, true)
                            .build()

                        val bundle = Bundle()
                        bundle.putBoolean("isLanguageRefresh", true)

                        view.findNavController().navigate(R.id.profileFragment, bundle, navOptions)

                        true
                    }

                    R.id.menu_item_language_2 -> {
                        viewModel.setUserLanguage("ru")
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.profileFragment, true)
                            .build()

                        val bundle = Bundle()
                        bundle.putBoolean("isLanguageRefresh", true)

                        view.findNavController().navigate(R.id.profileFragment, bundle, navOptions)

                        true
                    }

                    else -> false
                }
            }


            popupMenu.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

