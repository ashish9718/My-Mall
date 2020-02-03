package com.ashish.mymall;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DBquerries {

    public static FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    public static List<MyMallModel> myMallModelList=new ArrayList<>();
    public static List<CategoryModel> categoryModelList=new ArrayList<>();



    public static void loadCategories(final CategoryAdapter categoryAdapter, final Context context){

        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                categoryModelList.add(new CategoryModel(queryDocumentSnapshot.get("icon").toString(),queryDocumentSnapshot.get("categoryName").toString()));
                            }
                            categoryAdapter.notifyDataSetChanged();

                        }
                        else {
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public static void loadFragmentData(final MyMallAdapter myMallAdapter, final Context context){

        firebaseFirestore.collection("CATEGORIES")
                .document("HOME")
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
                                    myMallModelList.add(new MyMallModel(0,sliderModelList));

                                }else if((long)queryDocumentSnapshot.get("view_type") == 1){

                                    myMallModelList.add(new MyMallModel(1,queryDocumentSnapshot.get("strip_ad_banner").toString(),queryDocumentSnapshot.get("background").toString()));

                                }else if((long)queryDocumentSnapshot.get("view_type") == 2){
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
                                    }
                                    myMallModelList.add(new MyMallModel(2,queryDocumentSnapshot.get("layout_title").toString(),queryDocumentSnapshot.get("layout_background").toString(),horizontalProductScrollModelList));

                                }else if((long)queryDocumentSnapshot.get("view_type") == 3){

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
                                    myMallModelList.add(new MyMallModel(3,queryDocumentSnapshot.get("layout_title").toString(),queryDocumentSnapshot.get("layout_background").toString(),gridProductScrollModelList));

                                }
                            }
                            myMallAdapter.notifyDataSetChanged();
                        }
                        else {
                            String error=task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
