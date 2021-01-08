package app.bash.utilitis;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.bash.R;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class S {

  public static void I(Context cx, Class<?> startActivity, Bundle data) {
    Intent i = new Intent(cx, startActivity);
    if (data != null)
      i.putExtras(data);
    cx.startActivity(i);
  }

  public static void I_clear(Context cx, Class<?> startActivity, Bundle data) {
    Intent i = new Intent(cx, startActivity);
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    if (data != null)
      i.putExtras(data);
    cx.startActivity(i);
  }

  public static void E(String msg) {
    if (true) {
      if (msg.length() > 4000) {
        int chunkCount = msg.length() / 4000;
        for (int i = 0; i <= chunkCount; i++) {
          int max = 4000 * (i + 1);
          if (max >= msg.length()) {
            Log.e("Log.e By Sohel : ", msg.substring(4000 * i));
          } else {
            Log.e("Log.e By Sohel : ", msg.substring(4000 * i, max));
          }
        }
      } else {
        Log.e("Log.e By Sohel : ", msg);
      }
    }
  }

    /*public static void E(String msg) {
        if (true)
            Log.e("Log.e by SOHEL : ", msg);
    }*/

  public static void T(Context c, String msg) {
    Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
  }

  public static void snackBar(Context cx, View view, String msg) {
//        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
    View sbView = snackbar.getView();
    sbView.setBackgroundColor(ContextCompat.getColor(cx, R.color.colorPrimary));
    snackbar.show();
  }

    /*public static void setImageByPicasso(Context cx, String image_url, ImageView holder, final ProgressBar progressbar) {
        Picasso.get().load("https://" + image_url).placeholder(R.drawable.event_image).resize(300, 200).into(holder, new Callback() {
            @Override
            public void onSuccess() {
                if (progressbar != null) {
                    progressbar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(Exception e) {
                if (progressbar != null) {
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
    }*/

  public static ACProgressFlower initProgressDialog(Context c) {
        /*ProgressDialog pd = new ProgressDialog(c);
        pd.setMessage("loading");
        pd.show();
        return pd;*/
    final ACProgressFlower dialog = new ACProgressFlower.Builder(c)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .fadeColor(Color.DKGRAY).build();
    dialog.setCancelable(false);
    dialog.show();
    return dialog;
  }

  public static void share(Context c, String subject, String shareBody) {
    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
    c.startActivity(Intent.createChooser(sharingIntent, "Share via"));
  }

  public static void shareFile(Context c, File file, String subject, String shareBody) {
    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
    if (file.exists()) {
      intentShareFile.setType("application/image");
      intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));
      intentShareFile.putExtra(Intent.EXTRA_SUBJECT, subject);
      intentShareFile.putExtra(Intent.EXTRA_TEXT, shareBody);
      c.startActivity(Intent.createChooser(intentShareFile, "Share File"));
    }
  }

  public static boolean isAppIsInBackground(Context context) {
    boolean isInBackground = true;
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
      List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
      for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
        if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
          for (String activeProcess : processInfo.pkgList) {
            if (activeProcess.equals(context.getPackageName())) {
              isInBackground = false;
            }
          }
        }
      }
    } else {
      isInBackground = false;
    }
    return isInBackground;
  }

  public static String getDate(long time) {
    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    cal.setTimeInMillis(time);
    String dateStr = DateFormat.format("mm/dd/yyyy", cal).toString();
    return dateStr;
  }

  public static int getDifferenceInSecond(String startTime, String stopTime) {
    String dateStart = startTime;
    String dateStop = stopTime;
    long diffSeconds = 0;
    long diffMinutes = 0;
    //HH converts hour in 24 hours format (0-23), day calculation
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    Date d1 = null;
    Date d2 = null;

    try {
      d1 = format.parse(dateStart);
      d2 = format.parse(dateStop);

      //in milliseconds
      long diff = d2.getTime() - d1.getTime();

      diffSeconds = diff / 1000 % 60;
      diffMinutes = diff / (60 * 1000) % 60;
      long diffHours = diff / (60 * 60 * 1000) % 24;
      long diffDays = diff / (24 * 60 * 60 * 1000);

      Log.e("Sohel", "diffDays : " + diffDays);
      Log.e("Sohel", "diffHours : " + diffHours);
      Log.e("Sohel", "diffMinutes : " + diffMinutes);
      Log.e("Sohel", "diffSeconds : " + diffSeconds);

      System.out.print(diffDays + " days, ");
      System.out.print(diffHours + " hours, ");
      System.out.print(diffMinutes + " minutes, ");
      System.out.print(diffSeconds + " seconds.");
    } catch (Exception e) {
      e.printStackTrace();
    }
        /*if (diffMinutes > 0) {
            diffSeconds = diffSeconds + TimeUnit.MINUTES.toSeconds(diffMinutes);
        }*/
    return (int) diffSeconds;
  }

  public static String changeHourFormat(String value) {
    String newHour = "";
    try {
      SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
      SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
      Date _24StartHourDt = _24HourSDF.parse(value);
      S.E("Time start : " + _12HourSDF.format(_24StartHourDt));
      newHour = _12HourSDF.format(_24StartHourDt);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return newHour;
  }

  public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
    byte[] byteFormat = stream.toByteArray();
    String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    return imgString;
  }

  public static String parseDateToddMMyyyy(String oldDate) {
    String inputPattern = "yyyy-MM-dd";
    String outputPattern = "MM/dd/yy";
    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

    Date date = null;
    String str = null;

    try {
      date = inputFormat.parse(oldDate);
      str = outputFormat.format(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return str;
  }

  public static String numberCalculation(long number) {
    if (number < 1000)
      return "" + number;
    int exp = (int) (Math.log(number) / Math.log(1000));
    return String.format("%.1f %c", number / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
  }

  public static void setStatusBarGradiant(Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = activity.getWindow();
      // Drawable background = activity.getResources().getDrawable(R.drawable.app_gradient_bg);
      Drawable background = activity.getResources().getDrawable(R.drawable.btn_blue_bottom_half_bg);
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
      window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
      window.setBackgroundDrawable(background);
    }
  }
  public static void strikeThroughText(Activity activity, TextView price) {
    price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
  }
}