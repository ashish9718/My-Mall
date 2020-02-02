package com.ashish.mymall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {

    private List<RewardModel> rewardModelList;
    private Boolean useMinilayout=false;

    public MyRewardsAdapter(List<RewardModel> rewardModelList,Boolean useMinilayout) {
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
        String title=rewardModelList.get(position).getTitle();
        String date=rewardModelList.get(position).getExpiryDate();
        String body=rewardModelList.get(position).getCoupanBody();
        holder.setData(title,date,body);

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

        private void setData(final String title, final String date, final String body){
            coupanBody.setText(body);
            coupanExpiryDate.setText(date);
            coupanTitle.setText(title);

            if(useMinilayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ProductDetailsActivity.coupanTitle.setText(title);
                        ProductDetailsActivity.coupanExpiryDate.setText(date);
                        ProductDetailsActivity.coupanBody.setText(body);
                        ProductDetailsActivity.showDialogRecyclerView();
                    }
                });
            }


        }
    }
}
