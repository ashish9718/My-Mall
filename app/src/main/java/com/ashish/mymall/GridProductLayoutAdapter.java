package com.ashish.mymall;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    List<HorizontalProductScrollModel> horizontalProductScrollModelList;



    @Override
    public int getCount() {
        return horizontalProductScrollModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertview, final ViewGroup viewGroup) {

        View view;
        if(convertview==null){
            view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item_layout, null );
            view.setElevation(0);
            view.setBackgroundColor(Color.WHITE);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewGroup.getContext().startActivity(new Intent(viewGroup.getContext(),ProductDetailsActivity.class).putExtra("PRODUCT_ID",horizontalProductScrollModelList.get(position).getProductID()));
                }
            });

            ImageView productImage=view.findViewById(R.id.h_s_product_image);
            TextView productTitle=view.findViewById(R.id.h_s_product_title);
            TextView productDesc=view.findViewById(R.id.h_s_product_description);
            TextView productPrice=view.findViewById(R.id.h_s_product_price);

            Glide.with(viewGroup.getContext()).load(horizontalProductScrollModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.mipmap.pic)).into(productImage);
            productTitle.setText(horizontalProductScrollModelList.get(position).getProductTitle());
            productDesc.setText(horizontalProductScrollModelList.get(position).getProductDesc());
            productPrice.setText("Rs."+horizontalProductScrollModelList.get(position).getProductPrice()+"/-");


        }else {
            view=convertview;
        }
        return view;
    }
}
