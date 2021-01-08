package app.bash.data.data_model;

import android.database.sqlite.SQLiteDatabase;

public class MyCartModel {
    public static final String TABLE_NAME = "mycart";
    public static final String KEY_ID = "id";
    public static final String KEY_SERVICE_ID = "serviceId";
    public static final String KEY_SERVICE_NAME = "servicename";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_CAT_NAME = "cat_name";
    public static final String KEY_QUANTITY = "quantity";
    public static void creteTable(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SERVICE_ID + " text, "
                + KEY_SERVICE_NAME + " text, "
                + KEY_DURATION + " text, "
                + KEY_AMOUNT + " text, "
                + KEY_CAT_NAME + " text, "
                + KEY_QUANTITY + " text " +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    String id, servicename, duration, amount,quantity,serviceId, catName;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
