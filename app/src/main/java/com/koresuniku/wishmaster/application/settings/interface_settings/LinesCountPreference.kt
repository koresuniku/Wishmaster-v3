package com.koresuniku.wishmaster.application.settings.interface_settings

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.PreferenceUtils
import org.jetbrains.anko.find

class LinesCountPreference(context: Context, attributeSet: AttributeSet) :
        DialogPreference(context, attributeSet) {
    val LOG_TAG: String = LinesCountPreference::class.java.simpleName

    var mCurrentLinesCount: TextView? = null
    var mLinesCountSeekBar: SeekBar? = null
    var mMaxLinesCount: TextView? = null
    var mHintTextView: TextView? = null

    init {
        isPersistent = false
        dialogLayoutResource = R.layout.pref_lines_count
        positiveButtonText = ""
        negativeButtonText = ""
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        setSummaryToValue()
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)

        mCurrentLinesCount = view!!.find(R.id.min_height_current)
        mLinesCountSeekBar = view.find(R.id.min_height_seekbar)
        mMaxLinesCount = view.find(R.id.min_height_max)
        mHintTextView = view.find(R.id.lines_count_hint)

        mCurrentLinesCount!!.text = PreferenceUtils.getSharedPreferences(this.context).getString(
                this.context.getString(R.string.pref_lines_count_key),
                this.context.getString(R.string.pref_lines_count_default))
        mMaxLinesCount!!.text = this.context.getString(R.string.pref_lines_count_max)

        mLinesCountSeekBar!!.max = mMaxLinesCount!!.text.toString().toInt()
        mLinesCountSeekBar!!.progress = mCurrentLinesCount!!.text.toString().toInt()
        mLinesCountSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                mCurrentLinesCount!!.text = progress.toString()
                writeInChangedValue(progress.toString())
                setSummaryToValue()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    fun setSummaryToValue() {
        ChooseLinesCountUnit.setSummaryToValue(this,
                PreferenceUtils.getSharedPreferences(this.context).getString(
                        this.context.getString(R.string.pref_lines_count_key),
                        this.context.getString(R.string.pref_lines_count_default)))
    }

    fun writeInChangedValue(progress: String) {
        val editor: SharedPreferences.Editor = PreferenceUtils.getSharedPreferences(this.context).edit()
        editor.putString(this.context.getString(R.string.pref_lines_count_key), progress)
        editor.apply()
    }

}