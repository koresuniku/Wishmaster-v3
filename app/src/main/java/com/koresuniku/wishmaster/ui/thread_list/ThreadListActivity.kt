package com.koresuniku.wishmaster.ui.thread_list

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.FrameLayout
import android.widget.ListView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.DataLoader
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.boards_api.BoardsJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import com.koresuniku.wishmaster.system.IntentUtils
import com.koresuniku.wishmaster.ui.UIVisibilityManager
import com.koresuniku.wishmaster.ui.controller.ActionBarUnit
import com.koresuniku.wishmaster.ui.view.ActionBarView
import com.koresuniku.wishmaster.ui.view.LoadDataView

class ThreadListActivity : AppCompatActivity(), ActionBarView, LoadDataView {
    val LOG_TAG: String = ThreadListActivity::class.java.simpleName

    private var boardId: String? = null
    var boardName: String? = null
    var mSchema: ThreadListJsonSchema? = null

    var mActionBarUnit: ActionBarUnit? = null
    var mDataLoader: DataLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_list_drawer)

        boardId = intent.getStringExtra(IntentUtils.BOARD_ID_CODE)
        boardName = intent.getStringExtra(IntentUtils.BOARD_NAME_CODE)

        UIVisibilityManager.showSystemUI(this)

        mActionBarUnit = ActionBarUnit(this, false)

        mDataLoader = DataLoader(this)
        mDataLoader!!.loadData(boardId!!)

        //(findViewById(R.id.lv) as ListView).adapter = ThreadListListViewAdapterJava(this)
    }


    override fun onConfigurationChanged(newConfig: android.content.res.Configuration?) {
        super.onConfigurationChanged(newConfig)
        mActionBarUnit!!.onConfigurationChanged(newConfig!!)
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

        Log.d(LOG_TAG, "mSchema count: " + mSchema!!.getThreads().size)
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun showProgressBar() {

    }

    override fun getBoardId(): String? {
        return boardId
    }
}