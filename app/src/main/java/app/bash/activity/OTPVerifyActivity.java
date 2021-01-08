package app.bash.activity;

import app.bash.R;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.utilitis.S;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.os.Handler;

public class OTPVerifyActivity extends BaseActivity {
    String name, email, profile;

    @Override
    protected int getContentResId() {
        return R.layout.activity_otp_verify;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        profile = getIntent().getStringExtra("profile");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!UserDataHelper.getInstance().getUserDataModel().get(0).getFullName().equals("")) {
                    Bundle bundle=new Bundle();
                    bundle.putString("selected","home");
                    S.I_clear(OTPVerifyActivity.this, MainActivity.class, bundle);
                } else {
                    S.I_clear(OTPVerifyActivity.this, CreateAccountActivity.class, null);
                }
            }
        }, 2000);
    }
}