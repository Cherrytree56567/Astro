package app.ct5.astrocam

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    // total number of pages
    override fun getItemCount(): Int = 2

    // return the Fragment associated with a given position
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PhotoFragment()
            else -> AstroPhotographyFragment()
        }
    }

    private fun PhotoFragment(): Fragment {
        TODO("Not yet implemented")
    }

    private fun AstroPhotographyFragment(): Fragment {
        TODO("Not yet implemented")
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.TabLayout)
        viewPager = findViewById(R.id.ViewPager2)


        val adapter = MyPagerAdapter(this)
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> Fragment()
                    1 -> Fragment()
                    else -> throw IllegalArgumentException("Invalid position")
                }
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Set the tab text based on the position
            tab.text = when (position) {
                0 -> "Photo"  // Photo Tab
                1 -> "AstroPhotography"  // Video Tab
                else -> throw IllegalArgumentException("Invalid position")
            }
        }.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.getTabAt(position)?.select()

                tabLayout.post {
                    val tabStrip = tabLayout.getChildAt(0) as? ViewGroup
                    val selectedTabView = tabStrip?.getChildAt(position)
                    selectedTabView?.let { tabView ->
                        val scrollPos = tabView.left - (tabLayout.width - tabView.width) / 2
                        tabLayout.smoothScrollTo(scrollPos, 0)
                    }
                }
            }
        })
    }

    // Function to add a single tab at a time with just one argument (tab name)
    private fun addTab(tabName: String) {
        tabLayout.addTab(tabLayout.newTab().setText(tabName))
    }
}