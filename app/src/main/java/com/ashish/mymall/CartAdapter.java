package com.ashish.mymall;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;

    public CartAdapter(List<CartItemModel> cartItemModelList) {
        this.cartItemModelList = cartItemModelList;
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
                int resource=cartItemModelList.get(position).getProductImage();
                String title=cartItemModelList.get(position).getProductTitle();
                int freecoupans=cartItemModelList.get(position).getFreeCoupans();
                String productprice=cartItemModelList.get(position).getProductPrice();
                String cuttedPrice=cartItemModelList.get(position).getCuttedPrice();
                int offersApplied=cartItemModelList.get(position).getOffersApplied();

                ((cartItemViewHolder)holder).setItemDetails(resource,title,freecoupans,productprice,cuttedPrice,offersApplied);
                break;
            case CartItemModel.TOTAL_AMOUNT:

                String totalItems=cartItemModelList.get(position).getTotalItems();
                String totalItemsPrice=cartItemModelList.get(position).getTotalItemPrice();
                String deliveryPrice=cartItemModelList.get(position).getDeliveryPrice();
                String savedAmount=cartItemModelList.get(position).getSavedAmount();
                String totalAmount=cartItemModelList.get(position).getTotalAmount();

                ((cartTotalAmountViewHolder)holder).setTotalAmount(totalItems,totalItemsPrice,deliveryPrice,totalAmount,savedAmount);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class cartItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage,freeCoupanIcon;
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
        }

        private void setItemDetails(int resource, String title, int freecoupansNo, String productPriceText, String cuttedPriceText,int offersAppliedNo){
            productImage.setImageResource(resource);
            productTitle.setText(title);
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
            productPrice.setText(productPriceText);
            cuttedPrice.setText(cuttedPriceText);

            if(offersAppliedNo>0){
                offersApplied.setText(offersAppliedNo + " offers applied");
                offersApplied.setVisibility(View.VISIBLE);
            }else {
                offersApplied.setVisibility(View.INVISIBLE);
            }


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
        private void setTotalAmount( String totalItemText, String totalItemPriceText,String deliveryPricetext,String totalAmounttext,String savedAmounttext){
            totalItems.setText(totalItemText);
            totalItemsPrice.setText(totalItemPriceText);
            deliveryPrice.setText(deliveryPricetext);
            totalAmount.setText(totalAmounttext);
            savedAmount.setText(savedAmounttext);
        }
    }
}
