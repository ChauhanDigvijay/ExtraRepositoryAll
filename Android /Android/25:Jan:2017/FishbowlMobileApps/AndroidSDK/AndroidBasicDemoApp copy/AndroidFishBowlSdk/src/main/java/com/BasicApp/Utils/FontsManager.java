package com.BasicApp.Utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by Nauman Afzaal on 08/05/15.
 */
public class FontsManager {
    static FontsManager instance;
    private Typeface boldFont;
    private Typeface semiBoldFont;
    private Typeface nexaRustFont;
    private Typeface helveticaFont;
    private Typeface sansSerifFont;

    public static Context mContext;

    public FontsManager() {

    }

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static FontsManager sharedInstance(Context context){

        if(instance==null){
            instance=new FontsManager(context);
        }

        return  instance;
    }
    public FontsManager(Context context)
    {
        if(context == null);
        mContext = context;
    }

    public static FontsManager getInstance() {
        if (instance == null) {
            instance = new FontsManager();
        }
        return instance;
    }

    public Typeface getArcherBoldFont() {
        if (boldFont == null) {
            boldFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/archer_bold.otf");
        }
        return boldFont;
    }

    public Typeface getNexaRustFont() {
        if (nexaRustFont == null) {
            nexaRustFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/nexa_rust_sans_black.otf");
        }
        return nexaRustFont;
    }

    public Typeface getArcherSemiBoldFont() {
        if (semiBoldFont == null) {
            semiBoldFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/archer_semi_bold.otf");
        }
        return semiBoldFont;
    }

    public Typeface getArcherMediumFont() {
        if (semiBoldFont == null) {
            semiBoldFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/archer_medium.otf");
        }
        return semiBoldFont;
    }

    public Typeface getHelveticaFont() {
        if (helveticaFont == null) {
            helveticaFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/helvetica_regular.ttf");
        }
        return helveticaFont;
    }

    public Typeface getSanSerifFont() {
        if (sansSerifFont == null) {
            sansSerifFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/microsoft_sans_serif.ttf");
        }
        return sansSerifFont;
    }



}
