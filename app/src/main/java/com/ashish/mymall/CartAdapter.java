package com.ashish.mymall;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private boolean showDeleteBtn;
    private int lastpos=-1;
    private TextView cartTotalAmount;
    public CartAdapter(List<CartItemModel> cartItemModelList,TextView cartTotalAmount,boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount=cartTotalAmount;
        this.showDeleteBtn=showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {

        switch (cartItemModelList.get(position).getType()){
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case CartItemModel.CART_ITEM:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new cartItemViewHolder(view);
            case CartItemModel.TOTAL_AMOUNT:
                View carttotalview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new cartTotalAmountViewHolder(carttotalview);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String productID=cartItemModelList.get(position).getProductID();
                String resource=cartItemModelList.get(position).getProductImage();
                String title=cartItemModelList.get(position).getProductTitle();
                Long freecoupans=cartItemModelList.get(position).getFreeCoupans();
                String productprice=cartItemModelList.get(position).getProductPrice();
                String cuttedPrice=cartItemModelList.get(position).getCuttedPrice();
                Long offersApplied=cartItemModelList.get(position).getOffersApplied();
                boolean inStock=cartItemModelList.get(position).isInStock();

                ((cartItemViewHolder)holder).setItemDetails(productID,resource,title,freecoupans,productprice,cuttedPrice,offersApplied,position,inStock);
                break;
            case CartItemModel.TOTAL_AMOUNT:

                int totalItems=0,totalAmount,savedAmount=0;
                int totalItemsPrice=0;
                String deliveryPrice;

                for(int x=0;x<cartItemModelList.size();x++){
                    if(cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM  && cartItemModelList.get(x).isInStock()) {
                        totalItems++;
                        totalItemsPrice = totalItemsPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice());
                    }
                }
                if(totalItemsPrice>500){
                    deliveryPrice="FREE";
                    totalAmount=totalItemsPrice;
                }else {
                    deliveryPrice="60";
                    totalAmount=totalItemsPrice+60;
                }
                ((cartTotalAmountViewHolder)holder).setTotalAmount(totalItems,totalItemsPrice,deliveryPrice,totalAmount,savedAmount);
                break;
            default:
                return;
        }
        if(lastpos<position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastpos=position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class cartItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage,freeCoupanIcon;
        private LinearLayout deleteBtn,coupanRedemptionLayout;
        private TextView productTitle, freeCoupans, productPrice,cuttedPrice,offersApplied,coupansApplied,productQuantity;

        public cartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.product_image);
            freeCoupanIcon=itemView.findViewById(R.id.free_coupan_icon);
            productTitle=itemView.findViewById(R.id.product_title);
            freeCoupans=itemView.findViewById(R.id.tv_free_coupan);
            productPrice=itemView.findViewById(R.id.product_price);
            cuttedPrice=itemView.findViewById(R.id.cutted_price);
            offersApplied=itemView.findViewById(R.id.offers_applied);
            coupansApplied=itemView.findViewById(R.id.coupans_applied);
            productQuantity=itemView.findViewById(R.id.product_quantity);
            deleteBtn=itemView.findViewById(R.id.remove_item_btn);
            coupanRedemptionLayout=itemView.findViewById(R.id.coupan_redemption_layout);
            }

        private void setItemDetails(String productID, String resource, String title, Long freecoupansNo, String productPriceText, String cuttedPriceText, Long offersAppliedNo, final int position,boolean inStock){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.pic)).into(productImage);
            productTitle.setText(title);

            if(inStock) {
                if(freecoupansNo>0){
                    freeCoupans.setVisibility(View.VISIBLE);
                    freeCoupanIcon.setVisibility(View.VISIBLE);
                    if(freecoupansNo==1){
                        freeCoupans.setText("free " + freecoupansNo + " Coupan");
                    }else{
                        freeCoupans.setText("free " + freecoupansNo + " Coupans");
                    }
                }else {
                    freeCoupans.setVisibility(View.INVISIBLE);
                    freeCoupanIcon.setVisibility(View.INVISIBLE);
                }
                productPrice.setText("Rs." + productPriceText + "/-");
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setText("Rs." + cuttedPriceText + "/-");
                coupanRedemptionLayout.setVisibility(View.VISIBLE);
                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog qtyDialog=new Dialog(itemView.getContext());
                        qtyDialog.setContentView(R.layout.quantity_dialog);
                        qtyDialog.setCancelable(false);

                        qtyDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                        final EditText qtyNo=qtyDialog.findViewById(R.id.quantity_no);
                        Button cancelBtn=qtyDialog.findViewById(R.id.cancel_btn);
                        Button okBtn=qtyDialog.findViewById(R.id.ok_btn);

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                qtyDialog.dismiss();
                            }
                        });

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                productQuantity.setText("Qty: "+qtyNo.getText());
                                qtyDialog.dismiss();
                            }
                        });
                        qtyDialog.show();
                    }
                });
                if(offersAppliedNo>0){
                    offersApplied.setText(offersAppliedNo + " offers applied");
                    offersApplied.setVisibility(View.VISIBLE);
                }else {
                    offersApplied.setVisibility(View.INVISIBLE);
                }
            }else {
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                cuttedPrice.setText("");
                coupanRedemptionLayout.setVisibility(View.GONE);
                productQuantity.setVisibility(View.INVISIBLE);
                freeCoupans.setVisibility(View.INVISIBLE);
                freeCoupanIcon.setVisibility(View.INVISIBLE);
                coupansApplied.setVisibility(View.GONE);
                offersApplied.setVisibility(View.GONE);
            }


            if(showDeleteBtn){
                deleteBtn.setVisibility(View.VISIBLE);
            }else {
                deleteBtn.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!ProductDetailsActivity.running_cart_querry){
                        ProductDetailsActivity.running_cart_querry=true;
                        DBquerries.removeFromCart(position,itemView.getContext(),cartTotalAmount);
                    }
                }
            });
        }
    }

    class cartTotalAmountViewHolder extends RecyclerView.ViewHolder{

        private TextView totalItems,totalItemsPrice,deliveryPrice,totalAmount,savedAmount;

        public cartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            totalItems=itemView.findViewById(R.id.total_items);
            totalItemsPrice=itemView.findViewById(R.id.total_items_price);
            deliveryPrice=itemView.findViewById(R.id.delivery_price);
            totalAmount=itemView.findViewById(R.id.total_price);
            savedAmount=itemView.findViewById(R.id.saved_amount);
        }
        private void setTotalAmount( int totalItemText, int totalItemPriceText,String deliveryPricetext,int totalAmounttext,int savedAmounttext){
            totalItems.setText("Price("+totalItemText+" items");
            totalItemsPrice.setText("Rs."+totalItemPriceText+"/-");
            if(deliveryPricetext.equals("FREE")){
                deliveryPrice.setText(deliveryPricetext);
            }else {
                deliveryPrice.setText("Rs."+deliveryPricetext+"/-");
            }
            totalAmount.setText("Rs."+totalAmounttext+"/-");
            cartTotalAmount.setText("Rs."+totalAmounttext+"/-");
            savedAmount.setText("You saved Rs."+savedAmounttext+"/- on this order.");

            LinearLayout parent=(LinearLayout)cartTotalAmount.getParent().getParent();
            if(totalItemPriceText == 0){
                DBquerries.cartItemModelList.remove(DBquerries.cartItemModelList.size()-1);
                parent.setVisibility(View.GONE);
            }else {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }
}
