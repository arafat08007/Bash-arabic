package app.bash.activity;

import androidx.appcompat.app.AppCompatActivity;
import app.bash.R;
import butterknife.ButterKnife;

import android.os.Bundle;

public class RateAppActivity extends BaseActivity {

    @Override
    protected int getContentResId() {
        return R.layout.activity_rate_app;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}