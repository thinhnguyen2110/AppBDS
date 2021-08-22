package com.example.DoFireBase02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminCategoryActivity extends AppCompatActivity {
    Button btn_ThemLoai;
    Button btn_ListCategories;
    ImageButton btn_folder, btn_cam;
    EditText txt_ten, txt_maloai;
    ImageView imgHinh;
    Uri imageUri;
    String maL,tenL;
    String downloadImg;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    final int REQUEST_CAM=123;
    final int REQUEST_FOLDER=456;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode==REQUEST_CAM&& resultCode==RESULT_OK && data!=null)
        {
            Bitmap bitmap=(Bitmap) data.getExtras().get("data");
            imgHinh.setImageBitmap(bitmap);
        }
        else if(requestCode==REQUEST_FOLDER&& resultCode==RESULT_OK && data!=null)
        {
            imageUri=data.getData();
            imgHinh.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        anhXa();
        btn_ListCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,CategoriesActivity.class);
                startActivity(intent);
            }
        });

        btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_CAM);
            }
        });
        btn_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_FOLDER);
            }
        });

        btn_ThemLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sv viet ham them loai vao firebase gioosng nhu phan dang ky usser hinh: abc.png
                maL = txt_maloai.getText().toString();
                tenL = txt_ten.getText().toString();

                //kiemtra khac rong (giong bai truoc)
                if (TextUtils.isEmpty(maL)) {
                    Toast.makeText(AdminCategoryActivity.this, "Vui lòng điền mã loại...", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(tenL)) {
                    Toast.makeText(AdminCategoryActivity.this, "Vui lòng điền tên loại...", Toast.LENGTH_LONG).show();
                } else {
                    //jphat sinh te de hinh anh upload ko bi trung

                    progressDialog.setTitle("Them loai san pham");
                    progressDialog.setMessage("Vui long cho trong giaay lat...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                    String ngayHT = currentDate.format(calendar.getTime());

                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:SS a");
                    String gioHT = currentTime.format(calendar.getTime());

                    String tenUL = ngayHT + gioHT;


                    //ket thuc upload
                    StorageReference duongDan = storageReference.child(imageUri.getLastPathSegment() + tenUL + ".jpg");


                    final UploadTask file = duongDan.putFile(imageUri);
                    file.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            String mess = e.toString();
                            Toast.makeText(AdminCategoryActivity.this, "loi " + mess, Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AdminCategoryActivity.this, "thanh cong", Toast.LENGTH_LONG).show();
                            Task<Uri> uriTask = file.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    downloadImg = duongDan.getDownloadUrl().toString();
                                    return duongDan.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        downloadImg = task.getResult().toString();
                                        Toast.makeText(AdminCategoryActivity.this, "hinh anh da duoc upload xong", Toast.LENGTH_LONG).show();
                                        luuLoaiSPvaoDataBase();
                                    }

                                }
                            });
                        }
                    });

                }
            }
        });
    }
    private void checkrong(){

    }
    private void luuLoaiSPvaoDataBase() {
        HashMap<String, Object> loaiSP=new HashMap<>();
        loaiSP.put("ma",maL);
        loaiSP.put("ten",tenL);
        loaiSP.put("hinh",downloadImg);
        databaseReference.child(maL).updateChildren(loaiSP)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AdminCategoryActivity.this, "Them loai thanh cong", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(AdminCategoryActivity.this, "Loi" + task.getException(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void anhXa() {
        btn_cam=findViewById(R.id.imageCamera);
        btn_folder=findViewById(R.id.imageFolder);
        btn_ThemLoai=findViewById(R.id.btn_themLoai);
        btn_ListCategories=findViewById(R.id.out_categories);
        txt_ten=findViewById(R.id.txt_TenLoai);
        txt_maloai =findViewById(R.id.txt_maLoai);
        imgHinh=findViewById(R.id.img_category);
        progressDialog=new ProgressDialog(this);

        storageReference= FirebaseStorage.getInstance().getReference().child("HinhAnh");
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Categories");
    }
}