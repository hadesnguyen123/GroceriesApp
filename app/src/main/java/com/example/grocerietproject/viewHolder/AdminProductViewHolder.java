package com.example.grocerietproject.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerietproject.R;

public class AdminProductViewHolder extends RecyclerView.ViewHolder {
    public TextView tvRecycleAdminProductName;
    public TextView tvRecycleAdminProductId;
    public TextView tvRecycleAdminProductPrice;
    public TextView tvRecycleAdminProductCategory;
    public TextView tvRecycleAdminProductAmount;
    public ImageView adminProductRecycleImage;

    public ImageView buttonRecycleAdminProductDetail;
    public ImageView buttonRecycleAdminProductDelete;



    public AdminProductViewHolder(@NonNull View itemView) {
        super(itemView);

        tvRecycleAdminProductName = itemView.findViewById(R.id.tvRecycleAdminProductName);
        tvRecycleAdminProductId = itemView.findViewById(R.id.tvRecycleAdminProductId);
        tvRecycleAdminProductPrice = itemView.findViewById(R.id.tvRecycleAdminProductPrice);
        tvRecycleAdminProductCategory = itemView.findViewById(R.id.tvRecycleAdminProductCategory);
        tvRecycleAdminProductAmount = itemView.findViewById(R.id.tvRecycleAdminProductAmount);
        buttonRecycleAdminProductDetail = itemView.findViewById(R.id.imgButtonRecycleAdminProductDetail);
        buttonRecycleAdminProductDelete = itemView.findViewById(R.id.imgButtonRecycleAdminProductDelete);
        adminProductRecycleImage = itemView.findViewById(R.id.adminProductRecycleImage);

    }
}
