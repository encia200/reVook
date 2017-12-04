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
import com.jy.revook_1111.R;
import com.jy.revook_1111.Data.ReviewDTO;

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
    private List<String> uidLists = new ArrayList<>();

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
                uidLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReviewDTO reviewDTO = snapshot.getValue(ReviewDTO.class);
                    String uidKey = snapshot.getKey();
                    reviewDTOs.add(reviewDTO);
                    uidLists.add(uidKey);
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
            return new CustonViewHolder(view);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            Glide.with(parent.getContext()).load(reviewDTOs.get(position).imageUrl).into(((CustonViewHolder) holder).imageView);
            ((CustonViewHolder) holder).title.setText(reviewDTOs.get(position).title);
            ((CustonViewHolder) holder).content.setText(reviewDTOs.get(position).content);
            ((CustonViewHolder) holder).userId.setText(reviewDTOs.get(position).userId);
            ((CustonViewHolder) holder).starButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStarClicked(database.getReference().child("reviews").child(uidLists.get(position)));
                }
            });
            ((CustonViewHolder) holder).starCount.setText(Integer.toString(reviewDTOs.get(position).starCount));

            // 불러온 리뷰의 좋아요 uid리스트에 내 uid 있는지 확인
            if (reviewDTOs.get(position).stars.containsKey(auth.getCurrentUser().getUid())) {
                ((CustonViewHolder)holder).starButton.setImageResource(R.drawable.ic_favorite_black_24dp);
            }else{
                ((CustonViewHolder)holder).starButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
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

        private class CustonViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView title;
            TextView content;
            TextView userId;
            ImageView starButton;
            TextView starCount;


            CustonViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.cardview_imageview);
                title = (TextView) view.findViewById(R.id.cardview_title);
                content = (TextView) view.findViewById(R.id.cardview_content);
                userId = (TextView) view.findViewById(R.id.cardview_userId);
                starButton = (ImageView) view.findViewById(R.id.cardview_starButton_img);
                starCount = (TextView) view.findViewById(R.id.cardview_starcount);
            }
        }
    }

}
