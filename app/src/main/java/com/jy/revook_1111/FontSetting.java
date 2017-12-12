package com.jy.revook_1111;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by remna on 2017-12-12.
 */

public class FontSetting {

    public Context context;
    public Typeface typeface_Title = null;
    public Typeface typeface_Contents = null;

    public FontSetting(Context context)
    {
        this.context = context;
        setFont();
    }

    public void setFont()
    {
        AssetManager assetManager = context.getResources().getAssets();
        typeface_Title = Typeface.createFromAsset(assetManager,"seoulEB.ttf");
        typeface_Contents = Typeface.createFromAsset(assetManager,"seoulM.ttf");
    }

    public Typeface getTypeface_Title()
    {
        return typeface_Title;
    }

    public Typeface getTypeface_Contents()
    {
        return typeface_Contents;
    }


}
