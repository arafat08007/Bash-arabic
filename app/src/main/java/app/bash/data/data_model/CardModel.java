package app.bash.data.data_model;

import android.database.sqlite.SQLiteDatabase;

public class CardModel {
    public static final String TABLE_NAME = "card";
    public static final String KEY_ID = "id";
    public static final String KEY_CARD_NUMBER = "cardnumber";
    public static final String KEY_HOLDER_NAME = "holdername";
    public static final String KEY_EXPIRE_DATE = "expiredate";

    public static void creteTable(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_CARD_NUMBER + " text, "
                + KEY_HOLDER_NAME + " text, "
                + KEY_EXPIRE_DATE + " text " +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    String id, cardnumber, holdername, expiredate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getHoldername() {
        return holdername;
    }

    public void setHoldername(String holdername) {
        this.holdername = holdername;
    }

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate;
    }
}
