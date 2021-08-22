package com.example.DoFireBase02;



import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by reale on 2/22/2017.
 */

class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
{
    public TextView txt_motLoai_Ten;
    public ImageView img_motLoai_Hinh;

    private ItemClickListner itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        txt_motLoai_Ten = (TextView)itemView.findViewById(R.id.txt_motLoai_Ten);
        img_motLoai_Hinh = (ImageView)itemView.findViewById(R.id.img_motLoai_Hinh);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListner itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),true);
        return true;
    }
}

