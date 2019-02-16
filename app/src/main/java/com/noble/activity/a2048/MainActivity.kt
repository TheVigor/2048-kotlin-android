package com.noble.activity.a2048

import android.app.ActionBar
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.webkit.WebSettings
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val HTML_PATH = "file:///android_asset/2048/index.html?lang="
        private const val mBackPressThreshold: Long = 3000
    }

    private var mLastBackPress: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        loadWebViewSettings()
        applyFullScreen(false)

        if (savedInstanceState != null) {
            mainWebView.restoreState(savedInstanceState)
        } else {
            mainWebView.loadUrl(HTML_PATH + Locale.getDefault().language)
        }
    }

    override fun onResume() {
        super.onResume()
        mainWebView.loadUrl(HTML_PATH + Locale.getDefault().language)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        mainWebView.saveState(outState)
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (Math.abs(currentTime - mLastBackPress) > mBackPressThreshold) {
            showToast(getString(R.string.press_back_again_to_exit))
            mLastBackPress = currentTime
        } else {
            super.onBackPressed()
        }
    }

    private fun applyFullScreen(isFullScreen: Boolean) {
        if (isFullScreen) {
            window.clearFlags(FLAG_FULLSCREEN)
        } else {
            window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        }
    }

    private fun loadWebViewSettings() = with(mainWebView.settings) {
        javaScriptEnabled = true
        domStorageEnabled = true
        databaseEnabled = true
        setRenderPriority(WebSettings.RenderPriority.HIGH)
        databasePath = filesDir.parentFile.path + File.separator + "databases"
    }
}
