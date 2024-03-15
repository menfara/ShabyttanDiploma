package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.databinding.FragmentSearchBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.ArtistRecyclerViewAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.HistoryRecyclerViewAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.OnArtworkClickListener
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.RecommendationRecyclerViewAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.dp
import farkhat.myrzabekov.shabyttan.presentation.ui.decoration.HorizontalSpaceItemDecoration
import farkhat.myrzabekov.shabyttan.presentation.ui.decoration.PageIndicator
import farkhat.myrzabekov.shabyttan.presentation.ui.decoration.StartLinearSnapHelper
import farkhat.myrzabekov.shabyttan.presentation.ui.getStringInLocale
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel

@AndroidEntryPoint
class SearchFragment : Fragment(), OnArtworkClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    private var savedLanguage: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeLanguage()
        observeArtworksByCreator()
        observeRecommendedArtworks()
        observeLatestArtworks()
        setupSearchActionListener()

        binding.textInputSearch.requestFocus()
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.textInputSearch, InputMethodManager.SHOW_IMPLICIT)

    }

    private fun initUI() {
        setupArtistRecyclerViewUI()
        setupRecommendationRecyclerViewUI()
        setupHistoryRecyclerViewUI()
    }

    private fun observeLanguage() {
        viewModel.getUserLanguage()
        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            savedLanguage = language
            updateUIWithLanguage()
        }
    }

    private fun updateUIWithLanguage() {
        binding.apply {
            artistTextView.text = requireContext().getStringInLocale(R.string.author_paintings, savedLanguage)
            recommendationTextView.text = requireContext().getStringInLocale(R.string.recommended, savedLanguage)
            historyTextView.text = requireContext().getStringInLocale(R.string.history, savedLanguage)
            val hintText = requireContext().getStringInLocale(R.string.hint_search, savedLanguage)
            textBox.hint = hintText
        }
    }

    private fun observeArtworksByCreator() {
        viewModel.getArtworksByCreator("Claude Monet")
        viewModel.artworksByCreatorData.observe(viewLifecycleOwner) { artworks ->
            binding.horizontalRecyclerView.adapter = ArtistRecyclerViewAdapter(artworks, this)
        }
    }

    private fun observeRecommendedArtworks() {
        viewModel.getRecommendedArtworks()
        viewModel.recommendedArtworks.observe(viewLifecycleOwner) { artworks ->
            binding.recommendedRecyclerView.adapter =
                RecommendationRecyclerViewAdapter(artworks, this)
        }
    }

    private fun observeLatestArtworks() {
        viewModel.getLatestArtworks()
        viewModel.latestArtworks.observe(viewLifecycleOwner) { artworks ->
            binding.historyRecyclerView.adapter =
                HistoryRecyclerViewAdapter(artworks, this, savedLanguage)
        }
    }

    private fun setupSearchActionListener() {
        binding.textInputSearch.setOnEditorActionListener { it, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(it)
                navigateToSearchResults(it.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun navigateToSearchResults(keyword: String) {
        val bundle = Bundle()
        bundle.putString("keyword", keyword)
        view?.findNavController()?.navigate(R.id.searchResultsFragment, bundle)
    }

    private fun setupArtistRecyclerViewUI() {
        binding.horizontalRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.horizontalRecyclerView.addItemDecoration(HorizontalSpaceItemDecoration(15.dp))
    }

    private fun setupRecommendationRecyclerViewUI() {
        val layoutManagerRecommended =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recommendedRecyclerView.layoutManager = layoutManagerRecommended
        binding.recommendedRecyclerView.addItemDecoration(HorizontalSpaceItemDecoration(15.dp))
        val indicators =
            listOf(binding.indicator1, binding.indicator2, binding.indicator3, binding.indicator4)
        binding.recommendedRecyclerView.addOnScrollListener(
            PageIndicator(
                layoutManagerRecommended,
                indicators
            )
        )
        StartLinearSnapHelper().attachToRecyclerView(binding.recommendedRecyclerView)
    }

    private fun setupHistoryRecyclerViewUI() {
        binding.historyRecyclerView.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onArtworkClick(artworkId: Long) {
        val bottomSheetFragment = ArtworkBottomSheetFragment.newInstance(artworkId)
        bottomSheetFragment.show(childFragmentManager, "ArtworkBottomSheetTag")
    }
}
