package com.ashish.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.ashish.mymall.DBquerries.lists;
import static com.ashish.mymall.DBquerries.loadFragmentData;
import static com.ashish.mymall.DBquerries.loadedCategoriesNames;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    MyMallAdapter myMallAdapter;

    //private List<CategoryModel> categoryModelFakeList=new ArrayList<>();
    private List<MyMallModel> myMallModelFakeList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title=getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//////////home page fake list

        List<SliderModel> sliderModelFakeList=new ArrayList<>();

        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList=new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        myMallModelFakeList.add(new MyMallModel(0,sliderModelFakeList));
        myMallModelFakeList.add(new MyMallModel(1,"","#ffffff"));
        myMallModelFakeList.add(new MyMallModel(2,"","#ffffff",horizontalProductScrollModelFakeList,new ArrayList<WishlistModel>()));
        myMallModelFakeList.add(new MyMallModel(3,"","#ffffff",horizontalProductScrollModelFakeList));



//////////home page fake list



        categoryRecyclerView=findViewById(R.id.category_recycler_view);

        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);

        myMallAdapter=new MyMallAdapter(myMallModelFakeList);


        int listPosition=0;
        for(int x=0; x<loadedCategoriesNames.size();x++){
            if(loadedCategoriesNames.get(x).equals(title.toUpperCase())){
                listPosition=x;
            }
        }

        if(listPosition == 0){
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<MyMallModel>());
            loadFragmentData(categoryRecyclerView,this,loadedCategoriesNames.size()-1,title);
        }else {
            myMallAdapter=new MyMallAdapter(lists.get(listPosition));

        }

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
            startActivity(new Intent(CategoryActivity.this,SearchActivity.class));
            return true;
        }else if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
