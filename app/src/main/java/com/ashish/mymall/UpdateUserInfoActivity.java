package com.ashish.mymall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;

public class UpdateUserInfoActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private UpdateInfoFragment updateInfoFragment;
    private PasswordFragment passwordFragment;
    private String name,email,photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        tabLayout=findViewById(R.id.tab_layout);
        frameLayout=findViewById(R.id.frame_layout);

        updateInfoFragment=new UpdateInfoFragment();
        passwordFragment=new PasswordFragment();

        name=getIntent().getStringExtra("Name");
        email=getIntent().getStringExtra("Email");
        photo=getIntent().getStringExtra("Photo");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    setFragment(updateInfoFragment,true);
                }
                if(tab.getPosition() == 1){
                    setFragment(passwordFragment,false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.getTabAt(0).select();
        setFragment(updateInfoFragment,true);

    }

    private void setFragment(Fragment fragment,boolean setBundle) {
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();

        if(setBundle) {
            Bundle bundle = new Bundle();
            bundle.putString("Name", name);
            bundle.putString("Email", email);
            bundle.putString("Photo", photo);
            fragment.setArguments(bundle);
        }else {
            Bundle bundle = new Bundle();
            bundle.putString("Email", email);
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();

    }
}
