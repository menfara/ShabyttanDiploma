package farkhat.myrzabekov.shabyttan.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.fragments.EventsFragment
import farkhat.myrzabekov.shabyttan.presentation.ui.fragments.ForYouFragment

class DiscoverPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ForYouFragment()
            1 -> EventsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    companion object {
        private const val NUM_PAGES = 2
    }
}