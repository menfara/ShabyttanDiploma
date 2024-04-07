package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.databinding.FragmentFavoritesBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.HistoryRecyclerViewAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.OnArtworkClickListener
import farkhat.myrzabekov.shabyttan.presentation.ui.getStringInLocale
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel

@AndroidEntryPoint
class FavoritesFragment : Fragment(), OnArtworkClickListener {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var savedLanguage: String = "en"
    private var savedUserId: Long = -1
    private val viewModel: MainViewModel by viewModels()
    private var isPosts = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val args = arguments
        if (args != null) {
            isPosts = args.getBoolean("post", false)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        setupObservers()
        animateImageView(binding.emptyImageView)
    }

    private fun setupObservers() {
        viewModel.getUserId()
        viewModel.getUserLanguage()

        if (!isPosts)
            viewModel.getArtworksLikedByUser()
        else
            viewModel.getPostsByUser()


        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            Log.d(">>> YourFragment", "User language: $language")
            savedLanguage = language
            binding.apply {
                emptyTitleTextView.text =
                    requireContext().getStringInLocale(R.string.no_favorites_yet, language)
                emptyInfoTextView.text =
                    requireContext().getStringInLocale(R.string.no_favorites_info, language)
            }
        }

        viewModel.artworksLikedByUserData.asLiveData()
            .observe(viewLifecycleOwner) { userFavorites ->
                updateUIVisibility(userFavorites.isNotEmpty())
                binding.historyRecyclerView.adapter =
                    HistoryRecyclerViewAdapter(userFavorites, this, savedLanguage)
            }

    }

    private fun updateUIVisibility(hasFavorites: Boolean) {
        with(binding) {
            val visibility = if (hasFavorites) View.GONE else View.VISIBLE
            emptyImageView.visibility = visibility
            emptyInfoTextView.visibility = visibility
            emptyTitleTextView.visibility = visibility
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun animateImageView(imageView: ImageView) {
        imageView.drawable?.apply {
            when (this) {
                is AnimatedVectorDrawableCompat -> this.start()
                is AnimatedVectorDrawable -> this.start()
                else -> { /* not an animated icon */
                }
            }
        }
    }

    override fun onArtworkClick(artworkId: String) {
        val bottomSheetFragment = ArtworkBottomSheetFragment.newInstance(artworkId)
        bottomSheetFragment.show(childFragmentManager, "ArtworkBottomSheetTag")
    }
}
