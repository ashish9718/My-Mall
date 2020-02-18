package com.ashish.mymall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {

    private SimpleDateFormat simpleDateFormat;
    private Dialog loadingDialog,cancelDialog;
    private int position,rating;
    private LinearLayout rateNowcontainer;
    private TextView title,price,qty;
    private ImageView productImage,orderedIndicator,packedIndicator,shippedIndicator,deliveredIndicator;
    private ProgressBar O_P_progress,P_S_progress,S_D_progress;
    private TextView orderedTitle,packedTitle,shippedTitle,deliveredTitle;
    private TextView orderedDate,shippedDate,packedDate,deliveredDate;
    private TextView orderedBody,shippedBody,packedBody,deliveredBody;
    private TextView fullname,address,pincode;
    private TextView totalItems,totalItemsPrice,deliveryPrice,savedAmount,totalAmount;
    private Button cancelOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order details");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //////////loading dialog

        loadingDialog = new Dialog(OrderDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //////////loading dialog


        //////////cancel dialog

        cancelDialog = new Dialog(OrderDetailsActivity.this);
        cancelDialog.setContentView(R.layout.order_cancel_dialog);
        cancelDialog.setCancelable(true);
        cancelDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
//        cancelDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //////////cancel dialog

        title=findViewById(R.id.product_title);
        price=findViewById(R.id.product_price);
        qty=findViewById(R.id.product_quantity);
        productImage=findViewById(R.id.product_image);

        orderedIndicator=findViewById(R.id.ordered_indicator);
        packedIndicator=findViewById(R.id.packed_indicator);
        shippedIndicator=findViewById(R.id.shipping_indicator);
        deliveredIndicator=findViewById(R.id.delivered_indicator);

        O_P_progress=findViewById(R.id.ordered_packed_progress);
        P_S_progress=findViewById(R.id.packed_shipping_progress);
        S_D_progress=findViewById(R.id.shipping_delivered_progress);

        orderedTitle=findViewById(R.id.ordered_title);
        packedTitle=findViewById(R.id.packed_title);
        shippedTitle=findViewById(R.id.shipping_title);
        deliveredTitle=findViewById(R.id.delivered_title);

        orderedBody=findViewById(R.id.ordered_body);
        packedBody=findViewById(R.id.packed_body);
        shippedBody=findViewById(R.id.shipping_body);
        deliveredBody=findViewById(R.id.delivered_body);

        orderedDate=findViewById(R.id.ordered_date);
        packedDate=findViewById(R.id.packed_date);
        shippedDate=findViewById(R.id.shipping_date);
        deliveredDate=findViewById(R.id.delivered_date);

        rateNowcontainer=findViewById(R.id.rate_now_container);

        fullname=findViewById(R.id.fullname);
        address=findViewById(R.id.address);
        pincode=findViewById(R.id.pincode);

        totalItems=findViewById(R.id.total_items);
        totalItemsPrice=findViewById(R.id.total_items_price);
        deliveryPrice=findViewById(R.id.delivery_price);
        savedAmount=findViewById(R.id.saved_amount);
        totalAmount=findViewById(R.id.total_price);
        cancelOrderBtn=findViewById(R.id.cancel_btn);

        position=getIntent().getIntExtra("position",-1);

        final MyOrderItemModel model=DBquerries.myOrderItemModelList.get(position);


        title.setText(model.getProductTitle());
        if(!model.getDiscountedPrice().equals("")){
            price.setText("Rs."+model.getDiscountedPrice()+"/-");
        }else {
            price.setText("Rs."+model.getProductPrice()+"/-");
        }
        Glide.with(this).load(model.getProductImage()).into(productImage);
        qty.setText("Qty :"+String.valueOf(model.getProductQuantity()));

        simpleDateFormat=new SimpleDateFormat("EEE, dd MMM YYYY hh:mm aa");
        switch (model.getOrderStatus()){
            case "Ordered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                P_S_progress.setVisibility(View.GONE);
                S_D_progress.setVisibility(View.GONE);
                O_P_progress.setVisibility(View.GONE);

                packedIndicator.setVisibility(View.GONE);
                packedTitle.setVisibility(View.GONE);
                packedBody.setVisibility(View.GONE);
                packedDate.setVisibility(View.GONE);

                shippedIndicator.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);
                shippedBody.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);

                deliveredIndicator.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);

                break;

            case "Packed":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                O_P_progress.setProgress(100);

                P_S_progress.setVisibility(View.GONE);
                S_D_progress.setVisibility(View.GONE);


                shippedIndicator.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);
                shippedBody.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);

                deliveredIndicator.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);

                break;

            case "Shipped":
                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setVisibility(View.GONE);

                deliveredIndicator.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);

                break;
            case "out for Delivery":
                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getDelveredDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setProgress(100);

                deliveredTitle.setText("Out for Delivery");
                deliveredBody.setText("Your order is out for delivery");

                break;
            case "Delivered":
                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getDelveredDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setProgress(100);

                break;

            case "Cancelled":

                if(model.getPackedDate().after(model.getOrderedDate())){

                    if(model.getShippedDate().after(model.getPackedDate())){

                        deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getDelveredDate())));
                        deliveredBody.setText("Your order has been cancelled.");
                        deliveredTitle.setText("Cancelled");

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                        O_P_progress.setProgress(100);
                        P_S_progress.setProgress(100);
                        S_D_progress.setProgress(100);

                    }else {
                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));
                        shippedBody.setText("Your order has been cancelled.");
                        shippedTitle.setText("Cancelled");

                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                        O_P_progress.setProgress(100);
                        P_S_progress.setProgress(100);
                        S_D_progress.setVisibility(View.GONE);

                        deliveredIndicator.setVisibility(View.GONE);
                        deliveredTitle.setVisibility(View.GONE);
                        deliveredBody.setVisibility(View.GONE);
                        deliveredDate.setVisibility(View.GONE);
                    }

                }else {
                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                    orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                    packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));
                    packedBody.setText("Your order has been cancelled.");
                    packedTitle.setText("Cancelled");

                    O_P_progress.setProgress(100);

                    P_S_progress.setVisibility(View.GONE);
                    S_D_progress.setVisibility(View.GONE);


                    shippedIndicator.setVisibility(View.GONE);
                    shippedTitle.setVisibility(View.GONE);
                    shippedBody.setVisibility(View.GONE);
                    shippedDate.setVisibility(View.GONE);

                    deliveredIndicator.setVisibility(View.GONE);
                    deliveredTitle.setVisibility(View.GONE);
                    deliveredBody.setVisibility(View.GONE);
                    deliveredDate.setVisibility(View.GONE);
                }

                break;
        }

        //////rating layout
        rating=model.getRating();
        final String productId=model.getProductId();
        setRating(rating);

        for(int x=0;x<rateNowcontainer.getChildCount();x++){
            final int starPosition= x;
            rateNowcontainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
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
                                                Toast.makeText(OrderDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
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

        if(model.isCancellationrequested()){
            cancelOrderBtn.setVisibility(View.VISIBLE);
            cancelOrderBtn.setEnabled(false);
            cancelOrderBtn.setText("Cancellation in process");
            cancelOrderBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            cancelOrderBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        }else {
            if(model.getOrderStatus().equals("Ordered") || model.getOrderStatus().equals("Packed")){
                cancelOrderBtn.setVisibility(View.VISIBLE);
                cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelDialog.findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cancelDialog.dismiss();
                            }
                        });

                        cancelDialog.findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cancelDialog.dismiss();
                                loadingDialog.show();
                                Map<String, Object> map=new HashMap<>();
                                map.put("Order Id",model.getOrderId());
                                map.put("Product Id",model.getProductId());
                                map.put("Order Cancelled",false);
                                FirebaseFirestore.getInstance().collection("CANCELLED ORDERS").document(model.getOrderId()).set(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    FirebaseFirestore.getInstance().collection("ORDERS").document(model.getOrderId()).collection("ORDER_ITEMS").document(model.getProductId()).update("Cancellation requested",true)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        model.setCancellationrequested(true);
                                                                        cancelOrderBtn.setEnabled(false);
                                                                        cancelOrderBtn.setText("Cancellation in process");
                                                                        cancelOrderBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                                                                        cancelOrderBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                                                                    }else {
                                                                        String error=task.getException().getMessage();
                                                                        Toast.makeText(OrderDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    loadingDialog.dismiss();
                                                                }
                                                            });

                                                }else {
                                                    loadingDialog.dismiss();
                                                    String error=task.getException().getMessage();
                                                    Toast.makeText(OrderDetailsActivity.this,error,Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        });
                        cancelDialog.show();
                    }
                });
            }
        }

        fullname.setText(model.getFullName());
        address.setText(model.getAddress());
        pincode.setText(model.getPincode());

        Long totalPrice;

        totalItems.setText("Price("+model.getProductQuantity()+" items)");
        if(model.getDiscountedPrice().equals("")){
            totalPrice=Long.valueOf(model.getProductPrice())*model.getProductQuantity();
            totalItemsPrice.setText("Rs."+ String.valueOf(totalPrice) +"/-");
        }else {
            totalPrice=Long.valueOf(model.getDiscountedPrice())*model.getProductQuantity();
            totalItemsPrice.setText("Rs."+ String.valueOf(totalPrice)+"/-");
        }
        if(model.getDeliveryPrice().equals("FREE")){
            deliveryPrice.setText("FREE");
            totalAmount.setText("Rs."+ totalPrice +"/-");
        }else {
            deliveryPrice.setText("Rs."+model.getDeliveryPrice()+"/-");
            totalAmount.setText("Rs."+String.valueOf(totalPrice+Long.valueOf(model.getDeliveryPrice()))+"/-");
        }


        if(!model.getCuttedPrice().equals("")){
            if(!model.getDiscountedPrice().equals("")){
                savedAmount.setText("You saved Rs."+ (model.getProductQuantity())*(Long.valueOf(model.getCuttedPrice()) - Long.valueOf(model.getDiscountedPrice())) +"/- on this order.");
            }else {
                savedAmount.setText("You saved Rs."+ (model.getProductQuantity())*(Long.valueOf(model.getCuttedPrice()) - Long.valueOf(model.getProductPrice())) +"/- on this order.");
            }
        }else {
            if(!model.getDiscountedPrice().equals("")){
                savedAmount.setText("You saved Rs."+ (model.getProductQuantity())*(Long.valueOf(model.getProductPrice()) - Long.valueOf(model.getDiscountedPrice())) +"/- on this order.");
            }else {
                savedAmount.setText("You saved Rs.0/- on this order.");
            }
        }





    }

    private void setRating(int starPosition) {
        for(int x=0;x<rateNowcontainer.getChildCount();x++){
            ImageView starBtn=(ImageView)rateNowcontainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if(x<=starPosition){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
