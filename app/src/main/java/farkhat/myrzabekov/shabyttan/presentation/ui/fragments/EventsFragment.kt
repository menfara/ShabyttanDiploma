package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.EventAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.data.remote.model.Event
import farkhat.myrzabekov.shabyttan.databinding.FragmentEventsBinding

@AndroidEntryPoint
class EventsFragment : Fragment() {
    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private var lastVisibleDocument: DocumentSnapshot? = null
    private var stop = false
    private var isLoading = false
    private var isAdmin = false
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
        }

        eventAdapter = EventAdapter()

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

        var query = eventsCollection
            .limit(10)

        if (isAdmin) query = query.whereEqualTo("creator", currentUser?.uid)


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
                    val isFree = document.getBoolean("isFree") ?: false

                    val event = Event(id, title, location, imageUrl, description, isFree)
                    eventsList.add(event)
                }
                callback(
                    eventsList,
                    documents.lastOrNull()
                )
            }
            .addOnFailureListener { exception ->
                Log.e("EventsFragment", "Error getting documents: ", exception)
                callback(emptyList(), null)
            }
    }

}