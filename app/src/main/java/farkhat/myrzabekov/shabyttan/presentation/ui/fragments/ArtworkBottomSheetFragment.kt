package farkhat.myrzabekov.shabyttan.presentation.ui.fragments


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.data.remote.model.Event
import farkhat.myrzabekov.shabyttan.databinding.DialogFullScreenImageBinding
import farkhat.myrzabekov.shabyttan.databinding.FragmentArtworkBottomSheetBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.UIHelper
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel

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
        setupPreferences()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expandBottomSheetDialog()

        arguments?.getLong("artworkId")?.let { viewModel.getArtworkById(it) }
        arguments?.getString("artworkIdFirestore")?.let {
            if(it.startsWith("-")) {
                val firestore = FirebaseFirestore.getInstance()
                val eventsCollection = firestore.collection("events")
                eventsCollection.document(it.substring(1))
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val event = documentSnapshot.toObject(Event::class.java)
                            with(binding) {
                                if (event != null) {
                                    artTitle.text = event.title
                                    uiHelper.loadImage(artImage, event.imageUrl)

//                                    artAuthor.text = authorToShow.orEmpty()
                                    artDescription.text = event.description
//                                    artFunFact.text = didYouKnowToShow.orEmpty()
                                }

                                likeActionButton.setOnClickListener {
                                    if (!viewModel.isUserAuth()) {
                                        findNavController().navigate(R.id.action_homeFragment_to_signUpFragment)
                                    } else {
                                        if (event != null) {
                                            viewModel.addToSavedEvents(documentSnapshot.id)
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
            else
            viewModel.getArtworkByIdFirestore(it)
        }




        viewModel.artworkByIdLiveData.observe(viewLifecycleOwner) { artwork ->
            artwork?.let {
                todayArtwork = it
                updateUI()
            }
        }

        viewModel.artworkLiveData.observe(viewLifecycleOwner) { artwork ->
            artwork?.let {
                todayArtwork = it
                updateUI()
            }
        }
    }

    private fun expandBottomSheetDialog() {
        (dialog as? BottomSheetDialog)?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.let { bottomSheet ->
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            }
    }

    private fun updateUI() {

        val titleToShow =
            if (savedLanguage == "ru") todayArtwork?.titleRu else todayArtwork?.title
        val authorToShow =
            if (savedLanguage == "ru") todayArtwork?.creatorRu else todayArtwork?.creator
        val descriptionToShow =
            if (savedLanguage == "ru") todayArtwork?.descriptionRu else todayArtwork?.description
        val didYouKnowToShow =
            if (savedLanguage == "ru") todayArtwork?.didYouKnowRu else todayArtwork?.didYouKnow

        with(binding) {
            uiHelper.loadImage(artImage, todayArtwork?.imageUrl)
            artAuthor.text = authorToShow.orEmpty()
            artTitle.text = titleToShow.orEmpty()
            artDescription.text = descriptionToShow.orEmpty()
            artFunFact.text = didYouKnowToShow.orEmpty()

            uiHelper.setupAppBarListener(appbar, artImage, shareActionButton, likeActionButton)
            uiHelper.setupImageClickToShowDialog(artImage) {
                DialogFullScreenImageBinding.inflate(layoutInflater)
            }
            uiHelper.setupScrollViewListener(nestedScrollView, toTopActionButton)
            uiHelper.setupToTopButton(toTopActionButton, nestedScrollView, appbar)
            updateLikeButton()
            setupLikeButton()
            setupShareButton()
        }
    }

    private fun updateLikeButton() {
        todayArtwork?.let { viewModel.checkArtworkInFavorites(it.firestoreId) }
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
                todayArtwork?.let { it1 -> viewModel.toggleFavorite(it1.firestoreId) }
            }
        }
    }

    private fun setupShareButton() {
        binding.shareActionButton.setOnClickListener {
            uiHelper.shareArtworkDeepLink("https://farkhat.myrzabekov.shabyttan/artwork/${todayArtwork?.firestoreId}")
        }
    }

    private fun setupPreferences() {

        viewModel.getUserLanguage()
        viewModel.languageStateFlow.asLiveData().observe(viewLifecycleOwner) { language ->
            savedLanguage = language
        }

        viewModel.getUserId()
        viewModel.userIdLiveData.observe(viewLifecycleOwner) { userId ->
            if (userId != null) {
                savedUserId = userId
            }
        }

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

        fun newInstance(artworkId: String?) = ArtworkBottomSheetFragment().apply {
            arguments = Bundle().apply {
                if (artworkId != null) {
                    putString("artworkIdFirestore", artworkId)
                }
            }
        }


    }
}