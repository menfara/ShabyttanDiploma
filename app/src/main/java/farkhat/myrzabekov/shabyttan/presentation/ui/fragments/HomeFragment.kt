package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.databinding.DialogFullScreenImageBinding
import farkhat.myrzabekov.shabyttan.databinding.FragmentHomeBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.UIHelper
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val uiHelper by lazy { UIHelper(this) }

    private var savedUserId: Long = -1
    private var savedLanguage: String = ""
    private lateinit var todayArtwork: ArtworkEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserLanguage()
        viewModel.getUserId()
        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            savedLanguage = language
        }

        viewModel.getArtworkByViewDate("26/11/2023")

        setupObservers()
        setupLikeButton()
        setupShareButton()
        todayArtwork = ArtworkEntity()
    }

    private fun setupObservers() {
        viewModel.todayArtworkLiveData.observe(viewLifecycleOwner) { artwork ->
            if (artwork != null) {
                todayArtwork = artwork
            }
            updateArtworkDetails(todayArtwork)
            updateLikeButton()
        }

        viewModel.userIdLiveData.observe(viewLifecycleOwner) { userId ->
            if (userId != null) {
                savedUserId = userId
            }
        }
    }

    private fun updateArtworkDetails(artwork: ArtworkEntity?) {
        with(binding) {
            artTitle.text = getLocalizedText(artwork?.title, artwork?.titleRu)
            artAuthor.text = getLocalizedText(artwork?.creator, artwork?.creatorRu)
            val htmlDescription = getLocalizedText(artwork?.description, artwork?.descriptionRu)
            val spannedDescription = Html.fromHtml(htmlDescription, Html.FROM_HTML_MODE_LEGACY)

            artDescription.text = spannedDescription
            artFunFact.text = getLocalizedText(artwork?.didYouKnow, artwork?.didYouKnowRu)
        }

        Glide.with(this)
            .load(artwork?.imageUrl)
            .into(binding.artImage)

        uiHelper.loadImage(binding.artImage, artwork?.imageUrl)
        uiHelper.setupImageClickToShowDialog(binding.artImage) {
            DialogFullScreenImageBinding.inflate(layoutInflater)
        }
        uiHelper.setupScrollViewListener(binding.nestedScrollView, binding.toTopActionButton)
        uiHelper.setupToTopButton(
            binding.toTopActionButton,
            binding.nestedScrollView,
            binding.appbar
        )

        uiHelper.setupAppBarListener(
            binding.appbar,
            binding.artImage,
            binding.shareActionButton,
            binding.likeActionButton
        )

        removePlaceholders()
    }

    private fun getLocalizedText(defaultValue: String?, localizedValue: String?): String {
        return when (savedLanguage) {
            "ru" -> localizedValue ?: defaultValue ?: ""
            else -> defaultValue ?: ""
        }
    }

    private fun updateLikeButton() {
        viewModel.checkArtworkInFavorites(todayArtwork.firestoreId)
        viewModel.isArtworkInFavoritesStateFlow.asLiveData()
            .observe(viewLifecycleOwner) { isArtworkInFavorites ->
                val likeButtonIcon =
                    if (isArtworkInFavorites) R.drawable.ic_red_heart else R.drawable.ic_heart
                binding.likeActionButton.setImageResource(likeButtonIcon)
            }
    }

    private fun setupLikeButton() {
        binding.likeActionButton.setOnClickListener {
            if (!viewModel.isUserAuth()) {
                findNavController().navigate(R.id.action_homeFragment_to_signUpFragment)
            } else {
//                Log.d(">>> savedUserId", savedUserId.toString())
                viewModel.toggleFavorite(todayArtwork.firestoreId)

//                viewModel.handleUserFavorites(userId = savedUserId, artworkId = todayArtwork.id)
            }
        }
    }

    private fun setupShareButton() {
        binding.shareActionButton.setOnClickListener {
            uiHelper.shareArtworkDeepLink("https://farkhat.myrzabekov.shabyttan/artwork/${todayArtwork.firestoreId}")
        }
    }

    private fun removePlaceholders() {
        with(binding) {
            artTitle.setBackgroundColor(Color.TRANSPARENT)
            artAuthor.setBackgroundColor(Color.TRANSPARENT)
            artDescription.setBackgroundColor(Color.TRANSPARENT)

            val layoutParams = artDescription.layoutParams as LinearLayout.LayoutParams
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            artDescription.layoutParams = layoutParams
        }
    }

    override fun onDestroyView() {
        uiHelper.clearImage(binding.artImage)
        super.onDestroyView()
        _binding = null
    }
}
