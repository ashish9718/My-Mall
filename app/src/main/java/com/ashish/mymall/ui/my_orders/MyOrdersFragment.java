package com.ashish.mymall.ui.my_orders;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.mymall.MyOrderAdapter;
import com.ashish.mymall.MyOrderItemModel;
import com.ashish.mymall.R;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment {
    public MyOrdersFragment() {
    }

    private RecyclerView myordersRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_orders, container, false);
        myordersRecyclerView=root.findViewById(R.id.my_orders_recyclerview);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        myordersRecyclerView.setLayoutManager(linearLayoutManager);

        List<MyOrderItemModel> myOrderItemModelList=new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.mipmap.mobile,2,"Realme 2 pro","Delivered on sunday 26th jan,2020"));
        myOrderItemModelList.add(new MyOrderItemModel(R.mipmap.banner,3,"Realme 3 pro","Delivered on sunday 27th jan,2020"));
        myOrderItemModelList.add(new MyOrderItemModel(R.mipmap.usermale,4,"Realme 4 pro","Cancelled"));
        myOrderItemModelList.add(new MyOrderItemModel(R.mipmap.cart,1,"Realme 1 pro","Delivered on sunday 28th jan,2020"));

        MyOrderAdapter myOrderAdapter=new MyOrderAdapter(myOrderItemModelList);
        myordersRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();
        return root;
    }
}