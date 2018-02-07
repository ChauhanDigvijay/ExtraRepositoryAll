package com.womensafety;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HelpActionFragment extends Fragment {
    private Button btnITake;
    private TextView btnIneed;
    private Context context;
    private View helpMeF;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.helpMeF = inflater.inflate(R.layout.helpme2, null);
        this.btnIneed = (TextView) this.helpMeF.findViewById(R.id.btnINeed);
        this.btnITake = (Button) this.helpMeF.findViewById(R.id.btnITake);
        this.btnITake.setOnClickListener(new C06871());
        this.btnIneed.setOnClickListener(new C06882());
        this.btnIneed.setText(Html.fromHtml("<u>" + this.context.getString(R.string.already_pledge) + "</u>"));
        return this.helpMeF;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    class C06871 implements OnClickListener {
        C06871() {
        }

        public void onClick(View v) {
            ((HelpMeScreen) HelpActionFragment.this.context).performRegister();
        }
    }

    class C06882 implements OnClickListener {
        C06882() {
        }

        public void onClick(View v) {
            ((HelpMeScreen) HelpActionFragment.this.context).performLogin();
        }
    }
}
