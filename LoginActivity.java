package com.example.DoFireBase02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DoFireBase02.models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    EditText txtname, txtPass;
    Button btnLogin;
    private ProgressDialog progressDialog;
    static String tenBang="Users";
    //static String tenBang="Admins";
    private CheckBox ckNho;
    TextView txtAdmin;
    TextView txtNotAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();
        Paper.init(this);
        progressDialog=new ProgressDialog(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        txtAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setText("Dang nhap Quan tri");
                txtAdmin.setVisibility(View.INVISIBLE);
                txtNotAdmin.setVisibility(View.VISIBLE);
                tenBang="Admins";
            }
        });
        txtNotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                btnLogin.setText("Dang nhap");
                txtAdmin.setVisibility(View.VISIBLE);
                txtNotAdmin.setVisibility(View.INVISIBLE);
                tenBang="Users";
            }
        });
    }

    private void loginUser() {
        String name= txtname.getText().toString();
        String pass=txtPass.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Vui long dien so dien thoai...",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(this,"Vui long dien mat khau...",Toast.LENGTH_LONG).show();
        }
        else if(pass.length() <6)
        {
            Toast.makeText(this,"Vui lòng điền mật khẩu dài hơn 5 kí tự",Toast.LENGTH_LONG).show();
        }
        else
        {
            progressDialog.setTitle("Tao tai khoan");
            progressDialog.setMessage("Vui long cho cho...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            kiemTraTaiKhoan(name,pass);
        }
    }

    private void kiemTraTaiKhoan(String name, String pass) {
        if(ckNho.isChecked())
        {
            Paper.book().write(CongCu.UserKey,name);
            Paper.book().write(CongCu.PassKey,pass);
        }

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //Toast.makeText(LoginActivity.this,"Dang nhap Admin thanh cong..."+tenBang,Toast.LENGTH_LONG).show();
                if(snapshot.child(tenBang).child(name).exists())//==true
                {

                    Users usersHT=snapshot.child(tenBang).child(name).getValue(Users.class);

                   if(usersHT.getName().equals(name))
                    {
                        if(usersHT.getPass().equals(pass)) {
                            if(tenBang.equals("Admins"))
                            {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,"Dang nhap Admin thanh cong...",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,"Dang nhap thanh cong...",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this, CategoriesActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Sai mat khau...",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Tai khoan khong ton tai...",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void anhXa() {
        txtPass=findViewById(R.id.txt_pass);
        txtname=findViewById(R.id.txt_name);
        btnLogin=findViewById(R.id.btn_login);
        ckNho=findViewById(R.id.ck_remember);
        txtAdmin=findViewById(R.id.admin_login);
        txtNotAdmin=findViewById(R.id.not_admin_login);
    }
}