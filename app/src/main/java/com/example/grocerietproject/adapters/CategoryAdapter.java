package com.example.grocerietproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerietproject.R;
import com.example.grocerietproject.models.CategoryRecycle;
import com.example.grocerietproject.models.ProductRecycle;
import com.example.grocerietproject.viewHolder.CategoryViewHolder;
import com.example.grocerietproject.viewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private List<CategoryRecycle> mCategories;
    private Context mContext;
    private OnCategoryClickListener listener; // Thay đổi kiểu listener

    public CategoryAdapter(List<CategoryRecycle> mCategories, Context mContext) {
        this.mCategories = mCategories;
        this.mContext = mContext;
    }

    public interface OnCategoryClickListener {
        void onItemClick(CategoryRecycle category);
    }

    public void setOnItemClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryView = inflater.inflate(R.layout.category_rv_item, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(categoryView);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryRecycle categoryRecycle = mCategories.get(position);
        holder.categoryName.setText(categoryRecycle.getName());
        holder.bind(categoryRecycle, listener);

        // Tải ảnh từ drawable theo tên danh mục
        String categoryName = categoryRecycle.getName();
        String imageName = "category_" + categoryName.toLowerCase().replace(" ", "_"); // Định dạng tên ảnh
        int imageResId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
        if (imageResId != 0) {
            Picasso.get()
                    .load(imageResId)
                    .into(holder.imgCategoryItem);
        } else {
            holder.imgCategoryItem.setImageResource(R.drawable.category_vegetable); // Ảnh mặc định nếu không tìm thấy
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
