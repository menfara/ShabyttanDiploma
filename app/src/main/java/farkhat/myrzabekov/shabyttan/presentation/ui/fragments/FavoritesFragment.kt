package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.graphics.drawable.AnimatedVectorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

    private var savedLanguage: String = "en"
    private var savedUserId: Long = -1
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserId()
        viewModel.getUserLanguage()

        viewModel.userIdLiveData.observe(viewLifecycleOwner) { userId ->
            if (userId != null) {
                savedUserId = userId
                viewModel.getArtworksLikedByUser(userId)
            }
        }

        viewModel.getUserLanguage()
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
                binding.historyRecyclerView.adapter =
                    HistoryRecyclerViewAdapter(userFavorites, this, savedLanguage)

                if (userFavorites.isNotEmpty()) {
                    binding.apply {
                        emptyImageView.visibility = View.GONE
                        emptyInfoTextView.visibility = View.GONE
                        emptyTitleTextView.visibility = View.GONE
                    }
                } else {
                    binding.apply {
                        emptyImageView.visibility = View.VISIBLE
                        emptyInfoTextView.visibility = View.VISIBLE
                        emptyTitleTextView.visibility = View.VISIBLE
                    }
                }
            }

        animateImageView(binding.emptyImageView)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun animateImageView(imageView: ImageView) =
        imageView.drawable.apply {
            when (this) {
                is AnimatedVectorDrawableCompat -> this.start()
                is AnimatedVectorDrawable -> this.start()
                else -> { /* not an animated icon */
                }
            }
        }


    override fun onArtworkClick(artworkId: Long) {
        val bottomSheetFragment = ArtworkBottomSheetFragment.newInstance(artworkId)
        bottomSheetFragment.show(childFragmentManager, "ArtworkBottomSheetTag")
    }

}