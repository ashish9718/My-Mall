package com.ashish.mymall.ui.my_mall;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ashish.mymall.CategoryAdapter;
import com.ashish.mymall.CategoryModel;
import com.ashish.mymall.GridProductLayoutAdapter;
import com.ashish.mymall.HorizontalProductScrollAdapter;
import com.ashish.mymall.HorizontalProductScrollModel;
import com.ashish.mymall.MyMallAdapter;
import com.ashish.mymall.MyMallModel;
import com.ashish.mymall.R;
import com.ashish.mymall.SliderAdapter;
import com.ashish.mymall.SliderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyMallFragment extends Fragment {

    private RecyclerView categoryRecyclerView , testing;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryModelList;
    private FirebaseFirestore firebaseFirestore;

    public MyMallFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_mall, container, false);
        categoryRecyclerView=view.findViewById(R.id.category_recycler_view);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);

        categoryModelList =new ArrayList<CategoryModel>();

        categoryAdapter= new CategoryAdapter(categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        firebaseFirestore=FirebaseFirestore.getInstance();
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
                            Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        List<SliderModel> sliderModelList=new ArrayList<SliderModel>();

        sliderModelList.add(new SliderModel(R.mipmap.banner,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.images,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.cancel,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.cart,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.heart,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.user_blue,"#077AE4"));



        List<HorizontalProductScrollModel> horizontalProductScrollModelList=new ArrayList<>();
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.mobile,"Redmi 5A","SD 425 processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.images,"Redmi 6A","SD 425 processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.banner,"Redmi 7A","SD 425 processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.logoutt,"Redmi 8A","SD 425 processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.usermale,"Redmi 9A","SD 425 processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.home,"Redmi 5A","SD 425 processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.cart,"Redmi 6A","SD 425 processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.search,"Redmi 9A","SD 425 processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.home,"Redmi 5A","SD 425 processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.cart,"Redmi 6A","SD 425 processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.mipmap.search,"Redmi 9A","SD 425 processor","Rs.5999/-"));



        testing=view.findViewById(R.id.my_mall_recyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        testing.setLayoutManager(testingLayoutManager);

        List<MyMallModel> myMallModelList=new ArrayList<>();
        myMallModelList.add(new MyMallModel(0,sliderModelList));
        myMallModelList.add(new MyMallModel(1,R.mipmap.banner,"#000000"));
        myMallModelList.add(new MyMallModel(2,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(3,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(3,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(0,sliderModelList));
        myMallModelList.add(new MyMallModel(1,R.mipmap.banner,"#000000"));
        myMallModelList.add(new MyMallModel(2,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(3,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(1,R.mipmap.banner,"#000000"));
        myMallModelList.add(new MyMallModel(2,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(3,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(3,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(0,sliderModelList));
        myMallModelList.add(new MyMallModel(1,R.mipmap.banner,"#000000"));
        myMallModelList.add(new MyMallModel(2,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(3,"Deals of the Day",horizontalProductScrollModelList));

        MyMallAdapter myMallAdapter=new MyMallAdapter(myMallModelList);
        testing.setAdapter(myMallAdapter);
        myMallAdapter.notifyDataSetChanged();



        return view;

    }

}