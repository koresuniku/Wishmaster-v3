package com.koresuniku.wishmaster.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.widget.ExpandableListView
import android.widget.FrameLayout

import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.DataLoader
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.boards_api.model.BoardsJsonSchema
import com.koresuniku.wishmaster.system.PreferencesManager
import com.koresuniku.wishmaster.ui.controller.ActionBarUnit
import com.koresuniku.wishmaster.ui.view.ActionBarView
import com.koresuniku.wishmaster.ui.view.ExpandableListViewView
import com.koresuniku.wishmaster.ui.view.LoadDataView

class DashboardActivity : AppCompatActivity(), ActionBarView, ExpandableListViewView, LoadDataView {

    var mBoardListFragment: BoardListFragment? = null

    var mActionBarUnit: ActionBarUnit? = null
    var mDataLoader: DataLoader? = null
    var mPreferencesManager: PreferencesManager? = null

    var mSchema: BoardsJsonSchema? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        mPreferencesManager = PreferencesManager
        mDataLoader = DataLoader(this)

        mBoardListFragment = BoardListFragment(this)
        mActionBarUnit = ActionBarUnit(this)

        mDataLoader!!.loadData()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mActionBarUnit!!.onConfigurationChanged(newConfig!!)
    }

    override fun getAppCompatActivity(): AppCompatActivity {
        return this
    }

    override fun getToolbarContainer(): FrameLayout {
        return findViewById(R.id.toolbar_container) as FrameLayout
    }

    override fun setupActionBarTitle() {

    }

    override fun addTabs(): Boolean {
        return true
    }

    override fun getViewPager(): ViewPager {
        return findViewById(R.id.dashboard_viewpager) as ViewPager
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun getSchema(): BoardsJsonSchema {
        return this.mSchema!!
    }

    override fun onBoardsSelected(boardId: String, boardName: String) {
//        val intent = Intent(this, ThreadListActivity::class.java)
//
//        intent.putExtra(Dvach.BOARD_ID, boardId)
//        intent.putExtra(Dvach.BOARD_NAME, boardName)
//
//        if (DeviceUtils.sdkIsLollipopOrHigher()) {
//            mActivity.startActivity(intent)
//            mActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
//        } else mActivity.startActivity(intent)
    }

    override fun onDataLoaded(schema: List<IBaseJsonSchema>) {
        this.mSchema = schema[0] as BoardsJsonSchema
        this.mBoardListFragment!!.onDataLoaded()
    }

    override fun showProgressBar() {

    }


    override fun getBoardListFragment(): BoardListFragment {
        return this.mBoardListFragment!!
    }

    override fun getFavouritesFragment(): FavouritesFragment {
        return FavouritesFragment()
    }

    override fun getHistoryFragment(): HistoryFragment {
        return HistoryFragment()
    }

    override fun getPreferencesManager(): PreferencesManager {
        return this.mPreferencesManager!!
    }
}
