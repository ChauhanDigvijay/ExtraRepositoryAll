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

public class HelpLoginFragment extends Fragment {
    private Context context;
    private EditText etCellphone;
    private EditText etPIN;
    private View helpMeF;
    private ImageView ivSignUp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.helpMeF = inflater.inflate(R.layout.helpme4, null);
        this.etCellphone = (EditText) this.helpMeF.findViewById(R.id.etCellphone);
        this.etPIN = (EditText) this.helpMeF.findViewById(R.id.etPIN);
        this.ivSignUp = (ImageView) this.helpMeF.findViewById(R.id.ivSignUp);
        this.ivSignUp.setOnClickListener(new C06891());
        return this.helpMeF;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    class C06891 implements OnClickListener {
        C06891() {
        }

        public void onClick(View v) {
            ((HelpMeScreen) HelpLoginFragment.this.context).performLogin(HelpLoginFragment.this.etCellphone.getText().toString(), HelpLoginFragment.this.etPIN.getText().toString());
        }
    }
}
