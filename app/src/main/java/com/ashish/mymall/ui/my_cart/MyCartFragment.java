package com.ashish.mymall.ui.my_cart;

import android.app.Dialog;
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
import com.ashish.mymall.DBquerries;
import com.ashish.mymall.DeliveryActivity;
import com.ashish.mymall.R;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {

    public MyCartFragment() {
    }

    private RecyclerView cartItemsRecyclerView;
    private Button ContinueBtn;
    private Dialog loadingDialog;
    public static CartAdapter cartAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
//////////loading dialog

        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        //////////loading dialog

        cartItemsRecyclerView=view.findViewById(R.id.cart_items_recycler_view);
        ContinueBtn=view.findViewById(R.id.cart_continue_btn);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

        if(DBquerries.cartItemModelList.size() == 0){
            DBquerries.cartList.clear();
            DBquerries.loadCartList(getContext(),loadingDialog,true,new TextView(getContext()));
        }else {
            loadingDialog.dismiss();
        }


        cartAdapter=new CartAdapter(DBquerries.cartItemModelList);
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