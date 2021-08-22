package com.example.DoFireBase02;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.DoFireBase02.models.Categories;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class details extends AppCompatActivity {
    TextView txt_ten, txt_mota, txt_gia;
    ImageView hinhBds;
    String ma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        anhxa();

        ma = getIntent().getStringExtra("ma");
       if (ma!= null)
       {
          layDuLieuTuFirebase(ma);
       }
       else
           Toast.makeText(details.this, "Chưa kết nối Firebase", Toast.LENGTH_SHORT).show();
    }

    private void layDuLieuTuFirebase(String ma) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Categories");
        databaseReference.child(ma).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Categories loai=snapshot.getValue(Categories.class);
                    txt_ten.setText((loai.getTen()));
                    txt_mota.setText((loai.getMa()));
                    txt_gia.setText("10000");
                    Picasso.get().load(loai.getHinh()).into(hinhBds);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private void anhxa(){
        txt_gia =findViewById(R.id.ten_bds);
        txt_mota = findViewById(R.id.mota_bds);
        txt_gia = findViewById(R.id.gia_bds);
        hinhBds = findViewById(R.id.hinh_bds);

    }
}