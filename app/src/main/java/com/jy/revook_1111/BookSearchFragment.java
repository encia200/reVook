package com.jy.revook_1111;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jy.revook_1111.Card_BookSearch;
import com.jy.revook_1111.R;

import java.util.ArrayList;

/**
 * Created by remna on 2017-12-01.
 */

public class BookSearchFragment extends Fragment{

    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_search, container, false);

        listView = (ListView) view.findViewById(R.id.book_search_list);
        ArrayList<Card_BookSearch> list = new ArrayList<>();
        makeBookSearchList(list);
        CustomListAdapter adapter = new CustomListAdapter(getActivity(), R.layout.card_layout_book_search, list);
        listView.setAdapter(adapter);

        return view;
    }

    private void makeBookSearchList(ArrayList<Card_BookSearch> list) {
        String bookTitle;
        int j;
        for (int i = 0; i < APISearchNaverBook.bookInfoList.size(); i++) {
            j=0;
            bookTitle = APISearchNaverBook.bookInfoList.get(i).title;
            while(j < bookTitle.length())
            {
                if(bookTitle.charAt(j) == '(')
                    break;
                j++;
            }
            if(j != 0)
            {
                System.out.println("~~~~~~~~~~~~~"+j);
                bookTitle = bookTitle.substring(0, j);
            }

            list.add(new Card_BookSearch(bookTitle, APISearchNaverBook.bookInfoList.get(i).imageURL));
        }
    }

}
