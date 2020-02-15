package com.ashish.mymall;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {

    private List<RewardModel> rewardModelList;
    private List<CartItemModel> cartItemModelList;
    private Boolean useMinilayout=false;
    private RecyclerView coupanRecyclerview;
    private LinearLayout selectedCoupan;
    private Long productOriginalPrice;
    private TextView selectedcoupanTitle;
    private TextView selectedcoupanExpiryDate;
    private TextView selectedcoupanBody,discountedPrice;
    private int position=-1;

    public MyRewardsAdapter(int position,List<RewardModel> rewardModelList, Boolean useMinilayout, RecyclerView coupanRecyclerview, LinearLayout selectedCoupan, Long productOriginalPrice, TextView coupanTitle, TextView coupanExpiryDate, TextView coupanBody,TextView discountedPrice,List<CartItemModel> cartItemModelList) {
        this.position=position;
        this.rewardModelList = rewardModelList;
        this.useMinilayout = useMinilayout;
        this.coupanRecyclerview = coupanRecyclerview;
        this.selectedCoupan = selectedCoupan;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedcoupanTitle = coupanTitle;
        this.selectedcoupanExpiryDate = coupanExpiryDate;
        this.selectedcoupanBody = coupanBody;
        this.discountedPrice=discountedPrice;
        this.cartItemModelList=cartItemModelList;
    }

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMinilayout, RecyclerView coupanRecyclerview, LinearLayout selectedCoupan, Long productOriginalPrice, TextView coupanTitle, TextView coupanExpiryDate, TextView coupanBody,TextView discountedPrice) {
        this.rewardModelList = rewardModelList;
        this.useMinilayout = useMinilayout;
        this.coupanRecyclerview = coupanRecyclerview;
        this.selectedCoupan = selectedCoupan;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedcoupanTitle = coupanTitle;
        this.selectedcoupanExpiryDate = coupanExpiryDate;
        this.selectedcoupanBody = coupanBody;
        this.discountedPrice=discountedPrice;
    }


    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMinilayout) {
        this.rewardModelList = rewardModelList;
        this.useMinilayout=useMinilayout;
    }

    @NonNull
    @Override
    public MyRewardsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(useMinilayout){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewards_item_layout, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item_layout, parent, false);
        }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRewardsAdapter.Viewholder holder, int position) {
        String type=rewardModelList.get(position).getType();
        String upperLimit=rewardModelList.get(position).getUpperLimit();
        String lowerLimit=rewardModelList.get(position).getLowerLimit();
        String discORamt=rewardModelList.get(position).getDiscORamt();
        String body=rewardModelList.get(position).getCoupanBody();
        Date validity=rewardModelList.get(position).getTimestamp();
        boolean alreadyUsed=rewardModelList.get(position).isAlreadyUsed();
        String coupanId=rewardModelList.get(position).getCoupanId();
        holder.setData(type,upperLimit,lowerLimit,discORamt,body,validity,alreadyUsed,coupanId);

    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView coupanTitle,coupanExpiryDate,coupanBody;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            coupanTitle=itemView.findViewById(R.id.coupan_title);
            coupanExpiryDate=itemView.findViewById(R.id.coupan_validity);
            coupanBody=itemView.findViewById(R.id.coupan_body);
        }

        private void setData(final String type, final String upperLimit, final String lowerLimit, final String discORamt, final String body, final Date validity, final boolean alreadyUsed, final String coupanId){
            if(type.equals("Discount")){
                coupanTitle.setText(type);
            }else {
                coupanTitle.setText("FLAT Rs."+discORamt+" OFF");
            }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");

            if(alreadyUsed){
                coupanExpiryDate.setText("Already Used");
                coupanExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                coupanBody.setTextColor(Color.parseColor("#50ffffff"));
                coupanTitle.setTextColor(Color.parseColor("#50ffffff"));
            }else {
                coupanBody.setTextColor(Color.parseColor("#ffffff"));
                coupanTitle.setTextColor(Color.parseColor("#ffffff"));
                coupanExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.coupanPurple));
                coupanExpiryDate.setText("till " + simpleDateFormat.format(validity));
            }
            coupanBody.setText(body);


            if(useMinilayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!alreadyUsed) {

                            if (type.equals("Discount")) {
                                selectedcoupanTitle.setText(type);
                            } else {
                                selectedcoupanTitle.setText("FLAT Rs." + discORamt + " OFF");
                            }
                            selectedcoupanExpiryDate.setText("till " + simpleDateFormat.format(validity));
                            selectedcoupanBody.setText(body);

                            if (productOriginalPrice > Long.valueOf(lowerLimit) && productOriginalPrice < Long.valueOf(upperLimit)) {
                                if (type.equals("Discount")) {
                                    Long discountAmount = productOriginalPrice * Long.valueOf(discORamt) / 100;
                                    discountedPrice.setText("Rs." + String.valueOf(productOriginalPrice - discountAmount) + "/-");
                                } else {
                                    discountedPrice.setText("Rs." + String.valueOf(productOriginalPrice - Long.valueOf(discORamt)) + "/-");

                                }
                                if(position != -1) {
                                    cartItemModelList.get(position).setSelectedCoupanId(coupanId);
                                }
                            } else {
                                if(position != -1) {
                                    cartItemModelList.get(position).setSelectedCoupanId(null);
                                }
                                Toast.makeText(itemView.getContext(), "Product doesn't matches Coupan terms.", Toast.LENGTH_SHORT).show();
                            }

                            if (coupanRecyclerview.getVisibility() == View.GONE) {
                                coupanRecyclerview.setVisibility(View.VISIBLE);
                                selectedCoupan.setVisibility(View.GONE);
                            } else {
                                coupanRecyclerview.setVisibility(View.GONE);
                                selectedCoupan.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }


        }
    }
}
