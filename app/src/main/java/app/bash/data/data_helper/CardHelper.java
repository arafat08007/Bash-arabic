package app.bash.data.data_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import app.bash.data.DataManager;
import app.bash.data.data_model.CardModel;
import app.bash.utilitis.S;

public class CardHelper {
    private static CardHelper instance;
    private SQLiteDatabase db;
    private DataManager dm;
    private Context cx;

    public CardHelper(Context cx) {
        instance = this;
        this.cx = cx;
        dm = new DataManager(cx, DataManager.DATABASE_NAME, null, DataManager.DATABASE_VERSION);
    }

    private boolean isExist(CardModel cardModel) {
        read();
        Cursor cur = db.rawQuery("select * from " + CardModel.TABLE_NAME + " where " + CardModel.KEY_CARD_NUMBER + "='" + cardModel.getCardnumber() + "'", null);
        return cur.moveToFirst();
    }

    public static CardHelper getInstance() {
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

    public void insertCardModel(CardModel cardModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(CardModel.KEY_ID, cardModel.getId());
        values.put(CardModel.KEY_CARD_NUMBER, cardModel.getCardnumber());
        values.put(CardModel.KEY_HOLDER_NAME, cardModel.getHoldername());
        values.put(CardModel.KEY_EXPIRE_DATE, cardModel.getExpiredate());

        if (!isExist(cardModel)) {
            S.E("insert successfully");
            db.insert(CardModel.TABLE_NAME, null, values);
        } else {
            S.E("update successfully" + cardModel.getId());
            db.update(CardModel.TABLE_NAME, values, CardModel.KEY_CARD_NUMBER + "=" + cardModel.getCardnumber(), null);
        }
        close();
    }

    public ArrayList<CardModel> getCardModel() {
        ArrayList<CardModel> userItem = new ArrayList<CardModel>();
        read();
        Cursor clientCur = db.rawQuery("select * from " + CardModel.TABLE_NAME, null);

        if (clientCur != null && clientCur.getCount() > 0) {
            clientCur.moveToFirst();
            do {
                //company,address1,address2,country,state,city,zipcode;
                CardModel cardModel = new CardModel();
                cardModel.setId(clientCur.getString(clientCur.getColumnIndex(CardModel.KEY_ID)));
                cardModel.setCardnumber(clientCur.getString(clientCur.getColumnIndex(CardModel.KEY_CARD_NUMBER)));
                cardModel.setHoldername(clientCur.getString(clientCur.getColumnIndex(CardModel.KEY_HOLDER_NAME)));
                cardModel.setExpiredate(clientCur.getString(clientCur.getColumnIndex(CardModel.KEY_EXPIRE_DATE)));
               userItem.add(cardModel);
            } while ((clientCur.moveToNext()));
            clientCur.close();
        }
        close();
        return userItem;
    }

    public void delete() {
        open();
        db.delete(CardModel.TABLE_NAME, null, null);
        close();
    }
}
