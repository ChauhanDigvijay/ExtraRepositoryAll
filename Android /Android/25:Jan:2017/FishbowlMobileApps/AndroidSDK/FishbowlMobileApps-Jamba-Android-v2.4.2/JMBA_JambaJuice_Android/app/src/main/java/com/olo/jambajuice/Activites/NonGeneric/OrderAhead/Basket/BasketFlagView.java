package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.BasketProduct;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.TransitionManager;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Nauman Afzaal on 26/05/15.
 */
public class BasketFlagView extends RelativeLayout implements View.OnClickListener {
    WeakReference<Activity> activityReference;
    private View view;

    public BasketFlagView(Activity activity) {
        super(activity);
        setUpView(activity);
        updateCount();
        updateContext(activity);
    }

    public void updateContext(Activity activity) {
        activityReference = new WeakReference<Activity>(activity);
    }

    @Override
    public void onClick(View v) {
        if (activityReference != null && activityReference.get() != null) {
            TransitionManager.transitFrom(activityReference.get(), BasketActivity.class);
        }
    }

    public void updateCount() {
        Basket basket = DataManager.getInstance().getCurrentBasket();
        List<BasketProduct> products = basket.getProducts();
        int totalProducts = basket.totalProductsCount();
        TextView totalCount = (TextView) view.findViewById(R.id.totalCount);
        totalCount.setText(totalProducts + "");
        if (totalProducts > 0) {
            totalCount.setVisibility(View.VISIBLE);
        } else {
            totalCount.setVisibility(View.GONE);
        }
    }

    // Private
    private void setUpView(final Context context) {
        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        setLayoutParams(viewParams);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.basket_icon, this, true);
        view.setOnClickListener(this);
    }

}
