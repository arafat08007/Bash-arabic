package app.bash.data.data_helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import app.bash.data.DataManager;
import app.bash.data.data_model.UserDataModel;
import app.bash.utilitis.S;

/**
 * Created by androidsys1 on 8/12/2017.
 */

public class UserDataHelper {
    @SuppressLint("StaticFieldLeak")
    private static UserDataHelper instance;
    private SQLiteDatabase db;
    private DataManager dm;
    private Context cx;

    public UserDataHelper(Context cx) {
        instance = this;
        this.cx = cx;
        dm = new DataManager(cx, DataManager.DATABASE_NAME, null, DataManager.DATABASE_VERSION);
    }

    private boolean isExist(UserDataModel userDataModel) {
        read();
        @SuppressLint("Recycle") Cursor cur = db.rawQuery("select * from " + UserDataModel.TABLE_NAME + " where " + UserDataModel.KEY_USER_ID + "='" + userDataModel.getUserId() + "'", null);
        return cur.moveToFirst();
    }

    public static UserDataHelper getInstance() {
        return instance;
    }

    private void open() {
        db = dm.getWritableDatabase();
    }

    private void close() {
        db.close();
    }

    private void read() {
        db = dm.getReadableDatabase();
    }

    public void insertUserDataModel(UserDataModel userDataModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(UserDataModel.KEY_USER_ID, userDataModel.getUserId());
        values.put(UserDataModel.KEY_USER_EMAIL, userDataModel.getEmail());
        values.put(UserDataModel.KEY_PHONE, userDataModel.getPhone());
        values.put(UserDataModel.KEY_IMAGE, userDataModel.getImage());
        values.put(UserDataModel.KEY_FULL_NAME, userDataModel.getFullName());
        values.put(UserDataModel.KEY_USER_TYPE, userDataModel.getUserType());
        values.put(UserDataModel.KEY_DOB, userDataModel.getDob());
        values.put(UserDataModel.KEY_IS_VERIFY, userDataModel.getIsVerify());
        values.put(UserDataModel.KEY_DISPLAY_NAME, userDataModel.getDisplay_name());
        values.put(UserDataModel.KEY_COMPANY, userDataModel.getCompany());
        values.put(UserDataModel.KEY_gender, userDataModel.getgender());
        values.put(UserDataModel.KEY_occupation, userDataModel.getoccupation());
        values.put(UserDataModel.KEY_COUNTRY, userDataModel.getCountry());
        values.put(UserDataModel.KEY_STATE, userDataModel.getState());
        values.put(UserDataModel.KEY_CITY, userDataModel.getCity());
        values.put(UserDataModel.KEY_ZIPCODE, userDataModel.getZipcode());
        values.put(UserDataModel.KEY_STATUS, userDataModel.getStatus());
        values.put(UserDataModel.KEY_REFERRAL_CODE, userDataModel.getRefferalcode());

        if (!isExist(userDataModel)) {
            S.E("insert successfully");
            db.insert(UserDataModel.TABLE_NAME, null, values);
        } else {
            S.E("update successfully" + userDataModel.getUserId());
            db.update(UserDataModel.TABLE_NAME, values, UserDataModel.KEY_USER_ID + "=" + userDataModel.getUserId(), null);
        }
        close();
    }

    public ArrayList<UserDataModel> getUserDataModel() {
        ArrayList<UserDataModel> userItem = new ArrayList<UserDataModel>();
        read();
        Cursor clientCur = db.rawQuery("select * from " + UserDataModel.TABLE_NAME, null);

        if (clientCur != null && clientCur.getCount() > 0) {
            clientCur.moveToFirst();
            do {
                //company,address1,address2,country,state,city,zipcode;
                UserDataModel userDataModel = new UserDataModel();
                userDataModel.setUserId(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_USER_ID)));
                userDataModel.setEmail(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_USER_EMAIL)));
                userDataModel.setPhone(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_PHONE)));
                userDataModel.setImage(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_IMAGE)));
                userDataModel.setFullName(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_FULL_NAME)));
                userDataModel.setUserType(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_USER_TYPE)));
                userDataModel.setDob(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_DOB)));
                userDataModel.setIsVerify(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_IS_VERIFY)));
                userDataModel.setDisplay_name(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_DISPLAY_NAME)));
                userDataModel.setCompany(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_COMPANY)));
                userDataModel.setgender(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_gender)));
                userDataModel.setoccupation(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_occupation)));
                userDataModel.setCountry(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_COUNTRY)));
                userDataModel.setState(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_STATE)));
                userDataModel.setCity(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_CITY)));
                userDataModel.setZipcode(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_ZIPCODE)));
                userDataModel.setStatus(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_STATUS)));
                userDataModel.setRefferalcode(clientCur.getString(clientCur.getColumnIndex(UserDataModel.KEY_REFERRAL_CODE)));
                userItem.add(userDataModel);
            } while ((clientCur.moveToNext()));
            clientCur.close();
        }
        close();
        return userItem;
    }

    public void delete() {
        open();
        db.delete(UserDataModel.TABLE_NAME, null, null);
        close();
    }
}