package com.koresuniku.wishmaster.system.settings.interface_settings

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.system.PreferenceUtils
import org.jetbrains.anko.find

class ImageHeightPreference(context: Context, attributeSet: AttributeSet) :
        DialogPreference(context, attributeSet) {
    val LOG_TAG: String = ImageHeightPreference::class.java.simpleName

    var mCurrentMinHeight: TextView? = null
    var mMinHeightSeekBar: SeekBar? = null
    var mMaxMinHeight: TextView? = null
    var mCurrentMaxHeight: TextView? = null
    var mMaxHeightSeekBar: SeekBar? = null
    var mMaxMaxHeight: TextView? = null

    init {
        isPersistent = false
        dialogLayoutResource = R.layout.pref_image_heigth
        positiveButtonText = ""
        negativeButtonText = ""
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        setSummaryToValue()
    }

    fun setSummaryToValue() {
        ChooseImageHeightUnit.setSummaryToValue(this,
                getMinHeightFromSharedPreferences(),
                getMaxHeightFromSharedPreferences())
    }

    fun getMinHeightFromSharedPreferences(): String {
        return PreferenceUtils.getSharedPreferences(this.context).getString(
                this.context.getString(R.string.pref_image_height_min_key),
                this.context.getString(R.string.pref_image_height_min_default))
    }

    fun getMaxHeightFromSharedPreferences(): String {
        return PreferenceUtils.getSharedPreferences(this.context).getString(
                this.context.getString(R.string.pref_image_height_max_key),
                this.context.getString(R.string.pref_image_height_max_default))
    }

    fun writeInChangedValue(key: String, progress: String) {
        val editor: SharedPreferences.Editor = PreferenceUtils.getSharedPreferences(this.context).edit()
        editor.putString(key, progress)
        editor.apply()
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)

        mCurrentMinHeight = view!!.find(R.id.min_height_current)
        mMinHeightSeekBar = view.find(R.id.min_height_seekbar)
        mMaxMinHeight = view.find(R.id.min_height_max)
        mCurrentMaxHeight = view.find(R.id.max_height_current)
        mMaxHeightSeekBar = view.find(R.id.max_height_seekbar)
        mMaxMaxHeight = view.find(R.id.max_height_max)

        mMaxMinHeight!!.text = this.context.getString(R.string.pref_image_height_min_limit_max)
        mMaxMaxHeight!!.text = this.context.getString(R.string.pref_image_height_max_limit_max)

        mCurrentMinHeight!!.text = getMinHeightFromSharedPreferences()
        mCurrentMaxHeight!!.text = getMaxHeightFromSharedPreferences()

        mMinHeightSeekBar!!.max =  mMaxMinHeight!!.text.toString().toInt()
        mMinHeightSeekBar!!.progress = mCurrentMinHeight!!.text.toString().toInt()
        mMinHeightSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mCurrentMinHeight!!.text = progress.toString()
                writeInChangedValue(
                        get().context.getString(R.string.pref_image_height_min_key),
                        progress.toString())
                ChooseImageHeightUnit.setSummaryToValue(
                        get(),
                        getMinHeightFromSharedPreferences(),
                        getMaxHeightFromSharedPreferences())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        val maxDifference: Int =
                get().context.getString(R.string.pref_image_height_max_limit_max).toInt() -
                get().context.getString(R.string.pref_image_height_max_limit_min).toInt()
        mMaxHeightSeekBar!!.max = maxDifference
        mMaxHeightSeekBar!!.progress =
                mCurrentMaxHeight!!.text.toString().toInt() -
                get().context.getString(R.string.pref_image_height_max_limit_min).toInt()
        mMaxHeightSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mCurrentMaxHeight!!.text = (progress +
                        get().context.getString(R.string.pref_image_height_max_limit_min).toInt())
                        .toString()
                writeInChangedValue(
                        get().context.getString(R.string.pref_image_height_max_key),
                        (progress + get().context.getString(R.string.pref_image_height_max_limit_min).toInt())
                                .toString())
                ChooseImageHeightUnit.setSummaryToValue(
                        get(),
                        getMinHeightFromSharedPreferences(),
                        getMaxHeightFromSharedPreferences())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    fun get(): DialogPreference {
        return this
    }
}