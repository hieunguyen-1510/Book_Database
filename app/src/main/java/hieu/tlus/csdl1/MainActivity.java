package hieu.tlus.csdl1;

import static android.Manifest.permission_group.CAMERA;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import hieu.tlus.csdl1.model.Book;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText bookid, bookname, bookpage, bookprice, description, txts;

    Cursor cs;

    Button btnveke, btnvetruoc;
    Button btnthem, btnsua, btnxoa, btnluu, btnlamlai;
    int flag = 0;
    SQLiteDatabase db;

    ListView lvbook;
    ArrayList<Book> arrbook;

    DBHelperDatabase dbh;

    CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("Chi tiết của sách");
        toolbar.setBackgroundColor(Color.parseColor("#FFFF00"));
        setSupportActionBar(toolbar);
        dbh = new DBHelperDatabase(this);
        initView();
        cs = dbh.initRecordFirstDB();
        updateRecord();
        showDataListView();

    }
    private void showDialogconfirmSignOut(){
        //Tạo đối tượng
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Xác nhận");
        b.setMessage("Bạn có đồng ý signout không ?");
// Nút Ok
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                SharedPreferences settings = getSharedPreferences("Login", MODE_PRIVATE);
                settings.edit().clear().commit();
                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(in);
                Toast.makeText(getApplicationContext(), "SignOut thành công", Toast.LENGTH_LONG).show();
            }
        });
//Nút Cancel
        b.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
//Tạo dialog
        AlertDialog al = b.create();
//Hiển thị
        al.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }else if(item.getItemId() == R.id.textsignout){
            showDialogconfirmSignOut();
        }else if(item.getItemId() == R.id.sign_out){
            SharedPreferences settings = getSharedPreferences("Login", MODE_PRIVATE);
            settings.edit().clear().commit();
            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDataListView(){
        db = dbh.ketNoiDBRead();
        String s = txts.getText().toString();
        arrbook =new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM BOOKS where bookid  like  '%"+s+"%' or BookName like '%"+s+"%'", null);
        try {
            while (cursor.moveToNext()) {
                Book b=new Book(cursor.getString(0),cursor.getString(1),Integer.parseInt(cursor.getString(2)),Float.parseFloat(cursor.getString(3)),cursor.getString(4),cursor.getString(5));
                Log.d("bookssss",b.toString());
                arrbook.add(b);
                adapter= new CustomAdapter(this, arrbook);
                lvbook.setAdapter(adapter);
            }

        } finally {
            cursor.close();
        }
    }
    void initDB(){
        try {
            db = openOrCreateDatabase("books.db", MODE_PRIVATE, null);
            cs = db.rawQuery("SELECT * FROM BOOKS", null);
        }
        catch (Exception e) {
            finish();
        }
        cs.moveToNext();

        updateRecord();
    }
    void updateRecord() {
        bookid.setText(cs.getString(0));
        bookname.setText(cs.getString(1));
        bookpage.setText(cs.getString(2));
        bookprice.setText(cs.getString(3));
        description.setText(cs.getString(4));

        btnvetruoc.setEnabled(!cs.isFirst());
        btnveke.setEnabled(!cs.isLast());
    }
    void initView(){
        bookid = (EditText) findViewById(R.id.book_id);
        bookname = (EditText) findViewById(R.id.book_name);
        bookpage = (EditText) findViewById(R.id.book_page);
        bookprice = (EditText) findViewById(R.id.book_price);
        description = (EditText) findViewById(R.id.book_des);
        btnvetruoc = (Button) findViewById(R.id.btnvetruoc);
        btnveke = (Button) findViewById(R.id.btnveke);

        btnthem = (Button) findViewById(R.id.btnthem);
        btnsua = (Button) findViewById(R.id.btnsua);
        btnxoa = (Button) findViewById(R.id.btnxoa);
        btnluu = (Button) findViewById(R.id.btnluu);
        btnlamlai = (Button) findViewById(R.id.btnlamlai);
        lvbook = (ListView) findViewById(R.id.lvbook);
        txts = (EditText) findViewById(R.id.txts);
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1; //them
                resetView();
                bookid.requestFocus();
                btnthem.setEnabled(false);
                btnsua.setEnabled(false);
            }
        });

        btnlamlai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetView();
                flag=0;
                btnthem.setEnabled(true);
                btnsua.setEnabled(true);
            }
        });
        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=2;//sua du lieu
                btnthem.setEnabled(false);
                btnsua.setEnabled(false);
            }
        });
        btnluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==1){
                    Log.d("thao tac: ","them"+flag );
                    String bookId = bookid.getText().toString();
                    String bookName = bookname.getText().toString();
                    String bookPage = bookpage.getText().toString();
                    String bookPrice = bookprice.getText().toString();
                    String des = description.getText().toString();

                    String sql = "INSERT INTO BOOKS VALUES('"+bookId+"','"+bookName+"','"+bookPage+"','"+bookPrice+"','"+des+"')";
                    try {
//                        SQLiteDatabase db1 = openOrCreateDatabase("books.db", MODE_PRIVATE, null);
                        SQLiteDatabase db1 = dbh.ketNoiDBWrite();
                        db1.execSQL(sql);
                        //db1.close();
                        showDataListView();
                        Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Khong thanh cong", Toast.LENGTH_LONG).show();
                    }
                }else if(flag==2){
                    Log.d("thao tac: ","sua" );
                    String bookId = bookid.getText().toString();
                    String bookName = bookname.getText().toString();
                    String bookPage = bookpage.getText().toString();
                    String bookPrice = bookprice.getText().toString();
                    String des = description.getText().toString();
                    String sql = "UPDATE BOOKS SET  bookname='"+bookName+"', Page='"+bookPage+"',Price='"+bookPrice+"',Description='"+des+"'where BookID='"+bookId+"'";
                    try {
                        SQLiteDatabase db1 = openOrCreateDatabase("books.db", MODE_PRIVATE, null);
                        db1.execSQL(sql);
                        //db1.close();
                        showDataListView();
                        Toast.makeText(getApplicationContext(), "Sua thành công", Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Khong thanh cong", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "chua chon thao tac", Toast.LENGTH_LONG);
                }
            }
        });
        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookId=bookid.getText().toString();
                String Namebook=bookname.getText().toString();
                showDialogconfirm(Namebook,bookId);
            }
        });
        txts.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                showDataListView();
                return false;
            }
        });
    }
    private void showDialogconfirm(String namebook, String bookId){
        //Tạo đối tượng
        AlertDialog.Builder b = new AlertDialog.Builder(this);
//Thiết lập tiêu đề
        b.setTitle("Xác nhận");
        b.setMessage("Bạn có đồng ý xóa mục  "+namebook+" này không ?");
// Nút Ok
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                String sql = "DELETE FROM BOOKS WHERE BookID = '"+bookId+"'";
                SQLiteDatabase db = openOrCreateDatabase("books.db", MODE_PRIVATE, null);
                db.execSQL(sql);
                db.close();
                showDataListView();
                Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_LONG).show();
