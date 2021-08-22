package com.example.DoFireBase02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.DoFireBase02.models.Categories;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CategoriesRecyclerAdapter extends FirebaseRecyclerAdapter< Categories,CategoryViewHolder> {

    private List<String> listData = new ArrayList<>();
    private Context context;

    public CategoriesRecyclerAdapter(FirebaseRecyclerOptions<Categories> listData, Context context) {
        super(listData);

        this.context = context;
    }




    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.category_item_layout,parent,false);

        return new CategoryViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryViewHolder holder, int position, @NonNull @NotNull Categories model) {
        holder.txt_motLoai_Ten.setText(listData.get(position));
        Picasso.get().load(model.getHinh()).into(holder.img_motLoai_Hinh);

        holder.setItemClickListener(new ItemClickListner() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick)
                    Toast.makeText(context, "Long Click: "+listData.get(position), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, " "+listData.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}

