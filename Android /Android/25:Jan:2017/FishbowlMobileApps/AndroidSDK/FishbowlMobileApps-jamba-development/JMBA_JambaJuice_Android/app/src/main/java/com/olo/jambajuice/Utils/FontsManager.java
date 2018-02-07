package com.olo.jambajuice.Utils;

import android.graphics.Typeface;
import android.renderscript.Type;

import com.olo.jambajuice.JambaApplication;

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

    public static FontsManager getInstance() {
        if (instance == null) {
            instance = new FontsManager();
        }
        return instance;
    }

    public Typeface getArcherBoldFont() {
        if (boldFont == null) {
            boldFont = Typeface.createFromAsset(JambaApplication.getAppContext().getAssets(), "fonts/archer_bold.otf");
        }
        return boldFont;
    }

    public Typeface getNexaRustFont() {
        if (nexaRustFont == null) {
            nexaRustFont = Typeface.createFromAsset(JambaApplication.getAppContext().getAssets(), "fonts/nexa_rust_sans_black.otf");
        }
        return nexaRustFont;
    }

    public Typeface getArcherSemiBoldFont() {
        if (semiBoldFont == null) {
            semiBoldFont = Typeface.createFromAsset(JambaApplication.getAppContext().getAssets(), "fonts/archer_semi_bold.otf");
        }
        return semiBoldFont;
    }

    public Typeface getArcherMediumFont() {
        if (semiBoldFont == null) {
            semiBoldFont = Typeface.createFromAsset(JambaApplication.getAppContext().getAssets(), "fonts/archer_medium.otf");
        }
        return semiBoldFont;
    }

    public Typeface getHelveticaFont() {
        if (helveticaFont == null) {
            helveticaFont = Typeface.createFromAsset(JambaApplication.getAppContext().getAssets(), "fonts/helvetica_regular.ttf");
        }
        return helveticaFont;
    }

    public Typeface getSanSerifFont() {
        if (sansSerifFont == null) {
            sansSerifFont = Typeface.createFromAsset(JambaApplication.getAppContext().getAssets(), "fonts/microsoft_sans_serif.ttf");
        }
        return sansSerifFont;
    }

}
