package com.jy.revook_1111;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class temp_bookCard extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_book_card);
        if (APISearchNaverBook.bookInfoList != null)
            makeBookSearchList();
        else
            finish();
    }

    private void makeBookSearchList() {
        listView = (ListView) findViewById(R.id.book_search_list);
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
            {
              System.out.println("~~~~~~~~~~~~~"+j);
                bookTitle = bookTitle.substring(0, j);
            }

            list.add(new Card_BookSearch(bookTitle, APISearchNaverBook.bookInfoList.get(i).imageURL));
        }

        CustomListAdapter adapter = new CustomListAdapter(this, R.layout.card_layout_book_search, list);
        listView.setAdapter(adapter);
    }
}
