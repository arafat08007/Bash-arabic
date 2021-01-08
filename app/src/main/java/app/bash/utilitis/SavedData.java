package app.bash.utilitis;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import app.bash.AppController;

public class SavedData {
  private static final String AUTO_PLAY = "autoPLay";
  private static final String VIDEO_PROFILE_EXIST = "videoProfileExist";
  private static final String CALLER_ID = "callerId";
  private static final String TOPIC_DATA = "topicData";
  private static final String NOTIFICATION = "notification";
  private static final String PRODUCT_SIZE = "productsize";
  private static final String PRODUCT_COLOR = "productcolor";
  private static final String LANGUAGE = "language";
  private static final String PAYMENT_DETAIL = "paymentdetail";
  private static final String FOR_ADD = "foradd";

  static SharedPreferences prefs;

  public static SharedPreferences getInstance() {
    if (prefs == null) {
      prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance());
    }
    return prefs;
  }

  public static boolean getAutoPlay() {
    return getInstance().getBoolean(AUTO_PLAY, false);
  }

  public static void saveAutoPlay(boolean paymentdetail) {
    SharedPreferences.Editor editor = getInstance().edit();
    editor.putBoolean(PAYMENT_DETAIL, paymentdetail);
    editor.apply();
  }



  public static String getNotification() {
    return getInstance().getString(NOTIFICATION, "");
  }

  public static void saveNotification(String notification) {
    SharedPreferences.Editor editor = getInstance().edit();
    editor.putString(NOTIFICATION, notification);
    editor.apply();
  }

  public static boolean getVideoProfileExist() {
    return getInstance().getBoolean(VIDEO_PROFILE_EXIST, false);
  }

  public static void saveVideoProfileExist(boolean videoProfileExist) {
    SharedPreferences.Editor editor = getInstance().edit();
    editor.putBoolean(VIDEO_PROFILE_EXIST, videoProfileExist);
    editor.apply();
  }

  public static String getCallerId() {
    return getInstance().getString(CALLER_ID, "");
  }

  public static void saveCallerId(String callerId) {
    SharedPreferences.Editor editor = getInstance().edit();
    editor.putString(CALLER_ID, callerId);
    editor.apply();
  }

  public static String getTopicData() {
    return getInstance().getString(TOPIC_DATA, "");
  }

  public static void saveTopicData(String topicData) {
    SharedPreferences.Editor editor = getInstance().edit();
    editor.putString(TOPIC_DATA, topicData);
    editor.apply();
  }

  public static String getProductSize() {
    return getInstance().getString(PRODUCT_SIZE, "");
  }

  public static void saveProductSize(String productsize) {
    SharedPreferences.Editor editor = getInstance().edit();
    editor.putString(PRODUCT_SIZE, productsize);
    editor.apply();
  }
  public static String getProductColor() {
    return getInstance().getString(PRODUCT_COLOR, "");
  }

  public static void saveProductColor(String productcolor) {
    SharedPreferences.Editor editor = getInstance().edit();
    editor.putString(PRODUCT_COLOR, productcolor);
    editor.apply();
  }
  public static String getLanguage() {
    return getInstance().getString(LANGUAGE, "");
  }

  public static void saveLanguage(String language) {
    SharedPreferences.Editor editor = getInstance().edit();
    editor.putString(LANGUAGE, language);
    editor.apply();
  }

  public static String getPaymentDetail() {
    return getInstance().getString(PAYMENT_DETAIL, "");
  }

  public static void savePaymentDetail(String paymentdetail) {
    SharedPreferences.Editor editor = getInstance().edit();
    editor.putString(PAYMENT_DETAIL, paymentdetail);
    editor.apply();
  }
  public static String getForAdd() {
    return getInstance().getString(FOR_ADD, "");
  }

  public static void setForAdd(String foradd) {
    SharedPreferences.Editor editor = getInstance().edit();
    editor.putString(FOR_ADD, foradd);
    editor.apply();
  }
  public static void clear() {
    SharedPreferences.Editor editor = getInstance().edit();
    editor.clear();
    editor.apply();
  }

}