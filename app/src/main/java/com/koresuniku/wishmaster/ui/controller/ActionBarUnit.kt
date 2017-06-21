package com.koresuniku.wishmaster.ui.controller

import android.content.res.Configuration
import android.support.design.widget.TabLayout
import android.support.v4.app.*
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.system.PreferencesManager
import com.koresuniku.wishmaster.ui.dashboard.FavouritesFragment
import com.koresuniku.wishmaster.ui.dashboard.HistoryFragment
import com.koresuniku.wishmaster.ui.view.ActionBarView

class ActionBarUnit(val mView: ActionBarView) {
    val LOG_TAG: String = ActionBarUnit::class.java.simpleName

    val mActivityToolbarContainer: FrameLayout = mView.getToolbarContainer()

    var mLocalToolbarContainer: RelativeLayout? = null
    var mToolbar: Toolbar? = null
    var mTabLayout: TabLayout? = null
    var mViewPager: ViewPager? = null
    var mViewPagerAdapter: PagerAdapter? = null

    var mTabPosition: Int = -1

    init {
        setupActionBar(mView.getAppCompatActivity().resources.configuration)
        if (mView.addTabs()) addTabs()
    }

    fun setupActionBar(configuration: Configuration) {
        mActivityToolbarContainer.removeAllViews()

        mLocalToolbarContainer = mView.getAppCompatActivity().layoutInflater.inflate(R.layout.action_bar_layout, null, false) as RelativeLayout
        mToolbar = mLocalToolbarContainer!!.findViewById(R.id.toolbar) as Toolbar?
        val height: Int
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            height = mView.getAppCompatActivity().resources.getDimension(R.dimen.action_bar_height_portrait).toInt()
        } else {
            height = mView.getAppCompatActivity().resources.getDimension(R.dimen.action_bar_height_landscape).toInt()
        }
        mToolbar!!.layoutParams.height = height
        mToolbar!!.layoutParams.width = Toolbar.LayoutParams.MATCH_PARENT

        mActivityToolbarContainer.addView(mLocalToolbarContainer)

        mView.getAppCompatActivity().setSupportActionBar(mToolbar)
        mView.setupActionBarTitle()

    }

    fun addTabs() {

        setupViewPager()
        setupTabsIcons()

        mTabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d(LOG_TAG, "tab: " + tab.position)
                mTabPosition = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        if (mTabPosition == -1) {
            mTabPosition = mView.getPreferencesManager().getSharedPreferences(
                    mView.getAppCompatActivity()).getInt(
                    PreferencesManager.DASHBOARD_TAB_POSITION_KEY,
                    PreferencesManager.DASHBOARD_TAB_POSITION_BOARD_LIST_DEFAULT)
        }

        mTabLayout!!.getTabAt(mTabPosition)!!.select()
    }

    fun setupTabsIcons() {
        mTabLayout = mLocalToolbarContainer!!.findViewById(R.id.tab_layout) as TabLayout
        mTabLayout!!.setupWithViewPager(mView.getViewPager())

        mTabLayout!!.getTabAt(0)!!.icon = mView.getAppCompatActivity().resources.getDrawable(R.drawable.ic_favorite_black)
        mTabLayout!!.getTabAt(1)!!.icon = mView.getAppCompatActivity().resources.getDrawable(R.drawable.ic_list_black)
        mTabLayout!!.getTabAt(2)!!.icon = mView.getAppCompatActivity().resources.getDrawable(R.drawable.ic_history_black)
        mTabLayout!!.offsetLeftAndRight(1)
    }

    fun setupViewPager() {
        mViewPager = mView.getViewPager()
        mViewPagerAdapter = PagerAdapter(mView.getAppCompatActivity().supportFragmentManager)
        mViewPagerAdapter!!.addFragment(FavouritesFragment(mView.getAppCompatActivity()))
        mViewPagerAdapter!!.addFragment(mView.getBoardListFragment())
        mViewPagerAdapter!!.addFragment(HistoryFragment())
        mViewPager!!.adapter = mViewPagerAdapter

    }

    class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        val mFragmentList = ArrayList<Fragment>()

        override fun getItem(position: Int): Fragment? {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return 3
        }

        fun addFragment(fragment: Fragment) {
            mFragmentList.add(fragment)
        }
    }


    fun onConfigurationChanged(configuration: Configuration) {
        setupActionBar(configuration)
        if (mView.addTabs()) setupTabsIcons()
    }
}