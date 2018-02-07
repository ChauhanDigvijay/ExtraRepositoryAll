package com.womensafety;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class HelpRegisterFragment extends Fragment {
    private Context context;
    private EditText etCellphone;
    private EditText etFName;
    private EditText etLName;
    private EditText etPIN;
    private EditText etPinCode;
    private View helpMeF;
    private ImageView ivSignUp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.helpMeF = inflater.inflate(R.layout.helpme3, null);
        this.etFName = (EditText) this.helpMeF.findViewById(R.id.etFName);
        this.etCellphone = (EditText) this.helpMeF.findViewById(R.id.etCellphone);
        this.etPIN = (EditText) this.helpMeF.findViewById(R.id.etPIN);
        this.etPinCode = (EditText) this.helpMeF.findViewById(R.id.etPinCode);
        this.etLName = (EditText) this.helpMeF.findViewById(R.id.etLName);
        this.ivSignUp = (ImageView) this.helpMeF.findViewById(R.id.ivSignUp);
        this.ivSignUp.setOnClickListener(new C06971());
        return this.helpMeF;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    class C06971 implements OnClickListener {
        C06971() {
        }

        public void onClick(View v) {
            ((HelpMeScreen) HelpRegisterFragment.this.context).performSignUp(HelpRegisterFragment.this.etLName.getText().toString(), HelpRegisterFragment.this.etLName.getText().toString(), HelpRegisterFragment.this.etCellphone.getText().toString(), HelpRegisterFragment.this.etPIN.getText().toString(), HelpRegisterFragment.this.etPinCode.getText().toString());
        }
    }
}
