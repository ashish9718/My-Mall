package com.ashish.mymall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class RegisterActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    public static boolean onResetPassFragment=false;
    public static boolean setsignUpFragment=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        frameLayout=findViewById(R.id.reg_frame_layout);
        if(setsignUpFragment){
            setsignUpFragment=false;
            setDefaultFragment(new SignupFragment());
        }else {
            setDefaultFragment(new SigninFragment());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){
            SigninFragment.disableCloseBtn=false;
            SignupFragment.disableCloseBtn=false;
            if(onResetPassFragment){
                onResetPassFragment=false;
                setFragment(new SigninFragment());
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void setDefaultFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}
