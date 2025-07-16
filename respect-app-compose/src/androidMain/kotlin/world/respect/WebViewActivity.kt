package world.respect

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import world.respect.app.R

/**
 * A separate activity to show a WebView to view a LearningUnit .
 *
 * This can't be done as normal Jetpack Compose using the AndroidView as normal because the vh css
 * unit doesn't work; content that uses 100vh etc comes out as zero height or a small percentage of
 * the screen (at random).
 */
class WebViewActivity : AppCompatActivity() {

    private val webChromeClient = object: WebChromeClient() {

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            this@WebViewActivity.title = title ?: ""
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        setSupportActionBar(findViewById(R.id.toolbar))
        val webView: WebView = findViewById(R.id.web_view)
        webView.webChromeClient = webChromeClient
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        val url = intent.getStringExtra(EXTRA_URL) ?:
            throw IllegalStateException("No url specified")

        webView.loadUrl(url)
    }

    companion object {

        const val EXTRA_URL = "url"

    }

}