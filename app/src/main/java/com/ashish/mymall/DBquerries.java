package com.ashish.mymall;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.mymall.ui.my_mall.MyMallFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DBquerries {
    public static FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList=new ArrayList<>();
    public static List<List<MyMallModel>> lists=new ArrayList<>();
    public static List<String> loadedCategoriesNames=new ArrayList<>();
    public static List<String> wishlist=new ArrayList<>();


    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context){

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
                                                queryDocumentSnapshot.get("product_image_"+x).toString()
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

    public static void loadWishlist(final Context context, final Dialog dialog){
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_WISHLIST").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            for(long x=0;x<(long)task.getResult().get("list_size");x++){
                                wishlist.add(task.getResult().get("product_ID_"+x).toString());
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

}
