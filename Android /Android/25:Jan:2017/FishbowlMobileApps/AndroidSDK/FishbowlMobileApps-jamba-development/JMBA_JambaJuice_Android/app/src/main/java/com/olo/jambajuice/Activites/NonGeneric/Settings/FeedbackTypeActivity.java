package com.olo.jambajuice.Activites.NonGeneric.Settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;

/**
 * Created by Nauman Afzaal on 25/08/15.
 */
public class FeedbackTypeActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_type);
        setUpToolBar(true);
        setTitle("Feedback");
        setBackButton(true,false);
        setUpView();
    }

    private void setUpView() {
        findViewById(R.id.general).setOnClickListener(this);
        findViewById(R.id.bug).setOnClickListener(this);
        findViewById(R.id.newFeature).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.B_FEEDBACK_TYPE, btn.getText().toString());
        TransitionManager.transitFrom(this, FeedbackFormActivity.class, bundle);
    }
}
