package com.ashish.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ashish.mymall.ui.my_cart.MyCartFragment;
import com.ashish.mymall.ui.my_mall.MyMallFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.xml.parsers.DocumentBuilder;

import static com.ashish.mymall.MainActivity.showCart;
import static com.ashish.mymall.RegisterActivity.setsignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {

    public static boolean fromSearch=false;
    private Long productPriceValue;
    private boolean inStock=false;
    private TextView badgeCount;
    public static MenuItem cartItem;
    private ViewPager productImagesViewpager, productDetailsViewPager;
    private TabLayout viewpagerIndicator, productDetailsTablayout;
    private ConstraintLayout productDetailsOnlyContainer, productDetailsTabsContainer;
    private String productDescription, productOtherDetails;
    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    public static String productID;

    private TextView productTitle, productAvgRating, productTotalRatings, productPrice, cuttedPrice, productOnlyDescriptionBody;
    private ImageView codIndicator;
    private TextView tvcodIndicator, rewardTitle, rewardBody;

    public static FloatingActionButton addToWishlistBtn;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false, ALREADY_ADDED_TO_CART = false, running_wishlist_querry = false, running_rating_querry = false, running_cart_querry = false;

    //////rating layout
    public static int initialRating;
    public static LinearLayout rateNowContainer, ratingsProgrssBarContainer;
    private TextView totalRatings, totalRatingsFigure, avgRating;
    private LinearLayout ratingsNoContainer;

    //////rating layout

    private Button buyNowBtn, coupanRedeemButton;
    private LinearLayout addToCartBtn;
    /////coupan dialog
    private TextView coupanTitle,discountedPrice,originalPrice;
    private TextView coupanExpiryDate;
    private TextView coupanBody;
    private RecyclerView coupanRecyclerview;
    private LinearLayout selectedCoupan;
    private LinearLayout coupanRedemLayout;
    /////coupan dialog
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;
    private Dialog signInDialog, loadingDialog;
    private DocumentSnapshot documentSnapshot;
    public static Activity productDetailsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewpager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        addToWishlistBtn = findViewById(R.id.add_to_wishlist_btn);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);

        productDetailsViewPager = findViewById(R.id.product_details_viewpager);
        productDetailsTablayout = findViewById(R.id.product_details_tablayout);
        buyNowBtn = findViewById(R.id.buy_now_btn);
        coupanRedeemButton = findViewById(R.id.coupan_redemption_btn);

        productTitle = findViewById(R.id.product_title);
        productAvgRating = findViewById(R.id.tv_product_rating_miniview);
        productTotalRatings = findViewById(R.id.total_rating_miniview);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        codIndicator = findViewById(R.id.cod_indicator_imageview);
        tvcodIndicator = findViewById(R.id.tv_cod_indicator);
        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);
        productDetailsOnlyContainer = findViewById(R.id.product_details_container);
        productDetailsTabsContainer = findViewById(R.id.product_detail_tabs_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingsNoContainer = findViewById(R.id.ratings_numbers_container);
        ratingsProgrssBarContainer = findViewById(R.id.ratings_progressbar_container);
        avgRating = findViewById(R.id.average_rating);
        coupanRedemLayout = findViewById(R.id.coupan_redemption_layout);

        firebaseFirestore = FirebaseFirestore.getInstance();

        initialRating = -1;
        //////////loading dialog

        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        //////////loading dialog



////// coupan redemption dialog

        final Dialog checkCoupanPricedialog = new Dialog(ProductDetailsActivity.this);
        checkCoupanPricedialog.setCancelable(true);
        checkCoupanPricedialog.setContentView(R.layout.coupan_redeem_dialog);
        checkCoupanPricedialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toogleRecyclerView = checkCoupanPricedialog.findViewById(R.id.toggle_recyclerview);
        coupanRecyclerview = checkCoupanPricedialog.findViewById(R.id.coupans_recyclerView);
        selectedCoupan = checkCoupanPricedialog.findViewById(R.id.selected_coupan);
        originalPrice = checkCoupanPricedialog.findViewById(R.id.original_price);
        discountedPrice = checkCoupanPricedialog.findViewById(R.id.discounted_price);
        coupanTitle = checkCoupanPricedialog.findViewById(R.id.coupan_title);
        coupanExpiryDate = checkCoupanPricedialog.findViewById(R.id.coupan_validity);
        coupanBody = checkCoupanPricedialog.findViewById(R.id.coupan_body);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        coupanRecyclerview.setLayoutManager(linearLayoutManager);

        toogleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRecyclerView();
            }
        });

