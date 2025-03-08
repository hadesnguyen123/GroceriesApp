package com.example.grocerietproject.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerietproject.R;

public class CartItemViewHolder extends RecyclerView.ViewHolder {

    public TextView tvCartItemProductName;
    public TextView tvCartItemProductWeigh;
    public TextView tvCartItemQuantity;
    public TextView tvCartItemPrice;
    public ImageView imgButtonDeleteCartItem;
    public ImageButton buttonCartItemQuantitySubtract;
    public ImageButton buttonCartItemQuantityPlus;
    public ImageView imgCartItemImage;

    public CartItemViewHolder(@NonNull View itemView) {
        super(itemView);
        tvCartItemProductName = itemView.findViewById(R.id.tvCartItemProductName);
        tvCartItemProductWeigh = itemView.findViewById(R.id.tvCartItemProductWeigh);
        tvCartItemQuantity = itemView.findViewById(R.id.tvCartItemQuantity);
        tvCartItemPrice = itemView.findViewById(R.id.tvCartItemPrice);
        buttonCartItemQuantitySubtract = itemView.findViewById(R.id.buttonCartItemQuantitySubtract);
        buttonCartItemQuantityPlus = itemView.findViewById(R.id.buttonCartItemQuantityPlus);
        imgButtonDeleteCartItem = itemView.findViewById(R.id.imgButtonDeleteCartItem);
        imgCartItemImage = itemView.findViewById(R.id.imgCartItemImage);


    }
}
