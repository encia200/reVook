package com.jy.revook_1111.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.jy.revook_1111.R;

public class EditorBackgroundDialog extends Dialog implements View.OnClickListener {

    public Activity activity;
    public EditorActivity.ImagePathVariable imagePathVariable;

    public EditorBackgroundDialog(Activity c, EditorActivity.ImagePathVariable imagePathVariable) {
        super(c);
        this.activity = c;
        this.imagePathVariable = imagePathVariable;

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_editor_background);

        ImageView bg_color_yellow = (ImageView) findViewById(R.id.bg_dialog_color_yellow);
        bg_color_yellow.setOnClickListener(this);
        ImageView bg_color_brown = (ImageView) findViewById(R.id.bg_dialog_color_brown);
        bg_color_brown.setOnClickListener(this);
        ImageView bg_color_mint = (ImageView) findViewById(R.id.bg_dialog_color_mint);
        bg_color_mint.setOnClickListener(this);
        ImageView bg_color_lightnavy = (ImageView) findViewById(R.id.bg_dialog_color_lightnavy);
        bg_color_lightnavy.setOnClickListener(this);
        ImageView bg_color_navy = (ImageView) findViewById(R.id.bg_dialog_color_navy);
        bg_color_navy.setOnClickListener(this);
        ImageView bg_color_gray = (ImageView) findViewById(R.id.bg_dialog_color_gray);
        bg_color_gray.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bg_dialog_color_yellow:
                imagePathVariable.setImagePath(Uri.parse("android.resource://" + "com.jy.revook_1111" + "/drawable/bg_yellow").toString());
                dismiss();
                break;
            case R.id.bg_dialog_color_brown:
                imagePathVariable.setImagePath(Uri.parse("android.resource://" + "com.jy.revook_1111" + "/drawable/bg_brown").toString());
                dismiss();
                break;
            case R.id.bg_dialog_color_mint:
                break;
            case R.id.bg_dialog_color_lightnavy:
                break;
            case R.id.bg_dialog_color_navy:
                break;
            case R.id.bg_dialog_color_gray:
                break;
        }
    }
}
