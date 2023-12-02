package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
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
        setupRecommendationRecyclerViewUI()
        setupHistoryRecyclerViewUI()

        viewModel.getUserLanguage()
        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            savedLanguage = language
            binding.apply {
                artistTextView.text =
                    requireContext().getStringInLocale(R.string.author_paintings, language)
                recommendationTextView.text =
                    requireContext().getStringInLocale(R.string.recommended, language)
                historyTextView.text =
                    requireContext().getStringInLocale(R.string.history, language)
                val hintText = requireContext().getStringInLocale(R.string.hint_search, language)
                textBox.hint = hintText
            }
        }

        viewModel.getArtworksByCreator("Benjamin West")
        viewModel.artworksByCreatorData.observe(viewLifecycleOwner) { artworks ->
            binding.horizontalRecyclerView.adapter = ArtistRecyclerViewAdapter(artworks, this)
        }

        viewModel.getRecommendedArtworks()
        viewModel.recommendedArtworks.observe(viewLifecycleOwner) { artworks ->
            binding.recommendedRecyclerView.adapter =
                RecommendationRecyclerViewAdapter(artworks, this)
        }

        viewModel.getLatestArtworks()
        viewModel.latestArtworks.observe(viewLifecycleOwner) { artworks ->
            binding.historyRecyclerView.adapter =
                HistoryRecyclerViewAdapter(artworks, this, savedLanguage)
        }

        binding.textInputSearch.setOnEditorActionListener { it, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val bundle = Bundle()
                bundle.putString("keyword", it.text.toString())

                view.findNavController().navigate(R.id.searchResultsFragment, bundle)
                return@setOnEditorActionListener true
            }
            false
        }

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