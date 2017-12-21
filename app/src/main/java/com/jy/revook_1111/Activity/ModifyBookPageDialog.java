package com.jy.revook_1111.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.jy.revook_1111.APISearchNaverBook;
import com.jy.revook_1111.ApplicationController;
import com.jy.revook_1111.FontSetting;
import com.jy.revook_1111.Fragment.BookSearchFragment;
import com.jy.revook_1111.Fragment.HomeFragment;
import com.jy.revook_1111.Fragment.SearchFragment;
import com.jy.revook_1111.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by remna on 2017-12-21.
 */

public class ModifyBookPageDialog extends Dialog {

    public Activity a;
    public TextView textView_totalPage;
    public EditText editText_currentPage;
    public Button button;


    public ModifyBookPageDialog(Activity c) {
        super(c);

        this.a = c;

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modifybookpage_dialog);

        editText_currentPage = (EditText) findViewById(R.id.modify_currentpage_input);
        textView_totalPage = (TextView) findViewById(R.id.modify_totalpage_input);
        textView_totalPage.setText(ApplicationController.currentUser.MyBook_totalPage);
        button = (Button) findViewById(R.id.modify_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newCurrent = editText_currentPage.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("users").child(ApplicationController.currentUser.uid).
                        child("MyBook_currentPage").setValue(newCurrent)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HomeFragment.dialogData.setDialogData(true);
                    }
                });
                /*a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Fragment fragment = new HomeFragment();
                        HomeFragment.fragmentTransaction = HomeFragment.fragmentManager.beginTransaction();
                        HomeFragment.fragmentTransaction.replace(R.id.homeFragment, fragment);
                        HomeFragment.fragmentTransaction.addToBackStack(null);
                        HomeFragment.fragmentTransaction.commit();
                    }
                });*/

                dismiss();
            }
        });

    }


}
