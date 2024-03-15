package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.databinding.FragmentForYouBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.ForYouAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.OnArtworkClickListenerFirestore

@AndroidEntryPoint
class ForYouFragment : Fragment(), OnArtworkClickListenerFirestore {
    private var _binding: FragmentForYouBinding? = null
    private val binding get() = _binding!!
    private var lastVisibleDocument: DocumentSnapshot? = null
    private var stop = false
    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter1 = ForYouAdapter(emptyList(), this)
        binding.recyclerView1.layoutManager =
            StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        binding.recyclerView1.adapter = adapter1

        loadArtworkImages(adapter1)

        binding.recyclerView1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Пользователь пролистал список до конца
                    if (!isLoading) {
                        isLoading = true
                        loadArtworkImages(adapter1)
                    }
                }
            }
        })
    }

    private fun loadArtworkImages(adapter: ForYouAdapter) {
        getArtworkImagesFromFirestore(lastVisibleDocument) { imagesList, lastVisibleDoc ->
            adapter.addAll(imagesList)

            lastVisibleDocument = lastVisibleDoc
            isLoading = false
            if (lastVisibleDoc == null) stop = true
        }
    }

    private fun getArtworkImagesFromFirestore(
        lastVisibleDocument: DocumentSnapshot?,
        callback: (List<Pair<String, String>>, DocumentSnapshot?) -> Unit
    ) {
        if (stop) return
        val imagesList = mutableListOf<Pair<String, String>>()

        val db = FirebaseFirestore.getInstance()

        val imagesCollection = db.collection("artworks")

        var query = imagesCollection
            .orderBy("id")
            .limit(10)

        lastVisibleDocument?.let {
            query = query.startAfter(it)
        }

        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val imageUrl = document.getString("imageUrl")
                    val documentId = document.id
                    imageUrl?.let {
                        imagesList.add(Pair(it, documentId))
                    }
                }
                callback(
                    imagesList,
                    documents.lastOrNull()
                )
            }
            .addOnFailureListener { exception ->
                Log.e("ForYouFragment", "Error getting documents: ", exception)
                callback(emptyList(), null)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onArtworkClick(artworkId: String) {
        val bottomSheetFragment = ArtworkBottomSheetFragment.newInstance(artworkId)
        bottomSheetFragment.show(childFragmentManager, "ArtworkBottomSheetTag")
    }
}