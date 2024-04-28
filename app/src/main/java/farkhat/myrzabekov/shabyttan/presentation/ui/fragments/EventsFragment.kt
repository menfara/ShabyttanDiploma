package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.EventAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.data.remote.model.Event
import farkhat.myrzabekov.shabyttan.databinding.FragmentEventsBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.OnArtworkClickListener
import farkhat.myrzabekov.shabyttan.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class EventsFragment : Fragment(), OnArtworkClickListener {
    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private var lastVisibleDocument: DocumentSnapshot? = null
    private var stop = false
    private var isLoading = false
    private var isAdmin = false
    private var isSavedEvents = false

    private val viewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        if (args != null) {
            isAdmin = args.getBoolean("isAdmin", false)
            Log.d("EVENTS FRAGMENT >>> ", isAdmin.toString())

            isSavedEvents = args.getBoolean("isSavedEvents", false)
        }


        viewModel.fetchEventsIdList()

        eventAdapter = EventAdapter(this)

        binding.recyclerViewEvents.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = eventAdapter
        }

        loadEvents(eventAdapter)

        binding.recyclerViewEvents.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isLoading) {
                        isLoading = true
                        loadEvents(eventAdapter)
                    }
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadEvents(adapter: EventAdapter) {
        getEventsFromFirestore(lastVisibleDocument) { eventsList, lastVisibleDoc ->
            adapter.addAll(eventsList)

            lastVisibleDocument = lastVisibleDoc
            isLoading = false
            if (lastVisibleDoc == null) stop = true
        }
    }




    private fun getEventsFromFirestore(
        lastVisibleDocument: DocumentSnapshot?,
        callback: (List<Event>, DocumentSnapshot?) -> Unit
    ) {
        if (stop) return
        val eventsList = mutableListOf<Event>()

        val db = FirebaseFirestore.getInstance()
        val eventsCollection = db.collection("events")
        val currentUser = Firebase.auth.currentUser

        var query = eventsCollection.limit(10)

        if (isAdmin) {
            query = query.whereEqualTo("creator", currentUser?.uid)
        }

        if (isSavedEvents) {
            viewModel.eventsIdListLiveData.observe(viewLifecycleOwner) { eventsIds ->
                if (eventsIds.isNotEmpty()) {
                    query = eventsCollection.whereIn(FieldPath.documentId(), eventsIds).limit(10)
                } else {
                    callback(emptyList(), null)
                    return@observe
                }

                lastVisibleDocument?.let {
                    query = query.startAfter(it)
                }

                query.get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            Log.d("EventsFragment", document.id)
                            val id = document.id
                            val title = document.getString("title") ?: ""
                            val location = document.getString("location") ?: ""
                            val imageUrl = document.getString("imageUrl") ?: ""
                            val description = document.getString("description") ?: ""
                            val isFree = document.getBoolean("free") ?: false
                            val creator = document.getString("creator") ?: ""

                            val event = Event(id, title, location, imageUrl, description, isFree, creator)
                            eventsList.add(event)
                        }
                        callback(eventsList, documents.lastOrNull())
                    }
                    .addOnFailureListener { exception ->
                        Log.e("EventsFragment", "Error getting documents: ", exception)
                        callback(emptyList(), null)
                    }
            }
        } else {
            lastVisibleDocument?.let {
                query = query.startAfter(it)
            }

            query.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d("EventsFragment", document.id)
                        val id = document.id
                        val title = document.getString("title") ?: ""
                        val location = document.getString("location") ?: ""
                        val imageUrl = document.getString("imageUrl") ?: ""
                        val description = document.getString("description") ?: ""
                        val isFree = document.getBoolean("free") ?: false
                        val creator = document.getString("creator") ?: ""

                        val event = Event(id, title, location, imageUrl, description, isFree, creator)
                        eventsList.add(event)
                    }
                    callback(eventsList, documents.lastOrNull())
                }
                .addOnFailureListener { exception ->
                    Log.e("EventsFragment", "Error getting documents: ", exception)
                    callback(emptyList(), null)
                }
        }
    }


    override fun onArtworkClick(artworkId: String) {
        val bottomSheetFragment = ArtworkBottomSheetFragment.newInstance("-$artworkId")
        bottomSheetFragment.show(childFragmentManager, "ArtworkBottomSheetTag")
    }

}