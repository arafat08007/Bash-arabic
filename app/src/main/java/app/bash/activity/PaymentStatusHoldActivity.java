package app.bash.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.bash.R;
import app.bash.data.data_helper.MyCartHelper;
import app.bash.utilitis.S;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentStatusHoldActivity extends BaseActivity {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tvPlzWait)
    TextView tvPlzWait;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvOrder)
    TextView tvOrder;
    @BindView(R.id.btnHome)
    Button btnHome;

    @Override
    protected int getContentResId() {
        return R.layout.activity_payment_status_hold;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyCartHelper.getInstance().delete();
    }

    @OnClick({R.id.tvOrder, R.id.btnHome})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvOrder:
                //S.I(PaymentStatusHoldActivity.this,ViewOrderActivity.class,null);
                break;
            case R.id.btnHome:
                Bundle bundle=new Bundle();
                bundle.putString("selected","home");
                S.I_clear(PaymentStatusHoldActivity.this, MainActivity.class, bundle);
                break;
        }
    }
}
