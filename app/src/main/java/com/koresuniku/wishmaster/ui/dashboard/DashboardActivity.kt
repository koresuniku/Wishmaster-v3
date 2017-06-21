package com.koresuniku.wishmaster.ui.dashboard

import android.app.Activity
import android.content.res.Configuration
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout

import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.database.BoardsUtils
import com.koresuniku.wishmaster.database.DatabaseContract
import com.koresuniku.wishmaster.http.DataLoader
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.boards_api.BoardsJsonSchema
import com.koresuniku.wishmaster.http.boards_api.model.Tech
import com.koresuniku.wishmaster.system.PreferencesManager
import com.koresuniku.wishmaster.ui.controller.ActionBarUnit
import com.koresuniku.wishmaster.ui.view.ActionBarView
import com.koresuniku.wishmaster.ui.view.LoadDataView
import java.util.ArrayList

class DashboardActivity : AppCompatActivity(), ActionBarView, ExpandableListViewView, LoadDataView, FavouritesFragmentView {
    val LOG_TAG: String = DashboardActivity::class.java.simpleName

    var mBoardListFragment: BoardListFragment? = null
    var mFavouritesFragment: FavouritesFragment? = null

    var mActionBarUnit: ActionBarUnit? = null
    var mDataLoader: DataLoader? = null
    var mPreferencesManager: PreferencesManager? = null

    var mMenu: Menu? = null

    var mSchema: BoardsJsonSchema? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        mPreferencesManager = PreferencesManager
        mDataLoader = DataLoader(this)

        mBoardListFragment = BoardListFragment(this)
        mFavouritesFragment = FavouritesFragment(this)
        mActionBarUnit = ActionBarUnit(this)

        initBoardList()
    }

    fun initBoardList() {
        val cursor: Cursor = contentResolver.query(DatabaseContract.BoardsEntry.CONTENT_URI,
                BoardsUtils.mBoardsProjection, null, null, null)
        if (cursor.count == 0) {
            Log.d(LOG_TAG, "there are no rows in boards table, loading new boards...")
            mDataLoader!!.loadData()
        } else {
            this.mBoardListFragment!!.onDataLoaded()
        }
        cursor.close()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mActionBarUnit!!.onConfigurationChanged(newConfig!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        mMenu = menu

        when (mActionBarUnit!!.getTabPosition()) {
            0 -> {
                mMenu!!.findItem(R.id.action_add_board).isVisible = true
                mMenu!!.findItem(R.id.action_refresh_boards).isVisible = false
            }
            1 -> {
                mMenu!!.findItem(R.id.action_add_board).isVisible = false
                mMenu!!.findItem(R.id.action_refresh_boards).isVisible = true
            }
            2 -> {
                mMenu!!.findItem(R.id.action_add_board).isVisible = false
                mMenu!!.findItem(R.id.action_refresh_boards).isVisible = false
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_refresh_boards -> {
                mDataLoader!!.loadData()
            }
        }

        return true
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

        val cursor: Cursor = contentResolver.query(DatabaseContract.BoardsEntry.CONTENT_URI,
                BoardsUtils.mBoardsProjection, null, null, null)

        if (cursor.count == 0) {
            Log.d(LOG_TAG, "there are no rows in boards table, loading new boards...")
            BoardsUtils.writeInAllTheBoardsIntoDatabase(this.mSchema, this)
            cursor.close()
            this.mBoardListFragment!!.onDataLoaded()
            return
        }

        var totalBoardsCount: Int = 0
        totalBoardsCount += mSchema!!.adults!!.size
        totalBoardsCount += mSchema!!.creativity!!.size
        totalBoardsCount += mSchema!!.games!!.size
        totalBoardsCount += mSchema!!.japanese!!.size
        totalBoardsCount += mSchema!!.other!!.size
        totalBoardsCount += mSchema!!.politics!!.size
        totalBoardsCount += mSchema!!.subject!!.size
        totalBoardsCount += mSchema!!.tech!!.size
        totalBoardsCount += mSchema!!.users!!.size

        if (cursor.count != totalBoardsCount){
            Log.d(LOG_TAG, "boards count changed! " + cursor.count + " " + totalBoardsCount)
            if (cursor.count < totalBoardsCount) {
                Log.d(LOG_TAG, "new boards came!")
                BoardsUtils.insertNewBoards(this.mSchema, this)
            } else {
                Log.d(LOG_TAG, "some boards deleted")
                BoardsUtils.deleteOldBoards(this.mSchema, this)
            }
        } else {
            Log.d(LOG_TAG, "no new boards: cursor " + cursor.count + ", schema " + totalBoardsCount)
        }
        cursor.close()

        this.mBoardListFragment!!.onDataLoaded()
    }

    override fun showProgressBar() {

    }

    override fun getBoardListFragment(): BoardListFragment {
        return this.mBoardListFragment!!
    }

    override fun getFavouritesFragment(): FavouritesFragment {
        return this.mFavouritesFragment!!
    }

    override fun getHistoryFragment(): HistoryFragment {
        return HistoryFragment()
    }

    override fun getPreferencesManager(): PreferencesManager {
        return this.mPreferencesManager!!
    }

    override fun getMenu(): Menu? {
        return this.mMenu
    }
}
