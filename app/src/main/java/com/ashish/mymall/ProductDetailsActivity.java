package com.ashish.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.ashish.mymall.MainActivity.showCart;

public class ProductDetailsActivity extends AppCompatActivity {

    private ViewPager productImagesViewpager ,productDetailsViewPager;
    private TabLayout viewpagerIndicator, productDetailsTablayout;

    private FloatingActionButton addToWishlistBtn;
    private static boolean ALREADY_ADDED_TO_WISHLIST = false;

    //////rating layout
    private LinearLayout rateNowContainer;

    //////rating layout

    private Button buyNowBtn,coupanRedeemButton;
    /////coupan dialog
    public static TextView coupanTitle;
    public static TextView coupanExpiryDate;
    public static TextView coupanBody;
    private static RecyclerView coupanRecyclerview;
    private static LinearLayout selectedCoupan;
    /////coupan dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewpager=findViewById(R.id.product_images_viewpager);
        viewpagerIndicator=findViewById(R.id.viewpager_indicator);
        addToWishlistBtn=findViewById(R.id.add_to_wishlist_btn);

        productDetailsViewPager=findViewById(R.id.product_details_viewpager);
        productDetailsTablayout=findViewById(R.id.product_details_tablayout);
        buyNowBtn=findViewById(R.id.buy_now_btn);
        coupanRedeemButton=findViewById(R.id.coupan_redemption_btn);

        List<Integer> productImages = new ArrayList<>();
        productImages.add(R.mipmap.mobile);
        productImages.add(R.mipmap.banner);
        productImages.add(R.mipmap.home);
        productImages.add(R.mipmap.usermale);

        ProductImagesAdapter productImagesAdapter=new ProductImagesAdapter(productImages);
        productImagesViewpager.setAdapter(productImagesAdapter);

        viewpagerIndicator.setupWithViewPager(productImagesViewpager,true);

        addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ALREADY_ADDED_TO_WISHLIST){
                    ALREADY_ADDED_TO_WISHLIST=false;
                    addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                }else{
                    ALREADY_ADDED_TO_WISHLIST=true;
                    addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                }
            }
        });

        productDetailsViewPager.setAdapter(new ProductdetaisAdapter(getSupportFragmentManager(),productDetailsTablayout.getTabCount()));

        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));

        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//////rating layout

        rateNowContainer=findViewById(R.id.rate_now_container);

        for(int x=0;x<rateNowContainer.getChildCount();x++){
            final int starPosition= x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setRating(starPosition);
                }
            });
        }
//////rating layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetailsActivity.this,DeliveryActivity.class));
            }
        });

////// coupan redemption dialog

        final Dialog checkCoupanPricedialog= new Dialog(ProductDetailsActivity.this);
        checkCoupanPricedialog.setCancelable(true);
        checkCoupanPricedialog.setContentView(R.layout.coupan_redeem_dialog);
        checkCoupanPricedialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toogleRecyclerView=checkCoupanPricedialog.findViewById(R.id.toggle_recyclerview);
        coupanRecyclerview=checkCoupanPricedialog.findViewById(R.id.coupans_recyclerView);
        selectedCoupan=checkCoupanPricedialog.findViewById(R.id.selected_coupan);
        TextView originalPrice=checkCoupanPricedialog.findViewById(R.id.original_price);
        TextView discountedPrice=checkCoupanPricedialog.findViewById(R.id.discounted_price);
        coupanTitle=checkCoupanPricedialog.findViewById(R.id.coupan_title);
        coupanExpiryDate=checkCoupanPricedialog.findViewById(R.id.coupan_validity);
        coupanBody=checkCoupanPricedialog.findViewById(R.id.coupan_body);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ProductDetailsActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        coupanRecyclerview.setLayoutManager(linearLayoutManager);

        List<RewardModel> rewardModelList=new ArrayList<>();
        rewardModelList.add(new RewardModel("Cashback","till 26st jan, 2020","Get 20% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","till 27st jan, 2020","Get 30% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 28st jan, 2020","Get 40% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","till 29st jan, 2020","Get 50% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 30st jan, 2020","Get 10% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 26st jan, 2020","Get 20% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","till 27st jan, 2020","Get 30% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 28st jan, 2020","Get 40% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","till 29st jan, 2020","Get 50% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 30st jan, 2020","Get 10% discount on the purchase of product between rs500/- and Rs.3000/-"));

        MyRewardsAdapter rewardsAdapter=new MyRewardsAdapter(rewardModelList,true);
        rewardsAdapter.notifyDataSetChanged();
        coupanRecyclerview.setAdapter(rewardsAdapter);

        toogleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRecyclerView();
            }
        });

////// coupan redemption dialog

        coupanRedeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCoupanPricedialog.show();
            }
        });

    }

    public static void showDialogRecyclerView(){
        if(coupanRecyclerview.getVisibility()==View.GONE){
            coupanRecyclerview.setVisibility(View.VISIBLE);
            selectedCoupan.setVisibility(View.GONE);
        }else {
            coupanRecyclerview.setVisibility(View.GONE);
            selectedCoupan.setVisibility(View.VISIBLE);
        }
    }

    private void setRating(int starPosition) {
        for(int x=0;x<rateNowContainer.getChildCount();x++){
            ImageView starBtn=(ImageView)rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if(x<=starPosition){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
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
        }else if(id==R.id.main_cart_icon) {
            showCart=true;
            startActivity(new Intent(ProductDetailsActivity.this,MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
