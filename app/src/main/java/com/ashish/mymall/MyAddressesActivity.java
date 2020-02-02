package com.ashish.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import static com.ashish.mymall.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static AddressesAdapter addressesAdapter;
    private Button deliverHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My addresses");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.addresses_recyclerview);

        deliverHere=findViewById(R.id.deliver_here_button);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<AddressesModel> addressesModelList=new ArrayList<>();
        addressesModelList.add(new AddressesModel("John Doe","37, New Delhi, India","201001",true));
        addressesModelList.add(new AddressesModel("John Smith","38, New Delhi, India","201002",false));
        addressesModelList.add(new AddressesModel("John Dave","39, New Delhi, India","201003",false));
        addressesModelList.add(new AddressesModel("John Jonas","40, New Delhi, India","201004",false));

        int mode=getIntent().getIntExtra("MODE",-1);
        if(mode==SELECT_ADDRESS){
            deliverHere.setVisibility(View.VISIBLE);
        }else {
            deliverHere.setVisibility(View.GONE);
        }
        addressesAdapter=new AddressesAdapter(addressesModelList,mode);
        addressesAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);



    }

    public static void refreshItem(int deselect,int select){
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);

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
