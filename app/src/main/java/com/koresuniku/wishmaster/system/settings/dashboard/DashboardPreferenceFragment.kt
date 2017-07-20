package com.koresuniku.wishmaster.system.settings.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.View
import com.koresuniku.wishmaster.R

@SuppressLint("ValidFragment")
class DashboardPreferenceFragment : PreferenceFragment() {
    val LOG_TAG: String = DashboardPreferenceFragment::class.java.simpleName

    val mChooseTabUnit = ChooseTabUnit(this)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        addPreferencesFromResource(R.xml.pref_dashboard)
        view!!.setBackgroundColor(resources.getColor(R.color.colorBackground))
        mChooseTabUnit.initChooseTabPreference()
    }

}