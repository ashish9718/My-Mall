package com.ashish.mymall.ui.my_account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ashish.mymall.MyAddressesActivity;
import com.ashish.mymall.R;

public class MyAccountFragment extends Fragment {
    public MyAccountFragment() {
    }

    public final static int MANAGE_ADDRESS =1;
    private Button viewAllAddressButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_my_account, container, false);
        viewAllAddressButton=root.findViewById(R.id.view_all_address_button);

        viewAllAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), MyAddressesActivity.class).putExtra("MODE",MANAGE_ADDRESS));
            }
        });
        return root;
    }
}