package com.ashish.mymall;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {

    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;


    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder holder, int position) {

        String resource=horizontalProductScrollModelList.get(position).getProductImage();
        String title=horizontalProductScrollModelList.get(position).getProductTitle();
        String desc=horizontalProductScrollModelList.get(position).getProductDesc();
        String price=horizontalProductScrollModelList.get(position).getProductPrice();
        String productId=horizontalProductScrollModelList.get(position).getProductID();
        holder.setProduct(productId,resource,title,desc,price);


    }

    @Override
    public int getItemCount() {
        if (horizontalProductScrollModelList.size()>8){
            return 8;
        }else {
            return horizontalProductScrollModelList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle,productDesc,productPrice;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            productImage=itemView.findViewById(R.id.h_s_product_image);
            productTitle=itemView.findViewById(R.id.h_s_product_title);
            productDesc=itemView.findViewById(R.id.h_s_product_description);
            productPrice=itemView.findViewById(R.id.h_s_product_price);

        }

        private void setProduct(final String productId, String resource, String title, String desc, String price) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.pic)).into(productImage);
            productTitle.setText(title);
            productDesc.setText(desc);
            productPrice.setText("Rs."+price+"/-");

            if(!title.equals("")){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemView.getContext().startActivity(new Intent(itemView.getContext(),ProductDetailsActivity.class).putExtra("PRODUCT_ID",productId));
                    }
                });
            }
        }

    }
}
