package com.codequark.yelp.ui.adapters.viewPager

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.codequark.yelp.ui.fragments.DetailFragment
import com.codequark.yelp.ui.fragments.LocationFragment
import com.codequark.yelp.ui.fragments.MapDetailFragment
import com.codequark.yelp.ui.fragments.ReviewFragment
import com.codequark.yelp.utils.Constants

class DetailPagerAdapter(
    @NonNull
    fragmentManager: FragmentManager,

    @NonNull
    lifecycle: Lifecycle,
): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            Constants.PAGE_DETAIL -> {
                DetailFragment()
            }

            Constants.PAGE_MAP -> {
                MapDetailFragment()
            }

            Constants.PAGE_LOCATION -> {
                LocationFragment()
            }

            Constants.PAGE_REVIEWS -> {
                ReviewFragment()
            }

            else -> {
                throw RuntimeException("GraphPagerAdapter Unknown Page: $position")
            }
        }
    }

    override fun getItemCount(): Int {
        return Constants.TOTAL_PAGES_DETAIL
    }
}