package com.android.societysolutionregistration.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.societysolutionregistration.R;
import com.android.societysolutionregistration.Utils.FBUtils;
import com.android.societysolutionregistration.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;

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

    }

    @Override
    public void onStart() {
        super.onStart();

        super.onStart();


    }

}
