package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.databinding.FragmentDiscoverBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.DiscoverPagerAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.getStringInLocale
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel


@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private var savedLanguage: String = ""
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        val adapter = DiscoverPagerAdapter(requireActivity())
        viewPager.adapter = adapter


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.for_you)
                1 -> getString(R.string.events)
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }.attach()
        binding.searchButton.setOnClickListener {
            findNavController().navigate(R.id.action_discoverFragment_to_searchFragment)
        }
        viewModel.getUserLanguage()
        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            handleLanguageChange(language)
            tabLayout.getTabAt(0)?.text =
                requireContext().getStringInLocale(
                    R.string.for_you,
                    savedLanguage
                )
            tabLayout.getTabAt(1)?.text =
                requireContext().getStringInLocale(
                    R.string.events,
                    savedLanguage
                )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleLanguageChange(language: String) {
        savedLanguage = language
        binding.apply {
            discoverTextView.text =
                requireContext().getStringInLocale(R.string.discover, language)

        }
    }

}