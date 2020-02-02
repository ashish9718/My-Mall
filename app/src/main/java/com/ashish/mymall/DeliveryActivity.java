package com.ashish.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ashish.mymall.ui.my_cart.MyCartFragment;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView deliveryRecyclerView;
    private Button changeORaddNewAddressBtn;
    public final static int SELECT_ADDRESS =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Delivery");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deliveryRecyclerView=findViewById(R.id.delivery_recyclerview);
        changeORaddNewAddressBtn=findViewById(R.id.change_or_add_address_button);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(linearLayoutManager);

        List<CartItemModel> cartItemModelList=new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0,R.mipmap.mobile,0,1,1,2,"Realme 5 pro","Rs.49999/-","Rs.59999/-"));
        cartItemModelList.add(new CartItemModel(0,R.mipmap.mobile,2,2,0,1,"Realme 6 pro","Rs.49999/-","Rs.59999/-"));
        cartItemModelList.add(new CartItemModel(0,R.mipmap.mobile,0,3,0,0,"Realme 7 pro","Rs.49999/-","Rs.59999/-"));
        cartItemModelList.add(new CartItemModel(1,"Price (3 items)","Rs.499999/-","free","Rs.5999/-","Rs.499999/-"));

        CartAdapter cartAdapter=new CartAdapter(cartItemModelList);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeORaddNewAddressBtn.setVisibility(View.VISIBLE);
        changeORaddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeliveryActivity.this,MyAddressesActivity.class).putExtra("MODE",SELECT_ADDRESS));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
