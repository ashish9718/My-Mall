package com.ashish.mymall.ui.my_cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.mymall.AddAddressActivity;
import com.ashish.mymall.CartAdapter;
import com.ashish.mymall.CartItemModel;
import com.ashish.mymall.DeliveryActivity;
import com.ashish.mymall.R;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {

    public MyCartFragment() {
    }

    private RecyclerView cartItemsRecyclerView;
    private Button ContinueBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        cartItemsRecyclerView=view.findViewById(R.id.cart_items_recycler_view);
        ContinueBtn=view.findViewById(R.id.cart_continue_btn);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

        List<CartItemModel> cartItemModelList=new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0,R.mipmap.mobile,0,1,1,2,"Realme 5 pro","Rs.49999/-","Rs.59999/-"));
        cartItemModelList.add(new CartItemModel(0,R.mipmap.mobile,2,2,0,1,"Realme 6 pro","Rs.49999/-","Rs.59999/-"));
        cartItemModelList.add(new CartItemModel(0,R.mipmap.mobile,0,3,0,0,"Realme 7 pro","Rs.49999/-","Rs.59999/-"));
        cartItemModelList.add(new CartItemModel(1,"Price (3 items)","Rs.499999/-","free","Rs.5999/-","Rs.499999/-"));

        CartAdapter cartAdapter=new CartAdapter(cartItemModelList);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        ContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), AddAddressActivity.class));
            }
        });
        return view;
    }
}