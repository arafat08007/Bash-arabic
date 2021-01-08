package app.bash.activity;

import androidx.appcompat.app.AppCompatActivity;
import app.bash.R;
import app.bash.utilitis.S;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.os.Handler;

public class PleaseWaitActivity extends BaseActivity {

    @Override
    protected int getContentResId() {
        return R.layout.activity_please_wait;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                S.I_clear(PleaseWaitActivity.this, PaymentStatusHoldActivity.class, null);
            }
        }, 2000);
    }
}