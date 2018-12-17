package com.example.kamillog.hoopstracker.adapters

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.kamillog.hoopstracker.fragments.FinishedGamesFragment
import com.example.kamillog.hoopstracker.fragments.UpcomingGamesFragment

/**
 * Created by Konrad on 18.03.2018.
 */
class MyFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): android.support.v4.app.Fragment? {
        return when (position) {
            0 -> FinishedGamesFragment.newInstance()
            1 -> UpcomingGamesFragment.newInstance()
            else -> null
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Finished games"
            1 -> "Upcoming games"
            else -> ""
        }
    }
}