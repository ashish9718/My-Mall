package com.ashish.mymall.ui.my_mall;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.mymall.CategoryAdapter;
import com.ashish.mymall.MyMallAdapter;
import com.ashish.mymall.MyMallModel;
import com.ashish.mymall.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.ashish.mymall.DBquerries.categoryModelList;
import static com.ashish.mymall.DBquerries.lists;
import static com.ashish.mymall.DBquerries.loadCategories;
import static com.ashish.mymall.DBquerries.loadFragmentData;
import static com.ashish.mymall.DBquerries.loadedCategoriesNames;

public class MyMallFragment extends Fragment {

    private RecyclerView categoryRecyclerView , homepageRecyclerview;
    private CategoryAdapter categoryAdapter;
    private MyMallAdapter myMallAdapter;
    private ImageView noInternet;

    public MyMallFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_mall, container, false);
        noInternet=view.findViewById(R.id.no_internet);

        ConnectivityManager connectivityManager=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()==true){
            noInternet.setVisibility(View.GONE);

            categoryRecyclerView=view.findViewById(R.id.category_recycler_view);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            categoryRecyclerView.setLayoutManager(linearLayoutManager);

            categoryAdapter= new CategoryAdapter(categoryModelList);
            categoryRecyclerView.setAdapter(categoryAdapter);

            if(categoryModelList.size() == 0){
                loadCategories(categoryAdapter,getContext());
            }else {
                categoryAdapter.notifyDataSetChanged();
            }

            homepageRecyclerview =view.findViewById(R.id.my_mall_recyclerview);
            LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
            testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            homepageRecyclerview.setLayoutManager(testingLayoutManager);


            if(lists.size() == 0){
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<MyMallModel>());
                myMallAdapter=new MyMallAdapter(lists.get(0));
                loadFragmentData(myMallAdapter,getContext(),0,"Home");
            }else {
                myMallAdapter=new MyMallAdapter(lists.get(0));
                myMallAdapter.notifyDataSetChanged();
            }

            homepageRecyclerview.setAdapter(myMallAdapter);

        }
        else {
            Glide.with(this).load(R.drawable.no_internet).into(noInternet);
            noInternet.setVisibility(View.VISIBLE);

        }

        return view;
    }

}