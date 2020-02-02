package com.ashish.mymall.ui.my_wishlist;

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

import com.ashish.mymall.R;
import com.ashish.mymall.WishlistAdapter;
import com.ashish.mymall.WishlistModel;

import java.util.ArrayList;
import java.util.List;

public class MyWishlistFragment extends Fragment {
    public MyWishlistFragment() {
    }

    private RecyclerView wishlistRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_wishlist, container, false);
        wishlistRecyclerView=root.findViewById(R.id.my_wishlist_recyclerview);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);

        List<WishlistModel> wishlistModelList=new ArrayList<>();
        wishlistModelList.add(new WishlistModel(R.mipmap.mobile,1,2,"Realme 5 pro","2","Rs.49999/-","Rs.59999/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.mipmap.banner,2,3,"Realme 6 pro","3","Rs.49999/-","Rs.59999/-","EMI"));
        wishlistModelList.add(new WishlistModel(R.mipmap.star,3,4,"Realme 7 pro","4","Rs.49999/-","Rs.59999/-","Credit Card"));
        wishlistModelList.add(new WishlistModel(R.mipmap.images,0,5,"Realme 8 pro","5","Rs.49999/-","Rs.59999/-","Debit Card"));
        wishlistModelList.add(new WishlistModel(R.mipmap.mobile,1,2,"Realme 5 pro","2","Rs.49999/-","Rs.59999/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.mipmap.banner,2,3,"Realme 6 pro","3","Rs.49999/-","Rs.59999/-","EMI"));

        WishlistAdapter wishlistAdapter=new WishlistAdapter(wishlistModelList,true);
        wishlistAdapter.notifyDataSetChanged();
        wishlistRecyclerView.setAdapter(wishlistAdapter);
        return root;
    }
}