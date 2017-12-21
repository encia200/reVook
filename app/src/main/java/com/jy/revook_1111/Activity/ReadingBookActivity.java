package com.jy.revook_1111.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.jy.revook_1111.APISearchNaverBook;
import com.jy.revook_1111.ApplicationController;
import com.jy.revook_1111.FontSetting;
import com.jy.revook_1111.Fragment.SearchFragment;
import com.jy.revook_1111.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ReadingBookActivity extends AppCompatActivity {

    private String title;
    private String imgURL;

    private TextView textView_title;
    private TextView textView1;
    private TextView textView2;
    private EditText editText1;
    private EditText editText2;

    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_book);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        imgURL = intent.getStringExtra("imageURL");

        FontSetting fontSetting = new FontSetting(getApplicationContext());
        textView_title = (TextView) findViewById(R.id.readingbook_text1);
        textView_title.setTypeface(fontSetting.typeface_Title);
        textView1 = (TextView) findViewById(R.id.readingbook_currentpage);
        textView1.setTypeface(fontSetting.typeface_Contents);
        textView2 = (TextView) findViewById(R.id.readingbook_totalpage_input);
        textView2.setTypeface(fontSetting.typeface_Contents);
        editText1 = (EditText) findViewById(R.id.readingbook_currentpage_input);
        editText1.setTypeface(fontSetting.typeface_Contents);
        editText2 = (EditText) findViewById(R.id.readingbook_totalpage_input);
        editText2.setTypeface(fontSetting.typeface_Contents);

        button = (Button) findViewById(R.id.readingbook_button);
        button.setTypeface(fontSetting.typeface_Contents);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("users").child(ApplicationController.currentUser.uid).child("MyBook_title").setValue(title);
                FirebaseDatabase.getInstance().getReference().child("users").child(ApplicationController.currentUser.uid).child("MyBook_img").setValue(imgURL);
                FirebaseDatabase.getInstance().getReference().child("users").child(ApplicationController.currentUser.uid).child("MyBook_totalPage").setValue(editText2.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").child(ApplicationController.currentUser.uid).child("MyBook_currentPage").setValue(editText1.getText().toString());

                finish();
            }
        });

        setBookInfo();

    }

    private static class ViewHolder {
        static TextView title;
        static ImageView image;
    }

    public void setBookInfo() {
        final ViewHolder holder = new ViewHolder();

        FontSetting fontSetting = new FontSetting(getApplicationContext());
        holder.title = (TextView) findViewById(R.id.readingbook_title);
        holder.title.setTypeface(fontSetting.getTypeface_Title());
        holder.image = (ImageView) findViewById(R.id.readingbook_img);


        if (imgURL.length() == 0) {
            holder.image.setImageResource(R.drawable.nobookimg);
        }
        //create the imageloader object

        else {
            ImageLoader imageLoader = ImageLoader.getInstance();
            int defaultImage = getApplicationContext().getResources().getIdentifier("@drawable/nobookimg", null, getApplicationContext().getPackageName());

            //create display options
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(defaultImage)
                    .showImageOnFail(defaultImage)
                    .showImageOnLoading(defaultImage).build();

            //download and display image from url
            imageLoader.displayImage(imgURL, holder.image, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    }

            );
        }
    }


}