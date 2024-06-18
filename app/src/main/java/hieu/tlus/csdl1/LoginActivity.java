package hieu.tlus.csdl1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import hieu.tlus.csdl1.model.Book;

public class LoginActivity extends AppCompatActivity {
    DBHelperDatabase dbh;
    SharedPreferences settings;
    TextView txtSignUp;
    Button btnexit;
    CheckBox ck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CheckBox ck;
        EditText txtusername,txtpw;
        Button btnlogin,btnthoat;
        CheckBox ckremember;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ck = findViewById(R.id.ckremember);
        txtusername = findViewById(R.id.txtuser);
        txtpw = findViewById(R.id.txtpass);
        btnlogin = findViewById(R.id.btnsignin);
        txtSignUp = findViewById(R.id.txtregister);
        btnexit = findViewById(R.id.btnthoat);
        ckremember = findViewById(R.id.ckremember);
        dbh =new DBHelperDatabase(this);

        settings = getSharedPreferences("Login", 0);
        String username = settings.getString("user", "");
        String password = settings.getString("pass", "");
        if(username.isEmpty()){

        }else{
            Intent in = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(in);
        }
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u=txtusername.getText().toString();
                String p=txtpw.getText().toString();
                String sql="select * from login where username='"+u+"' and password='"+p+"'";
                SQLiteDatabase db=dbh.ketNoiDBRead();
                Cursor cs = db.rawQuery(sql, null);
                if(cs.moveToNext()){
                    if(ckremember.isChecked()){
                        settings.edit().putString("user", u).apply();
                        settings.edit().putString("pass", p).apply();
                    }
                    Intent in = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(in);
                }else{
                    Toast.makeText(getApplicationContext(),"Tài khoản chưa đúng", Toast.LENGTH_LONG).show();
                }
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),NewRegisterActivity.class);
                startActivity(in);
            }
        });

    }
}