////// coupan redemption dialog


        final List<String> productImages = new ArrayList<>();

        productID = getIntent().getStringExtra("PRODUCT_ID");

        firebaseFirestore.collection("PRODUCTS").document(productID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            documentSnapshot = task.getResult();

                            firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){

                                                for (int x = 1; x <= (long) documentSnapshot.get("no_of_product_images"); x++) {
                                                    productImages.add(documentSnapshot.get("product_image_" + x).toString());
                                                }
                                                ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                                                productImagesViewpager.setAdapter(productImagesAdapter);

                                                productTitle.setText(documentSnapshot.get("product_title").toString());
                                                productAvgRating.setText(documentSnapshot.get("average_rating").toString());
                                                productTotalRatings.setText("(" + (long) documentSnapshot.get("total_ratings") + ")ratings");
                                                productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");

                                                ///for coupan dialog
                                                originalPrice.setText(productPrice.getText());
                                                productPriceValue= Long.valueOf(documentSnapshot.get("product_price").toString());
                                                MyRewardsAdapter rewardsAdapter = new MyRewardsAdapter(DBquerries.rewardModelList, true,coupanRecyclerview,selectedCoupan,productPriceValue,coupanTitle,coupanExpiryDate,coupanBody,discountedPrice);
                                                rewardsAdapter.notifyDataSetChanged();
                                                coupanRecyclerview.setAdapter(rewardsAdapter);
                                                ///for coupan dialog

                                                cuttedPrice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "/-");

                                                if ((boolean) documentSnapshot.get("COD")) {
                                                    codIndicator.setVisibility(View.VISIBLE);
                                                    tvcodIndicator.setVisibility(View.VISIBLE);
                                                } else {
                                                    codIndicator.setVisibility(View.INVISIBLE);
                                                    tvcodIndicator.setVisibility(View.INVISIBLE);
                                                }
                                                rewardTitle.setText((long) documentSnapshot.get("free_coupans") + documentSnapshot.get("free_coupan_title").toString());
                                                rewardBody.setText(documentSnapshot.get("free_coupan_body").toString());

                                                if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                                    productDetailsTabsContainer.setVisibility(View.VISIBLE);
                                                    productDetailsOnlyContainer.setVisibility(View.GONE);

                                                    productDescription = documentSnapshot.get("product_description").toString();
                                                    productOtherDetails = documentSnapshot.get("product_other_details").toString();

                                                    for (long x = 1; x <= (long) documentSnapshot.get("total_spec_titles"); x++) {
                                                        productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));
                                                        for (long y = 1; y <= (long) documentSnapshot.get("spec_title_" + x + "_total_fields"); y++) {
                                                            productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));
                                                        }

                                                    }
                                                } else {
                                                    productDetailsTabsContainer.setVisibility(View.GONE);
                                                    productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                                                    productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                                                }


                                                totalRatings.setText((long) documentSnapshot.get("total_ratings") + " ratings");
                                                for (int x = 0; x < 5; x++) {
                                                    TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                                                    rating.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));

                                                    ProgressBar progressBar = (ProgressBar) ratingsProgrssBarContainer.getChildAt(x);
                                                    int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                                    progressBar.setMax(maxProgress);
                                                    progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));
                                                }
                                                totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                                avgRating.setText(documentSnapshot.get("average_rating").toString());
                                                productDetailsViewPager.setAdapter(new ProductdetaisAdapter(getSupportFragmentManager(), productDetailsTablayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));

                                                if (currentUser != null) {
                                                    if (DBquerries.myRating.size() == 0) {
                                                        DBquerries.loadRatingList(ProductDetailsActivity.this);
                                                    }

                                                    if (DBquerries.wishlist.size() == 0) {
                                                        DBquerries.loadWishlist(ProductDetailsActivity.this, loadingDialog, false);
                                                    }
                                                    if (DBquerries.cartList.size() == 0) {
                                                        DBquerries.loadCartList(ProductDetailsActivity.this, loadingDialog, false,badgeCount,new TextView(ProductDetailsActivity.this));
                                                    }
                                                    if (DBquerries.rewardModelList.size() == 0) {
                                                        DBquerries.loadRewards(ProductDetailsActivity.this, loadingDialog,false);
                                                    }
                                                    if (DBquerries.cartList.size() != 0 && DBquerries.wishlist.size() != 0  &&  DBquerries.rewardModelList.size() != 0){
                                                        loadingDialog.dismiss();
                                                    }

                                                } else {
                                                    loadingDialog.dismiss();
                                                }

                                                if (DBquerries.myRatedIds.contains(productID)) {
                                                    int index = DBquerries.myRatedIds.indexOf(productID);
                                                    initialRating = Integer.parseInt(String.valueOf(DBquerries.myRating.get(index))) - 1;
                                                    setRating(initialRating);
                                                }

                                                if (DBquerries.wishlist.contains(productID)) {
                                                    ALREADY_ADDED_TO_WISHLIST = true;
                                                    addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));

                                                } else {
                                                    addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                                    ALREADY_ADDED_TO_WISHLIST = false;
                                                }
                                                if (DBquerries.cartList.contains(productID)) {
                                                    ALREADY_ADDED_TO_CART = true;
                                                } else {
                                                    ALREADY_ADDED_TO_CART = false;
                                                }


                                                if(task.getResult().getDocuments().size()< (long)documentSnapshot.get("stock_quantity")){
                                                    inStock=true;
                                                    buyNowBtn.setVisibility(View.VISIBLE);
                                                    addToCartBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            if (currentUser == null) {
                                                                signInDialog.show();
                                                            }
                                                            else {
                                                                if (!running_cart_querry) {
                                                                    running_cart_querry = true;
                                                                    if (ALREADY_ADDED_TO_CART) {
                                                                        running_cart_querry = false;
                                                                        Toast.makeText(ProductDetailsActivity.this, "Already added to cart!", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        Map<String, Object> addProduct = new HashMap<>();
                                                                        addProduct.put("product_ID_" + String.valueOf(DBquerries.cartList.size()), productID);
                                                                        addProduct.put("list_size", (long) (DBquerries.cartList.size() + 1));

                                                                        firebaseFirestore.collection("USERS").document(currentUser.getUid())
                                                                                .collection("USER_DATA").document("MY_CART")
                                                                                .update(addProduct)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            if (DBquerries.cartItemModelList.size() != 0) {
                                                                                                DBquerries.cartItemModelList.add(0,new CartItemModel(CartItemModel.CART_ITEM
                                                                                                        , productID
                                                                                                        , documentSnapshot.get("product_image_1").toString()
                                                                                                        , (long) documentSnapshot.get("free_coupans")
                                                                                                        , (long) 1
                                                                                                        ,(long)documentSnapshot.get("offers_applied")
                                                                                                        , (long) 0
                                                                                                        , documentSnapshot.get("product_title").toString()
                                                                                                        , documentSnapshot.get("product_price").toString()
                                                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                                                        ,inStock
                                                                                                        ,(long)documentSnapshot.get("max-quantity")
                                                                                                        ,(long)documentSnapshot.get("stock_quantity")
                                                                                                        ,(boolean)documentSnapshot.get("COD")
                                                                                                ));
                                                                                            }
                                                                                            ALREADY_ADDED_TO_CART = true;
                                                                                            DBquerries.cartList.add(productID);
                                                                                            Toast.makeText(ProductDetailsActivity.this, "Added to Cart Successfully!", Toast.LENGTH_SHORT).show();
                                                                                            invalidateOptionsMenu();
                                                                                            running_cart_querry = false;
                                                                                        } else {
                                                                                            running_cart_querry = false;
                                                                                            String err = task.getException().getMessage();
                                                                                            Toast.makeText(ProductDetailsActivity.this, err, Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                });


                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                                }else {
                                                    inStock=false;
                                                    buyNowBtn.setVisibility(View.GONE);
                                                    TextView outOfStock = (TextView) addToCartBtn.getChildAt(0);
                                                    outOfStock.setText("Out of Stock");
                                                    outOfStock.setTextColor(getResources().getColor(R.color.colorPrimary));
                                                    outOfStock.setCompoundDrawables(null,null,null,null);
                                                }
                                            }
                                            else {
                                                String error=task.getException().getMessage();
                                                Toast.makeText(ProductDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else {
                            loadingDialog.dismiss();
                            String err = task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this, err, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        viewpagerIndicator.setupWithViewPager(productImagesViewpager, true);

        addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signInDialog.show();
                }
                else {
                    if (!running_wishlist_querry) {
                        running_wishlist_querry = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = DBquerries.wishlist.indexOf(productID);
                            DBquerries.removeFromWishlist(index, ProductDetailsActivity.this);
                            ALREADY_ADDED_TO_WISHLIST = false;
                            addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        } else {
                            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));

                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBquerries.wishlist.size()), productID);
                            addProduct.put("list_size", (long) (DBquerries.wishlist.size() + 1));

                            firebaseFirestore.collection("USERS").document(currentUser.getUid())
                                    .collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                if (DBquerries.wishlist.size() == 0) {
                                                    DBquerries.wishlistModelList.add(new WishlistModel(
                                                            productID
                                                            , documentSnapshot.get("product_image_1").toString()
                                                            , documentSnapshot.get("product_title").toString()
                                                            , (long) documentSnapshot.get("free_coupans")
                                                            , documentSnapshot.get("average_rating").toString()
                                                            , (long) documentSnapshot.get("total_ratings")
                                                            , documentSnapshot.get("product_price").toString()
                                                            , documentSnapshot.get("cutted_price").toString()
                                                            , (boolean) documentSnapshot.get("COD")
                                                            ,inStock
                                                    ));
                                                }

                                                ALREADY_ADDED_TO_WISHLIST = true;
                                                addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                                DBquerries.wishlist.add(productID);
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Wishlist Successfully!", Toast.LENGTH_SHORT).show();

                                            } else {
                                                addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                                String err = task.getException().getMessage();
                                                Toast.makeText(ProductDetailsActivity.this, err, Toast.LENGTH_SHORT).show();
                                            }
                                            running_wishlist_querry = false;
                                        }
                                    });


                        }
                    }
                }
            }
        });


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

        rateNowContainer = findViewById(R.id.rate_now_container);

        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentUser == null) {
                        signInDialog.show();
                    }
                    else {
                        if (starPosition != initialRating) {
                            if (!running_rating_querry) {
                                running_rating_querry = true;

                                setRating(starPosition);
                                Map<String, Object> updateRating = new HashMap<>();

                                if (DBquerries.myRatedIds.contains(productID)) {
                                    TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);

                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition - initialRating, true));
                                } else {
                                    updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);

                                }

                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(updateRating)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Map<String, Object> myRating = new HashMap<>();

                                                    if (DBquerries.myRatedIds.contains(productID)) {
                                                        myRating.put("rating_" + DBquerries.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                                    } else {
                                                        myRating.put("product_ID_" + DBquerries.myRatedIds.size(), productID);
                                                        myRating.put("rating_" + DBquerries.myRatedIds.size(), (long) starPosition + 1);
                                                        myRating.put("list_size", (long) DBquerries.myRatedIds.size() + 1);
                                                    }

                                                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                            .update(myRating)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        if (DBquerries.myRatedIds.contains(productID)) {
                                                                            DBquerries.myRating.set(DBquerries.myRatedIds.indexOf(productID), (long) starPosition + 1);

                                                                            TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                                            TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));
                                                                        } else {
                                                                            DBquerries.myRatedIds.add(productID);
                                                                            DBquerries.myRating.add((long) starPosition + 1);
                                                                            productTotalRatings.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ")ratings");
                                                                            totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));

                                                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                                            Toast.makeText(ProductDetailsActivity.this, "Thank you for rating!", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                        for (int x = 0; x < 5; x++) {
                                                                            TextView ratings = (TextView) ratingsNoContainer.getChildAt(x);
                                                                            ProgressBar progressBar = (ProgressBar) ratingsProgrssBarContainer.getChildAt(x);
                                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                                            progressBar.setMax(maxProgress);
                                                                            progressBar.setProgress(Integer.parseInt(ratings.getText().toString()));
                                                                        }
                                                                        initialRating = starPosition;
                                                                        avgRating.setText(calculateAverageRating(0, true));
                                                                        productAvgRating.setText(calculateAverageRating(0, true));

                                                                        if (DBquerries.wishlist.contains(productID) && DBquerries.wishlistModelList.size() != 0) {
                                                                            int index = DBquerries.wishlist.indexOf(productID);
                                                                            DBquerries.wishlistModelList.get(index).setRating(avgRating.getText().toString());
                                                                            DBquerries.wishlistModelList.get(index).setTotalRatings(Long.parseLong(totalRatingsFigure.getText().toString()));
                                                                        }
                                                                    } else {
                                                                        setRating(initialRating);
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    running_rating_querry = false;
                                                                }
                                                            });

                                                } else {
                                                    running_rating_querry = false;
                                                    setRating(initialRating);
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        }
                    }
                }

            });
        }
