package com.fishbowl.cbc.activities.nonGeneric.productfamily;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fishbowl.cbc.R;
import com.fishbowl.cbc.adapters.ProductFamilyAdapter;
import com.fishbowl.cbc.businesslogic.models.ProductFamilyModel;
import com.fishbowl.cbc.views.HexagonMaskView;

import java.util.ArrayList;
import java.util.List;

public class ProductFamilyActivity extends AppCompatActivity {

    HexagonMaskView mMaskView;
    RecyclerView mRecyclerView;
    private ProductFamilyAdapter mAdapter;
    List<ProductFamilyModel> mProductCategoryList;

    String[] mList = new String[]{
            "breakfast", "fresh salads", "sandwiches", "sweets", "pastas"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_family);
        setUpView();
        setUpPage();
    }

    private void setUpView() {
        mMaskView = (HexagonMaskView) findViewById(R.id.hexagonView);
        mRecyclerView = (RecyclerView) findViewById(R.id.menuRecyclerList);
        mProductCategoryList = new ArrayList<>();
        mMaskView.setBackgroundDrawable(Build.VERSION.SDK_INT < 21 ? getResources().getDrawable(R.drawable.normalripple)
                : getResources().getDrawable(R.drawable.materialripple));
    }

    private void setUpPage() {

        mProductCategoryList.add(new ProductFamilyModel(mList[0], null));
        mProductCategoryList.add(new ProductFamilyModel(mList[1], null));
        mProductCategoryList.add(new ProductFamilyModel(mList[2], null));
        mProductCategoryList.add(new ProductFamilyModel(mList[3], null));
        mProductCategoryList.add(new ProductFamilyModel(mList[4], null));

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        mAdapter = new ProductFamilyAdapter(this, mProductCategoryList);
        mRecyclerView.setAdapter(mAdapter);

    }

}
