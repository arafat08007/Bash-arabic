package app.bash.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.security.MessageDigest;
import java.util.Locale;

import app.bash.R;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.utilitis.S;
import app.bash.utilitis.SavedData;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected int getContentResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        /*WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        if (Build.VERSION.SDK_INT > 16) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);
        }
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("https://gopoc.in:3000/presenter");*/

        try {
            PackageInfo info = getPackageManager().getPackageInfo("app.bash", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
            S.E("EXCEPTION : " + e.getMessage());
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!SavedData.getLanguage().equals("")) {
                    Locale locale = new Locale(SavedData.getLanguage());
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                } else {
                    SavedData.saveLanguage("en");
                    Locale locale = new Locale(SavedData.getLanguage());
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                }
                if (UserDataHelper.getInstance().getUserDataModel().size() > 0) {
                    if (!UserDataHelper.getInstance().getUserDataModel().get(0).getFullName().equals("")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("selected", "home");
                        S.I_clear(SplashActivity.this, MainActivity.class, bundle);
                    } else {
                        S.I_clear(SplashActivity.this, WelcomeActivity.class, null);
                    }
                } else {
                    S.I_clear(SplashActivity.this, WelcomeActivity.class, null);
                }
            }
        }, 4000);
    }
}