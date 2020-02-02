package com.ashish.mymall;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private List<MyOrderItemModel> myOrderItemModelList;
    private LinearLayout rateNowContainer;

    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }

    @NonNull
    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.ViewHolder holder, int position) {

        int resource=myOrderItemModelList.get(position).getProductImage();
        int rating=myOrderItemModelList.get(position).getRating();
        String title=myOrderItemModelList.get(position).getProductTitle();
        String deliveredDate=myOrderItemModelList.get(position).getDeliveryStatus();
        holder.setdata(resource,title,deliveredDate,rating);

    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage,orderIndicator;
        private TextView productTitle,deliveryStatus;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.product_image);
            orderIndicator=itemView.findViewById(R.id.order_indicator);
            productTitle=itemView.findViewById(R.id.product_title);
            deliveryStatus=itemView.findViewById(R.id.order_delivered_date);
            rateNowContainer=itemView.findViewById(R.id.rate_now_container);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(),OrderDetailsActivity.class));
                }
            });
        }

        private void setdata(int resource,String title,String deliveredDate,int rating){
            productTitle.setText(title);
            productImage.setImageResource(resource);
            if(deliveredDate.equals("Cancelled")){
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
            }else {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.successGreen)));
            }
            deliveryStatus.setText(deliveredDate);

            //////rating layout
            setRating(rating);

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

    }

}
