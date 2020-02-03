package com.ashish.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridView gridView;
    public static List<HorizontalProductScrollModel> horizontalProductScrollModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.recycler_view);
        gridView=findViewById(R.id.grid_view);

        int layout=getIntent().getIntExtra("layout",-1);

        if(layout==0) {

            recyclerView.setVisibility(View.VISIBLE);

            List<WishlistModel> wishlistModelList = new ArrayList<>();
            wishlistModelList.add(new WishlistModel(R.mipmap.mobile, 1, 2, "Realme 5 pro", "2", "Rs.49999/-", "Rs.59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.mipmap.banner, 2, 3, "Realme 6 pro", "3", "Rs.49999/-", "Rs.59999/-", "EMI"));
            wishlistModelList.add(new WishlistModel(R.mipmap.star, 3, 4, "Realme 7 pro", "4", "Rs.49999/-", "Rs.59999/-", "Credit Card"));
            wishlistModelList.add(new WishlistModel(R.mipmap.images, 0, 5, "Realme 8 pro", "5", "Rs.49999/-", "Rs.59999/-", "Debit Card"));
            wishlistModelList.add(new WishlistModel(R.mipmap.mobile, 1, 2, "Realme 5 pro", "2", "Rs.49999/-", "Rs.59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.mipmap.banner, 2, 3, "Realme 6 pro", "3", "Rs.49999/-", "Rs.59999/-", "EMI"));
            wishlistModelList.add(new WishlistModel(R.mipmap.mobile, 1, 2, "Realme 5 pro", "2", "Rs.49999/-", "Rs.59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.mipmap.banner, 2, 3, "Realme 6 pro", "3", "Rs.49999/-", "Rs.59999/-", "EMI"));
            wishlistModelList.add(new WishlistModel(R.mipmap.star, 3, 4, "Realme 7 pro", "4", "Rs.49999/-", "Rs.59999/-", "Credit Card"));
            wishlistModelList.add(new WishlistModel(R.mipmap.images, 0, 5, "Realme 8 pro", "5", "Rs.49999/-", "Rs.59999/-", "Debit Card"));
            wishlistModelList.add(new WishlistModel(R.mipmap.mobile, 1, 2, "Realme 5 pro", "2", "Rs.49999/-", "Rs.59999/-", "Cash on delivery"));
            wishlistModelList.add(new WishlistModel(R.mipmap.banner, 2, 3, "Realme 6 pro", "3", "Rs.49999/-", "Rs.59999/-", "EMI"));

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);

            WishlistAdapter wishlistAdapter = new WishlistAdapter(wishlistModelList, false);
            wishlistAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(wishlistAdapter);

        }else if(layout==1) {

            gridView.setVisibility(View.VISIBLE);

            GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);
            gridProductLayoutAdapter.notifyDataSetChanged();
            gridView.setAdapter(gridProductLayoutAdapter);
        }

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
