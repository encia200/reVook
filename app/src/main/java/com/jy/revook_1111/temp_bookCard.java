package com.jy.revook_1111;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.jy.revook_1111.Adapter.CustomListAdapter;

import java.util.ArrayList;

public class temp_bookCard extends AppCompatActivity {
    private static Context context;
    private static ListView listView;
    private Button button_moreList;
    public static CustomListAdapter adapter;
    public static boolean isSearching = false;
    private static View footer;
    private static ArrayList<Card_BookSearch> list;

    public static Context getContext() {
        return context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        temp_bookCard.context = getApplicationContext();
        setContentView(R.layout.activity_temp_book_card);
        listView = (ListView) findViewById(R.id.book_search_list);
        footer = getLayoutInflater().inflate(R.layout.listview_footer, null, false);
        listView.addFooterView(footer);
        if (APISearchNaverBook.bookInfoList != null) {
            makeBookSearchList();

            button_moreList = (Button) findViewById(R.id.button_footer_moreList);
            button_moreList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new Thread() {
                        public void run() {
                            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^실행됨^^^^^^^^^^^^^^^^");
                            APISearchNaverBook.moreList();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    makeBookMoreList();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }.start();

                }
            });
        } else
            finish();

    }

    public static void makeBookSearchList() {
        list = new ArrayList<>();
        String bookTitle;
        int j;
        for (int i = 0; i < APISearchNaverBook.bookInfoList.size(); i++) {
            j = 0;
            bookTitle = APISearchNaverBook.bookInfoList.get(i).title;
            while (j < bookTitle.length()) {
                if (bookTitle.charAt(j) == '(')
                    break;
                j++;
            }
            if (j != 0)
                bookTitle = bookTitle.substring(0, j);

            list.add(new Card_BookSearch(bookTitle, APISearchNaverBook.bookInfoList.get(i).imageURL));
        }
        adapter = new CustomListAdapter(getContext(), R.layout.card_layout_book_search, list);
        listView.setAdapter(adapter);
    }

    public static void makeBookMoreList()
    {
        String bookTitle;
        int j;
        for(int i = list.size(); i<APISearchNaverBook.bookInfoList.size(); i++){
            j = 0;
            bookTitle = APISearchNaverBook.bookInfoList.get(i).title;
            while (j < bookTitle.length()) {
                if (bookTitle.charAt(j) == '(')
                    break;
                j++;
            }
            if (j != 0)
                bookTitle = bookTitle.substring(0, j);

            list.add(new Card_BookSearch(bookTitle, APISearchNaverBook.bookInfoList.get(i).imageURL));
        }
    }
}
