package com.jy.revook_1111;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BookInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        FloatingActionButton btn_upload_review = (FloatingActionButton) findViewById(R.id.btn_upload_review);
        btn_upload_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookInfoActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

}
