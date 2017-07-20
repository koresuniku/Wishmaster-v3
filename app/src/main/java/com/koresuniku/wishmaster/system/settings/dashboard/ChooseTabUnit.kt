package com.koresuniku.wishmaster.system.settings.dashboard

import android.preference.ListPreference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import com.koresuniku.wishmaster.R

class ChooseTabUnit(val fragment: PreferenceFragment) {


    fun initChooseTabPreference() {
        val chooseTabPref: ListPreference =
                fragment.findPreference(fragment.getString(R.string.pref_dashboard_tab_position_key)) as ListPreference
        chooseTabPref.setOnPreferenceChangeListener { preference, newValue -> run {
            setChooseTabSummaryAndValue(chooseTabPref, newValue.toString())
            false }
        }

        setChooseTabSummaryAndValue(chooseTabPref,
                PreferenceManager.getDefaultSharedPreferences(chooseTabPref.context).getString(
                        chooseTabPref.key,
                        fragment.getString(R.string.pref_dashboard_tab_position_board_list_default)))
    }

    fun setChooseTabSummaryAndValue(chooseTabPref: ListPreference, value: String) {
        when (value) {
            fragment.getString(R.string.pref_dashboard_tab_position_favourites) -> {
                chooseTabPref.summary = fragment.getString(R.string.pref_choose_tab_position_favourites)
                chooseTabPref.setValueIndex(fragment.getString(R.string.pref_dashboard_tab_position_favourites).toInt())
            }
            fragment.getString(R.string.pref_dashboard_tab_position_board_list_default) -> {
                chooseTabPref.summary = fragment.getString(R.string.pref_choose_tab_position_list)
                chooseTabPref.setValueIndex(fragment.getString(R.string.pref_dashboard_tab_position_board_list_default).toInt())
            }
            fragment.getString(R.string.pref_dashboard_tab_position_history) -> {
                chooseTabPref.summary = fragment.getString(R.string.pref_choose_tab_position_history)
                chooseTabPref.setValueIndex(fragment.getString(R.string.pref_dashboard_tab_position_history).toInt())
            }
        }
    }

}