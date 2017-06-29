package com.koresuniku.wishmaster.ui.thread_list

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ListView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.DataLoader
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import com.koresuniku.wishmaster.system.IntentUtils
import com.koresuniku.wishmaster.ui.UIVisibilityManager
import com.koresuniku.wishmaster.ui.controller.ActionBarUnit
import com.koresuniku.wishmaster.ui.controller.AppBarLayoutUnit
import com.koresuniku.wishmaster.ui.controller.ProgressUnit
import com.koresuniku.wishmaster.ui.view.ActionBarView
import com.koresuniku.wishmaster.ui.view.AppBarLayoutView
import com.koresuniku.wishmaster.ui.view.LoadDataView
import com.koresuniku.wishmaster.ui.view.ProgressView
import org.jetbrains.anko.find

class ThreadListActivity : AppCompatActivity(), AppBarLayoutView, ActionBarView,
        LoadDataView, ThreadListListViewView, ProgressView {
    val LOG_TAG: String = ThreadListActivity::class.java.simpleName

    private var boardId: String? = null
    var boardName: String? = null
    var mSchema: ThreadListJsonSchema? = null

    var mAppBarLayoutUnit: AppBarLayoutUnit? = null
    var mActionBarUnit: ActionBarUnit? = null
    var mProgressUnit: ProgressUnit? = null
    var mThreadListListViewUnit: ThreadListListViewUnit? = null
    var mDataLoader: DataLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_list_drawer)

        boardId = intent.getStringExtra(IntentUtils.BOARD_ID_CODE)
        boardName = intent.getStringExtra(IntentUtils.BOARD_NAME_CODE)

        UIVisibilityManager.showSystemUI(this)

        //mAppBarLayoutUnit = AppBarLayoutUnit(this)
        mActionBarUnit = ActionBarUnit(this, false)
        mProgressUnit = ProgressUnit(this)
        mThreadListListViewUnit = ThreadListListViewUnit(this)

        mDataLoader = DataLoader(this)

        mProgressUnit!!.showProgressYoba()
        mDataLoader!!.loadData(boardId!!)
    }


    override fun onConfigurationChanged(newConfig: android.content.res.Configuration?) {
        super.onConfigurationChanged(newConfig)
        mActionBarUnit!!.onConfigurationChanged(newConfig!!)
    }

    override fun getAppBarLayout(): AppBarLayout {
        return findViewById(R.id.app_bar_layout) as AppBarLayout
    }

    override fun getToolbarContainer(): FrameLayout {
        return findViewById(R.id.toolbar_container) as FrameLayout
    }

    override fun getAppCompatActivity(): AppCompatActivity {
        return this
    }

    override fun setupActionBarTitle() {

    }

    override fun onDataLoaded(schema: List<IBaseJsonSchema>) {
        this.mSchema = schema[0] as ThreadListJsonSchema

        mProgressUnit!!.hideProgressYoba()

        Log.d(LOG_TAG, "mSchema count: " + mSchema!!.getThreads().size)

        mThreadListListViewUnit!!.createListViewAdapter()
    }

    override fun getActivityOverridden(): Activity {
        return this
    }

    override fun showProgressBar() {

    }

    override fun getBoardId(): String? {
        return boardId
    }

    override fun getSchema(): ThreadListJsonSchema {
        return this.mSchema!!
    }

    override fun getThreadListView(): ListView {
        return findViewById(R.id.thread_list_list_view) as ListView
    }

    override fun getProgressContainer(): View {
        return find(R.id.progress_container)
    }

    override fun getActivity(): Activity {
        return this
    }
}