//////rating layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentUser == null) {
                    signInDialog.show();
                }
                else {
                    DeliveryActivity.fromCart=false;
                    loadingDialog.show();
                    productDetailsActivity=ProductDetailsActivity.this;
                    DeliveryActivity.cartItemModelList=new ArrayList<>();
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM
                            , productID
                            , documentSnapshot.get("product_image_1").toString()
                            , (long) documentSnapshot.get("free_coupans")
                            , (long) 1
                            ,(long)documentSnapshot.get("offers_applied")
                            , (long) 0
                            , documentSnapshot.get("product_title").toString()
                            , documentSnapshot.get("product_price").toString()
                            , documentSnapshot.get("cutted_price").toString()
                            ,inStock
                            ,(long)documentSnapshot.get("max-quantity")
                            ,(long)documentSnapshot.get("stock_quantity")
                            ,(boolean)documentSnapshot.get("COD")
                    ));
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                    if(DBquerries.addressesModelList.size() == 0) {
                        DBquerries.loadAddresses(ProductDetailsActivity.this, loadingDialog,true);
                    }else {
                        loadingDialog.dismiss();
                        startActivity(new Intent(ProductDetailsActivity.this, DeliveryActivity.class));
                    }

                }
            }
        });



        coupanRedeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCoupanPricedialog.show();
            }
        });

