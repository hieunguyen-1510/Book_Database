package hieu.tlus.csdl1;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;

import hieu.tlus.csdl1.model.Book;

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<Book> {
    private ArrayList<Book> arrbook;
    private final Activity context;
    private int lastPosition = -1;
    public CustomAdapter(Activity context, ArrayList<Book> arrbooks){
        super(context, R.layout.item_book, arrbooks);
        this.context = context;
        this.arrbook =arrbooks;
    }

    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_book,null, true);
        Book book = arrbook.get(position);

        TextView txtmabook =(TextView) rowView.findViewById(R.id.txtmasach);
        ImageView imgbook = (ImageView) rowView.findViewById(R.id.imgbook);
        TextView txttenbook = rowView.findViewById(R.id.txttensach);
        TextView txtgia = rowView.findViewById(R.id.txtgia);
        txtmabook.setText(arrbook.get(position).getBookid());
        txttenbook.setText(arrbook.get(position).getBookname());
        txtgia.setText(""+arrbook.get(position).getBookprice());
        Button btnchitiet = rowView.findViewById(R.id.btnchitiet);
        Log.d("chuoihinhanh",""+StringToBitMap(book.getAnhbook()));
        imgbook.setImageBitmap(StringToBitMap(book.getAnhbook()));
        btnchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, DeTailActivity.class);
                in.putExtra("ObjectDetails", book);
                context.startActivityForResult(in, 200);
                //context.startActivity(in);
            }
        });

        Button btndelete = rowView.findViewById(R.id.btndelete);
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogConfirm(book, position);
            }
        });

        //Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //rowView.startAnimation(animation);
        lastPosition = position;
//        rowView.setOnClickListener(new View.OnClickListener() {

//            @Override
//            public void onClick(View v) {
//                //Log.d("position",""+ arrbook.get(position).());
//                Intent in = new Intent(context, BooksDetail.class);
//                context.startActivity(in);
//                in.putExtra("MASV", arrbook.get(position).getId());
//                in.putExtra("TENSV", arrbook.get(position).getName());
//                in.putExtra("ANHSV", arrbook.get(position).getPrice());
//                context.startActivity(in);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText bookid= rowView.getRootView().findViewById(R.id.book_id);
                EditText bookname= rowView.getRootView().findViewById(R.id.book_name);
                EditText bookpage= rowView.getRootView().findViewById(R.id.book_page);
                EditText bookprice= rowView.getRootView().findViewById(R.id.book_price);
                EditText bookdes= rowView.getRootView().findViewById(R.id.book_des);
                bookid.setText(arrbook.get(position).getBookid());
                bookname.setText(arrbook.get(position).getBookname());
                bookpage.setText(""+arrbook.get(position).getBookpage());
                bookprice.setText(""+arrbook.get(position).getBookprice());
                bookdes.setText(arrbook.get(position).getBookdescription());
            }
        });


        return rowView;

    };
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    private void showDialogConfirm(Book book, int position) {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Xác nhận");
        b.setMessage("Bạn có đồng ý xóa mục " + book.getBookname() + " này không?");
        b.setPositiveButton("Đồng ý", (dialog, id) -> deleteBook(book.getBookid(), position));
        b.setNegativeButton("Không đồng ý", (dialog, id) -> dialog.cancel());
        AlertDialog al = b.create();
        al.show();
    }
    private void deleteBook(String bookId, int position) {
        try {
            String sql = "DELETE FROM BOOKS WHERE BookID = '" + bookId + "'";
            SQLiteDatabase db = context.openOrCreateDatabase("books.db", MODE_PRIVATE, null);
            db.execSQL(sql);
            arrbook.remove(position); // Cập nhật danh sách
            notifyDataSetChanged(); // Thông báo cho adapter để cập nhật giao diện
            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Không thành công", Toast.LENGTH_LONG).show();
        }
    }
}