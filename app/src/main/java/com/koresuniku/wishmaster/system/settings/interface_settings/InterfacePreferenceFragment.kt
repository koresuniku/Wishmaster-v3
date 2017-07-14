package com.koresuniku.wishmaster.system.settings.interface_settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.View
import com.koresuniku.wishmaster.R

@SuppressLint("ValidFragment")
class InterfacePreferenceFragment : PreferenceFragment() {
    val LOG_TAG: String = InterfacePreferenceFragment::class.java.simpleName

    val mChooseLinesCountUnit = ChooseLinesCountUnit(this)
    val mChooseLoadingViewUnit = ChooseLoadingViewUnit(this)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        addPreferencesFromResource(R.xml.pref_interface)
        view!!.setBackgroundColor(resources.getColor(R.color.colorBackground))

        mChooseLinesCountUnit.initChooseLinesCount()
        mChooseLoadingViewUnit.initChooseLoadingViewPreference()
    }



}