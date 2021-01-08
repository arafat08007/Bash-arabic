package app.bash.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import app.bash.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteFriendFragment extends Fragment {

    @BindView(R.id.btnWhatsapp)
    ImageView btnWhatsapp;
    @BindView(R.id.btnMessanger)
    ImageView btnMessanger;
    @BindView(R.id.btnGmail)
    ImageView btnGmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_invite_friend, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick({R.id.btnWhatsapp, R.id.btnMessanger, R.id.btnGmail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnWhatsapp:
                shareOnWhatsApp();
                break;
            case R.id.btnMessanger:
                shareOnMessenger();
                break;
            case R.id.btnGmail:
                shareOnGmail();
                break;
        }
    }

    private void shareOnMessenger() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Click on a link to download the app " + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.facebook.orca");
        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
        }
    }

    private void shareOnWhatsApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/html");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, (String) getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Click on a link to download the app " + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("com.whatsapp")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(
                        activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                getActivity().startActivity(shareIntent);
                break;
            }
        }
    }

    private void shareOnGmail() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Click on a link to download the app " + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("android.gm")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                getActivity().startActivity(shareIntent);
                break;
            }
        }
    }
}