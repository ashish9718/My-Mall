package com.ashish.mymall;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<WishlistModel> wishlistModelList;
    private Boolean wishlist;

    public WishlistAdapter(List<WishlistModel> wishlistModelList,Boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist=wishlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title=wishlistModelList.get(position).getProductTitle();
        int resource=wishlistModelList.get(position).getProductImage();
        int freeCoupans=wishlistModelList.get(position).getFreeCoupans();
        String rating=wishlistModelList.get(position).getRating();
        int totalRatings=wishlistModelList.get(position).getTotalRatings();
        String productPrice=wishlistModelList.get(position).getProductPrice();
        String cuttedPrice=wishlistModelList.get(position).getCuttedPrice();
        String paymentMethod=wishlistModelList.get(position).getPaymentMethod();
        holder.setData(resource,title,freeCoupans,rating,totalRatings,productPrice,cuttedPrice,paymentMethod);

    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage,coupanIcon;
        private TextView productTitle,freeCoupans,rating,totalRatings,productPrice,cuttedPrice,paymentMethod;
        private View priceCut;
        private ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.product_image);
            productTitle=itemView.findViewById(R.id.product_title);
            freeCoupans=itemView.findViewById(R.id.free_coupan);
            coupanIcon=itemView.findViewById(R.id.coupan_icon);
            rating=itemView.findViewById(R.id.tv_product_rating_miniview);
            totalRatings=itemView.findViewById(R.id.total_ratings);
            priceCut=itemView.findViewById(R.id.price_cut);
            productPrice=itemView.findViewById(R.id.product_price);
            cuttedPrice=itemView.findViewById(R.id.cutted_price);
            paymentMethod=itemView.findViewById(R.id.payment_method);
            deleteBtn=itemView.findViewById(R.id.delete_btn);
        }

        private void setData(int resource, String title, int freeCoupansNo, String averageRate, int totalRatingsNo, String price, String cuttedPriceValue, String payMethod){
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if(freeCoupansNo!=0){
                coupanIcon.setVisibility(View.VISIBLE);
                if(freeCoupansNo==1){
                    freeCoupans.setText("free "+freeCoupansNo+" coupan");
                }else {
                    freeCoupans.setText("free "+freeCoupansNo+" coupans");
                }
            }else {
                coupanIcon.setVisibility(View.INVISIBLE);
                freeCoupans.setVisibility(View.INVISIBLE);
            }
            rating.setText(averageRate);
            totalRatings.setText(totalRatingsNo+" (ratings)");
            productPrice.setText(price);
            cuttedPrice.setText(cuttedPriceValue);
            paymentMethod.setText(payMethod);

            if(wishlist){
                deleteBtn.setVisibility(View.VISIBLE);
            }else {
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(),"Delete",Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(),ProductDetailsActivity.class));
                }
            });
        }
    }
}
