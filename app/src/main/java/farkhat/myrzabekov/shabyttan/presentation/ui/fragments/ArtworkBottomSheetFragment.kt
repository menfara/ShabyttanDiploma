package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.data.repository.UserRepositoryImpl
import farkhat.myrzabekov.shabyttan.databinding.DialogFullScreenImageBinding
import farkhat.myrzabekov.shabyttan.databinding.FragmentArtworkBottomSheetBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.UIHelper
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel
import java.util.Locale

@AndroidEntryPoint
class ArtworkBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentArtworkBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val uiHelper by lazy { UIHelper(this) }
    private var todayArtwork: ArtworkEntity? = null
    private val viewModel: MainViewModel by viewModels()
    private var savedLanguage: String = "en"
    private var savedUserId: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtworkBottomSheetBinding.inflate(inflater, container, false)
        viewModel.getAllArtworks()
        setupPreferences()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.let { bottomSheet ->
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            }

        arguments?.getLong("artworkId")?.let { viewModel.getArtworkById(it) }

        viewModel.artworkByIdLiveData.observe(viewLifecycleOwner) { artwork ->
            if (artwork != null) {
                todayArtwork = artwork
            }

            val titleToShow =
                if (savedLanguage == "ru") todayArtwork?.titleRu else todayArtwork?.title
            val authorToShow =
                if (savedLanguage == "ru") todayArtwork?.creatorRu else todayArtwork?.creator
            val descriptionToShow =
                if (savedLanguage == "ru") todayArtwork?.descriptionRu else todayArtwork?.description
            val didYouKnowToShow =
                if (savedLanguage == "ru") todayArtwork?.didYouKnowRu else todayArtwork?.didYouKnow

            uiHelper.loadImage(binding.artImage, todayArtwork?.imageUrl)
            binding.artAuthor.text = authorToShow ?: ""
            binding.artTitle.text = titleToShow ?: ""
            binding.artDescription.text = descriptionToShow ?: ""
            binding.artFunFact.text = didYouKnowToShow ?: ""

            uiHelper.setupAppBarListener(
                binding.appbar,
                binding.artImage,
                binding.shareActionButton,
                binding.likeActionButton
            )
            uiHelper.setupImageClickToShowDialog(binding.artImage) {
                DialogFullScreenImageBinding.inflate(layoutInflater)
            }
            uiHelper.setupScrollViewListener(binding.nestedScrollView, binding.toTopActionButton)
            uiHelper.setupToTopButton(
                binding.toTopActionButton,
                binding.nestedScrollView,
                binding.appbar
            )
            updateLikeButton()
            setupLikeButton()
            setupShareButton()
        }
    }

    private fun updateLikeButton() {
        todayArtwork?.let { viewModel.checkArtworkInFavorites(savedUserId, it.id) }
        viewModel.isArtworkInFavoritesStateFlow.asLiveData()
            .observe(viewLifecycleOwner) { isArtworkInFavorites ->
                val likeButtonIcon =
                    if (isArtworkInFavorites) R.drawable.ic_red_heart else R.drawable.ic_heart
                binding.likeActionButton.setImageResource(likeButtonIcon)
            }
    }

    private fun setupLikeButton() {
        binding.likeActionButton.setOnClickListener {
            todayArtwork?.let { it1 ->
                viewModel.handleUserFavorites(
                    userId = savedUserId,
                    artworkId = it1.id
                )
            }
        }
    }

    private fun setupShareButton() {
        binding.shareActionButton.setOnClickListener {
            uiHelper.shareArtworkDeepLink("https://farkhat.myrzabekov.shabyttan/artwork/${todayArtwork?.id}")
        }
    }

    private fun setupPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)


        savedUserId = sharedPreferences.getLong(
            UserRepositoryImpl.SharedPreferencesKey.USER_ID.key,
            UserRepositoryImpl.SharedPreferencesKey.USER_ID.defaultValue as Long
        )

        savedLanguage = sharedPreferences.getString(
            UserRepositoryImpl.SharedPreferencesKey.LANGUAGE.key,
            UserRepositoryImpl.SharedPreferencesKey.LANGUAGE.defaultValue as String
        ).toString()
    }

    override fun onDestroyView() {
        uiHelper.clearImage(binding.artImage)
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(artworkId: Long?) = ArtworkBottomSheetFragment().apply {
            arguments = Bundle().apply {
                if (artworkId != null) {
                    putLong("artworkId", artworkId)
                }
            }
        }
    }
}