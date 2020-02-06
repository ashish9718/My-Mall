package com.ashish.mymall;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List <CategoryModel> categoryModelList;
    private int lastPos=-1;

    public CategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }


    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        String icon=categoryModelList.get(position).getCategoryIconLink();
        String name=categoryModelList.get(position).getCategoryName();
        holder.setCategory(name,position);
        holder.setCategoryIcon(icon);

        if(lastPos<position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPos=position;
        }
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView categoryIcon;
        private TextView categoryName;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            categoryIcon=itemView.findViewById(R.id.category_icon);
            categoryName=itemView.findViewById(R.id.category_name);
        }

        private void setCategoryIcon(String iconUrl){
            if(!iconUrl.equals("null")) {
                Glide.with(itemView.getContext()).load(iconUrl).apply(new RequestOptions().placeholder(R.mipmap.pic)).into(categoryIcon);
            }else {
                categoryIcon.setImageResource(R.mipmap.homeicon);
            }
        }

        private void setCategory(final String name,final int position){
            categoryName.setText(name);

            if(!name.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position != 0) {
                            Intent categoryIntent = new Intent(itemView.getContext(), CategoryActivity.class);
                            categoryIntent.putExtra("CategoryName", name);
                            itemView.getContext().startActivity(categoryIntent);
                        }
                    }
                });
            }
        }
    }
}
