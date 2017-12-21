package com.jy.revook_1111.Fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.revook_1111.Activity.ModifyBookPageDialog;
import com.jy.revook_1111.ApplicationController;
import com.jy.revook_1111.FontSetting;
import com.jy.revook_1111.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


public class HomeFragment extends Fragment {
    public static DialogData dialogData;
    public static String title;
    public static String imgURL;
    public static String totalPage;
    public static String currentPage;

    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;

    private TextView textView_title;
    private TextView textView_text1;
    private TextView textView_text2;
    private TextView textView_text3;
    private TextView textView_text4;

    private ImageView imageView_img;

    private Button button_modify;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentManager = getFragmentManager();

        if (ApplicationController.currentUser.MyBook_title != null)
            setBookInfo(view);

        button_modify = (Button) view.findViewById(R.id.HomeFrag_modify);
        button_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyBookPageDialog cd = new ModifyBookPageDialog(getActivity());
                cd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cd.setCancelable(true);
                cd.show();

            }
        });
        dialogData = new DialogData();
        dialogData.setDialogData(false);
        dialogData.setListener(new DialogData.ChangeListener() {
            @Override
            public void onChange() {
                setBookInfo(view);
            }
        });
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

        String page = ApplicationController.currentUser.MyBook_currentPage + "/" + ApplicationController.currentUser.MyBook_totalPage + " 페이지";
        textView_text3.setText(page);
        double total = Double.parseDouble(ApplicationController.currentUser.MyBook_totalPage);
        double current = Double.parseDouble(ApplicationController.currentUser.MyBook_currentPage);
        double progress = ((int) (current / total * 100 * 10)) / 10.0;
        if (progress < 20.0)
            textView_text4.setTextColor(Color.MAGENTA);
        else if (progress < 40.0)
            textView_text4.setTextColor(Color.YELLOW);
        else if (progress < 60.0)
            textView_text4.setTextColor(Color.BLUE);
        else
            textView_text4.setTextColor(Color.GREEN);
        textView_text4.setText(String.valueOf(progress) + "%");

        holder.title = (TextView) v.findViewById(R.id.HomeFrag_readingbook_title);
        holder.title.setTypeface(fontSetting.getTypeface_Title());
        holder.image = (ImageView) v.findViewById(R.id.HomeFrag_readingbook_img);
        holder.title.setText(title);


        if (imgURL == null) {
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

    public static class DialogData {
        public Boolean data;
        private ChangeListener listener;

        public void setListener(ChangeListener listener) {
            this.listener = listener;
        }

        public void setDialogData(boolean data) {
            this.data = data;
            if (listener != null) listener.onChange();
        }

        public interface ChangeListener {
            void onChange();
        }
    }
}
