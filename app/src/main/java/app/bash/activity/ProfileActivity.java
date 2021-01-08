package app.bash.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.recyclerview.widget.LinearLayoutManager;

import app.bash.R;
import app.bash.adapter.HomeServiceAdapter;
import app.bash.adapter.ReviewsAdapter;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.data.data_model.UserDataModel;
import app.bash.model.HomeFirstModel;
import app.bash.model.OccupationModel;
import app.bash.model.ReviewModel;
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
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.imageView)
    CircleImageView imageView;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
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
    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.btnSave)
    Button btnSave;

    private int mYear, mMonth, mDay;
    ArrayList<OccupationModel> occupationModelArrayList = new ArrayList<>();
    ArrayList<String> occupationStr = new ArrayList<>();
    String OccupationId, selectedOccupationId;
    private Uri filePath;
    File imageFile;
    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,};
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String encodedImageBase64 = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        S.setStatusBarGradiant(this);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        if (SavedData.getLanguage().equals("ar")) {
            edtFullName.setGravity(View.FOCUS_LEFT);
            edtEmail.setGravity(View.FOCUS_LEFT);
            spinOccupation.setGravity(View.FOCUS_LEFT);
            spinGender.setGravity(View.FOCUS_LEFT);
            spinDateOfBirth.setGravity(View.FOCUS_LEFT);
            edtPhone.setGravity(View.FOCUS_LEFT);

            edtFullName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_name, 0);
            edtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_email, 0);

            spinOccupation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_occupation, 0);
            spinOccupation.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.icon_occupation, 0);

            spinGender.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_gender, 0);
            spinGender.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_gender, 0);

            spinDateOfBirth.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_dob, 0);
            spinDateOfBirth.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.icon_dob, 0);

            edtPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_phone, 0);
        } else {
            edtFullName.setGravity(View.FOCUS_RIGHT);
            edtEmail.setGravity(View.FOCUS_RIGHT);
            spinOccupation.setGravity(View.FOCUS_RIGHT);
            spinGender.setGravity(View.FOCUS_RIGHT);
            spinDateOfBirth.setGravity(View.FOCUS_RIGHT);
            edtPhone.setGravity(View.FOCUS_RIGHT);

            edtFullName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_name, 0, 0, 0);
            edtEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_email, 0, 0, 0);

            spinOccupation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_occupation, 0, 0, 0);
            spinOccupation.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.icon_occupation, 0, 0, 0);

            spinGender.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_gender, 0, 0, 0);
            spinGender.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_gender, 0, 0, 0);

            spinDateOfBirth.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_dob, 0, 0, 0);
            spinDateOfBirth.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.icon_dob, 0, 0, 0);

            edtPhone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_phone, 0, 0, 0);
        }

        if (!hasPermissions(PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
            }
        }

        if (UserDataHelper.getInstance().getUserDataModel().get(0).getUserType() != null) {
            if (UserDataHelper.getInstance().getUserDataModel().get(0).getUserType().equals("1")) {
                edtPhone.setClickable(false);
                edtPhone.setEnabled(false);
                edtPhone.setFocusable(false);
            } else {
                edtEmail.setClickable(false);
                edtEmail.setEnabled(false);
                edtEmail.setFocusable(false);
            }
        }

        setUserProfileData();

        if (NetworkUtil.isNetworkAvailable(ProfileActivity.this)) {
            getOccupation();
        } else {
            S.T(ProfileActivity.this, getString(R.string.no_internet_connection));
        }

    }

    private void setUserProfileData() {
        tvUserName.setText(UserDataHelper.getInstance().getUserDataModel().get(0).getFullName());
        edtFullName.setText(UserDataHelper.getInstance().getUserDataModel().get(0).getFullName());
        edtEmail.setText(UserDataHelper.getInstance().getUserDataModel().get(0).getEmail());
        if (UserDataHelper.getInstance().getUserDataModel().get(0).getPhone().equals("null")) {
            edtPhone.setText("");
        } else {
            edtPhone.setText(UserDataHelper.getInstance().getUserDataModel().get(0).getPhone());
        }
        spinDateOfBirth.setText(UserDataHelper.getInstance().getUserDataModel().get(0).getDob());
        if (UserDataHelper.getInstance().getUserDataModel().get(0).getgender() != null) {
            if (UserDataHelper.getInstance().getUserDataModel().get(0).getgender().equals("Female")) {
                // spinGender.setSelection(1);
                spinGender.setText("Female");
            } else {
                spinGender.setText("Male");
            }
        }
        Picasso.get().load(UserDataHelper.getInstance().getUserDataModel().get(0).getImage())
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder(R.drawable.ic_user_nav)
                .error(R.drawable.ic_user_nav)
                .into(imageView);

    }

    private void getGender() {
        String[] genders = new String[]{"Male", "Female"};
        ArrayAdapter<String> adapterGender =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        genders);

        spinGender.setAdapter(adapterGender);
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
                        S.T(ProfileActivity.this, jsonObject.getString("message"));
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
                        ProfileActivity.this,
                        R.layout.dropdown_menu_popup_item,
                        occupationStr);
        spinOccupation.setAdapter(adapterOccupations);
        OccupationId = UserDataHelper.getInstance().getUserDataModel().get(0).getoccupation();


        for (OccupationModel account : occupationModelArrayList) {

            if (account.getId().equals(OccupationId)) {
                spinOccupation.setText(account.getName());
                break;
            }
        }
        spinOccupation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                OccupationId = occupationModelArrayList.get(position).getId();
            }
        });
    }

    @OnClick({R.id.imgBack, R.id.btnSave, R.id.spinGender, R.id.spinOccupation, R.id.spinDateOfBirth, R.id.imageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                Bundle bundle = new Bundle();
                bundle.putString("selected", "home");
                S.I_clear(ProfileActivity.this, MainActivity.class, bundle);
                break;
            case R.id.imageView:
                if (UserDataHelper.getInstance().getUserDataModel().get(0).getUserType().equals("1")) {
                    selectImage();
                } else {
                    S.T(ProfileActivity.this, "You have login with FaceBook, You can't change the picture.");
                }
                break;
            case R.id.spinGender:
                getGender();
                break;
            case R.id.spinDateOfBirth:
                spinDateOfBirth.setEnabled(false);
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                spinDateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
                dpd.show();
                spinDateOfBirth.setEnabled(true);
                break;
            case R.id.spinOccupation:
                ArrayAdapter<String> adapterOccupations =
                        new ArrayAdapter<>(
                                ProfileActivity.this,
                                R.layout.dropdown_menu_popup_item,
                                occupationStr);
                spinOccupation.setAdapter(adapterOccupations);
                break;
            case R.id.btnSave:
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
                            if (UserDataHelper.getInstance().getUserDataModel().get(0).getUserType().equals("1")) {
                                updateProfileMultipart();
                            } else {
                                updateProfileFb();
                            }
                        } else {
                            S.T(this, getString(R.string.no_internet_connection));
                        }


                        //S.I(CreateAccountActivity.this, MainActivity.class, null);
                    }
                }
                break;
        }
    }

    public boolean hasPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                Toast.makeText(ProfileActivity.this, "Some Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 1);//zero can be replaced with any action code
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                Uri contentURI = getImageUri(thumbnail);
                imageView.setImageBitmap(thumbnail);
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(contentURI);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImageBase64 = encodeImage(compressBitmap(selectedImage));
            } else if (requestCode == 2) {
                if (data != null) {
                    Uri contentURI = data.getData();

                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(contentURI);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(selectedImage);
                    encodedImageBase64 = resizeBase64Image(encodeImage(compressBitmap(selectedImage)));
                    S.E(encodedImageBase64);
                }
            }
        }
    }

    public Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

        byte[] byteArray = stream.toByteArray();
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        return compressedBitmap;
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String resizeBase64Image(String base64image) {
        byte[] encodeByte = Base64.decode(base64image.getBytes(), Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);


        if (image.getHeight() <= 400 && image.getWidth() <= 400) {
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, 200, 200, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);

        byte[] b = baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);

    }

    private void updateProfileMultipart() {
        new JSONParser(ProfileActivity.this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).updateProfile(
                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId(),
                edtFullName.getText().toString(),
                edtEmail.getText().toString(),
                OccupationId,
                spinGender.getText().toString(),
                spinDateOfBirth.getText().toString(),
                encodedImageBase64
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
                            Bundle bundle = new Bundle();
                            bundle.putString("selected", "home");
                            S.I(ProfileActivity.this, MainActivity.class, bundle);
                        }
                    } else {
                        S.T(ProfileActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateProfileFb() {
        new JSONParser(ProfileActivity.this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).updateProfileFbUser(
                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId(),
                edtFullName.getText().toString(),
                OccupationId,
                spinGender.getText().toString(),
                spinDateOfBirth.getText().toString(),
                edtPhone.getText().toString(),
                encodedImageBase64
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
                            Bundle bundle = new Bundle();
                            bundle.putString("selected", "home");
                            S.I(ProfileActivity.this, MainActivity.class, bundle);
                        }
                    } else {
                        S.T(ProfileActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getProfile() {
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).getProfile(
                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId()
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        /*JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.optJSONObject(i);
                            ReviewModel reviewModel = new ReviewModel();
                            reviewModel.setId(data.getString("id"));
                            reviewModel.setStatus(data.getString("status"));
                            reviewModel.setService_id(data.getString("service_id"));
                            reviewModel.setUser_name(data.getString("user_name"));
                            reviewModel.setUser_id(data.getString("user_id"));
                            reviewModel.setRating_star(data.getString("rating_star"));
                            reviewModel.setReview(data.getString("review"));
                            reviewModel.setUpdated_at(data.getString("updated_at"));
                            reviewModel.setProfile(data.getString("profile"));
                            reviewModel.setService_name(data.getString("service_name"));
                            reviewModelArrayList.add(reviewModel);
                        }
                        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(ReviewsActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerviewReviews.setLayoutManager(horizontalLayoutManagaer);
                        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(ReviewsActivity.this, reviewModelArrayList);
                        recyclerviewReviews.setAdapter(reviewsAdapter);*/
                    } else {
                        S.T(ProfileActivity.this, jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}