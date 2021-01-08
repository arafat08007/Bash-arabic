package app.bash.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import androidx.appcompat.widget.Toolbar;
import app.bash.R;
import app.bash.utilitis.NetworkUtil;
import app.bash.utilitis.S;
import app.bash.utilitis.UserAccount;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnterMobileNumberActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    @BindView(R.id.edit_phoneNumber)
    EditText editPhoneNumber;
    @BindView(R.id.btnLoginPhoneNumber)
    Button btnLoginPhoneNumber;

    @Override
    protected int getContentResId() {
        return R.layout.activity_enter_mobile_number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarWithBackButton("");
    }

    @OnClick(R.id.btnLoginPhoneNumber)
    public void onViewClicked() {
        if (UserAccount.isPhoneLengthValid(this, editPhoneNumber)){
            if (NetworkUtil.isNetworkAvailable(this)) {
                S.E(ccp.getSelectedCountryCode());
                Bundle bundle=new Bundle();
                bundle.putString("code",ccp.getSelectedCountryCode());
                bundle.putString("number",editPhoneNumber.getText().toString());
                bundle.putString("phone","+"+ccp.getSelectedCountryCode()+editPhoneNumber.getText().toString());
                S.I(EnterMobileNumberActivity.this,OTPActivity.class,bundle);
            } else {
                S.T(this, getString(R.string.no_internet_connection));
            }
        }


    }
}
