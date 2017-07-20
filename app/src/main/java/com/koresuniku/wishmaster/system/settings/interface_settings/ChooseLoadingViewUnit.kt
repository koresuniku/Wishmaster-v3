package com.koresuniku.wishmaster.system.settings.interface_settings

import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import com.koresuniku.wishmaster.R

class ChooseLoadingViewUnit(val fragment: PreferenceFragment) {

    fun initChooseLoadingViewPreference() {
        val chooseLoadingViewPreference =
                fragment.findPreference(fragment.getString(R.string.pref_loading_view_key)) as ListPreference
        chooseLoadingViewPreference.setOnPreferenceChangeListener { preference, newValue -> run {
            setChooseLoadingViewSummaryToValue(chooseLoadingViewPreference, newValue.toString())
            false }
        }

        setChooseLoadingViewSummaryToValue(chooseLoadingViewPreference,
                PreferenceManager.getDefaultSharedPreferences(chooseLoadingViewPreference.context)
                        .getString(chooseLoadingViewPreference.key,
                                fragment.getString(R.string.pref_loading_yoba_default)))

    }

    fun setChooseLoadingViewSummaryToValue(chooseLoadingViewPref: ListPreference, value: String) {
        when (value) {
            fragment.getString(R.string.pref_loading_yoba_default) -> {
                chooseLoadingViewPref.summary = fragment.getString(R.string.pref_choose_loading_view_yoba)
                chooseLoadingViewPref.setValueIndex(fragment.getString(R.string.pref_loading_yoba_default).toInt())
            }
            fragment.getString(R.string.pref_loading_spinner) -> {
                chooseLoadingViewPref.summary = fragment.getString(R.string.pref_choose_loading_view_spinner)
                chooseLoadingViewPref.setValueIndex(fragment.getString(R.string.pref_loading_spinner).toInt())
            }
            fragment.getString(R.string.pref_loading_android_progress_bar) -> {
                chooseLoadingViewPref.summary = fragment.getString(R.string.pref_choose_loading_view_android_progress_bar)
                chooseLoadingViewPref.setValueIndex(fragment.getString(R.string.pref_loading_android_progress_bar).toInt())
            }
        }
    }
}