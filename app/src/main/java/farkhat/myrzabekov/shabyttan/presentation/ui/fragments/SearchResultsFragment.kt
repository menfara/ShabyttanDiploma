package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
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
                textView.text = requireContext().getStringInLocale(R.string.search_results, language)

                emptyTitleTextView.text = requireContext().getStringInLocale(R.string.no_result, language)
                emptyInfoTextView.text = requireContext().getStringInLocale(R.string.no_result_info, language)
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

                if (artworks.isNotEmpty()) {
                    binding.apply {
                        emptyImageView.visibility = View.GONE
                        emptyInfoTextView.visibility = View.GONE
                        emptyTitleTextView.visibility = View.GONE
                        root.gravity = Gravity.TOP
                        binding.textView.visibility = View.VISIBLE
                    }
                } else {
                    binding.apply {
                        emptyImageView.visibility = View.VISIBLE
                        emptyInfoTextView.visibility = View.VISIBLE
                        emptyTitleTextView.visibility = View.VISIBLE
                        root.gravity = Gravity.CENTER
                        binding.textView.visibility = View.GONE
                    }
                }
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
