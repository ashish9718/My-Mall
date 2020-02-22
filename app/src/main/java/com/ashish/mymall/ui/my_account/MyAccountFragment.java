package com.ashish.mymall.ui.my_account;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ashish.mymall.DBquerries;
import com.ashish.mymall.DeliveryActivity;
import com.ashish.mymall.MainActivity;
import com.ashish.mymall.MyAddressesActivity;
import com.ashish.mymall.MyOrderItemModel;
import com.ashish.mymall.R;
import com.ashish.mymall.RegisterActivity;
import com.ashish.mymall.UpdateUserInfoActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccountFragment extends Fragment {
    public MyAccountFragment() {
    }

    private FloatingActionButton settingsBtn;
    public final static int MANAGE_ADDRESS =1;
    private Button viewAllAddressButton,signOutBtn;
    private CircleImageView profileView,currentOrderImage;
    private TextView name,email,currentOrderstatus,recentOrdersTitle,addressname,address,pincode;
    private LinearLayout layoutContainer,recentOrdersContainer;
    private Dialog loadingDialog;
    private ImageView orderedIndicator,packedIndicator,shippedIndicator,deliveredIndicator;
    private ProgressBar O_P_progress,P_S_progress,S_D_progress;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_my_account, container, false);
        viewAllAddressButton=root.findViewById(R.id.view_all_address_button);
        profileView=root.findViewById(R.id.profile_pic);
        name=root.findViewById(R.id.username);
        email=root.findViewById(R.id.user_email);
        layoutContainer=root.findViewById(R.id.layout_container);
        currentOrderImage=root.findViewById(R.id.current_order_image);
        currentOrderstatus=root.findViewById(R.id.tv_current_order_status);

        orderedIndicator=root.findViewById(R.id.ordered_indicator);
        packedIndicator=root.findViewById(R.id.packed_indicator);
        shippedIndicator=root.findViewById(R.id.shipped_indicator);
        deliveredIndicator=root.findViewById(R.id.delivered_indicator);

        O_P_progress=root.findViewById(R.id.order_packed_progress);
        P_S_progress=root.findViewById(R.id.packed_shipped_progress);
        S_D_progress=root.findViewById(R.id.shipped_delivered_progress);

        recentOrdersTitle=root.findViewById(R.id.your_recent_orders_title);
        recentOrdersContainer=root.findViewById(R.id.recent_orders_container);

        addressname=root.findViewById(R.id.name);
        address=root.findViewById(R.id.address);
        pincode=root.findViewById(R.id.pincode);
        signOutBtn=root.findViewById(R.id.sign_out_btn);
        settingsBtn=root.findViewById(R.id.settings_btn);


//////////loading dialog

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
//////////loading dialog

        layoutContainer.getChildAt(1).setVisibility(View.GONE);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                for(MyOrderItemModel orderItemModel:DBquerries.myOrderItemModelList){
                    if(!orderItemModel.isCancellationrequested()){
                        if(!orderItemModel.getOrderStatus().equals("Delivered") && !orderItemModel.getOrderStatus().equals("Cancelled")){
                            layoutContainer.getChildAt(1).setVisibility(View.VISIBLE);
                            Glide.with(getContext()).load(orderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.mipmap.pic)).into(currentOrderImage);
                            currentOrderstatus.setText(orderItemModel.getOrderStatus());

                            switch (orderItemModel.getOrderStatus()){
                                case "Ordered":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    break;
                                case "Packed":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    O_P_progress.setProgress(100);
                                    break;
                                case "Shipped":
                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    O_P_progress.setProgress(100);
                                    P_S_progress.setProgress(100);
                                    break;
                                case "out for Delivery":
                                    deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    O_P_progress.setProgress(100);
                                    P_S_progress.setProgress(100);
                                    S_D_progress.setProgress(100);

                                    break;
                            }


                        }
                    }
                }
                int i=0;
                for(MyOrderItemModel myOrderItemModel:DBquerries.myOrderItemModelList){
                    if(i<4) {
                        if (myOrderItemModel.getOrderStatus().equals("Delivered")) {
                            Glide.with(getContext()).load(myOrderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.mipmap.pic)).into((CircleImageView) recentOrdersContainer.getChildAt(i));
                            i++;
                        }
                    }else {
                        break;
                    }
                }
                if(i==0){
                    recentOrdersTitle.setText("No recent Orders");
                }
                if(i<3){
                    for (int x=i ; x<4;x++){
                        recentOrdersContainer.getChildAt(x).setVisibility(View.GONE);
                    }
                }
                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        loadingDialog.setOnDismissListener(null);
                        if(DBquerries.addressesModelList.size() == 0){
                            addressname.setText("No Address");
                            address.setText("-");
                            pincode.setText("-");
                        }else {
                            setAddress();
                        }
                    }
                });
                DBquerries.loadAddresses(getContext(),loadingDialog,false);

            }
        });

        DBquerries.loadOrders(getContext(),null,loadingDialog);

        viewAllAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), MyAddressesActivity.class).putExtra("MODE",MANAGE_ADDRESS));
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                DBquerries.clearData();
                DBquerries.email=null;  //my code
                startActivity(new Intent(getContext(), RegisterActivity.class));
                getActivity().finish();
            }
        });


        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateUserInfo = new Intent(getContext(), UpdateUserInfoActivity.class);
                updateUserInfo.putExtra("Name",name.getText());
                updateUserInfo.putExtra("Email",email.getText());
                updateUserInfo.putExtra("Photo",DBquerries.profile);
                startActivity(updateUserInfo);
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        name.setText(DBquerries.fullname);
        email.setText(DBquerries.email);
        if(!DBquerries.profile.equals("")){
            Glide.with(getContext()).load(DBquerries.profile).apply(new RequestOptions().placeholder(R.mipmap.user_blue)).into(profileView);
        }else {
            profileView.setImageResource(R.mipmap.user_blue);
        }

        if(!loadingDialog.isShowing()){
            if(DBquerries.addressesModelList.size() == 0){
                addressname.setText("No Address");
                address.setText("-");
                pincode.setText("-");
            }else {
                setAddress();
            }
        }
    }

    private void setAddress() {
        String nametext,mobileNo;
        nametext = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getName();
        mobileNo = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getMobileNo();
        if(DBquerries.addressesModelList.get(DBquerries.selectedAddress).getAlternateMobileNo().equals("")){
            addressname.setText(nametext + " - " + mobileNo);
        }else {
            addressname.setText(nametext + " - " + mobileNo+" or "+DBquerries.addressesModelList.get(DBquerries.selectedAddress).getAlternateMobileNo());
        }

        String flatNo = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getFlatNo();
        String city = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getCity();
        String locality = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getLocality();
        String landmark = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getLandmark();
        String state = DBquerries.addressesModelList.get(DBquerries.selectedAddress).getState();

        if(landmark.equals("")){
            address.setText(flatNo+","+locality+","+city+","+state);
        }else {
            address.setText(flatNo+","+locality+","+landmark+","+city+","+state);
        }
        pincode.setText(DBquerries.addressesModelList.get(DBquerries.selectedAddress).getPincode());

    }
}