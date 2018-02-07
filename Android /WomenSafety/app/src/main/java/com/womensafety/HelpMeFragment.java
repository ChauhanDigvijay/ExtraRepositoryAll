package com.womensafety;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HelpMeFragment extends Fragment {
    private Context context;
    private View helpMeF;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.helpMeF = inflater.inflate(R.layout.helpme1, null);
        ((ImageView) this.helpMeF.findViewById(R.id.ivHelpMe)).setOnClickListener(new C06901());
        return this.helpMeF;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    class C06901 implements OnClickListener {
        C06901() {
        }

        public void onClick(View v) {
            ((HelpMeScreen) HelpMeFragment.this.context).performHelp();
        }
    }
}
