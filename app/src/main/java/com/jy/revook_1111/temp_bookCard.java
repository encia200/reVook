package com.jy.revook_1111;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.util.ArrayList;

public class temp_bookCard extends AppCompatActivity {
    private ListView listView;
    private Button button_moreList;
    private CustomListAdapter adapter;
    private boolean isSearching = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_book_card);

        if (APISearchNaverBook.bookInfoList != null)
            makeBookSearchList();
        else
            finish();
    }

    public void onClicked(View view)
    {

    }

    private void makeBookSearchList() {
        listView = (ListView) findViewById(R.id.book_search_list);
        View footer = getLayoutInflater().inflate(R.layout.listview_footer, null, false);
        listView.addFooterView(footer);
        ArrayList<Card_BookSearch> list = new ArrayList<>();
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
                bookTitle = bookTitle.substring(0, j);

            list.add(new Card_BookSearch(bookTitle, APISearchNaverBook.bookInfoList.get(i).imageURL));
        }

        final CustomListAdapter adapter = new CustomListAdapter(this, R.layout.card_layout_book_search, list);
        listView.setAdapter(adapter);
        isSearching = true;
        button_moreList = (Button) findViewById(R.id.button_footer_moreList);
        button_moreList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             if(isSearching) {
                    new Thread() {
                        public void run() {
                            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^실행됨^^^^^^^^^^^^^^^^");
                            APISearchNaverBook.moreList();
                        }
                    };
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
