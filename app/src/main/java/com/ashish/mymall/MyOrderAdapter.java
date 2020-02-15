package com.ashish.mymall;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private List<MyOrderItemModel> myOrderItemModelList;
    private LinearLayout rateNowContainer;
    private Dialog loadingDialog;

    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList,Dialog loadingDialog) {
        this.myOrderItemModelList = myOrderItemModelList;
        this.loadingDialog=loadingDialog;
    }

    @NonNull
    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.ViewHolder holder, int position) {

        String productId=myOrderItemModelList.get(position).getProductId();
//        String address=myOrderItemModelList.get(position).getAddress();
//        String coupanId=myOrderItemModelList.get(position).getCoupanId();
//        String productPrice=myOrderItemModelList.get(position).getProductPrice();
//        String cuttedPrice=myOrderItemModelList.get(position).getCuttedPrice();
//        String discountedPrice=myOrderItemModelList.get(position).getDiscountedPrice();
//        Date orderedDate=myOrderItemModelList.get(position).getOrderedDate();
//        Date packedDate=myOrderItemModelList.get(position).getPackedDate();
//        Date shippedDate=myOrderItemModelList.get(position).getShippedDate();
//        Date delveredDate=myOrderItemModelList.get(position).getDelveredDate();
//        Date cancelleddate=myOrderItemModelList.get(position).getCancelleddate();
//        Long freeCoupans=myOrderItemModelList.get(position).getFreeCoupans();
//        Long productQuantity=myOrderItemModelList.get(position).getProductQuantity();
//        String fullName=myOrderItemModelList.get(position).getFullName();
//        String orderId=myOrderItemModelList.get(position).getOrderId();
//        String paymentMethod=myOrderItemModelList.get(position).getPaymentMethod();
//        String pincode=myOrderItemModelList.get(position).getPincode();
//        String userId=myOrderItemModelList.get(position).getUserId();

        String productTitle=myOrderItemModelList.get(position).getProductTitle();
        String orderStatus=myOrderItemModelList.get(position).getOrderStatus();
        String productImage=myOrderItemModelList.get(position).getProductImage();

        Date date;
        switch (orderStatus){
            case "Ordered":
                date=myOrderItemModelList.get(position).getOrderedDate();
                break;
            case "Packed":
                date=myOrderItemModelList.get(position).getPackedDate();
                break;
            case "Shipped":
                date=myOrderItemModelList.get(position).getShippedDate();
                break;
            case "Delivered":
                date=myOrderItemModelList.get(position).getDelveredDate();
                break;
            case "Cancelled":
                date=myOrderItemModelList.get(position).getCancelleddate();
                break;
            default:
                date=myOrderItemModelList.get(position).getCancelleddate();
                break;

        }
        int rating=myOrderItemModelList.get(position).getRating();
//        holder.setdata(productId, orderStatus, address, coupanId, productPrice, cuttedPrice, discountedPrice, orderedDate, packedDate, shippedDate, delveredDate, cancelleddate, freeCoupans, productQuantity, fullName, orderId, paymentMethod,pincode,userId,productTitle,productImage,rating);
        holder.setdata(productImage,productTitle,orderStatus,date,rating,productId,position);
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

        }

        private void setdata(String resource, String productTitleText, String orderStatus, Date date, final int rating, final String productId, final int position){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE, dd MMM YYYY hh:mm aa");

            productTitle.setText(productTitleText);
            Glide.with(itemView.getContext()).load(resource).into(productImage);
            if(orderStatus.equals("Cancelled")){
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
            }else {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.successGreen)));
            }
            deliveryStatus.setText(orderStatus +" "+simpleDateFormat.format(date));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(),OrderDetailsActivity.class).putExtra("position",position));
                }
            });

            //////rating layout
            setRating(rating);

            for(int x=0;x<rateNowContainer.getChildCount();x++){
                final int starPosition= x;
                rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadingDialog.show();
                        setRating(starPosition);
                        final DocumentReference documentReference= FirebaseFirestore.getInstance().collection("PRODUCTS").document(productId);
                        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
                            @Nullable
                            @Override
                            public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                DocumentSnapshot documentSnapshot=transaction.get(documentReference);

                                if(rating != 0){
                                    Long increase=documentSnapshot.getLong(starPosition+1+"_star")+1;
                                    Long decrease=documentSnapshot.getLong(rating+1+"_star")-1;
                                    transaction.update(documentReference,starPosition+1+"_star",increase);
                                    transaction.update(documentReference,rating+1+"_star",decrease);
                                }else {
                                    Long increase=documentSnapshot.getLong(starPosition+1+"_star")+1;
                                    transaction.update(documentReference,starPosition+1+"_star",increase);
                                }
                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                Map<String, Object> myRating = new HashMap<>();

                                if (DBquerries.myRatedIds.contains(productId)) {
                                    myRating.put("rating_" + DBquerries.myRatedIds.indexOf(productId), (long) starPosition + 1);
                                } else {
                                    myRating.put("product_ID_" + DBquerries.myRatedIds.size(), productId);
                                    myRating.put("rating_" + DBquerries.myRatedIds.size(), (long) starPosition + 1);
                                    myRating.put("list_size", (long) DBquerries.myRatedIds.size() + 1);
                                }

                                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                                        .update(myRating)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if (task.isSuccessful()) {
                                                       DBquerries.myOrderItemModelList.get(position).setRating(starPosition);
                                                       if(DBquerries.myRatedIds.contains(productId)){
                                                           DBquerries.myRating.set(DBquerries.myRatedIds.indexOf(productId), Long.valueOf(starPosition+1));
                                                       }else {
                                                           DBquerries.myRatedIds.add(productId);
                                                           DBquerries.myRating.add(Long.valueOf(starPosition+1));
                                                       }
                                                   }else {
                                                       String error=task.getException().getMessage();
                                                       Toast.makeText(itemView.getContext(),error,Toast.LENGTH_SHORT).show();
                                                   }
                                                   loadingDialog.dismiss();
                                               }
                                        });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingDialog.dismiss();
                            }
                        });
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
