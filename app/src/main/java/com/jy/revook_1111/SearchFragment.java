package com.jy.revook_1111;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class SearchFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        ImageView img_recommand_1 = (ImageView) v.findViewById(R.id.img_recommand_1);
        Glide.with(container.getContext()).load(R.drawable.cardview_default).into(img_recommand_1);
        ImageView img_recommand_2 = (ImageView) v.findViewById(R.id.img_recommand_2);
        Glide.with(container.getContext()).load(R.drawable.cardview_default2).into(img_recommand_2);
        ImageView img_recommand_3 = (ImageView) v.findViewById(R.id.img_recommand_3);
        Glide.with(container.getContext()).load(R.drawable.cardview_default3).into(img_recommand_3);

        final EditText edittext_search = (EditText) v.findViewById(R.id.edittext_search);
        Button btn_search = (Button) v.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),APISearchNaverBook.search(edittext_search.getText().toString()),Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }
}
