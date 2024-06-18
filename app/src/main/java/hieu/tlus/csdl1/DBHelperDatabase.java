package hieu.tlus.csdl1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DBHelperDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "managebooks.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_BOOK = "books";
    private static final String TABLE_LOGIN = "login";
    public DBHelperDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //        String sqltext = "CREATE TABLE BOOKS(BookID integer PRIMARY KEY, BookName text, Page integer, Price Float, Description text, bookimage text,NXBID integer);\n"
//        +"CREATE TABLE NHAXB(NXBID integer PRIMARY KEY,NXBNAME text);\n"
        String sqltext = "CREATE TABLE BOOKS(BookID integer PRIMARY KEY, BookName text, Page integer, Price Float, Description text, bookimage text);\n"
                + "INSERT INTO BOOKS VALUES(1, 'Java', 100, 9.99, 'sách về java','');\n"
                + "INSERT INTO BOOKS VALUES(2, 'Android', 320, 19.00, 'Android cơ bản','');\n"
                + "INSERT INTO BOOKS VALUES(3, 'Học làm giàu', 120, 0.99, 'sách đọc cho vui','');\n"
                + "INSERT INTO BOOKS VALUES(4, 'Tử điển Anh-Việt', 1000, 29.50, 'Từ điển 100.000 từ','');\n"
                + "INSERT INTO BOOKS VALUES(5, 'CNXH', 1, 1, 'chuyện cổ tích','');";


        for (String sql : sqltext.split("\n"))
            db.execSQL(sql);
        String sqlLogin = "CREATE TABLE login(username text PRIMARY KEY, password text, fullname text, sex text, date_of_birth int);";
        db.execSQL(sqlLogin);
//        String sqlInsert = "INSERT INTO LOGIN values('tien','1')";
//        db.execSQL(sqlInsert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Xoá bảng cũ
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        //Tiến hành tạo bảng mới
        onCreate(db);

    }

    Cursor initRecordFirstDB(){
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cs = db.rawQuery("SELECT * FROM BOOKS", null);
            cs.moveToNext();
            return cs;
        }
        catch (Exception io) {

        }
        return null;
    }

    SQLiteDatabase ketNoiDBRead() {
        SQLiteDatabase db = getReadableDatabase();
        return db;
    }
    SQLiteDatabase ketNoiDBWrite() {
        SQLiteDatabase db = getWritableDatabase();
        return db;
    }


}