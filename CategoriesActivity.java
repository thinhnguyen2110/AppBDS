package com.example.DoFireBase02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DoFireBase02.models.Categories;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity implements ItemClickListner {

    private RecyclerView recyclerView;
    DatabaseReference databaseReference;
    CategoriesRecyclerAdapter categoriesRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<String> listData = new ArrayList<>();

    FirebaseRecyclerOptions<Categories> options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Categories");
        anhXa();

        FirebaseRecyclerOptions<Categories> options= new FirebaseRecyclerOptions.Builder<Categories>().setQuery(databaseReference,Categories.class).build();
        categoriesRecyclerAdapter=new CategoriesRecyclerAdapter(options,getApplicationContext());
        recyclerView.setAdapter(categoriesRecyclerAdapter);
        layoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        setupList();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.ryc_HienThiAllLoai);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




    }

    private void setupList() {
        for(int i = 1;i<=10;i++)
            listData.add("Click me "+i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        categoriesRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        categoriesRecyclerAdapter.stopListening();
    }

    private void anhXa() {

        recyclerView=findViewById(R.id.ryc_HienThiAllLoai);

    }

    @Override
    public void onClick(View view, int positon, boolean isLongClick) {
        Intent intent = new Intent(CategoriesActivity.this, details.class);
        Categories DL;
        DL =options.getSnapshots().get(positon);
        intent.putExtra("ma",DL.getMa());
        startActivity(intent);

    }


}