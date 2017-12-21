package com.jy.revook_1111.Fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jy.revook_1111.APISearchNaverBook;
import com.jy.revook_1111.ApplicationController;
import com.jy.revook_1111.R;
import com.jy.revook_1111.chat.MessageActivity;
import com.jy.revook_1111.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class FollowerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                /* 검색 중 BACK버튼 누르면 */
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    UserInfoFragment.buttonLogout.setVisibility(View.VISIBLE);
                    /*Fragment스택에 쌓여 있던 것들을 비워준다.*/
                    UserInfoFragment.fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    return true;
                } else {
                    return false;
                }
            }
        });


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.peoeplefragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());

        return view;
    }

    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<UserModel> userModels;

        public PeopleFragmentRecyclerViewAdapter() {
            userModels = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModels.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (int i = 0; i < ApplicationController.getFollowersSize(); i++) {
                            if (snapshot.getKey().equals(ApplicationController.currentUser_followers.get(i))) {
                                userModels.add(snapshot.getValue(UserModel.class));
                            }
                        }
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            Glide.with
                    (holder.itemView.getContext())
                    .load(userModels.get(position).profileImageUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(((CustomViewHolder) holder).imageView);
            ((CustomViewHolder) holder).textView.setText(userModels.get(position).userName);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid", userModels.get(position).uid);
                    Log.e("message" , "destinationUID ---> " + userModels.get(position).uid);
                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright, R.anim.toleft);
                    startActivity(intent, activityOptions.toBundle());
                }
            });

        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;

            public CustomViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.frienditem_imageview);
                textView = (TextView) view.findViewById(R.id.frienditem_textview);
            }
        }
    }

}
