package com.koresuniku.wishmaster.ui.single_thread

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ListView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.DataLoader
import com.koresuniku.wishmaster.http.IBaseJsonSchema
import com.koresuniku.wishmaster.http.IBaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import com.koresuniku.wishmaster.system.IntentUtils
import com.koresuniku.wishmaster.system.settings.ResultCodes
import com.koresuniku.wishmaster.system.settings.SettingsActivity
import com.koresuniku.wishmaster.ui.UIVisibilityManager
import com.koresuniku.wishmaster.ui.controller.ActionBarUnit
import com.koresuniku.wishmaster.ui.controller.AppBarLayoutUnit
import com.koresuniku.wishmaster.ui.controller.ProgressUnit
import com.koresuniku.wishmaster.ui.controller.SwipyRefreshLayoutUnit
import com.koresuniku.wishmaster.ui.controller.view_interface.*
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find

class SingleThreadActivity : AppCompatActivity(), AppBarLayoutView, ActionBarView, LoadDataView,
        SingleThreadListViewView, ProgressView, SwipyRefreshLayoutView, IActivityView, NewPostsView {
    val LOG_TAG: String = SingleThreadActivity::class.java.simpleName

    private var boardId: String? = null
    var boardName: String? = null
    var threadNumber: String? = null
    var mSchema: IBaseJsonSchemaImpl? = null

    var mAppBarLayoutUnit: AppBarLayoutUnit? = null
    var mActionBarUnit: ActionBarUnit? = null
    var mProgressUnit: ProgressUnit? = null
    var mSingleThreadListViewUnit: SingleThreadListViewUnit? = null
    var mSwipyRefreshLayoutUnit: SwipyRefreshLayoutUnit? = null
    var mNewPostsNotifier: NewPostsNotifier? = null

    var mDataLoader: DataLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_thread_drawer)

        boardId = intent.getStringExtra(IntentUtils.BOARD_ID_CODE)
        boardName = intent.getStringExtra(IntentUtils.BOARD_NAME_CODE)
        threadNumber = intent.getStringExtra(IntentUtils.THREAD_NUMBER_CODE)

        UIVisibilityManager.showSystemUI(this)

        mAppBarLayoutUnit = AppBarLayoutUnit(this)
        mActionBarUnit = ActionBarUnit(this, false, true)
        mProgressUnit = ProgressUnit(this)
        mSingleThreadListViewUnit = SingleThreadListViewUnit(this)
        mSwipyRefreshLayoutUnit = SwipyRefreshLayoutUnit(this)
        mNewPostsNotifier = NewPostsNotifier(this)

        mDataLoader = DataLoader(this)
        mProgressUnit!!.showProgressYoba()

        loadData()
    }

    //Context getters

    override fun getContext(): Context {
        return this
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun getAppCompatActivity(): AppCompatActivity {
        return this
    }

    override fun getActivityOverridden(): Activity {
        return getActivity()
    }

    //View getters

    override fun getAppBarLayout(): AppBarLayout {
        return findViewById(R.id.app_bar_layout) as AppBarLayout
    }

    override fun getToolbarContainer(): FrameLayout {
        return findViewById(R.id.toolbar_container) as FrameLayout
    }

    override fun getProgressContainer(): View {
        return find(R.id.progress_container)
    }

    override fun getRefreshLayout(): SwipyRefreshLayout {
        return find(R.id.srl)
    }

    override fun getSingleThreadListView(): ListView {
        return findViewById(R.id.single_thread_list_view) as ListView
    }

    //Field getters

    override fun getBoardId(): String? {
        return boardId
    }

    override fun getAppBarLayoutUnit(): AppBarLayoutUnit {
        return this.mAppBarLayoutUnit!!
    }

    override fun getListView(): ListView {
        return mSingleThreadListViewUnit!!.mListView!!
    }

    override fun getListViewAdapter(): INotifyableListViewAdapter {
        return mSingleThreadListViewUnit!!.mListViewAdapter!!
    }

    override fun getSchema(): IBaseJsonSchemaImpl {
        return this.mSchema!!
    }

    override fun getSwipyRefreshLayoutUnit(): SwipyRefreshLayoutUnit {
        return this.mSwipyRefreshLayoutUnit!!
    }

    //Activity methods

    override fun setupActionBarTitle() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        if (mSchema != null) supportActionBar!!.title = mSchema!!.getPosts()!![0].getSubject()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.single_thread_menu, menu)
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
                startActivityForResult(intent, ResultCodes.SINGLE_THREAD_RESULT_CODE)
            }
            android.R.id.home -> onBackPressed()
        }

        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mActionBarUnit!!.onConfigurationChanged(newConfig!!)
    }

    override fun loadData() {
        if (mSchema != null) {
            mNewPostsNotifier!!.fetchPostsCount(mSchema!!.getPosts()!!.size)
            mSingleThreadListViewUnit!!.mListViewAdapter!!.mAnswersHolder.savePreviousSchema(mSchema!!)
        }
        doAsync {  mDataLoader!!.loadData(boardId!!, threadNumber!!) }
    }

    override fun onDataLoaded(schema: List<IBaseJsonSchema>) {
        if (mSchema != null) {
            mNewPostsNotifier!!.notifyNewPosts(schema[0].getPosts()!!.size)

        }
        this.mSchema = schema[0] as IBaseJsonSchemaImpl


        mProgressUnit!!.hideProgressYoba()

        if (!mSingleThreadListViewUnit!!.adapterIsCreated()) {
            mSingleThreadListViewUnit!!.createListViewAdapter()
        } else {
            mSwipyRefreshLayoutUnit!!.onDataLoaded()
            mSingleThreadListViewUnit!!.mListViewAdapter!!.mAnswersHolder.appointAnswersToPosts()
        }

        setupActionBarTitle()
    }

    override fun showProgressBar() {

    }

    override fun showPostDialog(position: Int) {

    }

}