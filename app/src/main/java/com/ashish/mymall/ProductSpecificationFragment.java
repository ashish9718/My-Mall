package com.ashish.mymall;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductSpecificationFragment extends Fragment {

    private RecyclerView productSpecificationrecyclerview;
    public List<ProductSpecificationModel> productSpecificationModelList;

    public ProductSpecificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product_specification, container, false);
        productSpecificationrecyclerview=view.findViewById(R.id.product_specification_recyclerview);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        productSpecificationrecyclerview.setLayoutManager(linearLayoutManager);


        ProductSpecificationAdapter productSpecificationAdapter=new ProductSpecificationAdapter(productSpecificationModelList);
        productSpecificationAdapter.notifyDataSetChanged();

        productSpecificationrecyclerview.setAdapter(productSpecificationAdapter);
        return view;
    }

}
