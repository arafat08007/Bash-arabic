package app.bash.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.widget.Toolbar;

import app.bash.R;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.data.data_model.UserDataModel;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import app.bash.utilitis.NetworkUtil;
import app.bash.utilitis.S;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OTPActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.firstPinView)
    PinView firstPinView;
    @BindView(R.id.tvResndOTP)
    TextView tvResndOTP;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.btnContinue)
    Button btnContinue;
    String user_id;
    String phone, code, number;

    @Override
    protected int getContentResId() {
        return R.layout.activity_otp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarWithBackButton("");
        phone = this.getIntent().getStringExtra("phone");
        code = this.getIntent().getStringExtra("code");
        number = this.getIntent().getStringExtra("number");
        tvPhone.setText(phone);

        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //    tvCount.setText("" + millisUntilFinished / 1000);
                tvCount.setText("" + String.format(Locale.ENGLISH, "%02d : %02d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                tvCount.setText("00:00");
                tvCount.setVisibility(View.GONE);
                tvResndOTP.setVisibility(View.VISIBLE);

            }
        }.start();
        if (NetworkUtil.isNetworkAvailable(this)) {
            sendOtp(phone);
        } else {
            S.T(this, getString(R.string.no_internet_connection));
        }
    }

    private void sendOtp(String phone) {
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).sendOtp(
                "1",
                code,
                number
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        String otp = jsonObject.optString("otp");
                        user_id = jsonObject.optString("user_id");
                        tvResndOTP.setVisibility(View.GONE);
//                        firstPinView.setText(otp);
                    } else {
                        S.T(OTPActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.tvResndOTP, R.id.btnContinue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvResndOTP:
                sendOtp(phone);
                break;
            case R.id.btnContinue:
                if (firstPinView.getText().length() < 4) {
                    S.T(this, "Enter pin");
                } else {
                    if (NetworkUtil.isNetworkAvailable(this)) {
                        otpVerify(firstPinView.getText().toString());
                    } else {
                        S.T(this, getString(R.string.no_internet_connection));
                    }

                }

                break;
        }
    }

    private void otpVerify(String otp) {
        S.E("FirebaseInstanceId.getInstance().getToken() :: "+FirebaseInstanceId.getInstance().getToken());
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).verifyOtp(
                "1",
                otp,
                user_id,
                FirebaseInstanceId.getInstance().getToken()
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.optJSONObject(i);
                            UserDataModel userDataModel = new UserDataModel();
                            userDataModel.setUserId(data.getString("id"));
                            userDataModel.setFullName(data.getString("name"));
                            userDataModel.setEmail(data.getString("email"));
                            userDataModel.setDob(data.getString("date_of_birth"));
                            userDataModel.setgender(data.getString("gender"));
                            userDataModel.setoccupation(data.getString("occupation"));
                            userDataModel.setStatus(data.getString("status"));
                            userDataModel.setPhone(data.getString("phoneNumber"));
                            userDataModel.setImage(data.getString("profile"));
                            userDataModel.setUserType("1");
                            UserDataHelper.getInstance().insertUserDataModel(userDataModel);
                            S.I(OTPActivity.this, OTPVerifyActivity.class, null);
                        }
                    } else {
                        S.T(OTPActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
