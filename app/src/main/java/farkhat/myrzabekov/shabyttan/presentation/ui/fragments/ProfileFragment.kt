package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.databinding.FragmentProfileBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.HistoryRecyclerViewAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.OnArtworkClickListener
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.ProfilePagerAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.SearchResultRecyclerViewAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.getStringInLocale
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var savedLanguage: String = "en"
    private var savedUserId: Long = -1
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()


    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: ProfilePagerAdapter
    private lateinit var tabLayout: TabLayout

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

        viewModel.getUserId()
        viewModel.userIdLiveData.observe(viewLifecycleOwner) { userId ->
            savedUserId = userId
            viewModel.getArtworksLikedByUser(userId)
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



//        binding.logoutImageView.setOnClickListener {
//            viewModel.setUserLanguage()
//
//            val navOptions = NavOptions.Builder()
//                .setPopUpTo(R.id.profileFragment, true)
//                .build()
//
//            view.findNavController().navigate(R.id.profileFragment, null, navOptions)
//        }

        setupObservers()
        setupRecyclerView()
//        setLanguage(savedLanguage)


        viewPager = binding.viewPager
        pagerAdapter = ProfilePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        pagerAdapter.addFragment(FavoritesFragment())
        pagerAdapter.addFragment(SettingsFragment())




        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            savedLanguage = language
            tabLayout.getTabAt(0)?.text =
                requireContext().getStringInLocale(R.string.favorites, savedLanguage)
            tabLayout.getTabAt(1)?.text =
                requireContext().getStringInLocale(R.string.settings, savedLanguage)
        }




        tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        val receivedArguments = arguments
        if (receivedArguments != null) {
            val isLanguageRefresh = receivedArguments.getBoolean("isLanguageRefresh")
            if (isLanguageRefresh) viewPager.setCurrentItem(1, true)

        }

        viewPager.setPageTransformer { page, _ ->
            updatePagerHeightForChild(page, viewPager)
        }


    }


    private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
        view.post {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)
            pager.layoutParams = (pager.layoutParams).also { lp -> lp.height = view.measuredHeight }
            pager.invalidate()
        }
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


}