package com.ashish.mymall;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyMallAdapter extends RecyclerView.Adapter {

    private List<MyMallModel> myMallModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private int lastPos=-1;

    public MyMallAdapter(List<MyMallModel> myMallModelList) {
        this.myMallModelList = myMallModelList;
        recycledViewPool=new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {

        switch (myMallModelList.get(position).getType()){
            case 0:
                return MyMallModel.BANNER_SLIDER;
            case 1:
                return MyMallModel.STRIP_AD_BANNER;
            case 2:
                return MyMallModel.HORIZONTAL_PRODUCT_VIEW;
            case 3:
                return MyMallModel.GRID_PRODUCT_VIEW;
            default:
                return -1;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case MyMallModel.BANNER_SLIDER:
                View BannerSliderview= LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_layout,parent,false);
                return new BannerSliderViewHolder(BannerSliderview);
            case MyMallModel.STRIP_AD_BANNER:
                View StripAdview= LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout,parent,false);
                return new StripAdBannerViewHolder(StripAdview);
            case MyMallModel.HORIZONTAL_PRODUCT_VIEW:
                View horizontalproductview= LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scoll_layout,parent,false);
                return new HorizontalProductViewHolder(horizontalproductview);
            case MyMallModel.GRID_PRODUCT_VIEW:
                View gridproductview= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout,parent,false);
                return new GridProductViewHolder(gridproductview);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (myMallModelList.get(position).getType()){
            case MyMallModel.BANNER_SLIDER:
                List<SliderModel> sliderModelList=myMallModelList.get(position).getSliderModelList();
                ((BannerSliderViewHolder) holder).setBannerSliderViewPager(sliderModelList);
                break;
            case MyMallModel.STRIP_AD_BANNER:
                String resource=myMallModelList.get(position).getResource();
                String color=myMallModelList.get(position).getBackgroundColor();
                ((StripAdBannerViewHolder)holder).StripAd(resource,color);
                break;
            case MyMallModel.HORIZONTAL_PRODUCT_VIEW:
                String title=myMallModelList.get(position).getTitle();
                String colorr=myMallModelList.get(position).getBackgroundColor();
                List<HorizontalProductScrollModel> horizontalProductScrollModelList=myMallModelList.get(position).getHorizontalProductScrollModelList();
                List<WishlistModel> viewAllProductList=myMallModelList.get(position).getViewAllProductList();
                ((HorizontalProductViewHolder) holder).setHorizontalProductLayout(horizontalProductScrollModelList,title,colorr,viewAllProductList);
                break;
            case MyMallModel.GRID_PRODUCT_VIEW:
                String gridcolorr=myMallModelList.get(position).getBackgroundColor();
                String gridtitle=myMallModelList.get(position).getTitle();
                List<HorizontalProductScrollModel> gridProductScrollModelList=myMallModelList.get(position).getHorizontalProductScrollModelList();
                ((GridProductViewHolder) holder).setGridProductLayout(gridProductScrollModelList,gridtitle,gridcolorr);
                break;
            default:
                return;
        }
        if(lastPos<position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPos=position;
        }
    }

    @Override
    public int getItemCount() {
        return myMallModelList.size();
    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {

        private ViewPager bannerSliderViewPager;
        private int currentPage;
        private Timer timer;
        final private long DELAY_TIME=3000;
        final private long PERIOD_TIME=3000;
        private List<SliderModel> arrangedList;


        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);

            bannerSliderViewPager=itemView.findViewById(R.id.banner_slider_view_pager);

        }

        public void setBannerSliderViewPager(final List<SliderModel> sliderModelList){
            currentPage=2;
            if(timer!=null){
                timer.cancel();
            }
            arrangedList=new ArrayList<>();
            for(int x=0;x<sliderModelList.size();x++){
                arrangedList.add(x,sliderModelList.get(x));
            }
            arrangedList.add(0,sliderModelList.get(sliderModelList.size()-2));
            arrangedList.add(1,sliderModelList.get(sliderModelList.size()-1));
            arrangedList.add(sliderModelList.get(0));
            arrangedList.add(sliderModelList.get(1));

            SliderAdapter sliderAdapter=new SliderAdapter(arrangedList);
            bannerSliderViewPager.setAdapter(sliderAdapter);

            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);

            bannerSliderViewPager.setCurrentItem(currentPage);

            ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage=position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if(state==ViewPager.SCROLL_STATE_IDLE){
                        pageLooper(arrangedList);
                    }
                }
            };
            bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);

            startBannerSlideShow(arrangedList);

            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    pageLooper(arrangedList);
                    stopBannerSlideShow();

                    if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                        startBannerSlideShow(arrangedList);
                    }
                    return false;
                }
            });

        }

        private void pageLooper(List<SliderModel> sliderModelList){
            if(currentPage==sliderModelList.size()-2){
                currentPage=2;
                bannerSliderViewPager.setCurrentItem(currentPage,false);
            }

            if(currentPage==1){
                currentPage=sliderModelList.size()-3;
                bannerSliderViewPager.setCurrentItem(currentPage,false);
            }
        }

        private void startBannerSlideShow(final List<SliderModel> sliderModelList){
            final Handler handler=new Handler();
            final Runnable update=new Runnable() {
                @Override
                public void run() {
                    if(currentPage>=sliderModelList.size()){
                        currentPage=1;
                    }
                    bannerSliderViewPager.setCurrentItem(currentPage++,true);
                }
            };

            timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            },DELAY_TIME,PERIOD_TIME);
        }

        private void stopBannerSlideShow(){
            timer.cancel();
        }

    }

    public class StripAdBannerViewHolder extends RecyclerView.ViewHolder{

        private ImageView stripAdImage;
        private ConstraintLayout stripAdContainer;

        public StripAdBannerViewHolder(@NonNull View itemView) {
            super(itemView);

            stripAdImage=itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer=itemView.findViewById(R.id.strip_ad_container);

        }

        public void StripAd(String resource,String color){

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.pic)).into(stripAdImage);

            stripAdContainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    public class HorizontalProductViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout container;
        private TextView horizontallayoutTitle;
        private Button horizontalviewAllBtn;
        private RecyclerView horizontalRecyclerView;

        public HorizontalProductViewHolder(@NonNull View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.container);
            horizontalRecyclerView=itemView.findViewById(R.id.horizontal_scroll_layout_recycler_view);
            horizontalviewAllBtn=itemView.findViewById(R.id.horizontal_scroll_layout_viewall_button);
            horizontallayoutTitle=itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalRecyclerView.setRecycledViewPool(recycledViewPool);
        }

        private void setHorizontalProductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String color, final List<WishlistModel> viewAllProductList){
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontallayoutTitle.setText(title);

            if(horizontalProductScrollModelList.size() >8){
                horizontalviewAllBtn.setVisibility(View.VISIBLE);
                horizontalviewAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewAllActivity.wishlistModelList=viewAllProductList;
                        itemView.getContext().startActivity(new Intent(itemView.getContext(),ViewAllActivity.class)
                                .putExtra("layout",0)
                                .putExtra("title",title)
                        );
                    }
                });
            }else {
                horizontalviewAllBtn.setVisibility(View.INVISIBLE);
            }

            HorizontalProductScrollAdapter horizontalProductScrollAdapter=new HorizontalProductScrollAdapter(horizontalProductScrollModelList);

            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalRecyclerView.setLayoutManager(linearLayoutManager1);

            horizontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();

        }
    }

    public class GridProductViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout container;
        private TextView gridLayoutTitle;
        private Button gridLayoutButton;
        private GridLayout gridProductLayout;

        public GridProductViewHolder(@NonNull final View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.container);
            gridLayoutTitle=itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoutButton=itemView.findViewById(R.id.grid_product_layout_viewall_button);
            gridProductLayout=itemView.findViewById(R.id.grid_layout);

        }
        private void setGridProductLayout(final List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String color) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);

            for(int x=0;x<4;x++){
                ImageView productImage=gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_image);
                TextView productTitle=gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_title);
                TextView productDesc=gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_description);
                TextView productPrice=gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_price);

                Glide.with(itemView.getContext()).load(horizontalProductScrollModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.mipmap.pic)).into(productImage);
                productTitle.setText(horizontalProductScrollModelList.get(x).getProductTitle());
                productDesc.setText(horizontalProductScrollModelList.get(x).getProductDesc());
                productPrice.setText("Rs."+horizontalProductScrollModelList.get(x).getProductPrice()+"/-");

                gridProductLayout.getChildAt(x).setBackgroundColor(Color.WHITE);
                if(!title.equals("")){
                    final int finalX = x;
                    gridProductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            itemView.getContext().startActivity(new Intent(itemView.getContext(),ProductDetailsActivity.class).putExtra("PRODUCT_ID",horizontalProductScrollModelList.get(finalX).getProductID()));
                        }
                    });
                }
            }

            if(!title.equals("")) {
                gridLayoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewAllActivity.horizontalProductScrollModelList = horizontalProductScrollModelList;
                        itemView.getContext().startActivity(new Intent(itemView.getContext(), ViewAllActivity.class)
                                .putExtra("layout", 1)
                                .putExtra("title", title)
                        );
                    }
                });
            }
        }

    }
}
