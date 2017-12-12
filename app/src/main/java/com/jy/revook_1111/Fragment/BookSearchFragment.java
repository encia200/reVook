package com.jy.revook_1111.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.jy.revook_1111.APISearchNaverBook;
import com.jy.revook_1111.Activity.CustomDialog;
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

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                /* 검색 중 BACK버튼 누르면 */
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    isSearching = false;
                    APISearchNaverBook.searchWord = null;
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.searchFragment);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.detach(fragment);
                    fragmentTransaction.commit();

                    return true;
                } else {
                    return false;
                }
            }
        });

        context = getContext();
        listView = (ListView) view.findViewById(R.id.book_search_list);
        footer = getLayoutInflater().inflate(R.layout.listview_footer, null, false);
        listView.addFooterView(footer);
        if (APISearchNaverBook.bookInfoList != null) {
            makeBookSearchList();
            button_moreList = (Button) view.findViewById(R.id.button_footer_moreList);
            if (APISearchNaverBook.bookInfoList.size() < 4 || (APISearchNaverBook.tot_items < 4)) {
                button_moreList.setVisibility(View.INVISIBLE);
            } else {
                button_moreList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new Thread() {
                            public void run() {
                                APISearchNaverBook.moreList();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        makeBookMoreList();
                                        if (APISearchNaverBook.start >= APISearchNaverBook.tot_items) {
                                            button_moreList.setVisibility(View.INVISIBLE);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }.start();


                    }
                });
            }
        }

        /* 책을 길게 누르면 책 상세정보 창을 띄운다 */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomDialog cd = new CustomDialog(getActivity(), i, CustomDialog.BOOK_SEARCH_FRAGMENT);
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.setCancelable(true);
                cd.show();
                return true;
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

            list.add(new Card_BookSearch(String.valueOf(i), bookTitle, bookAuthor, bookPrice, APISearchNaverBook.bookInfoList.get(i).imageURL));
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

            list.add(new Card_BookSearch(String.valueOf(i), bookTitle, bookAuthor, bookPrice, APISearchNaverBook.bookInfoList.get(i).imageURL));
        }
    }
}
