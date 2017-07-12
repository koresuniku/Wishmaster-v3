package com.koresuniku.wishmaster.ui.thread_list

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ListView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.DataLoader
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import com.koresuniku.wishmaster.system.IntentUtils
import com.koresuniku.wishmaster.ui.UIVisibilityManager
import com.koresuniku.wishmaster.ui.view.*
import org.jetbrains.anko.find
import com.koresuniku.wishmaster.system.DeviceUtils
import android.view.ViewGroup.LayoutParams.FILL_PARENT
import android.widget.ScrollView
import com.koresuniku.wishmaster.ui.controller.*
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import org.jetbrains.anko.doAsync


class ThreadListActivity : AppCompatActivity(), AppBarLayoutView, ActionBarView,
        LoadDataView, ThreadListListViewView, ProgressView,
        DialogManager.PostCallBack, IActivityView, SwipyRefreshLayoutView {
    val LOG_TAG: String = ThreadListActivity::class.java.simpleName

    val DIALOG_POST_ID = 0
    val DIALOG_FULL_POST_ID = 1

    private var boardId: String? = null
    var boardName: String? = null
    var mSchema: ThreadListJsonSchema? = null

    var mAppBarLayoutUnit: AppBarLayoutUnit? = null
    var mActionBarUnit: ActionBarUnit? = null
    var mProgressUnit: ProgressUnit? = null
    var mThreadListListViewUnit: ThreadListListViewUnit? = null
    var mSwipyRefreshLayoutUnit: SwipyRefreshLayoutUnit? = null
    var mDataLoader: DataLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_list_drawer)

        boardId = intent.getStringExtra(IntentUtils.BOARD_ID_CODE)
        boardName = intent.getStringExtra(IntentUtils.BOARD_NAME_CODE)

        UIVisibilityManager.showSystemUI(this)

        mAppBarLayoutUnit = AppBarLayoutUnit(this)
        mActionBarUnit = ActionBarUnit(this, false)
        mProgressUnit = ProgressUnit(this)
        mThreadListListViewUnit = ThreadListListViewUnit(this)
        mSwipyRefreshLayoutUnit = SwipyRefreshLayoutUnit(this)
        mDataLoader = DataLoader(this)

        mProgressUnit!!.showProgressYoba()
        mDataLoader!!.loadData(boardId!!)
    }


    override fun onConfigurationChanged(newConfig: android.content.res.Configuration?) {
        super.onConfigurationChanged(newConfig)
        mActionBarUnit!!.onConfigurationChanged(newConfig!!)
    }

    override fun loadData() {
        doAsync { mDataLoader!!.loadData(boardId!!) }
    }

    override fun getListViewAdapter(): INotifyableLisViewAdapter {
        return mThreadListListViewUnit!!.mListViewAdapter!!
    }

    override fun getAppBarLayout(): AppBarLayout {
        return findViewById(R.id.app_bar_layout) as AppBarLayout
    }

    override fun getAppBarLayoutUnit(): AppBarLayoutUnit {
        return mAppBarLayoutUnit!!
    }

    override fun getSwipyRefreshLayoutUnit(): SwipyRefreshLayoutUnit {
        return mSwipyRefreshLayoutUnit!!
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

        if (!mThreadListListViewUnit!!.adapterIsCreated()) {
            mThreadListListViewUnit!!.createListViewAdapter()
        } else mSwipyRefreshLayoutUnit!!.onDataLoaded()

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

    override fun showPostDialog(position: Int) {
        val args: Bundle = Bundle()
        args.putInt(DialogManager.DIALOG_THREAD_ITEM_POSITION_KEY, position)
        removeDialog(DIALOG_POST_ID)
        showDialog(DIALOG_POST_ID, args)
    }

    override fun onCreateDialog(id: Int, args: Bundle?): Dialog {
        when (id) {
            DIALOG_POST_ID -> {
                return DialogManager.createPostDialog(this, args)
            }

        }
        return AlertDialog.Builder(this).create()
    }

    override fun openFullPost(position: Int) {
        val args: Bundle = Bundle()
        args.putInt(DialogManager.DIALOG_THREAD_ITEM_POSITION_KEY, position)
        removeDialog(DIALOG_FULL_POST_ID)

        val dialog: Dialog = Dialog(this)
        val scrollView = ScrollView(this)
        mThreadListListViewUnit!!.mListViewAdapter!!.viewModeIsDialog = true
        val itemView: View = mThreadListListViewUnit!!.mListViewAdapter!!.getView(
                args.getInt(DialogManager.DIALOG_THREAD_ITEM_POSITION_KEY),
                null, scrollView)
        scrollView.addView(itemView)
        dialog.setContentView(scrollView)
        dialog.window.attributes.width = WindowManager.LayoutParams.MATCH_PARENT

        dialog.setOnDismissListener {
            Log.d(LOG_TAG, "on dismiss")
            mThreadListListViewUnit!!.mListViewAdapter!!.viewModeIsDialog = false
        }

        dialog.show()

    }

    override fun getRefreshLayout(): SwipyRefreshLayout {
        return find(R.id.srl)
    }

    override fun getListView(): ListView {
        return mThreadListListViewUnit!!.mListView!!
    }
}