package com.jy.revook_1111.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.revook_1111.FontSetting;
import com.jy.revook_1111.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


public class HomeFragment extends Fragment {

    public static String title;
    public static String imgURL;

    private TextView textView_title;
    private TextView textView_text1;
    private TextView textView_text2;
    private TextView textView_text3;
    private TextView textView_text4;

    private ImageView imageView_img;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setBookInfo(view);

        return view;
    }

    private static class ViewHolder {
        static TextView title;
        static ImageView image;
    }

    public void setBookInfo(View v) {
        final ViewHolder holder = new ViewHolder();

        FontSetting fontSetting = new FontSetting(getContext());
        textView_text1 = (TextView) v.findViewById(R.id.HomeFrag_readingbook_text1);
        textView_text1.setTypeface(fontSetting.typeface_Title);
        textView_text2 = (TextView) v.findViewById(R.id.HomeFrag_readingbook_text2);
        textView_text2.setTypeface(fontSetting.typeface_Title);
        textView_text3 = (TextView) v.findViewById(R.id.HomeFrag_readingbook_pages);
        textView_text3.setTypeface(fontSetting.typeface_Contents);
        textView_text4 = (TextView) v.findViewById(R.id.HomeFrag_readingbook_progress);


        holder.title = (TextView) v.findViewById(R.id.HomeFrag_readingbook_title);
        holder.title.setTypeface(fontSetting.getTypeface_Title());
        holder.image = (ImageView) v.findViewById(R.id.HomeFrag_readingbook_img);
        holder.title.setText(title);


        if(imgURL==null) {
            holder.image.setImageResource(R.drawable.nobookimg);
        }
        //create the imageloader object

        else {
            ImageLoader imageLoader = ImageLoader.getInstance();
            int defaultImage = getContext().getResources().getIdentifier("@drawable/nobookimg", null, getContext().getPackageName());

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
