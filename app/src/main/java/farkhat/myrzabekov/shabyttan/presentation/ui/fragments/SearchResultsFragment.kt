package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.databinding.FragmentSearchResultsBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.OnArtworkClickListener
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.SearchResultRecyclerViewAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.getStringInLocale
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel

@AndroidEntryPoint
class SearchResultsFragment : Fragment(), OnArtworkClickListener {

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private var savedLanguage: String = "en"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.getUserLanguage()
        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            Log.d(">>> YourFragment", "User language: $language")
            savedLanguage = language
            binding.apply {
                val localizedString =
                    requireContext().getStringInLocale(R.string.search_results, language)

                textView.text = localizedString
            }
        }

        val receivedArguments = arguments
        if (receivedArguments != null) {
            val keyword = receivedArguments.getString("keyword").toString()
            viewModel.searchArtworks(keyword)
            viewModel.searchArtworksData.observe(viewLifecycleOwner) { artworks ->
                val recyclerView = binding.recyclerView
                val layoutManager = LinearLayoutManager(requireContext())
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter =
                    SearchResultRecyclerViewAdapter(requireContext(), artworks, savedLanguage, this)
            }
        }





        return view
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
