package com.ashish.mymall;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<WishlistModel> wishlistModelList;
    private Boolean wishlist,fromSearch=false;
    private int lastpos=-1;

    public List<WishlistModel> getWishlistModelList() {
        return wishlistModelList;
    }

    public void setWishlistModelList(List<WishlistModel> wishlistModelList) {
        this.wishlistModelList = wishlistModelList;
    }

    public Boolean getFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(Boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public WishlistAdapter(List<WishlistModel> wishlistModelList, Boolean wishlist) {
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
        String productId=wishlistModelList.get(position).getProductId();
        String title=wishlistModelList.get(position).getProductTitle();
        String resource=wishlistModelList.get(position).getProductImage();
        long freeCoupans=wishlistModelList.get(position).getFreeCoupans();
        String rating=wishlistModelList.get(position).getRating();
        long totalRatings=wishlistModelList.get(position).getTotalRatings();
        String productPrice=wishlistModelList.get(position).getProductPrice();
        String cuttedPrice=wishlistModelList.get(position).getCuttedPrice();
        Boolean paymentMethod=wishlistModelList.get(position).getCOD();
        boolean inStock=wishlistModelList.get(position).isInStock();
        holder.setData(productId,resource,title,freeCoupans,rating,totalRatings,productPrice,cuttedPrice,paymentMethod,position,inStock);
        if(lastpos<position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastpos=position;
        }
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

        private void setData(final String productId, String resource, String title, long freeCoupansNo, String averageRate, long totalRatingsNo, String price, String cuttedPriceValue, Boolean payMethod, final int index,boolean inStock){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.pic)).into(productImage);
            productTitle.setText(title);
            if(freeCoupansNo!=0 && inStock){
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
            LinearLayout linearLayout= (LinearLayout) rating.getParent();
            if(inStock) {
                rating.setVisibility(View.VISIBLE);
                totalRatings.setVisibility(View.VISIBLE);
                cuttedPrice.setVisibility(View.VISIBLE);
                productPrice.setTextColor(Color.parseColor("#000000"));

                rating.setText(averageRate);
                totalRatings.setText("(" + totalRatingsNo + ")ratings");
                productPrice.setText("Rs." + price + "/-");
                cuttedPrice.setText("Rs." + cuttedPriceValue + "/-");

                if (payMethod) {
                    paymentMethod.setVisibility(View.VISIBLE);
                } else {
                    paymentMethod.setVisibility(View.INVISIBLE);
                }
                linearLayout.setVisibility(View.VISIBLE);
            }else {
                linearLayout.setVisibility(View.INVISIBLE);
                paymentMethod.setVisibility(View.INVISIBLE);
                rating.setVisibility(View.INVISIBLE);
                totalRatings.setVisibility(View.INVISIBLE);
                cuttedPrice.setVisibility(View.INVISIBLE);
                productPrice.setText("Out Of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
            }

            if(wishlist){
                deleteBtn.setVisibility(View.VISIBLE);
            }else {
                deleteBtn.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!ProductDetailsActivity.running_wishlist_querry) {
                        ProductDetailsActivity.running_wishlist_querry = true;
                        DBquerries.removeFromWishlist(index, itemView.getContext());
                        Toast.makeText(itemView.getContext(),"Deleted Successfully!",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(fromSearch){
                        ProductDetailsActivity.fromSearch=true;
                    }
                    itemView.getContext().startActivity(new Intent(itemView.getContext(),ProductDetailsActivity.class).putExtra("PRODUCT_ID",productId));
                }
            });
        }
    }
}
