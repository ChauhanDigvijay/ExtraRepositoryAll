package com.fishbowl.BasicApp;

/**
 * Created by digvijay(dj)
 */
import android.graphics.Typeface;


public class FontUtil
{
    private Typeface boldFont;
    private Typeface semiBoldFont;
    private Typeface nexaRustFont;

    static FontUtil instance;

    public static FontUtil getInstance()
    {
        if (instance == null)
        {
            instance = new FontUtil();
        }
        return instance;
    }

    public Typeface getArcherBoldFont()
    {
        if (boldFont == null)
        {
            boldFont = Typeface.createFromAsset(AuthApplication.getAppContext().getAssets(), "fonts/archer_bold.otf");
        }
        return boldFont;
    }

    public Typeface getNexaRustFont()
    {
        if (nexaRustFont == null)
        {
            nexaRustFont = Typeface.createFromAsset(AuthApplication.getAppContext().getAssets(), "fonts/nexa_rust_sans_black.otf");
        }
        return nexaRustFont;
    }

    public Typeface getArcherSemiBoldFont()
    {
        if (semiBoldFont == null)
        {
            semiBoldFont = Typeface.createFromAsset(AuthApplication.getAppContext().getAssets(), "fonts/archer_semi_bold.otf");
        }
        return semiBoldFont;
    }
    public Typeface getArcherMediumFont()
    {
        if (semiBoldFont == null)
        {
            semiBoldFont = Typeface.createFromAsset(AuthApplication.getAppContext().getAssets(), "fonts/archer_medium.otf");
        }
        return semiBoldFont;
    }

}
