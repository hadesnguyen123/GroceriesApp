package com.example.grocerietproject.viewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerietproject.R;
import com.example.grocerietproject.adapters.CategoryAdapter;
import com.example.grocerietproject.models.CategoryRecycle;

public class CategoryViewHolder extends RecyclerView.ViewHolder{
    public TextView categoryName;
    public ImageView imgCategoryItem;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryName = itemView.findViewById(R.id.tvCategoryItemName);
        imgCategoryItem = itemView.findViewById(R.id.imgCategoryItem);
    }

    public void bind(final CategoryRecycle categoryRecycle, final CategoryAdapter.OnCategoryClickListener listener){
        categoryName.setText(categoryRecycle.getName());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onItemClick(categoryRecycle);
                }
            }
        });
    }
}
