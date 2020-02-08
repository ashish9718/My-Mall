package com.ashish.mymall;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.mymall.ui.my_cart.MyCartFragment;
import com.ashish.mymall.ui.my_mall.MyMallFragment;
import com.ashish.mymall.ui.my_wishlist.MyWishlistFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBquerries {
    public static FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList=new ArrayList<>();

    public static List<List<MyMallModel>> lists=new ArrayList<>();
    public static List<String> loadedCategoriesNames=new ArrayList<>();

    public static List<String> wishlist=new ArrayList<>();
    public static List<WishlistModel> wishlistModelList=new ArrayList<>();

    public static List<String> myRatedIds=new ArrayList<>();
    public static List<Long> myRating=new ArrayList<>();

    public static List<String> cartList=new ArrayList<>();
    public static List<CartItemModel> cartItemModelList=new ArrayList<>();


    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context){

        categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                categoryModelList.add(new CategoryModel(queryDocumentSnapshot.get("icon").toString(),queryDocumentSnapshot.get("categoryName").toString()));
                            }
                            CategoryAdapter categoryAdapter= new CategoryAdapter(categoryModelList);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();

                        }
                        else {
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public static void loadFragmentData(final RecyclerView myMallRecyclerView, final Context context, final int index, String categoryName){

        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){

                                if((long)queryDocumentSnapshot.get("view_type") == 0){

                                    List<SliderModel> sliderModelList=new ArrayList<SliderModel>();
                                    long no_of_banners= (long) queryDocumentSnapshot.get("no_of_banners");
                                    for(long x=1;x<=no_of_banners;x++){
                                        sliderModelList.add(new SliderModel(queryDocumentSnapshot.get("banner_"+x).toString(),queryDocumentSnapshot.get("banner_"+x+"_background").toString()));
                                    }
                                    lists.get(index).add(new MyMallModel(0,sliderModelList));

                                }

                                else if((long)queryDocumentSnapshot.get("view_type") == 1){

                                    lists.get(index).add(new MyMallModel(1,queryDocumentSnapshot.get("strip_ad_banner").toString(),queryDocumentSnapshot.get("background").toString()));

                                }

                                else if((long)queryDocumentSnapshot.get("view_type") == 2){

                                    List<WishlistModel> viewAllProductList=new ArrayList<>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList=new ArrayList<>();
                                    long no_of_products= (long) queryDocumentSnapshot.get("no_of_products");

                                    for(long x=1;x<=no_of_products;x++){
                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(
                                                queryDocumentSnapshot.get("product_ID_"+x).toString()
                                                ,queryDocumentSnapshot.get("product_image_"+x).toString()
                                                ,queryDocumentSnapshot.get("product_title_"+x).toString()
                                                ,queryDocumentSnapshot.get("product_subtitle_"+x).toString()
                                                ,queryDocumentSnapshot.get("product_price_"+x).toString()
                                        ));

                                        viewAllProductList.add(new WishlistModel(
                                                queryDocumentSnapshot.get("product_ID_"+x).toString()
                                                ,queryDocumentSnapshot.get("product_image_"+x).toString()
                                                ,queryDocumentSnapshot.get("product_full_title_"+x).toString()
                                                ,(long)queryDocumentSnapshot.get("free_coupans_"+x)
                                                ,queryDocumentSnapshot.get("average_rating_"+x).toString()
                                                ,(long)queryDocumentSnapshot.get("total_ratings_"+x)
                                                ,queryDocumentSnapshot.get("product_price_"+x).toString()
                                                ,queryDocumentSnapshot.get("cutted_price_"+x).toString()
                                                ,(boolean)queryDocumentSnapshot.get("COD_"+x)
                                        ));
                                    }
                                    lists.get(index).add(new MyMallModel(2,queryDocumentSnapshot.get("layout_title").toString(),queryDocumentSnapshot.get("layout_background").toString(),horizontalProductScrollModelList,viewAllProductList));

                                }

                                else if((long)queryDocumentSnapshot.get("view_type") == 3){

                                    List<HorizontalProductScrollModel> gridProductScrollModelList=new ArrayList<>();
                                    long no_of_products= (long) queryDocumentSnapshot.get("no_of_products");
                                    for(long x=1;x<=no_of_products;x++){
                                        gridProductScrollModelList.add(new HorizontalProductScrollModel(
                                                queryDocumentSnapshot.get("product_ID_"+x).toString()
                                                ,queryDocumentSnapshot.get("product_image_"+x).toString()
                                                ,queryDocumentSnapshot.get("product_title_"+x).toString()
                                                ,queryDocumentSnapshot.get("product_subtitle_"+x).toString()
                                                ,queryDocumentSnapshot.get("product_price_"+x).toString()
                                        ));
                                    }
                                    lists.get(index).add(new MyMallModel(3,queryDocumentSnapshot.get("layout_title").toString(),queryDocumentSnapshot.get("layout_background").toString(),gridProductScrollModelList));

                                }
                            }
                            MyMallAdapter myMallAdapter=new MyMallAdapter(lists.get(index));
                            myMallRecyclerView.setAdapter(myMallAdapter);
                            myMallAdapter.notifyDataSetChanged();
                            MyMallFragment.swipeRefreshLayout.setRefreshing(false);
                        }
                        else {
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public static void loadWishlist(final Context context, final Dialog dialog,final boolean loadProductData){
        wishlist.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_WISHLIST").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            for(long x=0;x<(long)task.getResult().get("list_size");x++){
                                wishlist.add(task.getResult().get("product_ID_"+x).toString());

                                if(DBquerries.wishlist.contains(ProductDetailsActivity.productID)){
                                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST=true;
                                    if(ProductDetailsActivity.addToWishlistBtn != null) {
                                        ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                                    }
                                }else {
                                    if(ProductDetailsActivity.addToWishlistBtn != null) {
                                        ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                    }
                                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST=false;
                                }

                                if(loadProductData) {
                                    wishlistModelList.clear();
                                    final String productId=task.getResult().get("product_ID_" + x).toString();
                                    firebaseFirestore.collection("PRODUCTS").document(productId).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        wishlistModelList.add(new WishlistModel(
                                                                productId
                                                                ,task.getResult().get("product_image_1").toString()
                                                                , task.getResult().get("product_title").toString()
                                                                , (long) task.getResult().get("free_coupans")
                                                                , task.getResult().get("average_rating").toString()
                                                                , (long) task.getResult().get("total_ratings")
                                                                , task.getResult().get("product_price").toString()
                                                                , task.getResult().get("cutted_price").toString()
                                                                , (boolean) task.getResult().get("COD")
                                                        ));
                                                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }

                        }
                        else {
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }

    public static void removeFromWishlist(final int index, final Context context) {
        final String removedProductId=wishlist.get(index);
        wishlist.remove(index);

        Map<String, Object> updateWishlist = new HashMap<>();

        for (int x = 0; x < wishlist.size(); x++) {
            updateWishlist.put("product_ID_" + x, wishlist.get(x));
        }
        updateWishlist.put("list_size", (long) wishlist.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_WISHLIST").set(updateWishlist)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if(wishlistModelList.size() != 0){
                                wishlistModelList.remove(index);
                                MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                            }
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST=false;
                            Toast.makeText(context,"Removed Successfully!",Toast.LENGTH_SHORT).show();
                        } else {
                            if(ProductDetailsActivity.addToWishlistBtn != null) {
                                ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                            }
                            wishlist.add(index,removedProductId);
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                        ProductDetailsActivity.running_wishlist_querry=false;
                    }
                });

    }

    public static void loadRatingList(final Context context){
        if(!ProductDetailsActivity.running_rating_querry) {
            ProductDetailsActivity.running_rating_querry=true;
            myRatedIds.clear();
            myRating.clear();
            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            myRatedIds.add(task.getResult().get("product_ID_" + x).toString());
                            myRating.add((long) task.getResult().get("rating_" + x));
                            if (task.getResult().get("product_ID_" + x).toString().equals(ProductDetailsActivity.productID)) {
                                ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if(ProductDetailsActivity.rateNowContainer != null) {
                                    ProductDetailsActivity.setRating(ProductDetailsActivity.initialRating);
                                }
                            }
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                    ProductDetailsActivity.running_rating_querry=false;
                }
            });
        }
    }

    public static void loadCartList(final Context context, final Dialog dialog,final boolean loadProductData,final TextView badgeCount){

        cartList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_CART").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            for(long x=0;x<(long)task.getResult().get("list_size");x++){
                                cartList.add(task.getResult().get("product_ID_"+x).toString());

                                if(DBquerries.cartList.contains(ProductDetailsActivity.productID)){
                                    ProductDetailsActivity.ALREADY_ADDED_TO_CART=true;
                                }else {
                                    ProductDetailsActivity.ALREADY_ADDED_TO_CART=false;
                                }

                                if(loadProductData) {
                                    cartItemModelList.clear();
                                    final String productId=task.getResult().get("product_ID_" + x).toString();
                                    firebaseFirestore.collection("PRODUCTS").document(productId).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        int index=0;
                                                        if(cartList.size()>=2){
                                                            index=cartList.size()-2;
                                                        }
                                                        cartItemModelList.add(index,new CartItemModel(CartItemModel.CART_ITEM
                                                                ,productId
                                                                ,task.getResult().get("product_image_1").toString()
                                                                , (long) task.getResult().get("free_coupans")
                                                                ,(long)1
                                                                ,(long)0
                                                                ,(long)0
                                                                , task.getResult().get("product_title").toString()
                                                                , task.getResult().get("product_price").toString()
                                                                , task.getResult().get("cutted_price").toString()
                                                                ));
                                                        if(cartList.size() == 1){
                                                            cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                                        }
                                                        if(cartList.size() == 0) {
                                                            cartItemModelList.clear();
                                                        }
                                                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                            if(cartList.size() != 0){
                                badgeCount.setVisibility(View.VISIBLE);
                            }else {
                                badgeCount.setVisibility(View.INVISIBLE);
                            }
                            if(DBquerries.cartList.size()<99) {
                                badgeCount.setText(String.valueOf(DBquerries.cartList.size()));
                            }else {
                                badgeCount.setText("99");
                            }
                        }
                        else {
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }

    public static void removeFromCart(final int index, final Context context) {
        final String removedProductId=cartList.get(index);
        cartList.remove(index);

        Map<String, Object> updateCart = new HashMap<>();

        for (int x = 0; x < cartList.size(); x++) {
            updateCart.put("product_ID_" + x, cartList.get(x));
        }
        updateCart.put("list_size", (long) cartList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_CART").set(updateCart)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if(cartItemModelList.size() != 0){
                                cartItemModelList.remove(index);
                                MyCartFragment.cartAdapter.notifyDataSetChanged();
                            }
                            if(cartList.size() == 0) {
                                cartItemModelList.clear();
                            }
                            Toast.makeText(context,"Removed Successfully!",Toast.LENGTH_SHORT).show();
                        } else {
                            cartList.add(index,removedProductId);
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                        ProductDetailsActivity.running_cart_querry=false;
                    }
                });

    }


    public static void clearData(){

        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        wishlist.clear();
        wishlistModelList.clear();
        cartList.clear();
        cartItemModelList.clear();

    }
}
