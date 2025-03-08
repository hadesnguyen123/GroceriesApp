package com.example.grocerietproject.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerietproject.R;

public class AdminCategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView tvRecycleCategoryId, tvRecycleCategoryName, tvRecycleCategoryDescription;
    public Button button;
    public ImageView buttonRecycleAdminCategoryDetail;
    public ImageView buttonRecycleAdminCategoryDelete;
    public ImageView imgAdminCategoryRecycleImage;

    public AdminCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        tvRecycleCategoryId = itemView.findViewById(R.id.tvRecycleCategoryId);
        tvRecycleCategoryName = itemView.findViewById(R.id.tvRecycleCategoryName);
        tvRecycleCategoryDescription = itemView.findViewById(R.id.tvRecycleCategoryDescription);
        buttonRecycleAdminCategoryDetail = itemView.findViewById(R.id.imgButtonRecycleAdminCategoryDetail);
        buttonRecycleAdminCategoryDelete = itemView.findViewById(R.id.imgButtonRecycleAdminCategoryDelete);
        imgAdminCategoryRecycleImage = itemView.findViewById(R.id.imgAdminCategoryRecycleImage);

    }
}
