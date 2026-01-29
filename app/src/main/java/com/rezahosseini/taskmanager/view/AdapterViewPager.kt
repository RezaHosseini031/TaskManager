package com.rezahosseini.taskmanager.view

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.rezahosseini.taskmanager.R
import com.rezahosseini.taskmanager.view.menu.MenuFragment
import com.rezahosseini.taskmanager.view.timer.TimerFragment

class AdapterViewPager(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MenuFragment()
            else -> TimerFragment()

        }
    }

}