package com.jy.revook_1111;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<CardViewItemDTO> cardViewItemDTOs = new ArrayList<>();
    private ViewGroup parent;
    ReviewRecyclerViewAdapter() {
        cardViewItemDTOs.add(new CardViewItemDTO(R.drawable.cardview_default, "나미야 잡화점의 기적", "히가시노 게이고"));
        cardViewItemDTOs.add(new CardViewItemDTO(R.drawable.cardview_default2, "미움받을 용기", "기시미 이치로"));
        cardViewItemDTOs.add(new CardViewItemDTO(R.drawable.cardview_default3, "신경끄기의 기술", "마크 맨슨"));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //XML세팅
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item,parent,false);
        return new RowCell(view);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Glide.with(parent.getContext()).load(cardViewItemDTOs.get(position).imageview).into(((RowCell)holder).imageView);
        ((RowCell)holder).title.setText(cardViewItemDTOs.get(position).title);
        ((RowCell)holder).subtitle.setText(cardViewItemDTOs.get(position).subtitle);
        //아이템 세팅
    }

    @Override
    public int getItemCount() {
        //이미지 카운터
        return cardViewItemDTOs.size();
    }

    private class RowCell extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView subtitle;

        RowCell(View view) {
            super(view);
            imageView = (ImageView)view.findViewById(R.id.cardview_imageview);
            title = (TextView)view.findViewById(R.id.cardview_title);
            subtitle = (TextView)view.findViewById(R.id.cardview_subtitle);
        }
    }
}
