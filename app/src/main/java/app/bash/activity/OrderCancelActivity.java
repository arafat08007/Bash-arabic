package app.bash.activity;

import androidx.appcompat.app.AppCompatActivity;
import app.bash.R;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.utilitis.S;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.os.Handler;

public class OrderCancelActivity extends BaseActivity {
    String orderId;

    @Override
    protected int getContentResId() {
        return R.layout.activity_order_cancel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        orderId=this.getIntent().getStringExtra("orderId");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle=new Bundle();
                bundle.putString("orderId",orderId);
                S.I_clear(OrderCancelActivity.this, ViewOrderActivity.class, bundle);

            }
        }, 2000);
    }
}
