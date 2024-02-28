package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.EventAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.data.remote.model.Event
import farkhat.myrzabekov.shabyttan.databinding.FragmentEventsBinding

@AndroidEntryPoint
class EventsFragment : Fragment() {
    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = EventAdapter()

        val events = generateSampleEvents()

        binding.recyclerViewEvents.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = eventAdapter
        }

        eventAdapter.submitList(events)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generateSampleEvents(): List<Event> {
        val sampleEvents = mutableListOf<Event>()
        for (i in 1..4) {
            sampleEvents.add(
                Event(
                    "myId$i",
                    "The Inspiring story of Uemura Shoen",
                    "Louvre Museum, Paris",
                    "https://img.etimg.com/thumb/msid-103080803,width-650,height-488,imgsize-71246,resizemode-75/gallery-exhibition-art2_istock.jpg",
                    "Description for Event $i",
                    true
                )
            )
        }
        return sampleEvents
    }
}