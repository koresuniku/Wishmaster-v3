package com.koresuniku.wishmaster.application.settings.interface_settings

import android.preference.Preference
import android.preference.PreferenceFragment
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.PreferenceUtils

class ChooseLinesCountUnit(val fragment: PreferenceFragment) {

    fun initChooseLinesCount() {
        val preference = fragment.findPreference(fragment.getString(R.string.pref_lines_count_key))

        setSummaryToValue(preference,
                PreferenceUtils.getSharedPreferences(fragment.activity).getString(
                fragment.getString(R.string.pref_lines_count_key),
                fragment.getString(R.string.pref_lines_count_default)))
    }

    companion object {
        fun setSummaryToValue(preference: Preference, value: String) {
            if (value == "0") preference.summary = preference.context.getString(R.string.pref_lines_count_indefinite)
            else preference.summary = value
        }
    }

}