package com.example.DoFireBase02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.DoFireBase02.models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Button btnDangNhap,btnDangKy;
    final String tenBang="Users";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        anhXa();
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        progressDialog=new ProgressDialog(this);
        String u=Paper.book().read(CongCu.UserKey);
        String p=Paper.book().read(CongCu.PassKey);
        if(u!=""&&p!="")
        {
            if(!TextUtils.isEmpty(u)&&!TextUtils.isEmpty(p))
            {
                progressDialog.setTitle("Tu dong dang nhap");
                progressDialog.setMessage("Vui long cho cho...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                tuDongDangNhap(u,p);
            }
        }
    }

    private void tuDongDangNhap(String phone, String pass) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.child(tenBang).child(phone).exists())
                {
                    Users usersHT=snapshot.child(tenBang).child(phone).getValue(Users.class);
                    if(usersHT.getPhone().equals(phone))
                    {
                        if(usersHT.getPass().equals(pass)) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Dang nhap thanh cong...",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Sai mat khau...",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Tai khoan khong ton tai...",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void anhXa() {
        btnDangKy=findViewById(R.id.btn_thamgia);
        btnDangNhap=findViewById(R.id.btn_login);
    }
}