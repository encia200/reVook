package com.jy.revook_1111.Fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.jy.revook_1111.APISearchNaverBook;
import com.jy.revook_1111.Adapter.CustomListAdapter;
import com.jy.revook_1111.Card_BookSearch;
import com.jy.revook_1111.R;

import java.util.ArrayList;

/**
 * Created by remna on 2017-12-01.
 */

public class BookSearchFragment extends Fragment {
    private static Context context;
    private static ListView listView;
    private Button button_moreList;
    public static CustomListAdapter adapter;
    public static boolean isSearching = false;
    private static View footer;
    private static ArrayList<Card_BookSearch> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_search, container, false);
        context = getContext();
        listView = (ListView) view.findViewById(R.id.book_search_list);
        footer = getLayoutInflater().inflate(R.layout.listview_footer, null, false);
        listView.addFooterView(footer);
        if (APISearchNaverBook.bookInfoList != null) {
            makeBookSearchList();

            button_moreList = (Button) view.findViewById(R.id.button_footer_moreList);
            button_moreList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new Thread() {
                        public void run() {
                            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^실행됨^^^^^^^^^^^^^^^^");
                            APISearchNaverBook.moreList();
                        }
                    }.start();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            makeBookMoreList();
                            adapter.notifyDataSetChanged();
                        }
                    });

                }
            });
        }
        return view;
    }

    public static void makeBookSearchList() {
        list = new ArrayList<>();
        String bookTitle, bookAuthor, bookPrice;
        int j;
        for (int i = 0; i < APISearchNaverBook.bookInfoList.size(); i++) {
            j = 0;
            bookTitle = APISearchNaverBook.bookInfoList.get(i).title;
            bookAuthor = APISearchNaverBook.bookInfoList.get(i).author;
            bookPrice = "₩" + APISearchNaverBook.bookInfoList.get(i).price;
            while (j < bookTitle.length()) {
                if (bookTitle.charAt(j) == '(')
                    break;
                j++;
            }
            if (j != 0)
                bookTitle = bookTitle.substring(0, j);

            list.add(new Card_BookSearch(bookTitle, bookAuthor, bookPrice, APISearchNaverBook.bookInfoList.get(i).imageURL));
        }
        adapter = new CustomListAdapter(context, R.layout.card_layout_book_search, list);
        listView.setAdapter(adapter);
    }

    public static void makeBookMoreList() {
        String bookTitle, bookAuthor, bookPrice;
        int j;
        for (int i = list.size(); i < APISearchNaverBook.bookInfoList.size(); i++) {
            j = 0;
            bookTitle = APISearchNaverBook.bookInfoList.get(i).title;
            bookAuthor = APISearchNaverBook.bookInfoList.get(i).author;
            bookPrice = "₩" + APISearchNaverBook.bookInfoList.get(i).price;
            while (j < bookTitle.length()) {
                if (bookTitle.charAt(j) == '(')
                    break;
                j++;
            }
            if (j != 0)
                bookTitle = bookTitle.substring(0, j);

            list.add(new Card_BookSearch(bookTitle, bookAuthor, bookPrice, APISearchNaverBook.bookInfoList.get(i).imageURL));
        }
    }
}
