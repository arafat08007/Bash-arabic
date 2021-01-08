package app.bash.data.data_model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Sohel Khan on 8/12/2017.
 */

public class UserDataModel {
    public static final String TABLE_NAME = "user";
    public static final String KEY_ID = "_id";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_EMAIL = "Email";
    public static final String KEY_PHONE = "Phone";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_FULL_NAME = "FullName";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_DOB = "dob";
    public static final String KEY_IS_VERIFY = "is_verify";
    public static final String KEY_DISPLAY_NAME = "display_name";
    public static final String KEY_COMPANY = "company";
    public static final String KEY_occupation = "occupation";
    public static final String KEY_gender = "gender";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_STATE = "state";
    public static final String KEY_CITY = "city";
    public static final String KEY_ZIPCODE = "zipcode";
    public static final String KEY_STATUS = "status";
    public static final String KEY_REFERRAL_CODE = "referral_code";

    public static void creteTable(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_ID + " text, "
                + KEY_USER_EMAIL + " text, "
                + KEY_PHONE + " text, "
                + KEY_IMAGE + " text, "
                + KEY_FULL_NAME + " text, "
                + KEY_USER_TYPE + " text, "
                + KEY_DOB + " text, "
                + KEY_IS_VERIFY + " text, "
                + KEY_DISPLAY_NAME + " text, "
                + KEY_COMPANY + " text, "
                + KEY_occupation + " text, "
                + KEY_gender + " text, "
                + KEY_COUNTRY + " text, "
                + KEY_STATE + " text, "
                + KEY_CITY + " text, "
                + KEY_ZIPCODE + " text, "
                + KEY_STATUS + " text, "
                + KEY_REFERRAL_CODE + " text "
                +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    private String userId, Email, phone, Image, FullName, userType, dob, isVerify, display_name, company, occupation, gender, country, state, city, zipcode, status,refferalcode;

    public String getRefferalcode() {
        return refferalcode;
    }

    public void setRefferalcode(String refferalcode) {
        this.refferalcode = refferalcode;
    }

    public String getDob() {
        return dob;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getoccupation() {
        return occupation;
    }

    public void setoccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getgender() {
        return gender;
    }

    public void setgender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(String isVerify) {
        this.isVerify = isVerify;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }
}