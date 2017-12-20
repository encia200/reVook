package com.jy.revook_1111.Fragment;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.jy.revook_1111.ApplicationController;
import com.jy.revook_1111.Data.ReviewDTO;
import com.jy.revook_1111.R;
import com.jy.revook_1111.model.UserModel;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {
    RecyclerView recyclerView;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private List<ReviewDTO> reviewDTOs = new ArrayList<>();
    private List<String> reviewUidLists = new ArrayList<>();
    private List<UserModel> userModels = new ArrayList<>();
    private List<String> userUidLists = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_review, container, false);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        //카드 리스트뷰 어댑터에 연결
        final ReviewRecyclerViewAdapter adapter = new ReviewRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // db 불러오기
        database.getReference().child("reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviewDTOs.clear();
                reviewUidLists.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReviewDTO reviewDTO = snapshot.getValue(ReviewDTO.class);
                    String reviewUidKey = snapshot.getKey();
                    reviewDTOs.add(reviewDTO);
                    reviewUidLists.add(reviewUidKey);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    String userUidKey = snapshot.getKey();
                    userModels.add(userModel);
                    userUidLists.add(userUidKey);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }

    public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ViewGroup parent;

        ReviewRecyclerViewAdapter() {
        /*reviewDTOs.add(new ReviewDTO(R.drawable.cardview_default, "나미야 잡화점의 기적", "히가시노 게이고"));
        reviewDTOs.add(new ReviewDTO(R.drawable.cardview_default2, "미움받을 용기", "기시미 이치로"));
        reviewDTOs.add(new ReviewDTO(R.drawable.cardview_default3, "신경끄기의 기술", "마크 맨슨"));*/
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //XML세팅
            this.parent = parent;
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item, parent, false);
            return new CustomViewHolder(view);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final UserModel[] userModel = new UserModel[1];
            database.getReference().child("users").child(reviewDTOs.get(position).uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModel[0] = dataSnapshot.getValue(UserModel.class);
                    if (userModel[0].profileImageUrl != null)
                        Glide.with(parent.getContext()).load(userModel[0].profileImageUrl).into(((CustomViewHolder) holder).profileImage);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            if (reviewDTOs.get(position).imageUrl != null)
                Glide.with(parent.getContext()).load(reviewDTOs.get(position).imageUrl).into(((CustomViewHolder) holder).imageView);
            // ((CustomViewHolder) holder).title.setText(reviewDTOs.get(position).title);
            // ((CustomViewHolder) holder).content.setText(reviewDTOs.get(position).content);
            ((CustomViewHolder) holder).userName.setText(reviewDTOs.get(position).userName);
            ((CustomViewHolder) holder).starButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStarClicked(database.getReference().child("reviews").child(reviewUidLists.get(position)));
                }
            });
            ((CustomViewHolder) holder).starCount.setText(Integer.toString(reviewDTOs.get(position).starCount));
            ((CustomViewHolder) holder).follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFollowClicked(database.getReference().child("users").child(reviewDTOs.get(position).uid), database.getReference().child("users").child(auth.getCurrentUser().getUid()));
                }
            });
            if(reviewDTOs.get(position).uid.equals(ApplicationController.currentUser.uid)){
                ((CustomViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.getReference().child("users").child(ApplicationController.currentUser.uid).child("reviewCount").setValue(ApplicationController.currentUser.reviewCount = ApplicationController.currentUser.reviewCount - 1);
                        database.getReference().child("users").child(auth.getCurrentUser().getUid()).child("reviews").child(reviewUidLists.get(position)).removeValue();
                        database.getReference().child("reviews").child(reviewUidLists.get(position)).removeValue();
                    }
                });
            }else {
                ((CustomViewHolder) holder).delete.setVisibility(View.GONE);
            }


            // 불러온 리뷰의 좋아요 uid리스트에 내 uid 있는지 확인
            if (reviewDTOs.get(position).stars.containsKey(auth.getCurrentUser().getUid())) {
                ((CustomViewHolder) holder).starButton.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                ((CustomViewHolder) holder).starButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }

            if (ApplicationController.currentUser.followings.containsKey(reviewDTOs.get(position).uid)) {
                ((CustomViewHolder) holder).follow.setText("팔로우 취소");
            } else {
                ((CustomViewHolder) holder).follow.setText("팔로우");
            }
        }

        @Override
        public int getItemCount() {
            //이미지 카운터
            return reviewDTOs.size();
        }

        private void onStarClicked(DatabaseReference postRef) {
            postRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    ReviewDTO reviewDTO = mutableData.getValue(ReviewDTO.class);
                    if (reviewDTO == null) {
                        return Transaction.success(mutableData);
                    }

                    if (reviewDTO.stars.containsKey(auth.getCurrentUser().getUid())) {
                        // Unstar the post and remove self from stars
                        reviewDTO.starCount = reviewDTO.starCount - 1;
                        reviewDTO.stars.remove(auth.getCurrentUser().getUid());
                    } else {
                        // Star the post and add self to stars
                        reviewDTO.starCount = reviewDTO.starCount + 1;
                        reviewDTO.stars.put(auth.getCurrentUser().getUid(), true);
                    }

                    // Set value and report transaction success
                    mutableData.setValue(reviewDTO);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    // Transaction completed
                    //Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                }
            });
        }

        private void onFollowClicked(final DatabaseReference toUser, DatabaseReference fromUser) {
            final UserModel[] toUserModel = new UserModel[1];
            final UserModel[] fromUserModel = new UserModel[1];

            if (!toUser.getKey().equals(fromUser.getKey())) {
                toUser.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        toUserModel[0] = mutableData.getValue(UserModel.class);
                        if (toUserModel[0] == null) {
                            return Transaction.success(mutableData);
                        }
                        if (toUserModel[0].followers.containsKey(auth.getCurrentUser().getUid())) {
                            // Unstar the post and remove self from stars
                            toUserModel[0].followerCount = toUserModel[0].followerCount - 1;
                            toUserModel[0].followers.remove(auth.getCurrentUser().getUid());
                        } else {
                            // Star the post and add self to stars
                            toUserModel[0].followerCount = toUserModel[0].followerCount + 1;
                            toUserModel[0].followers.put(auth.getCurrentUser().getUid(), true);
                        }

                        // Set value and report transaction success
                        mutableData.setValue(toUserModel[0]);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed
                        //Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                    }
                });
                fromUser.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        fromUserModel[0] = mutableData.getValue(UserModel.class);
                        if (fromUserModel[0] == null) {
                            return Transaction.success(mutableData);
                        }
                        if (fromUserModel[0].followings.containsKey(toUser.getKey())) {
                            fromUserModel[0].followingCount = fromUserModel[0].followingCount - 1;
                            fromUserModel[0].followings.remove(toUser.getKey());
                        } else {
                            // Star the post and add self to stars
                            fromUserModel[0].followingCount = fromUserModel[0].followingCount + 1;
                            fromUserModel[0].followings.put(toUser.getKey(), true);
                        }

                        // Set value and report transaction success
                        mutableData.setValue(fromUserModel[0]);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed
                        //Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                    }
                });
            }
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            CircularImageView profileImage;
            ImageView imageView;
            //            TextView title;
            TextView content;
            TextView userName;
            ImageView starButton;
            TextView starCount;
            Button follow;
            Button delete;


            CustomViewHolder(View view) {
                super(view);
                profileImage = (CircularImageView) view.findViewById(R.id.cardview_userprofile);
                imageView = (ImageView) view.findViewById(R.id.cardview_content_imageview);
//                title = (TextView) view.findViewById(R.id.cardview_title);
                content = (TextView) view.findViewById(R.id.cardview_content_edittext);
                userName = (TextView) view.findViewById(R.id.cardview_userName);
                starButton = (ImageView) view.findViewById(R.id.cardview_starButton_img);
                starCount = (TextView) view.findViewById(R.id.cardview_starcount);
                follow = (Button) view.findViewById(R.id.cardview_follow_btn);
                delete = (Button) view.findViewById(R.id.cardview_delete_btn);
            }
        }
    }
}