/////// sign in dialog box
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);

        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button signInDialogBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button signUpDialogBtn = signInDialog.findViewById(R.id.sign_up_btn);

        signInDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SigninFragment.disableCloseBtn = true;
                SignupFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setsignUpFragment = false;
                startActivity(new Intent(ProductDetailsActivity.this, RegisterActivity.class));
            }
        });

        signUpDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SigninFragment.disableCloseBtn = true;
                SignupFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setsignUpFragment = true;
                startActivity(new Intent(ProductDetailsActivity.this, RegisterActivity.class));
            }
        });

/////// sign in dialog box

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            coupanRedemLayout.setVisibility(View.GONE);
        } else {
            coupanRedemLayout.setVisibility(View.VISIBLE);
        }
        if (currentUser != null) {
            if (DBquerries.myRating.size() == 0) {
                DBquerries.loadRatingList(ProductDetailsActivity.this);
            }

            if (DBquerries.wishlist.size() == 0) {
                DBquerries.loadWishlist(ProductDetailsActivity.this, loadingDialog, false);
            }

            if (DBquerries.rewardModelList.size() == 0) {
                DBquerries.loadRewards(ProductDetailsActivity.this, loadingDialog,false);
            }
            if (DBquerries.cartList.size() != 0 && DBquerries.wishlist.size() != 0  &&  DBquerries.rewardModelList.size() != 0){
                loadingDialog.dismiss();
            }

        } else {
            loadingDialog.dismiss();
        }

        if (DBquerries.myRatedIds.contains(productID)) {
            int index = DBquerries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBquerries.myRating.get(index))) - 1;
            setRating(initialRating);
        }
        if (DBquerries.wishlist.contains(productID)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));

        } else {
            addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }
        if (DBquerries.cartList.contains(productID)) {
            ALREADY_ADDED_TO_CART = true;
        } else {
            ALREADY_ADDED_TO_CART = false;
        }
        invalidateOptionsMenu();

    }

    private void showDialogRecyclerView() {
        if (coupanRecyclerview.getVisibility() == View.GONE) {
            coupanRecyclerview.setVisibility(View.VISIBLE);
            selectedCoupan.setVisibility(View.GONE);
        } else {
            coupanRecyclerview.setVisibility(View.GONE);
            selectedCoupan.setVisibility(View.VISIBLE);
        }
    }

    public static void setRating(int starPosition) {
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }


    private String calculateAverageRating(long currentUserRating, boolean update) {
        Double totalStars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString()) * x);
        }
        totalStars = totalStars + currentUserRating;
        if (update) {
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0, 3);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        cartItem= menu.findItem(R.id.main_cart_icon);


        cartItem.setActionView(R.layout.badge_layout);
        ImageView badgeIcon=cartItem.getActionView().findViewById(R.id.badge_icon);
        badgeIcon.setImageResource(R.drawable.shopping_cart);
        badgeCount=cartItem.getActionView().findViewById(R.id.badge_count);
        if(currentUser!=null){
            if(DBquerries.cartList.size() == 0){
                DBquerries.loadCartList(ProductDetailsActivity.this,loadingDialog,false,badgeCount,new TextView(ProductDetailsActivity.this));
            }else {
                badgeCount.setVisibility(View.VISIBLE);
                if(DBquerries.cartList.size()<99) {
                    badgeCount.setText(String.valueOf(DBquerries.cartList.size()));
                }else {
                    badgeCount.setText("99");
                }
            }
        }
        cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    showCart = true;
                    startActivity(new Intent(ProductDetailsActivity.this, MainActivity.class));
                }
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.main_search_icon) {
            if(fromSearch) {
                finish();
            }else {
                startActivity(new Intent(this,SearchActivity.class));
            }
            return true;
        } else if (id == android.R.id.home) {
            productDetailsActivity=null;
            finish();
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                showCart = true;
                startActivity(new Intent(ProductDetailsActivity.this, MainActivity.class));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        productDetailsActivity=null;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSearch=false;
    }
}
