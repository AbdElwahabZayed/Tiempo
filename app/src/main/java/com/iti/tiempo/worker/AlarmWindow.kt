package com.iti.tiempo.worker

import android.content.Context
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.util.Log
import android.view.*
import com.iti.tiempo.R

class AlarmWindow(val context: Context, val msg: String) {

    private val mView: View
    private val mParams: WindowManager.LayoutParams
    private var mWindowManager: WindowManager? = null
    private val layoutInflater: LayoutInflater
    private var mediaPlayer: MediaPlayer? = null

    init {

        // set the layout parameters of the window
        mParams = WindowManager.LayoutParams( // Shrink the window to wrap the content rather
            // than filling the screen
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,  // Display it on top of other application windows
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // Don't let it grab the input focus
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  // Make the underlying application window visible
            // through any transparent parts
            PixelFormat.TRANSLUCENT)

        // getting a LayoutInflater
        // getting a LayoutInflater
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // inflating the view with the custom layout we created
        // inflating the view with the custom layout we created
        mView = layoutInflater.inflate(R.layout.dialog_alarm, null)
        // Define the position of the
        // window within the screen
        // Define the position of the
        // window within the screen
        mParams.gravity = Gravity.CENTER
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mediaPlayer = MediaPlayer.create(context, R.raw.thunder)
        mediaPlayer?.start()
    }

    fun open() {
        try {
            // check if the view is already
            // inflated or present in the window
            if (mView.windowToken == null) {
                if (mView.parent == null) {
                    mWindowManager!!.addView(mView, mParams)
                }
            }
        } catch (e: Exception) {
            Log.d("Error1", e.toString())
        }
    }

    fun close() {
        try {
            // remove the view from the window
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(mView)
            // invalidate the view
            mView.invalidate()
            // remove all views
            (mView.parent as ViewGroup).removeAllViews()
            mediaPlayer?.release()
            mediaPlayer = null
            // the above steps are necessary when you are adding and removing
            // the view simultaneously, it might give some exceptions
        } catch (e: java.lang.Exception) {
            Log.d("Error2", e.toString())
        }
    }

}