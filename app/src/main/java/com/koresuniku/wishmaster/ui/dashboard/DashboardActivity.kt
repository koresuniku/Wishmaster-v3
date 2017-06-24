package com.koresuniku.wishmaster.ui.dashboard

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.database.Cursor
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout

import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.database.BoardsUtils
import com.koresuniku.wishmaster.database.DatabaseContract
import com.koresuniku.wishmaster.http.DataLoader
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.boards_api.BoardsJsonSchema
import com.koresuniku.wishmaster.system.PreferencesManager
import com.koresuniku.wishmaster.ui.controller.ActionBarUnit
import com.koresuniku.wishmaster.ui.controller.DialogManager
import com.koresuniku.wishmaster.ui.controller.DialogManager.DIALOG_BOARD_ID_KEY
import com.koresuniku.wishmaster.ui.controller.DialogManager.DIALOG_DASHBOARD_ID
import com.koresuniku.wishmaster.ui.controller.DialogManager.DIALOG_REMOVE_FROM_FAVOURITES_KEY
import com.koresuniku.wishmaster.ui.view.ActionBarView
import com.koresuniku.wishmaster.ui.view.LoadDataView
import android.view.animation.LinearInterpolator
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import com.koresuniku.wishmaster.ui.settings.SettingsActivity
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.startActivity


class DashboardActivity : AppCompatActivity(), ActionBarView, ExpandableListViewView,
        LoadDataView, FavouritesFragmentView, DialogManager.DashBoardActivityCallback {
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
        Log.d(LOG_TAG, "onCreate:")
        setContentView(R.layout.activity_dashboard_drawer)

        mPreferencesManager = PreferencesManager
        mDataLoader = DataLoader(this)

        mBoardListFragment = BoardListFragment(this)
        mFavouritesFragment = FavouritesFragment(this)
        mActionBarUnit = ActionBarUnit(this)

        initBoardList()


    }

    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "onStart:")
        mActionBarUnit!!.setTabPosition()
    }

    fun initBoardList() {
        val cursor: Cursor = contentResolver.query(DatabaseContract.BoardsEntry.CONTENT_URI,
                BoardsUtils.mBoardsProjection, null, null, null)
        if (cursor.count == 0) {
            Log.d(LOG_TAG, "there are no rows in boards table, loading boards for the first time...")
            showProgressBar()
            async { mDataLoader!!.loadData() }
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
                mMenu!!.findItem(R.id.action_refresh_boards).isVisible = false
            }
            1 -> {
                mMenu!!.findItem(R.id.action_refresh_boards).isVisible = true
            }
            2 -> {
                mMenu!!.findItem(R.id.action_refresh_boards).isVisible = false
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_refresh_boards -> {
                async { mDataLoader!!.loadData() }
            }
            R.id.action_settings -> {
                val intent: Intent = Intent(getActivity(), SettingsActivity::class.java)
                startActivity(intent)
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

    override fun onCreateDialog(id: Int, args: Bundle?): Dialog {
        return DialogManager.createDashBoardDialog(this, id, args)
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

        mBoardListFragment!!.hideProgressYoba()

        val cursor: Cursor = contentResolver.query(DatabaseContract.BoardsEntry.CONTENT_URI,
                BoardsUtils.mBoardsProjection, null, null, null)

        if (cursor.count == 0) {
            Log.d(LOG_TAG, "there are no rows in boards table, loading new boards...")
            async {BoardsUtils.writeInAllTheBoardsIntoDatabase(getSchema(), getActivity())}
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
        mBoardListFragment!!.showProgressYoba()

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

    override fun getContentContainer(): FrameLayout {
        return findViewById(R.id.dashboard_viewpager_container) as FrameLayout
    }

    override fun updateBoardListFragment() {
        this.mBoardListFragment!!.onDataLoaded()
    }

    override fun showChoiceDialog(removeFromFavourites: Boolean, boardId: String) {
        Log.d(LOG_TAG, "showChoiceDialog, received board: " + boardId)
        var bundle: Bundle = Bundle()
        bundle.putBoolean(DIALOG_REMOVE_FROM_FAVOURITES_KEY, removeFromFavourites)
        bundle.putString(DIALOG_BOARD_ID_KEY, boardId)
        removeDialog(DIALOG_DASHBOARD_ID)
        showDialog(DIALOG_DASHBOARD_ID, bundle)
    }

    override fun removeFromFavourites(boardId: String) {
        PreferencesManager.deleteFavouriteBoard(
                getActivity(), "/$boardId/")
        val values = ContentValues()
        values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_PREFERRED,
                DatabaseContract.BoardsEntry.BOARD_PREFERRED_FALSE)
        contentResolver.update(DatabaseContract.BoardsEntry.CONTENT_URI,
                values, DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " =? ",
                arrayOf<String>(boardId))
        mFavouritesFragment!!.onBoardRemoved(boardId)
    }

    override fun addNewFavouriteBoard(boardId: String) {
        PreferencesManager.addNewFavouriteBoard(
                getActivity(), "/$boardId/")
        val values = ContentValues()
        values.put(DatabaseContract.BoardsEntry.COLUMN_BOARD_PREFERRED,
                DatabaseContract.BoardsEntry.BOARD_PREFERRED_TRUE)
        contentResolver.update(DatabaseContract.BoardsEntry.CONTENT_URI,
                values, DatabaseContract.BoardsEntry.COLUMN_BOARD_ID + " =? ",
                arrayOf<String>(boardId))
        mFavouritesFragment!!.notifyOrInitBoardList()
    }
}
