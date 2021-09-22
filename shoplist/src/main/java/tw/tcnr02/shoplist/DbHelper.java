package tw.tcnr02.shoplist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

////----------------------------------------------------------
//建構式參數說明：
//context   可以操作資料庫的內容本文，一般可直接傳入Activity物件。
//name  要操作資料庫名稱，如果資料庫不存在，會自動被建立出來並呼叫onCreate()方法。
//factory  用來做深入查詢用，入門時用不到。
//version  版本號碼。
////-----------------------
public class DbHelper extends SQLiteOpenHelper {
    String TAG = "tcnr02=>";
    public String sCreateTableCommand;    // 資料庫名稱
    private static final String DB_FILE = "Fridge.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;    // 資料表名稱
    private static final String DB_TABLE_Shoplist01 = "shoplist01";
    private static final String DB_TABLE_Shoplist02 = "shoplist02";    // 資料庫物件，固定的欄位變數
    private static final String crTBsql_Shoplist01 = "CREATE TABLE " + DB_TABLE_Shoplist01 + " ( "
            + "id INTEGER PRIMARY KEY," + "shop_title TEXT NOT NULL," + "shoplist_contain TEXT);";

    private static final String crTBsql_Shoplist02 = "CREATE TABLE " + DB_TABLE_Shoplist02 + " ( "
            + "id INTEGER PRIMARY KEY," + "titleID INTEGER NOT NULL," + "shoplist_item TEXT);";
    private static SQLiteDatabase database;

    //----------------------------------------------
    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new DbHelper(context, DB_FILE, null, VERSION).getWritableDatabase();
        }
        return database;
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
        super(context, "Fridge.db", null, 1);
        sCreateTableCommand = "";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crTBsql_Shoplist01);
        db.execSQL(crTBsql_Shoplist02);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_Shoplist01);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_Shoplist02);
        onCreate(db);
    }

    //===========Shoplist01的新增資料===========
    public long insertRec_Shoplist01(String b_title, String b_recipe_text) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("shoplist_item", b_title);
        rec.put("shoplist_contain", b_recipe_text);

        long rowID = db.insert(DB_TABLE_Shoplist01, null, rec);
        db.close();
        return rowID;
    }

    //===========維尼的新增資料===========
    //    ContentValues values
    public long insertRec_Shoplist01(ContentValues newRow) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_Shoplist01, null, newRow);
        db.close();
        return rowID;
    }

    public int RecCount_Shoplist01() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Shoplist01;
        Cursor recSet = db.rawQuery(sql, null);
        int count = recSet.getCount();
        recSet.close();
        return count;
    }

    public ArrayList<String> getRecSet_Shoplist01() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Shoplist01;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        recSet.moveToFirst();
        String fldSet = "";

        if (recSet.getCount() != 0) { // 判斷資料如果 不是0比 才執行抓資料
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#"; // 欄位跟欄位 用 # 做區隔
            recAry.add(fldSet);
        }

        while (recSet.moveToNext()) {
            fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }
//-------------------------

    public int clearRec_Shoplist01() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Shoplist01;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//   String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_Shoplist01, "1", null);
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }


    public int updateRec_Shoplist01(String b_id, String b_title, String b_recipe_text) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Shoplist01;
        Cursor recSet = db.rawQuery(sql, null);

        if (recSet.getCount() != 0) {
            ContentValues rec = new ContentValues();
//   rec.put("id", b_id);
            rec.put("shop_title", b_title);
            rec.put("shoplist_contain", b_recipe_text);


            String whereClause = "id='" + b_id + "'";

            int rowsAffected = db.update(DB_TABLE_Shoplist01, rec, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public int deleteRec_Shoplist01(String b_id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Shoplist01;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            String whereClause = "id='" + b_id + "'";
            int rowsAffected = db.delete(DB_TABLE_Shoplist01, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public ArrayList<String> getRecSet_query_Shoplist01(String ttitle, String trecipe_text) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Shoplist01 +
                " WHERE shop_title LIKE ? AND shoplist_contain LIKE ? ORDER BY id ASC";
        String[] args = new String[]{"%%",
                "%%"};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }


    //===========Shoplist01的區塊結束，勿動===========

    //===========現在開始為Shoplist02的區塊，勿動===========
    public long insertRec_Shoplist02_m(String b_title, String b_recipe_text) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();
        rec.put("titleID", b_title);
        rec.put("shoplist_item", b_recipe_text);


        long rowID = db.insert(DB_TABLE_Shoplist02, null, rec);
        db.close();
        return rowID;
    }

    //===========維尼的新增資料===========
    //    ContentValues values
    public long insertRec_Shoplist02(ContentValues newRow_s02) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_Shoplist02, null, newRow_s02);
        db.close();
        return rowID;
    }

    public int RecCount_Shoplist02() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Shoplist02;
        Cursor recSet = db.rawQuery(sql, null);
        int count = recSet.getCount();
        recSet.close();
        return count;
    }

    public ArrayList<String> getRecSet_Shoplist02() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Shoplist02;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        recSet.moveToFirst();
        String fldSet = "";

        if (recSet.getCount() != 0) { // 判斷資料如果 不是0比 才執行抓資料
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#"; // 欄位跟欄位 用 # 做區隔
            recAry.add(fldSet);
        }

        while (recSet.moveToNext()) {
            fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }
//-------------------------

    public int clearRec_Shoplist02() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Shoplist02;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//   String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_Shoplist02, "1", null);
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }


    public int updateRec_Shoplist02(String b_id, String b_title, String b_recipe_text) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Shoplist02;
        Cursor recSet = db.rawQuery(sql, null);

        if (recSet.getCount() != 0) {
            ContentValues rec = new ContentValues();
//   rec.put("id", b_id);
            rec.put("titleID", b_title);
            rec.put("shoplist_item", b_recipe_text);


            String whereClause = "id='" + b_id + "'";

            int rowsAffected = db.update(DB_TABLE_Shoplist02, rec, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public int deleteRec_Shoplist02(String b_id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Shoplist02;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            String whereClause = "id='" + b_id + "'";
            int rowsAffected = db.delete(DB_TABLE_Shoplist02, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public ArrayList<String> getRecSet_query_Shoplist02(String get_titleID) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Shoplist02 +
                " WHERE titleID LIKE ?  ORDER BY id ASC";
        String[] args = new String[]{"%" + get_titleID + "%"};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }
//===========Shoplist02的區塊結束，勿動===========
}
