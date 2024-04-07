package farkhat.myrzabekov.shabyttan.presentation.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import farkhat.myrzabekov.shabyttan.presentation.ui.fragments.EventsFragment
import farkhat.myrzabekov.shabyttan.presentation.ui.fragments.FavoritesFragment
import farkhat.myrzabekov.shabyttan.presentation.ui.fragments.SettingsFragment

class ProfilePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragmentList = mutableListOf<Fragment>()

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val bundle = Bundle().apply {
                    putBoolean("post", true)
                }
                val fragment = FavoritesFragment()
                fragment.arguments = bundle
                fragment
            }
            1 -> FavoritesFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }


}