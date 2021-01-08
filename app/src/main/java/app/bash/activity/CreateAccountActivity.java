package app.bash.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.widget.Toolbar;

import app.bash.R;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.data.data_model.UserDataModel;
import app.bash.model.OccupationModel;
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

public class CreateAccountActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.edtFullName)
    EditText edtFullName;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.spinOccupation)
    AutoCompleteTextView spinOccupation;
    @BindView(R.id.spinGender)
    AutoCompleteTextView spinGender;
    @BindView(R.id.spinDateOfBirth)
    AutoCompleteTextView spinDateOfBirth;
    @BindView(R.id.btnCreate)
    Button btnCreate;
    private int mYear, mMonth, mDay;
    ArrayList<OccupationModel> occupationModelArrayList = new ArrayList<>();
    ArrayList<String> occupationStr = new ArrayList<>();
    String OccupationId;

    @Override
    protected int getContentResId() {
        return R.layout.activity_create_account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarWithBackButton("");

        if (SavedData.getLanguage().equals("ar")) {
            edtFullName.setGravity(View.FOCUS_LEFT);
            edtEmail.setGravity(View.FOCUS_LEFT);
            spinOccupation.setGravity(View.FOCUS_LEFT);
            spinGender.setGravity(View.FOCUS_LEFT);
            spinDateOfBirth.setGravity(View.FOCUS_LEFT);

            edtFullName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_name, 0);
            edtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_email, 0);

            spinOccupation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_occupation, 0);
            spinOccupation.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.icon_occupation, 0);

            spinGender.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_gender, 0);
            spinGender.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_gender, 0);

            spinDateOfBirth.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_dob, 0);
            spinDateOfBirth.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.icon_dob, 0);
        } else {
            edtFullName.setGravity(View.FOCUS_RIGHT);
            edtEmail.setGravity(View.FOCUS_RIGHT);
            spinOccupation.setGravity(View.FOCUS_RIGHT);
            spinGender.setGravity(View.FOCUS_RIGHT);
            spinDateOfBirth.setGravity(View.FOCUS_RIGHT);

            edtFullName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_name, 0, 0, 0);
            edtEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_email, 0, 0, 0);

            spinOccupation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_occupation, 0, 0, 0);
            spinOccupation.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.icon_occupation, 0, 0, 0);

            spinGender.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_gender, 0, 0, 0);
            spinGender.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_gender, 0, 0, 0);

            spinDateOfBirth.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_dob, 0, 0, 0);
            spinDateOfBirth.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.icon_dob, 0, 0, 0);
        }


        if (NetworkUtil.isNetworkAvailable(this)) {
            getOccupation();
        } else {
            S.T(this, getString(R.string.no_internet_connection));
        }
        String[] genders = new String[]{"Male", "Female"};

        ArrayAdapter<String> adapterGender =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        genders);

        spinGender.setAdapter(adapterGender);

        spinDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinDateOfBirth.setEnabled(false);
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(CreateAccountActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                spinDateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
                dpd.show();
                spinDateOfBirth.setEnabled(true);
            }
        });
    }

    private void getOccupation() {
        if (!occupationModelArrayList.isEmpty()) {
            occupationModelArrayList.clear();
        }
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getOccupation(

        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.optJSONObject(i);
                            OccupationModel occupationModel = new OccupationModel();
                            occupationModel.setId(data.getString("id"));

                            if (SavedData.getLanguage().equals("ar")) {
                                if (data.getString("arabic_name").endsWith("null")) {
                                    occupationModel.setName(data.getString("name"));
                                    occupationStr.add(data.getString("name"));
                                } else {
                                    occupationModel.setName(data.getString("arabic_name"));
                                    occupationStr.add(data.getString("arabic_name"));
                                }
                            } else {
                                occupationModel.setName(data.getString("name"));
                                occupationStr.add(data.getString("name"));
                            }
                            occupationModelArrayList.add(occupationModel);
                        }

                        setOccupationAdapter(occupationStr);

                    } else {
                        S.T(CreateAccountActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
    }

    private void setOccupationAdapter(ArrayList<String> occupationStr) {
        ArrayAdapter<String> adapterOccupations =
                new ArrayAdapter<>(
                        CreateAccountActivity.this,
                        R.layout.dropdown_menu_popup_item,
                        occupationStr);
        spinOccupation.setAdapter(adapterOccupations);

        spinOccupation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OccupationId = occupationModelArrayList.get(i).getId();
                S.E("occupationModelArrayList.get(i).getId() :: " + occupationModelArrayList.get(i).getId());
            }
        });
    }

    @OnClick(R.id.btnCreate)
    public void onViewClicked() {
        if (edtFullName.getText().toString().trim().equals("")) {
            S.T(this, "Enter name");
        } else if (UserAccount.isEmailValid(this, edtEmail)) {
            if (spinOccupation.getText().toString().equals("")) {
                S.T(this, "Select Occupation");
            } else if (spinGender.getText().toString().equals("")) {
                S.T(this, "Select Gender");
            } else if (spinDateOfBirth.getText().toString().equals("")) {
                S.T(this, "Select date of birth");
            } else {
                if (NetworkUtil.isNetworkAvailable(this)) {
                    newRegistration();
                } else {
                    S.T(this, getString(R.string.no_internet_connection));
                }

                //S.I(CreateAccountActivity.this, MainActivity.class, null);
            }
        }
    }

    private void newRegistration() {
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).newRegistration(
                edtFullName.getText().toString().trim(),
                edtEmail.getText().toString().trim(),
                OccupationId,
                spinGender.getText().toString(),
                spinDateOfBirth.getText().toString(),
                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId()
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
                            Bundle bundle = new Bundle();
                            bundle.putString("selected", "home");
                            S.I_clear(CreateAccountActivity.this, MainActivity.class, bundle);
                        }
                    } else {
                        S.T(CreateAccountActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
