package app.bash.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.bash.R;
import app.bash.activity.MainActivity;
import app.bash.activity.WelcomeActivity;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import app.bash.utilitis.NetworkUtil;
import app.bash.utilitis.S;
import app.bash.utilitis.SavedData;
import app.bash.utilitis.UserAccount;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsFragment extends Fragment {
    @BindView(R.id.edtFullName)
    EditText edtFullName;
    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtNotes)
    EditText edtNotes;
    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ButterKnife.bind(this, v);

        if (SavedData.getLanguage().equals("ar")) {
            edtFullName.setGravity(View.FOCUS_LEFT);
            edtPhone.setGravity(View.FOCUS_LEFT);
            edtEmail.setGravity(View.FOCUS_LEFT);

            edtFullName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_name, 0);
            edtPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_phone, 0);
            edtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_email, 0);
        } else {
            edtFullName.setGravity(View.FOCUS_RIGHT);
            edtPhone.setGravity(View.FOCUS_RIGHT);
            edtEmail.setGravity(View.FOCUS_RIGHT);

            edtFullName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_name, 0, 0, 0);
            edtPhone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_phone, 0, 0, 0);
            edtEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_email, 0, 0, 0);
        }


        getContactDetails();

        return v;
    }

    private void getContactDetails() {
        new JSONParser(getActivity()).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getContactDetails(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject json = jsonArray.getJSONObject(0);
                        tvPhone.setText(json.getString("phone"));
                        tvEmail.setText(json.getString("email"));
                        if (SavedData.getLanguage().equals("ar")) {
                            tvAddress.setText(json.getString("arabic_address"));
                        }else {
                            tvAddress.setText(json.getString("address"));
                        }
                    } else {
                        S.T(getActivity(), jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.btnSend)
    public void onViewClicked() {
        if (UserAccount.isEmpty(edtFullName, edtPhone, edtEmail, edtNotes)) {
            if (UserAccount.isPhoneLengthValid(getActivity(), edtPhone)) {
                if (NetworkUtil.isNetworkAvailable(getActivity())) {
                    contactUs();
                } else {
                    S.T(getActivity(), getString(R.string.no_internet_connection));
                }
            }
        }
    }

    private void contactUs() {
        String user_id;
        if (UserDataHelper.getInstance().getUserDataModel().size() > 0) {
            user_id = UserDataHelper.getInstance().getUserDataModel().get(0).getUserId();
        } else {
            user_id = "0";
        }
        new JSONParser(getActivity()).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).contactUs(
                user_id,
                edtFullName.getText().toString(),
                edtEmail.getText().toString(),
                edtPhone.getText().toString(),
                edtNotes.getText().toString()
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        S.T(getActivity(), jsonObject.getString("message"));
                        Bundle bundle=new Bundle();
                        bundle.putString("selected","home");
                        S.I(getActivity(), MainActivity.class, bundle);
                    } else {
                        S.T(getActivity(), jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}