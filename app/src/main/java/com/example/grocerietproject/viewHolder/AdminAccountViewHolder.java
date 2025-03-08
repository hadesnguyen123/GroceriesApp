package com.example.grocerietproject.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerietproject.R;

public class AdminAccountViewHolder extends RecyclerView.ViewHolder {
    public TextView tvRecycleAccountId, tvRecycleAccountRole ,tvRecycleAccountEmail, tvRecycleAccountFullName ;
    public ImageView buttonRecycleAdminAccountDetail;
    public ImageView buttonRecycleAdminAccountDelete;

    public AdminAccountViewHolder(@NonNull View itemView) {
        super(itemView);

        tvRecycleAccountId = itemView.findViewById(R.id.tvRecycleAccountId);
        tvRecycleAccountRole = itemView.findViewById(R.id.tvRecycleAccountRole);
        tvRecycleAccountEmail = itemView.findViewById(R.id.tvRecycleAccountEmail);
        tvRecycleAccountFullName = itemView.findViewById(R.id.tvRecycleAccountFullName);

        buttonRecycleAdminAccountDetail = itemView.findViewById(R.id.imgButtonRecycleAdminAccountDetail);
        buttonRecycleAdminAccountDelete = itemView.findViewById(R.id.imgButtonRecycleAdminAccountDelete);
    }
}
