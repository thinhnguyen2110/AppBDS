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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewProductActivity extends AppCompatActivity {
    Button btn_ThemSP;
    ImageButton btn_folder1, btn_cam1;
    EditText txt_maLoai_sp, txt_ma_sp,txt_ten_sp,txt_gia_sp,txt_date;
    ImageView imgHinh1;
    Uri imageUri1;
    String maL,maSP,tenSP,giaSP,Ngay;
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
            imgHinh1.setImageBitmap(bitmap);
        }
        else if(requestCode==REQUEST_FOLDER&& resultCode==RESULT_OK && data!=null)
        {
            imageUri1=data.getData();
            imgHinh1.setImageURI(imageUri1);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        anhXa();

        btn_cam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_CAM);
            }
        });
        btn_folder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_FOLDER);
            }
        });

        btn_ThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sv viet ham them loai vao firebase gioosng nhu phan dang ky usser hinh: abc.png
                maL=txt_maLoai_sp.getText().toString();
                maSP= txt_ma_sp.getText().toString();
                tenSP= txt_ten_sp.getText().toString();
                giaSP= txt_gia_sp.getText().toString();
                Ngay= txt_date.getText().toString();

                //kiemtra khac rong (giong bai truoc)
                if (TextUtils.isEmpty(maL)) {
                    Toast.makeText(AddNewProductActivity.this, "Vui lòng điền mã loại...", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(maSP)) {
                    Toast.makeText(AddNewProductActivity.this, "Vui lòng điền mã sản phẩm...", Toast.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(tenSP)) {
                    Toast.makeText(AddNewProductActivity.this, "Vui lòng điền tên sản phẩm...", Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(giaSP)) {
                    Toast.makeText(AddNewProductActivity.this, "Vui lòng điền giá sản phẩm...", Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(Ngay)) {
                    Toast.makeText(AddNewProductActivity.this, "Vui lòng điền ngày...", Toast.LENGTH_LONG).show();
                }
                else {
                    //jphat sinh te de hinh anh upload ko bi trung
                    progressDialog.setTitle("Them loai san pham");
                    progressDialog.setMessage("Vui long cho trong giaay lat...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat currentDate=new SimpleDateFormat("dd MM yyyy");
                    String ngayHT=currentDate.format(calendar.getTime());

                    SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:SS a");
                    String gioHT=currentTime.format(calendar.getTime());

                    String tenUL=ngayHT+gioHT;


                    //ket thuc upload
                    StorageReference duongDan=storageReference.child(imageUri1.getLastPathSegment()+ tenUL+".jpg");


                    final UploadTask file=duongDan.putFile(imageUri1);
                    file.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            String mess=e.toString();
                            Toast.makeText(AddNewProductActivity.this,"loi " + mess,Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddNewProductActivity.this,"thanh cong",Toast.LENGTH_LONG).show();
                            Task<Uri> uriTask=file.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if(!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    downloadImg=duongDan.getDownloadUrl().toString();
                                    return duongDan.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                    if(task.isSuccessful()) {
                                        downloadImg=task.getResult().toString();
                                        Toast.makeText(AddNewProductActivity.this, "hinh anh da duoc upload xong", Toast.LENGTH_LONG).show();
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
    private void luuLoaiSPvaoDataBase() {
        HashMap<String, Object> SP=new HashMap<>();
        SP.put("maloai",maL);
        SP.put("masp",maSP);
        SP.put("tensp",tenSP);
        SP.put("giasp",giaSP);
        SP.put("ngay",Ngay);
        SP.put("hinh",downloadImg);
        databaseReference.child(maSP).updateChildren(SP)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AddNewProductActivity.this, "Them SP thanh cong", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(AddNewProductActivity.this, "Loi" + task.getException(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void anhXa() {
        btn_cam1=findViewById(R.id.imageCamera1);
        btn_folder1=findViewById(R.id.imageFolder1);
        btn_ThemSP=findViewById(R.id.btn_themsp);
        txt_maLoai_sp =findViewById(R.id.txt_maLoai_sp);
        txt_ma_sp = findViewById(R.id.txt_ma_sp);
        txt_ten_sp = findViewById(R.id.txt_ten_sp);
        txt_gia_sp = findViewById(R.id.txt_gia_sp);
        txt_date = findViewById(R.id.txt_date);
        imgHinh1=findViewById(R.id.img_category1);
        progressDialog=new ProgressDialog(this);
        storageReference= FirebaseStorage.getInstance().getReference().child("HinhAnh");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Product");
    }
}