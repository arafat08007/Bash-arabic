package app.bash.data.data_helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import app.bash.data.DataManager;
import app.bash.data.data_model.MyCartModel;
import app.bash.utilitis.S;

public class MyCartHelper {
    private static MyCartHelper instance;
    private SQLiteDatabase db;
    private DataManager dm;
    private Context cx;

    public MyCartHelper(Context cx) {
        instance = this;
        this.cx = cx;
        dm = new DataManager(cx, DataManager.DATABASE_NAME, null, DataManager.DATABASE_VERSION);
    }

    private boolean isExist(MyCartModel myCartModel) {
        read();
        Cursor cur = db.rawQuery("select * from " + MyCartModel.TABLE_NAME + " where " + MyCartModel.KEY_SERVICE_ID + "='" + myCartModel.getServiceId() + "'", null);
        return cur.moveToFirst();
    }

    public static MyCartHelper getInstance() {
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

    public void insertMyCartModel(MyCartModel myCartModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(MyCartModel.KEY_ID, myCartModel.getId());
        values.put(MyCartModel.KEY_SERVICE_ID, myCartModel.getServiceId());
        values.put(MyCartModel.KEY_SERVICE_NAME, myCartModel.getServicename());
        values.put(MyCartModel.KEY_DURATION, myCartModel.getDuration());
        values.put(MyCartModel.KEY_AMOUNT, myCartModel.getAmount());
        values.put(MyCartModel.KEY_CAT_NAME, myCartModel.getCatName());
        values.put(MyCartModel.KEY_QUANTITY, myCartModel.getQuantity());


        if (!isExist(myCartModel)) {
            S.E("insert successfully");
            db.insert(MyCartModel.TABLE_NAME, null, values);
        } else {
            S.E("update successfully" + myCartModel.getId());
            db.update(MyCartModel.TABLE_NAME, values, MyCartModel.KEY_ID + "=" + myCartModel.getId(), null);
        }
        close();
    }

    public ArrayList<MyCartModel> getMyCartModel() {
        ArrayList<MyCartModel> userItem = new ArrayList<MyCartModel>();
        read();
        Cursor clientCur = db.rawQuery("select * from " + MyCartModel.TABLE_NAME, null);

        if (clientCur != null && clientCur.getCount() > 0) {
            clientCur.moveToFirst();
            do {
                //company,address1,address2,country,state,city,zipcode;
                MyCartModel myCartModel = new MyCartModel();
                myCartModel.setId(clientCur.getString(clientCur.getColumnIndex(MyCartModel.KEY_ID)));
                myCartModel.setServiceId(clientCur.getString(clientCur.getColumnIndex(MyCartModel.KEY_SERVICE_ID)));
                myCartModel.setServicename(clientCur.getString(clientCur.getColumnIndex(MyCartModel.KEY_SERVICE_NAME)));
                myCartModel.setAmount(clientCur.getString(clientCur.getColumnIndex(MyCartModel.KEY_AMOUNT)));
                myCartModel.setCatName(clientCur.getString(clientCur.getColumnIndex(MyCartModel.KEY_CAT_NAME)));
                myCartModel.setDuration(clientCur.getString(clientCur.getColumnIndex(MyCartModel.KEY_DURATION)));
                myCartModel.setQuantity(clientCur.getString(clientCur.getColumnIndex(MyCartModel.KEY_QUANTITY)));
               userItem.add(myCartModel);
            } while ((clientCur.moveToNext()));
            clientCur.close();
        }
        close();
        return userItem;
    }

    public void delete() {
        open();
        db.delete(MyCartModel.TABLE_NAME, null, null);
        close();
    }
}
