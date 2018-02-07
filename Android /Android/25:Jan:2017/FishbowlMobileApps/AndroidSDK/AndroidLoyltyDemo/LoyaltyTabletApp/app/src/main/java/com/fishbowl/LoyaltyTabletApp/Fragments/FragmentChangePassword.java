package com.fishbowl.LoyaltyTabletApp.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Menu.ProfileMenu.UpdateProfile;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.CustomVolleyRequestQueue;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.ProgressBarHandler;
import com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService;
import com.fishbowl.loyaltymodule.Services.FB_LY_UserService;

import org.json.JSONObject;

public class FragmentChangePassword extends Fragment {
    EditText oldpass, newpass, confirmpass;
    Button change;
    ProgressBarHandler progressBarHandler;
    ImageView backImage;
    private ImageLoader mImageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);
        progressBarHandler = new ProgressBarHandler(getContext());
        oldpass = (EditText) v.findViewById(R.id.old_pass);
        newpass = (EditText) v.findViewById(R.id.new_pass);
        backImage = (ImageView) v.findViewById(R.id.imageView2);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UpdateProfile.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        confirmpass = (EditText) v.findViewById(R.id.confirm_pass);
        change = (Button) v.findViewById(R.id.change_pass_button);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change();
            }
        });

        return v;
    }

    public boolean checkValidation() {
        if (!(FBUtils.isValidString(newpass.getText().toString()))) {
            FBUtils.showAlert(getActivity(), "Empty password");
            return false;
        }
        if (!(FBUtils.isValidString(confirmpass.getText().toString()))) {
            FBUtils.showAlert(getActivity(), "Empty confirm password");
            return false;
        }
        if (newpass.getText().toString().length() < 6) {
            FBUtils.showAlert(getActivity(), "Minimum digit required six");
            return false;
        }
        if (confirmpass.getText().toString().length() < 6) {
            FBUtils.showAlert(getActivity(), "Minimum digit required six");
            return false;
        }
        return true;
    }

    public void change() {
        oldpass.getText().toString();
        String newPassword = newpass.getText().toString();
        String confPass = confirmpass.getText().toString();

        if (checkValidation()) {
            if (newPassword.equals(confPass)) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Change password");
                alertDialog.setMessage("Do you want to change password");
                alertDialog.setIcon(R.drawable.logomain);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle extras = new Bundle();
                        changePassword();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            } else {
                FBUtils.showAlert(getContext(), "Password not match");
            }
        }
    }


    public void changePassword() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("oldPassword", oldpass.getText().toString());
            obj.put("password", newpass.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressBarHandler.show();
        FB_LY_UserService.sharedInstance().changePassword(obj, new FB_LY_UserService.FBChangePasswordCallback() {
            @Override
            public void onChangePasswordCallback(JSONObject response, Exception error) {
                progressBarHandler.dismiss();
                if (response != null) {
                    getActivity().finish();
                } else {
                    FBUtils.showErrorAlert((Activity) getContext(), error);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        super.onStart();
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        String buttoncolor = FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor");
        if (buttoncolor != null) {
            String btncolor = "#" + buttoncolor;
            change.setBackgroundColor(Color.parseColor(btncolor));
            change.setBackgroundColor(Color.parseColor("#" + FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor")));


            change.setBackgroundResource(R.drawable.normal);
            GradientDrawable gd = (GradientDrawable) change.getBackground().getCurrent();
            gd.setColor(Color.parseColor("#" + FBThemeMobileSettingsService.sharedInstance().generalmapsetting.get("GeneralButtonType1NormalColor")));
            gd.setCornerRadii(new float[]{10, 10, 10, 10, 10, 10, 10, 10});
            gd.setStroke(1, Color.parseColor("#444444"), 0, 0);

        }
    }

}
