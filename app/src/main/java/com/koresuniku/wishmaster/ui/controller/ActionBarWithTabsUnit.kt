package com.koresuniku.wishmaster.ui.controller

import android.content.res.Configuration
import android.preference.PreferenceManager
import android.support.design.widget.TabLayout
import android.support.v4.app.*
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.FrameLayout
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.system.DeviceUtils
import com.koresuniku.wishmaster.ui.dashboard.HistoryFragment
import com.koresuniku.wishmaster.ui.controller.view_interface.ActionBarView
import com.koresuniku.wishmaster.ui.controller.view_interface.ActionBarWithTabsView

class ActionBarWithTabsUnit(val mView: ActionBarWithTabsView) : ActionBarView {
    val LOG_TAG: String = ActionBarWithTabsUnit::class.java.simpleName

    var mActionBarUnit: ActionBarUnit? = null

    var mTabLayout: TabLayout? = null
    var mViewPager: ViewPager? = null
    var mViewPagerAdapter: PagerAdapter? = null

    var mTabPosition: Int = -1

    init {
        mActionBarUnit = ActionBarUnit(this, false)
        setupActionBar(mView.getAppCompatActivity().resources.configuration)
        if (mView.addTabs()) addTabs()
    }

    fun setupActionBar(configuration: Configuration) {
        mActionBarUnit!!.setupActionBar(configuration)
    }

    fun addTabs() {
        setupViewPager()
        setupTabsIcons()

        mTabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d(LOG_TAG, "tab: " + tab.position)
                mTabPosition = tab.position
                if (mView.getMenu() != null) setupMenuItems(mTabPosition)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        if (mTabPosition == -1) {
            mTabPosition = mView.getPreferencesManager().getSharedPreferences(
                    mView.getAppCompatActivity()).getString(
                    mView.getAppCompatActivity().getString(R.string.pref_dashboard_tab_position_key),
                    mView.getAppCompatActivity().getString(R.string.pref_dashboard_tab_position_board_list_default)).toInt()
        }

        mTabLayout!!.getTabAt(mTabPosition)!!.select()
    }

    fun setTabPosition() {
        val pos = PreferenceManager.getDefaultSharedPreferences(mView.getAppCompatActivity()).getString(
                mView.getAppCompatActivity().getString(R.string.pref_dashboard_tab_position_key),
                mView.getAppCompatActivity().getString(R.string.pref_dashboard_tab_position_board_list_default))
        mTabPosition = pos.toInt()
        Log.d(LOG_TAG, "tab position: " + pos)
        mTabLayout!!.getTabAt(mTabPosition )!!.select()
    }

    fun setupTabsIcons() {
        mTabLayout = mActionBarUnit!!.mLocalToolbarContainer!!.findViewById(R.id.tab_layout) as TabLayout
        mActionBarUnit!!.showTabLayout()
        mTabLayout!!.setupWithViewPager(mView.getViewPager())

        if (DeviceUtils.sdkIsLollipopOrHigher()) {
            mTabLayout!!.getTabAt(0)!!.icon =
                    mView.getAppCompatActivity().resources.getDrawable(R.drawable.ic_favorite_black)
            mTabLayout!!.getTabAt(1)!!.icon =
                    mView.getAppCompatActivity().resources.getDrawable(R.drawable.ic_list_black)
            mTabLayout!!.getTabAt(2)!!.icon =
                    mView.getAppCompatActivity().resources.getDrawable(R.drawable.ic_history_black)
        }
        mTabLayout!!.offsetLeftAndRight(1)
    }

    fun setupViewPager() {
        mViewPager = mView.getViewPager()
        mViewPagerAdapter = PagerAdapter(mView.getAppCompatActivity().supportFragmentManager)
        mViewPagerAdapter!!.addFragment(mView.getFavouritesFragment())
        mViewPagerAdapter!!.addFragment(mView.getBoardListFragment())
        mViewPagerAdapter!!.addFragment(HistoryFragment())
        mViewPager!!.adapter = mViewPagerAdapter

    }

    fun setupMenuItems(tabPosition: Int) {
        when (tabPosition) {
            0 -> {
                mView.getMenu()!!.findItem(R.id.action_refresh_boards).isVisible = false
            }
            1 -> {
                mView.getMenu()!!.findItem(R.id.action_refresh_boards).isVisible = true
            }
            2 -> {
                mView.getMenu()!!.findItem(R.id.action_refresh_boards).isVisible = false
            }
        }
    }

    fun getTabPosition(): Int {
        return mTabPosition
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

    override fun getToolbarContainer(): FrameLayout {
        return mView.getToolbarContainer()
    }

    override fun getAppCompatActivity(): AppCompatActivity {
        return mView.getAppCompatActivity()
    }

    override fun setupActionBarTitle() {
        mView.setupActionBarTitle()
    }
}