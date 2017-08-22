package com.koresuniku.wishmaster.ui.thread_list

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ListView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.DataLoader
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import com.koresuniku.wishmaster.application.IntentUtils
import com.koresuniku.wishmaster.ui.UiVisibilityManager
import com.koresuniku.wishmaster.ui.controller.view_interface.*
import org.jetbrains.anko.find
import com.koresuniku.wishmaster.application.DeviceUtils
import com.koresuniku.wishmaster.application.settings.ResultCodes
import com.koresuniku.wishmaster.application.settings.SettingsActivity
import com.koresuniku.wishmaster.ui.controller.*
import com.koresuniku.wishmaster.ui.dialog.DialogManager
import com.koresuniku.wishmaster.ui.single_thread.SingleThreadActivity
import com.koresuniku.wishmaster.ui.thread_list.rv.ThreadListRecyclerViewAdapter
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import org.jetbrains.anko.doAsync


class ThreadListActivity : AppCompatActivity(), AppBarLayoutView, ActionBarView,
        LoadDataView, ThreadListListViewView, ProgressView, SwipyRefreshLayoutView,
        DialogManager.ThreadPostCallback, IActivityView {
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
    var mFullPostDialogManager: FullPostDialogManager? = null

    var mDataLoader: DataLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_list_drawer)

        boardId = intent.getStringExtra(IntentUtils.BOARD_ID_CODE)
        boardName = intent.getStringExtra(IntentUtils.BOARD_NAME_CODE)

        UiVisibilityManager.showSystemUI(this)

        mAppBarLayoutUnit = AppBarLayoutUnit(this)
        mActionBarUnit = ActionBarUnit(this, false, false)
        mProgressUnit = ProgressUnit(this)
        mThreadListListViewUnit = ThreadListListViewUnit(this)
        mSwipyRefreshLayoutUnit = SwipyRefreshLayoutUnit(this)
        mFullPostDialogManager = FullPostDialogManager(this)

        mDataLoader = DataLoader(this)
        mProgressUnit!!.showProgressYoba()
        loadData()

        //testRvAdapter()
    }

    fun testRvAdapter() {
        val rvAdapter = ThreadListRecyclerViewAdapter(this, boardId!!)
        rvAdapter.initAdapter()
    }

    override fun getGalleryLayoutContainer(): ViewGroup {
        return find(R.id.gallery_layout_container)
    }

    override fun getRefreshLayout(): SwipyRefreshLayout {
        return find(R.id.srl)
    }

    override fun getAppBarLayoutUnit(): AppBarLayoutUnit {
        return mAppBarLayoutUnit!!
    }

    override fun getListView(): ListView {
        return mThreadListListViewUnit!!.mListView!!
    }

    override fun getListViewAdapter(): INotifyableListViewAdapter {
        return mThreadListListViewUnit!!.mListViewAdapter!!
    }

    override fun onBackPressed() {
        if (mThreadListListViewUnit!!.adapterIsCreated()) {
            if (mThreadListListViewUnit!!.mListViewAdapter!!.onBackPressedOverridden()) return
        }

        super.onBackPressed()
    }

    override fun onBackPressedOverridden(): Boolean {
        return false
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration?) {
        super.onConfigurationChanged(newConfig)
        mActionBarUnit!!.onConfigurationChanged(newConfig!!)
        if (mThreadListListViewUnit!!.adapterIsCreated())
            mThreadListListViewUnit!!.mListViewAdapter!!.onConfigurationChanged(newConfig)
    }

    override fun loadData() {
        doAsync {  mDataLoader!!.loadData(boardId!!) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.thread_list_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_refresh -> {
                mSwipyRefreshLayoutUnit!!.setRefreshing(true)
                loadData(); Log.d(LOG_TAG, "action_refresh:")
            }
            R.id.action_settings -> {
                val intent: Intent = Intent(getActivity(), SettingsActivity::class.java)
                startActivityForResult(intent, ResultCodes.THREAD_LIST_RESULT_CODE)
            }
            android.R.id.home -> onBackPressed()
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ResultCodes.THREAD_LIST_RESULT_CODE) {
            mThreadListListViewUnit!!.notifyItemCommentTextViewChanged()
            mThreadListListViewUnit!!.notifyItemImageSizeChanged()
        }
    }

    override fun getSwipyRefreshLayoutUnit(): SwipyRefreshLayoutUnit {
        return mSwipyRefreshLayoutUnit!!
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
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "/$boardId/ - $boardName"
    }

    override fun onDataLoaded(schema: List<IBaseJsonSchema>) {
        this.mSchema = schema[0] as ThreadListJsonSchema

        mProgressUnit!!.hideProgressYoba()

        Log.d(LOG_TAG, "mSchema count: " + mSchema!!.getThreads().size)

        if (!mThreadListListViewUnit!!.adapterIsCreated()) mThreadListListViewUnit!!.createListViewAdapter()
        else mSwipyRefreshLayoutUnit!!.onDataLoadedReturnToTop()
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

    override fun getViewPager(): ViewPager {
        return find(R.id.gallery_pager)
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
                return DialogManager.createThreadPostDialog(this, args)
            }

        }
        return AlertDialog.Builder(this).create()
    }

    override fun openFullPost(position: Int) {
        mFullPostDialogManager!!.openFullPost(position)
    }

    override fun notifyGalleryShown() {
        mFullPostDialogManager!!.dismissFullPostDialog()
    }

    override fun notifyGalleryHidden() {
        mFullPostDialogManager!!.showFullPostDialog()
    }

    override fun openThread(threadNumber: String) {
        val intent = Intent(this, SingleThreadActivity::class.java)

        intent.putExtra(IntentUtils.BOARD_ID_CODE, boardId)
        intent.putExtra(IntentUtils.BOARD_NAME_CODE, boardName)
        intent.putExtra(IntentUtils.THREAD_NUMBER_CODE, threadNumber)

        if (DeviceUtils.sdkIsLollipopOrHigher()) {
            this.startActivity(intent)
            //this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        } else this.startActivity(intent)
    }
}