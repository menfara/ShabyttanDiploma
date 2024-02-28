package farkhat.myrzabekov.shabyttan.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.databinding.FragmentForYouBinding
import farkhat.myrzabekov.shabyttan.presentation.ui.adapter.DiscoverRecyclerViewAdapter

@AndroidEntryPoint
class ForYouFragment : Fragment() {
    private var _binding: FragmentForYouBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawableList1 = listOf(
            R.drawable.discover8,
            R.drawable.discover9,
            R.drawable.discover6,
            R.drawable.discover1,
            R.drawable.discover7,
            R.drawable.discover2,
            R.drawable.discover5,
            R.drawable.discover4,
            R.drawable.discover3,
        )

        val adapter1 = DiscoverRecyclerViewAdapter(drawableList1)


        binding.recyclerView1.layoutManager =
            StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        binding.recyclerView1.adapter = adapter1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}