//                data.remove(pst);
//                adapter.notifyDataSetChanged();
            }
        });
//Nút Cancel
        b.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
//Tạo dialog
        AlertDialog al = b.create();
//Hiển thị
        al.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200){
            showDataListView();
        }
    }

    void resetView(){
        bookid.setText("");
        bookname.setText("");
        bookpage.setText("");
        bookprice.setText("");
        description.setText("");
    }
//    void createBookDatabase() {
//        String sqltext = "DROP TABLE IF EXISTS BOOKS;\n"
//                + "CREATE TABLE BOOKS(BookID integer PRIMARY KEY, BookName text, Page integer, Price Float, Description text, Bookimage text);\n"
//                + "INSERT INTO BOOKS VALUES(1, 'Java', 100, 9.99, 'sách về java', '');\n"
//                + "INSERT INTO BOOKS VALUES(2, 'Android', 320, 19.00, 'Android cơ bản', '');\n"
//                + "INSERT INTO BOOKS VALUES(3, 'Học làm giàu', 120, 0.99, 'sách đọc cho vui', '');\n"
//                + "INSERT INTO BOOKS VALUES(4, 'Tử điển Anh-Việt', 1000, 29.50, 'Từ điển 100.000 từ', '');\n"
//                + "INSERT INTO BOOKS VALUES(5, 'CNXH', 1, 1, 'chuyện cổ tích', '');";
//        // tạo DB và thực hiện một số câu SQL
//        SQLiteDatabase db = openOrCreateDatabase("books.db", MODE_PRIVATE, null);
//        for (String sql : sqltext.split("\n"))
//            db.execSQL(sql);
//        db.close();
//    }

    public void VeKe(View v) {
        cs.moveToNext();
        updateRecord();
    }
    public void VeTruoc(View v) {
        cs.moveToPrevious();
        updateRecord();

    }
    public void VeCuoi(View v) {
        cs.moveToLast();
        updateRecord();

    }
    public void VeDau(View v) {
        cs.moveToFirst();
        updateRecord();

    }

}