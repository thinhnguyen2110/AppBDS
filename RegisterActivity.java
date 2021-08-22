package com.example.DoFireBase02;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    Button btnDangKy;
    EditText txtTen, txtSDT, txtMK;
    //private ProgressBar loadingBar;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        anhXa();
        //loadingBar.setVisibility(ProgressBar.INVISIBLE);
        progressDialog=new ProgressDialog(this);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangky();
            }
        });

    }

    private void dangky() {
        String name=txtTen.getText().toString();
        String phone=txtSDT.getText().toString();
        String pass=txtMK.getText().toString();
        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Vui lòng điền số điện thoại...",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Vui lòng điền tên...",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(this,"Vui lòng điền mật khẩu...",Toast.LENGTH_LONG).show();
        }
        else if(pass.length() <6)
        {
            Toast.makeText(this,"Vui lòng điền mật khẩu dài hơn 5 kí tự",Toast.LENGTH_LONG).show();
        }
        else
        {
//            loadingBar.setVisibility(ProgressBar.VISIBLE);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            progressDialog.setTitle("Đang Tạo Tài khoản");
            progressDialog.setMessage("Vui lòng chờ....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Toast.makeText(RegisterActivity.this,"vo day",Toast.LENGTH_LONG).show();
            kiemTraSoDienThoai(name,phone,pass);
        }

    }

    private void kiemTraSoDienThoai(String name, String phone, String pass) {
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(name).exists()))
                {
                    HashMap<String, Object> userData=new HashMap<>();
                    userData.put("phone",phone);
                    userData.put("name",name);
                    userData.put("pass",pass);
                    rootRef.child("Users").child(name).updateChildren(userData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Đăng kí thành công", Toast.LENGTH_LONG).show();
                                        // loadingBar.setVisibility(ProgressBar.INVISIBLE);
                                        progressDialog.dismiss();
                                        Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    }
                                    else
                                    {
                                        // loadingBar.setVisibility(ProgressBar.INVISIBLE);
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Thất bại",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else
                {
                    String tb="So dt: " + phone +" da ton tai";
                    Toast.makeText(RegisterActivity.this,tb, Toast.LENGTH_LONG).show();
                    // loadingBar.setVisibility(ProgressBar.INVISIBLE);
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this,"Vui lòng sử dụng Họ và tên khác ", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }

    private void anhXa() {
        btnDangKy=findViewById(R.id.btn_register_dk);
        txtTen=findViewById(R.id.txt_name_dk);
        txtSDT=findViewById(R.id.txt_phone_dk);
        txtMK=findViewById(R.id.txt_pass_dk);
        //loadingBar=findViewById(R.id.progressBar);
    }
}