package com.example.grocerietproject.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerietproject.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public ConstraintLayout layoutProductRecyclerView;
    public TextView productName, productWeight, productPrice, productAmount;
    public ImageView imgProductItem;
    public ImageButton buttonProductItemAddToCart;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        layoutProductRecyclerView = itemView.findViewById(R.id.layoutProductRecyclerView);
        productName = itemView.findViewById(R.id.tvProductItemName);
        productWeight = itemView.findViewById(R.id.tvProductItemWeight);
        productPrice = itemView.findViewById(R.id.tvProductItemPrice);
        productAmount = itemView.findViewById(R.id.tvProductItemAmount);
        imgProductItem = itemView.findViewById(R.id.imgProductItem);
        buttonProductItemAddToCart = itemView.findViewById(R.id.buttonProductItemAddToCart);
    }
}
