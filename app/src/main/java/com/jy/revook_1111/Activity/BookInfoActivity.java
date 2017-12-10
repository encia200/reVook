package com.jy.revook_1111.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jy.revook_1111.APISearchNaverBook;
import com.jy.revook_1111.Fragment.ReviewFragment;
import com.jy.revook_1111.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class BookInfoActivity extends Activity {

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.activity_book_info);
        Intent intent = getIntent();
        index = Integer.parseInt(intent.getStringExtra("index"));

      /*  FloatingActionButton btn_upload_review = (FloatingActionButton) findViewById(R.id.btn_upload_review);
        btn_upload_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookInfoActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });*/
    }

    private static class ViewHolder {
        TextView title;
        TextView author;
        TextView price;
        ImageView image;
    }

    /*public void setBookInfo(int index){
        final ViewHolder holder;
        String title = APISearchNaverBook.bookInfoList.get(index).title;
        String imgUrl = APISearchNaverBook.bookInfoList.get(index).imageURL;
        String author =APISearchNaverBook.bookInfoList.get(index).author;
        String price = APISearchNaverBook.bookInfoList.get(index).price;
        String publisher = APISearchNaverBook.bookInfoList.get(index).publisher;
        APISearchNaverBook.bookInfoList.get(index).

        if(imgUrl.length() == 0) {
            holder.image.setImageResource(R.drawable.nobookimg);
            holder.dialog.setVisibility(View.INVISIBLE);
        }
        //create the imageloader object

        else {
            ImageLoader imageLoader = ImageLoader.getInstance();

            int defaultImage = mContext.getResources().getIdentifier("@drawable/nobookimg", null, mContext.getPackageName());

            //create display options
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(defaultImage)
                    .showImageOnFail(defaultImage)
                    .showImageOnLoading(defaultImage).build();

            //download and display image from url
            imageLoader.displayImage(imgUrl, holder.image, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.dialog.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            holder.dialog.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            holder.dialog.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    }

            );
        }
    }*/



}
