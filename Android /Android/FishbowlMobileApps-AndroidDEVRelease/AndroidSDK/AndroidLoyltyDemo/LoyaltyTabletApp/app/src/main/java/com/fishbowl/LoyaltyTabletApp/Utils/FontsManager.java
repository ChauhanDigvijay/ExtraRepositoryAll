package com.fishbowl.LoyaltyTabletApp.Utils;

import android.graphics.Typeface;

import com.fishbowl.LoyaltyTabletApp.LoyaltyApplication;

/**
 * Created by Nauman Afzaal on 08/05/15.
 */
public class FontsManager {
    static FontsManager instance;
    private Typeface boldFont;
    private Typeface semiBoldFont;
    private Typeface nexaRustFont;

    public static FontsManager getInstance() {
        if (instance == null) {
            instance = new FontsManager();
        }
        return instance;
    }

    public Typeface getArcherBoldFont() {
        if (boldFont == null) {
            boldFont = Typeface.createFromAsset(LoyaltyApplication.getAppContext().getAssets(), "fonts/archer_bold.otf");
        }
        return boldFont;
    }

    public Typeface getNexaRustFont() {
        if (nexaRustFont == null) {
            nexaRustFont = Typeface.createFromAsset(LoyaltyApplication.getAppContext().getAssets(), "fonts/nexa_rust_sans_black.otf");
        }
        return nexaRustFont;
    }

    public Typeface getArcherSemiBoldFont() {
        if (semiBoldFont == null) {
            semiBoldFont = Typeface.createFromAsset(LoyaltyApplication.getAppContext().getAssets(), "fonts/archer_semi_bold.otf");
        }
        return semiBoldFont;
    }

    public Typeface getArcherMediumFont() {
        if (semiBoldFont == null) {
            semiBoldFont = Typeface.createFromAsset(LoyaltyApplication.getAppContext().getAssets(), "fonts/archer_medium.otf");
        }
        return semiBoldFont;
    }

}
