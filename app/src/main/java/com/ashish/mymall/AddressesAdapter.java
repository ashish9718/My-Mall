package com.ashish.mymall;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static com.ashish.mymall.DeliveryActivity.SELECT_ADDRESS;
import static com.ashish.mymall.MyAddressesActivity.refreshItem;
import static com.ashish.mymall.ui.my_account.MyAccountFragment.MANAGE_ADDRESS;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    private List<AddressesModel> addressesModelList;
    private int MODE,preSelectedPos;
    private boolean refresh=false;
    private Dialog loadingDialog;

    public AddressesAdapter(List<AddressesModel> addressesModelList, int MODE,Dialog loadingDialog) {
        this.addressesModelList = addressesModelList;
        this.MODE=MODE;
        preSelectedPos=DBquerries.selectedAddress;
        this.loadingDialog=loadingDialog;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name=addressesModelList.get(position).getName();
        String mobileNo=addressesModelList.get(position).getMobileNo();
        String pincode=addressesModelList.get(position).getPincode();
        boolean selected=addressesModelList.get(position).getSelected();
        String flatNo=addressesModelList.get(position).getFlatNo();
        String locality=addressesModelList.get(position).getLocality();
        String city=addressesModelList.get(position).getCity();
        String landmark=addressesModelList.get(position).getLandmark();
        String state=addressesModelList.get(position).getState();
        String alternateMobileNo=addressesModelList.get(position).getAlternateMobileNo();
        holder.setdata(name,mobileNo,pincode,selected,flatNo,landmark,locality,city,state,alternateMobileNo,position);
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView fullname,pincode,address;
        private ImageView icon;
        private LinearLayout optionContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullname=itemView.findViewById(R.id.name);
            address=itemView.findViewById(R.id.address);
            pincode=itemView.findViewById(R.id.pincode);
            icon=itemView.findViewById(R.id.icon_view);
            optionContainer=itemView.findViewById(R.id.option_container);
        }

        private void setdata(String name,String mobileNo,String pincodevalue,boolean selected,String flatNo,String landmark,String locality,String city,String state,String alternateMobileNo,final int position){

            if(alternateMobileNo.equals("")){
                fullname.setText(name + " - " + mobileNo);
            }else {
                fullname.setText(name + " - " + mobileNo+" or "+alternateMobileNo);
            }

            if(landmark.equals("")){
                address.setText(flatNo+","+locality+","+city+","+state);
            }else {
                address.setText(flatNo+","+locality+","+landmark+","+city+","+state);
            }
            pincode.setText(pincodevalue);

            if(MODE==SELECT_ADDRESS){
                icon.setImageResource(R.drawable.check);
                if(selected){
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPos=position;
                }else {
                    icon.setVisibility(View.GONE);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(preSelectedPos!=position) {
                            addressesModelList.get(position).setSelected(true);
                            addressesModelList.get(preSelectedPos).setSelected(false);
                            refreshItem(preSelectedPos, position);
                            preSelectedPos = position;
                            DBquerries.selectedAddress=position;
                        }
                    }
                });
            }
            else if(MODE==MANAGE_ADDRESS){
                optionContainer.setVisibility(View.GONE);
                optionContainer.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {  ////edit address
                        itemView.getContext().startActivity(new Intent(itemView.getContext(),AddAddressActivity.class).putExtra("INTENT","update_address").putExtra("Position",position));
                        refresh=false;
                    }
                });
                optionContainer.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {  /// remove address
                        loadingDialog.show();
                        Map<String,Object> addresses = new HashMap<>();
                        int x=0,selected=-1;
                        for(int i=0;i<addressesModelList.size();i++){
                            if(i != position){
                                x++;
                                addresses.put("city_"+x,addressesModelList.get(i).getCity());
                                addresses.put("locality_"+x,addressesModelList.get(i).getLocality());
                                addresses.put("flat_no_"+x,addressesModelList.get(i).getFlatNo());
                                addresses.put("pincode_"+x,addressesModelList.get(i).getPincode());
                                addresses.put("landmark_"+x,addressesModelList.get(i).getLandmark());
                                addresses.put("name_"+x,addressesModelList.get(i).getName());
                                addresses.put("mobile_no_"+x,addressesModelList.get(i).getMobileNo());
                                addresses.put("alternate_mobile_no_"+x,addressesModelList.get(i).getAlternateMobileNo());
                                addresses.put("state_"+x,addressesModelList.get(i).getState());
                                if(addressesModelList.get(position).getSelected()){
                                    if(position-1>=0 ){
                                        if(x == position){
                                            addresses.put("selected_"+x,true);
                                            selected=x;
                                        }else {
                                            addresses.put("selected_"+x,addressesModelList.get(i).getSelected());
                                        }
                                    }else {
                                        if(x == 1){
                                            addresses.put("selected_"+x,true);
                                            selected=x;
                                        }else {
                                            addresses.put("selected_"+x,addressesModelList.get(i).getSelected());
                                        }
                                    }
                                }else {
                                    addresses.put("selected_"+x,addressesModelList.get(i).getSelected());
                                    if(addressesModelList.get(i).getSelected()){
                                        selected=x;
                                    }
                                }
                            }
                        }
                        addresses.put("list_size",x);
                        final int finalSelected = selected;
                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                                .set(addresses).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    DBquerries.addressesModelList.remove(position);
                                    if(finalSelected != -1){
                                        DBquerries.selectedAddress= finalSelected -1;
                                        DBquerries.addressesModelList.get(finalSelected-1).setSelected(true);
                                    }else if(DBquerries.addressesModelList.size() == 0){
                                        DBquerries.selectedAddress=-1;
                                    }
                                    notifyDataSetChanged();
                                }else {
                                    String error=task.getException().getMessage();
                                    Toast.makeText(itemView.getContext(),error,Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });
                        refresh=false;
                    }
                });
                icon.setImageResource(R.drawable.menu);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        optionContainer.setVisibility(View.VISIBLE);
                        if(refresh){
                            refreshItem(preSelectedPos, preSelectedPos);
                        }else {
                            refresh=true;
                        }
                        preSelectedPos = position;

                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refreshItem(preSelectedPos, preSelectedPos);
                        preSelectedPos=-1;
                    }
                });
            }
        }
    }
}
