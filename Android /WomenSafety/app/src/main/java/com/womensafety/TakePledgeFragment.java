package com.womensafety;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class TakePledgeFragment extends Fragment {
    private Context context;
    private View helpMeF;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.helpMeF = inflater.inflate(R.layout.helpme1_1, null);
        Button btnAlready = (Button) this.helpMeF.findViewById(R.id.btnAlready);
        ((Button) this.helpMeF.findViewById(R.id.btnTake)).setOnClickListener(new C07041());
        btnAlready.setOnClickListener(new C07052());
        return this.helpMeF;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    class C07041 implements OnClickListener {
        C07041() {
        }

        public void onClick(View v) {
            ((HelpMeScreen) TakePledgeFragment.this.context).performInfo();
        }
    }

    class C07052 implements OnClickListener {
        C07052() {
        }

        public void onClick(View v) {
            ((HelpMeScreen) TakePledgeFragment.this.context).performLogin();
        }
    }
}
