package com.example.grocerietproject.fragments.shopping;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.grocerietproject.DAO.CartItemDao;
import com.example.grocerietproject.DAO.ProductDao;
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.entities.CartItem;
import com.example.grocerietproject.entities.Product;
import com.example.grocerietproject.util.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProductDetailFragment extends Fragment {
    AppDatabase db;
    UserDao userDao;
    ProductDao productDao;
    CartItemDao cartItemDao;
    ImageView imgButtonBack;
    NavController navController;
    ImageView imgProductDetailImage;
    TextView tvProductDetailName;
    TextView tvProductDetailWeight;
    TextView tvProductDetailPrice;
    TextView tvProducDetailNumberQuantity;
    TextView tvProductDetailsDescription;
    TextView tvProductDetailLabel;
    ImageView imgProductDetailQuantitySubtract;
    ImageView imgProductDetailQuantityPlus;
    Button buttonAddToCart;
    private int quantity = 1;
    private double price;

    public ProductDetailFragment() {
        super(R.layout.fragment_product_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgProductDetailImage = view.findViewById(R.id.imgProductDetailImage);
        imgButtonBack = view.findViewById(R.id.imgProductDetailImage);
        tvProductDetailName = view.findViewById(R.id.tvProductDetailName);
        tvProductDetailWeight = view.findViewById(R.id.tvProductDetailWeight);
        tvProductDetailPrice = view.findViewById(R.id.tvProductDetailPrice);
        tvProducDetailNumberQuantity = view.findViewById(R.id.tvProducDetailNumberQuantity);
        tvProductDetailsDescription = view.findViewById(R.id.tvProductDetailsDescription);
        tvProductDetailLabel = view.findViewById(R.id.tvProductDetailLabel);
        imgProductDetailQuantitySubtract = view.findViewById(R.id.imgProductDetailQuantitySubtract);
        imgProductDetailQuantityPlus = view.findViewById(R.id.imgProductDetailQuantityPlus);
        buttonAddToCart = view.findViewById(R.id.buttonProductDetailAddToCart);

        initRoomDatabase();

        imgButtonBack.setOnClickListener(v -> {
            navController = NavHostFragment.findNavController(this);
            navController.popBackStack();
        });

        Bundle args = getArguments();
        if (args != null) {
            int productId = args.getInt("productId");
            Product productDetail = productDao.getProductById(productId);

            if (productDetail != null) {
                tvProductDetailName.setText(productDetail.name);
                tvProductDetailWeight.setText(productDetail.weight);
                tvProductDetailPrice.setText(String.valueOf(productDetail.price * quantity));
                tvProducDetailNumberQuantity.setText(String.valueOf(quantity));
                tvProductDetailsDescription.setText(productDetail.description);
                tvProductDetailLabel.setText(productDetail.label);

                // Tải ảnh từ drawable dựa trên tên sản phẩm
                String imageName = "product_" + productDetail.name.toLowerCase().replace(" ", "_"); // Định dạng tên ảnh
                int imageResId = getContext().getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());

                if (imageResId != 0) {
                    Picasso.get()
                            .load(imageResId)
                            .into(imgProductDetailImage);
                } else {
                    imgProductDetailImage.setImageResource(R.drawable.product_apple); // Ảnh mặc định nếu không tìm thấy
                }
            } else {
                Toast.makeText(getContext(), "Cannot find product", Toast.LENGTH_SHORT).show();
                return;
            }

            // Các sự kiện tăng giảm số lượng sản phẩm và thêm vào giỏ hàng
            quantity = Integer.parseInt(tvProducDetailNumberQuantity.getText().toString());
            imgProductDetailQuantitySubtract.setOnClickListener(v -> {
                if (quantity > 1) {
                    quantity -= 1;
                    tvProducDetailNumberQuantity.setText(String.valueOf(quantity));
                    tvProductDetailPrice.setText(String.valueOf(productDetail.price * quantity));
                } else {
                    Toast.makeText(getContext(), "Quantity of product should be greater than 1", Toast.LENGTH_SHORT).show();
                }
            });

            imgProductDetailQuantityPlus.setOnClickListener(v -> {
                quantity += 1;
                tvProducDetailNumberQuantity.setText(String.valueOf(quantity));
                tvProductDetailPrice.setText(String.valueOf(productDetail.price * quantity));
            });

            buttonAddToCart.setOnClickListener(v -> {
                SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("AccountPreference", Context.MODE_PRIVATE);
                int userId = pref.getInt("userId", 1);

                if (userId != 0) {
                    CartItem existCartItem = cartItemDao.getCartItemsByProductId(productId, userId);
                    if (existCartItem != null) {
                        existCartItem.quantity += quantity;
                        cartItemDao.updateCartItem(existCartItem);
                    } else {
                        CartItem newCartItem = new CartItem();
                        newCartItem.quantity = quantity;
                        newCartItem.productId = productId;
                        newCartItem.userId = userId;
                        cartItemDao.insertCartItem(newCartItem);
                    }
                    Toast.makeText(v.getContext(), "Added to cart successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(v.getContext(), "You need to log in first", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Cannot find product", Toast.LENGTH_SHORT).show();
        }
    }


    private void initRoomDatabase() {
        db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();
        productDao = db.productDao();
        cartItemDao = db.cartItemDao();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Hiển thị lại thanh điều hướng khi rời khỏi trang chi tiết
        View bottomNavigationView = getActivity().findViewById(R.id.bottomNavigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Ẩn thanh điều hướng khi vào trang chi tiết
        if (getActivity() != null) {
            BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigation);
            if (bottomNavigation != null) {
                bottomNavigation.setVisibility(View.GONE);
            }
        }
    }
}
