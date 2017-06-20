package com.koresuniku.wishmaster.ui.dashboard

import android.app.Activity
import android.content.ContentValues
import android.content.res.Configuration
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.widget.FrameLayout

import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.database.DatabaseContract
import com.koresuniku.wishmaster.http.DataLoader
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.boards_api.BoardsJsonSchema
import com.koresuniku.wishmaster.system.PreferencesManager
import com.koresuniku.wishmaster.ui.controller.ActionBarUnit
import com.koresuniku.wishmaster.ui.view.ActionBarView
import com.koresuniku.wishmaster.ui.view.ExpandableListViewView
import com.koresuniku.wishmaster.ui.view.LoadDataView

class DashboardActivity : AppCompatActivity(), ActionBarView, ExpandableListViewView, LoadDataView {
    val LOG_TAG: String = DashboardActivity::class.java.simpleName

    var mBoardListFragment: BoardListFragment? = null

    var mActionBarUnit: ActionBarUnit? = null
    var mDataLoader: DataLoader? = null
    var mPreferencesManager: PreferencesManager? = null

    var mSchema: BoardsJsonSchema? = null

    var mBoardsProjection = arrayOf(
            DatabaseContract.BoardsEntry.COLUMN_BOARD_ID,
            DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME,
            DatabaseContract.BoardsEntry.COLUMN_BOARD_PREFERRED
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        mPreferencesManager = PreferencesManager
        mDataLoader = DataLoader(this)

        mBoardListFragment = BoardListFragment(this)
        mActionBarUnit = ActionBarUnit(this)


        initBoardList()
    }

    fun initBoardList() {
        val cursor: Cursor = contentResolver.query(DatabaseContract.BoardsEntry.CONTENT_URI, mBoardsProjection, null, null, null)
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

        val cursor: Cursor = contentResolver.query(DatabaseContract.BoardsEntry.CONTENT_URI, mBoardsProjection, null, null, null)

        if (cursor.count == 0) {
            Log.d(LOG_TAG, "there are no rows in boards table, loading new boards...")
            var values: ContentValues = ContentValues()

            for (board in mSchema!!.adults!!) {
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, R.string.adult_boards_en)
                contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
                values = ContentValues()
            }

            for (board in mSchema!!.creativity!!) {
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, R.string.creativity_boards_en)
                contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
                values = ContentValues()
            }

            for (board in mSchema!!.games!!) {
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, R.string.games_boards_en)
                contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
                values = ContentValues()
            }

            for (board in mSchema!!.japanese!!) {
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, R.string.japanese_boards_en)
                contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
                values = ContentValues()
            }

            for (board in mSchema!!.other!!) {
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, R.string.different_boards_en)
                contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
                values = ContentValues()
            }

            for (board in mSchema!!.politics!!) {
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, R.string.politics_boards_en)
                contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
                values = ContentValues()
            }

            for (board in mSchema!!.subject!!) {
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, R.string.subject_boards_en)
                contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
                values = ContentValues()
            }

            for (board in mSchema!!.tech!!) {
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, R.string.tech_boards_en)
                contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
                values = ContentValues()
            }

            for (board in mSchema!!.users!!) {
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_ID, board.id)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_NAME, board.name)
                values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_CATEGORY, R.string.users_boards_en)
                contentResolver.insert(DatabaseContract.BoardsEntry.CONTENT_URI, values)
                values = ContentValues()
            }

            this.mBoardListFragment!!.onDataLoaded()
            cursor.close()
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
            Log.d(LOG_TAG, "new boards came! " + cursor.count + " " + totalBoardsCount)

        } else {
            Log.d(LOG_TAG, "no new boards")
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
        return FavouritesFragment()
    }

    override fun getHistoryFragment(): HistoryFragment {
        return HistoryFragment()
    }

    override fun getPreferencesManager(): PreferencesManager {
        return this.mPreferencesManager!!
    }
}
