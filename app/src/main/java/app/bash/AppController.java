package app.bash;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import app.bash.data.data_helper.CardHelper;
import app.bash.data.data_helper.MyCartHelper;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.utilitis.S;

/**
 * Created by SOHEL KHAN on 12-Feb-18.
 */

public class AppController extends MultiDexApplication {
    private static AppController mInstance;
    public static final String TAG = AppController.class.getSimpleName();

    public static synchronized AppController getInstance() {
        return mInstance;
    }

//    private Locale locale = null;

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            Locale.setDefault(locale);
            Configuration config = new Configuration(newConfig);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        new UserDataHelper(this);
        new MyCartHelper(this);
        new CardHelper(this);
//        FacebookSdk.sdkInitialize(this);
        printHashKey();
//        S.E("Locale.getDefault().getDisplayLanguage() :: " + Locale.getDefault().getLanguage());
//        String lang = "hi";
//        changeLang(lang);
    }

    /*public void changeLang(String lang) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration conf = new Configuration(config);
            conf.locale = locale;
            getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        }
    }*/

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}