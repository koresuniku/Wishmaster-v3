package com.koresuniku.wishmaster.system

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.*
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.koresuniku.wishmaster.R


class SettingsActivity : PreferenceActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    val LOG_TAG: String = SettingsActivity::class.java.simpleName

    var mToolbar: Toolbar? = null

    companion object {
        var mSharedPreferences: SharedPreferences? = null
    }

    override fun onBuildHeaders(target: MutableList<Header>?) {
        loadHeadersFromResource(R.xml.pref_headers, target)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mSharedPreferences = getPreferences(Context.MODE_PRIVATE)
        setupActionBar()

        mSharedPreferences!!.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSharedPreferences!!.unregisterOnSharedPreferenceChangeListener(this)
    }

    fun setupActionBar() {
        val root = findViewById(android.R.id.list).parent.parent.parent as LinearLayout
        mToolbar = LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false) as Toolbar
        root.addView(mToolbar, 0)
        mToolbar!!.setNavigationOnClickListener { onBackPressed() }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar!!.elevation = 4.0f
        }

        listView.setBackgroundColor(resources.getColor(R.color.colorBackground))

        mToolbar!!.title = getString(R.string.settings_text)
    }


    override fun isValidFragment(fragmentName: String?): Boolean {
        return true
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        if (key == getString(R.string.pref_dashboard_tab_position_key)) {
            Log.d(LOG_TAG, "pref clicked:")
            val i = sharedPreferences!!.getInt(getString(R.string.pref_dashboard_tab_position_key),
                    getString(R.string.pref_dashboard_tab_position_board_list_default).toInt())
            Log.d(LOG_TAG, "newValue: " + i)
        }
    }

    @SuppressLint("ValidFragment")
    class DashboardPreferenceFragment : PreferenceFragment() {
        val LOG_TAG: String = DashboardPreferenceFragment::class.java.simpleName

        fun initChooseTabPreference() {
            val chooseTabPref: ListPreference =
                    findPreference(getString(R.string.pref_dashboard_tab_position_key)) as ListPreference
            chooseTabPref.setOnPreferenceChangeListener { preference, newValue -> run {
                setChooseTabSummaryAndValue(chooseTabPref, newValue.toString())
                false }
            }

            setChooseTabSummaryAndValue(chooseTabPref,
                    PreferenceManager.getDefaultSharedPreferences(chooseTabPref.context).getString(
                            chooseTabPref.key,
                            getString(R.string.pref_dashboard_tab_position_board_list_default)))
        }

        fun setChooseTabSummaryAndValue(chooseTabPref: ListPreference, value: String) {
            when (value) {
                getString(R.string.pref_dashboard_tab_position_favourites) -> {
                    chooseTabPref.summary = getString(R.string.pref_choose_tab_position_favourites)
                    chooseTabPref.setValueIndex(getString(R.string.pref_dashboard_tab_position_favourites).toInt())
                }
                getString(R.string.pref_dashboard_tab_position_board_list_default) -> {
                    chooseTabPref.summary = getString(R.string.pref_choose_tab_position_list)
                    chooseTabPref.setValueIndex(getString(R.string.pref_dashboard_tab_position_board_list_default).toInt())
                }
                getString(R.string.pref_dashboard_tab_position_history) -> {
                    chooseTabPref.summary = getString(R.string.pref_choose_tab_position_history)
                    chooseTabPref.setValueIndex(getString(R.string.pref_dashboard_tab_position_history).toInt())
                }
            }
        }

        override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            addPreferencesFromResource(R.xml.pref_dashboard)
            view!!.setBackgroundColor(resources.getColor(R.color.colorBackground))
            initChooseTabPreference()
        }

    }

    @SuppressLint("ValidFragment")
    class InterfacePreferenceFragment : PreferenceFragment() {
        val LOG_TAG: String = InterfacePreferenceFragment::class.java.simpleName

        override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            addPreferencesFromResource(R.xml.pref_interface)
            view!!.setBackgroundColor(resources.getColor(R.color.colorBackground))
            initChooseLoadingViewPreference()
        }

        fun initChooseLoadingViewPreference() {
            val chooseLoadingViewPreference =
                    findPreference(getString(R.string.pref_loading_view_key)) as ListPreference
            chooseLoadingViewPreference.setOnPreferenceChangeListener { preference, newValue -> run {
                setChooseLoadingViewSummaryToValue(chooseLoadingViewPreference, newValue.toString())
                false }
            }

            setChooseLoadingViewSummaryToValue(chooseLoadingViewPreference,
                    PreferenceManager.getDefaultSharedPreferences(chooseLoadingViewPreference.context)
                            .getString(chooseLoadingViewPreference.key,
                            getString(R.string.pref_loading_yoba_default)))

        }

        fun setChooseLoadingViewSummaryToValue(chooseLoadingViewPref: ListPreference, value: String) {
            when (value) {
                getString(R.string.pref_loading_yoba_default) -> {
                    chooseLoadingViewPref.summary = getString(R.string.pref_choose_loading_view_yoba)
                    chooseLoadingViewPref.setValueIndex(getString(R.string.pref_loading_yoba_default).toInt())
                }
                getString(R.string.pref_loading_spinner) -> {
                    chooseLoadingViewPref.summary = getString(R.string.pref_choose_loading_view_spinner)
                    chooseLoadingViewPref.setValueIndex(getString(R.string.pref_loading_spinner).toInt())
                }
                getString(R.string.pref_loading_android_progress_bar) -> {
                    chooseLoadingViewPref.summary = getString(R.string.pref_choose_loading_view_android_progress_bar)
                    chooseLoadingViewPref.setValueIndex(getString(R.string.pref_loading_android_progress_bar).toInt())
                }
            }
        }
    }


}