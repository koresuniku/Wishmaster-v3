package com.koresuniku.wishmaster.ui.dashboard

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout

import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.controller.ActionBarUnit
import com.koresuniku.wishmaster.ui.view.ActionBarView

class DashboardActivity : AppCompatActivity(), ActionBarView {
    var mActionBarUnit: ActionBarUnit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mActionBarUnit = ActionBarUnit(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        mActionBarUnit!!.onConfigurationChanged(newConfig!!)
    }

    override fun getAppCompatActivity(): AppCompatActivity {
        return this
    }

    override fun getToolbarContainer(): FrameLayout {
        return findViewById(R.id.toolbar_container) as FrameLayout
    }


}
