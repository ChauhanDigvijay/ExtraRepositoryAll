package com.olo.jambajuice.Activites.NonGeneric.Authentication.SignUp;

import android.view.View;
import android.widget.ImageView;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.BitmapUtils;

/**
 * Created by Nauman on 17/06/15.
 */
public class SignUpBaseActivity extends BaseActivity {
    protected void initializeCircle(int circleId) {
        View pageControlView = findViewById(R.id.pageControl);
        if (pageControlView != null) {
            View circle = null;
            switch (circleId) {
                case 1:
                    circle = pageControlView.findViewById(R.id.circle1);
                    break;
                case 2:
                    circle = pageControlView.findViewById(R.id.circle2);
                    break;
                case 3:
                    circle = pageControlView.findViewById(R.id.circle3);
                    break;
                case 4:
                    circle = pageControlView.findViewById(R.id.circle4);
                    break;
                case 5:
                    circle = pageControlView.findViewById(R.id.circle5);
                    break;
                case 6:
                    circle = pageControlView.findViewById(R.id.circle6);
                    break;
                case 7:
                    circle = pageControlView.findViewById(R.id.circle7);
                    break;
                case 8:
                    circle = pageControlView.findViewById(R.id.circle8);
                    break;
            }
            if (circle != null) {
                circle.setBackgroundResource(R.drawable.circle_white);
            }
        }
    }

    @Override
    public void setContentView(int resId) {
        super.setContentView(resId);
        ImageView view = (ImageView) findViewById(R.id.glow_layout);
        BitmapUtils.loadBitmapResource(view, R.drawable.glow_sign_up, true);
    }

}
