package app.bash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import app.bash.R;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.data.data_model.UserDataModel;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import app.bash.utilitis.S;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.btnLoginPhoneNumber)
    Button btnLoginPhoneNumber;
    @BindView(R.id.btnLoginFb)
    LinearLayout btnLoginFb;
    @BindView(R.id.btnSkip)
    TextView btnSkip;

    @Override
    protected int getContentResId() {
        return R.layout.activity_welcome;
    }

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();
    }

    @OnClick({R.id.btnLoginPhoneNumber, R.id.btnLoginFb, R.id.btnSkip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLoginPhoneNumber:
                S.I(WelcomeActivity.this, EnterMobileNumberActivity.class, null);
                break;
            case R.id.btnLoginFb:
                LoginManager.getInstance().logOut();
                signInFacebook();
                break;
            case R.id.btnSkip:
                Bundle bundle=new Bundle();
                bundle.putString("selected","home");
                S.I(WelcomeActivity.this, MainActivity.class, bundle);
                break;
        }
    }

    private void signInFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(
                this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
// App code
            }

            @Override
            public void onError(FacebookException exception) {
// App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        String name = "", email = "", profile = "";
                        try {
                            JSONObject jsonObject = new JSONObject(json_object.toString());
                            //http://graph.facebook.com/114413816964317/picture?type=large
                            name = jsonObject.getString("name");
                            email = jsonObject.getString("email");
                            profile = "http://graph.facebook.com/" + jsonObject.getString("id") + "/picture?type=large";


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        S.E(" json_object.toString()::" + json_object.toString());
                        S.E(" json_object.toString()::" + name);
                        S.E(" json_object.toString()::" + email);
                        facebookLoginApi(email, name, profile);

                        //{"id":"114413816964317","name":"Palms Tree","email":"palmstreetechnologies@gmail.com","picture":{"data":{"height":120,"is_silhouette":false,"url":"https:\/\/platform-lookaside.fbsbx.com\/platform\/profilepic\/?asid=114413816964317&height=120&width=120&ext=1594037037&hash=AeTm5DcGlWIVkC52","width":120}}}
               /* Intent intent = new Intent(WelcomeActivity.this, EnterMobileNumberActivity.class);
                intent.putExtra("userProfile", json_object.toString());
                startActivity(intent);*/
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    private void facebookLoginApi(String email, String name, String profile) {
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).facebookLogin(
                name,
                "",
                "",
                "",
                profile,
                "2",
                email
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
                            userDataModel.setUserType(data.getString("registration_type"));
                            UserDataHelper.getInstance().insertUserDataModel(userDataModel);
                            Bundle bundle=new Bundle();
                            bundle.putString("selected","home");
                            S.I(WelcomeActivity.this, MainActivity.class, bundle);
                        }
                    } else {
                        S.T(WelcomeActivity.this, jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}