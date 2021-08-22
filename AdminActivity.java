package com.example.DoFireBase02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {
    Button btntaoCategory,btntaoProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        anhXa();
        btntaoCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,AdminCategoryActivity.class);
                startActivity(intent);
            }
        });
        btntaoProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,AddNewProductActivity.class);
                startActivity(intent);
            }
        });
    }
    private void anhXa() {
        btntaoCategory=findViewById(R.id.btn_create_category);
        btntaoProduct=findViewById(R.id.btn_create_product);
    }
}