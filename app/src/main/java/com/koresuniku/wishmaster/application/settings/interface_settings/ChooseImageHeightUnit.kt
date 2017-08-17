package com.koresuniku.wishmaster.application.settings.interface_settings

import android.preference.Preference
import android.preference.PreferenceFragment
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.PreferenceUtils

class ChooseImageHeightUnit(val fragment: PreferenceFragment) {

    fun initChooseImageHeight() {
        val preference = fragment.findPreference(fragment.getString(R.string.pref_images_height_key))

        setSummaryToValue(preference,
                PreferenceUtils.getSharedPreferences(fragment.activity).getString(
                        fragment.getString(R.string.pref_image_height_min_key),
                        fragment.getString(R.string.pref_image_height_min_default)),
                PreferenceUtils.getSharedPreferences(fragment.activity).getString(
                        fragment.getString(R.string.pref_image_height_max_key),
                        fragment.getString(R.string.pref_image_height_max_default))
                )


    }

    companion object {
        fun setSummaryToValue(preference: Preference, min: String, max: String) {
            var summary: String = preference.context.getString(R.string.pref_image_height_min_height_text)
            summary += " "
            summary += min
            summary += "dp, "
            summary += preference.context.getString(R.string.pref_image_height_max_height_text)
            summary += " "
            summary += max
            summary += "dp"
            preference.summary = summary
        }
    }
}