package app.com.example.althomas04.topnews;

/**
 * Created by al.thomas04 on 8/18/2016.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class WebViewActivity extends Activity {

    private WebView webView;

    private String articleUrl = MainActivity.newsArticleUrl;
    private ImageButton backButton;

    //Creates a WebView to display the full article within the app
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        // Calls listener method for custom up button
        addListenerOnButton();

        webView = (WebView) findViewById(R.id.web_view_layout);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new MyCustomWebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(articleUrl);

    }

    // Custom UP button that navigates back to the main activity.
    // Displays the same state the main activity was in before the webview was initialized.
    public void addListenerOnButton() {
        backButton = (ImageButton) findViewById(R.id.arrow_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = NavUtils.getParentActivityIntent(WebViewActivity.this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(WebViewActivity.this, intent);
            }
        });
    }

    // IF the user further navigates within the webview, pressing the device back button will
    // allow users to navigate back within the webview itself rather than bringing them back to the main activity.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check for browser web page history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            // Back history item
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //Prevents external browser from loading url. Loads url within WebView instead.
    private class MyCustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
