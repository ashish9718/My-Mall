package com.ashish.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title=getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryRecyclerView=findViewById(R.id.category_recycler_view);


        List<SliderModel> sliderModelList=new ArrayList<SliderModel>();

        sliderModelList.add(new SliderModel(R.mipmap.heart,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.user_blue,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.banner,"#077AE4"));


        sliderModelList.add(new SliderModel(R.mipmap.images,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.cancel,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.cart,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.heart,"#077AE4"));


        sliderModelList.add(new SliderModel(R.mipmap.user_blue,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.banner,"#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.images,"#077AE4"));




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

        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);

        List<MyMallModel> myMallModelList=new ArrayList<>();
        myMallModelList.add(new MyMallModel(0,sliderModelList));
        myMallModelList.add(new MyMallModel(1,R.mipmap.banner,"#000000"));
        myMallModelList.add(new MyMallModel(2,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(3,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(3,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(1,R.mipmap.banner,"#000000"));
        myMallModelList.add(new MyMallModel(2,"Deals of the Day",horizontalProductScrollModelList));
        myMallModelList.add(new MyMallModel(3,"Deals of the Day",horizontalProductScrollModelList));

        MyMallAdapter myMallAdapter=new MyMallAdapter(myMallModelList);
        categoryRecyclerView.setAdapter(myMallAdapter);
        myMallAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.main_search_icon){
            return true;
        }else if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
