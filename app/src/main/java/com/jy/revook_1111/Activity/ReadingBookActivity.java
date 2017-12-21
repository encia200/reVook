package com.jy.revook_1111.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jy.revook_1111.R;

public class ReadingBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_book);

        Intent intent = getIntent();
        intent.getStringExtra("title");
        intent.getStringExtra("imageURL");


    }
}