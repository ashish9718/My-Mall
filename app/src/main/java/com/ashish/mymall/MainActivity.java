package com.ashish.mymall;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.ashish.mymall.ui.my_account.MyAccountFragment;
import com.ashish.mymall.ui.my_cart.MyCartFragment;
import com.ashish.mymall.ui.my_mall.MyMallFragment;
import com.ashish.mymall.ui.my_orders.MyOrdersFragment;
import com.ashish.mymall.ui.my_rewards.MyRewardsFragment;
import com.ashish.mymall.ui.my_wishlist.MyWishlistFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import static com.ashish.mymall.RegisterActivity.setsignUpFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout frameLayout;

    private static final int MyMallFragment=0,CART_FRAGMENT=1,ORDERS_FRAGMENT=2,WISHLIST_FRAGMENT=3,REWARDS_FRAGMENT=4,MYACCOUNT_FRAGMENT=5;
    private int currentFragment=-1;
    private ImageView actionbarLogo;
    private Window window;
    private Toolbar toolbar;
    public static Boolean showCart=false;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        actionbarLogo=findViewById(R.id.action_bar_logo);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_my_mall,
                R.id.nav_my_orders, R.id.nav_my_rewards, R.id.nav_my_cart,
                R.id.nav_my_wishlist, R.id.nav_my_account,R.id.nav_sign_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().getItem(0).setChecked(true);


        frameLayout=findViewById(R.id.main_frame_layout);

        if(showCart){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            gotoFragment("My Cart",new MyCartFragment(),-2);
        }else {
            setFragment(new MyMallFragment(), MyMallFragment);
            ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.nav_my_mall) {
                    actionbarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new MyMallFragment(),MyMallFragment);
                    //navigationView.getMenu().getItem(0).setChecked(true);
                }
                else if (id == R.id.nav_my_orders) {
                    gotoFragment("My Orders",new MyOrdersFragment(),ORDERS_FRAGMENT);
                }
                else if (id == R.id.nav_my_rewards) {
                    gotoFragment("My Rewards",new MyRewardsFragment(),REWARDS_FRAGMENT);
                }
                else if (id == R.id.nav_my_cart) {
                    gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);
                }
                else if (id == R.id.nav_my_wishlist) {
                    gotoFragment("My Wishlist",new MyWishlistFragment(),WISHLIST_FRAGMENT);
                }
                else if (id == R.id.nav_my_account) {
                    gotoFragment("My Account",new MyAccountFragment(),MYACCOUNT_FRAGMENT);
                }
                else if(id==R.id.nav_sign_out){
                }
                drawer.closeDrawers();
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(currentFragment== MyMallFragment){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.main_search_icon){
            return true;
        }else if(id==R.id.main_notification_icon){
            return true;
        }else if(id==R.id.main_cart_icon){

            final Dialog signInDialog=new Dialog(MainActivity.this);
            signInDialog.setContentView(R.layout.sign_in_dialog);
            signInDialog.setCancelable(true);

            signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            Button signInDialogBtn=signInDialog.findViewById(R.id.sign_in_btn);
            Button signUpDialogBtn=signInDialog.findViewById(R.id.sign_up_btn);

            signInDialogBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signInDialog.dismiss();
                    setsignUpFragment=false;
                    startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                }
            });

            signUpDialogBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signInDialog.dismiss();
                    setsignUpFragment=true;
                    startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                }
            });
            signInDialog.show();

            //gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);
            return true;
        }else if(id==android.R.id.home){
            if(showCart) {
                showCart = false;
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title,Fragment fragment,int fragementNo) {
        actionbarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment,fragementNo);
        if(fragementNo==CART_FRAGMENT) {
            navigationView.getMenu().getItem(3).setChecked(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setFragment(Fragment fragment,int fragementNo){
        if(fragementNo!=currentFragment) {

            if(fragementNo==REWARDS_FRAGMENT){
                window.setStatusBarColor(Color.parseColor("#5b04b1"));
                toolbar.setBackgroundColor(Color.parseColor("#5b04b1"));
            }else {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            currentFragment = fragementNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            if(currentFragment==MyMallFragment){
                currentFragment=-1;
                super.onBackPressed();
            }else {
                if(showCart){
                    showCart=false;
                    finish();
                }else {
                    actionbarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new MyMallFragment(),MyMallFragment);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }
}
