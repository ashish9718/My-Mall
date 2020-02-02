package com.ashish.mymall.ui.my_rewards;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.mymall.MyRewardsAdapter;
import com.ashish.mymall.R;
import com.ashish.mymall.RewardModel;

import java.util.ArrayList;
import java.util.List;

public class MyRewardsFragment extends Fragment {
    public MyRewardsFragment() {
    }

    private RecyclerView rewardsRecyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_rewards, container, false);
        rewardsRecyclerView=root.findViewById(R.id.my_rewards_recyclerview);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rewardsRecyclerView.setLayoutManager(linearLayoutManager);

        List<RewardModel> rewardModelList=new ArrayList<>();
        rewardModelList.add(new RewardModel("Cashback","till 26st jan, 2020","Get 20% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","till 27st jan, 2020","Get 30% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 28st jan, 2020","Get 40% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","till 29st jan, 2020","Get 50% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 30st jan, 2020","Get 10% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 26st jan, 2020","Get 20% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","till 27st jan, 2020","Get 30% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 28st jan, 2020","Get 40% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","till 29st jan, 2020","Get 50% discount on the purchase of product between rs500/- and Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","till 30st jan, 2020","Get 10% discount on the purchase of product between rs500/- and Rs.3000/-"));

        MyRewardsAdapter rewardsAdapter=new MyRewardsAdapter(rewardModelList,false);
        rewardsAdapter.notifyDataSetChanged();
        rewardsRecyclerView.setAdapter(rewardsAdapter);
        return root;
    }
}