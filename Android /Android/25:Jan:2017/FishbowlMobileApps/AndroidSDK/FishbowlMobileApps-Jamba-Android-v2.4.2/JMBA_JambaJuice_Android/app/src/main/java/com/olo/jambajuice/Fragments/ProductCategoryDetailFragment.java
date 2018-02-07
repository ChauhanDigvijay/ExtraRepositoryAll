package com.olo.jambajuice.Fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olo.jambajuice.Adapters.ProductCategoryDetailAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.ProductServiceCallback;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;
import com.olo.jambajuice.BusinessLogic.Services.ProductService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.olo.jambajuice.Utils.Constants.B_PRODUCT_CATEGORY;

public class ProductCategoryDetailFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ProductCategoryDetailAdapter adapter;
    private ProductCategory category;
    private List<Product> products;

    public static ProductCategoryDetailFragment getInstance(ProductCategory product) {
        ProductCategoryDetailFragment fragment = new ProductCategoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(B_PRODUCT_CATEGORY, product);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_category_detail, container, false);
        getIntentData();
        initComponents();
        fetchData();
        AnalyticsManager.getInstance().trackEvent(Constants.GA_CATEGORY.UX.value, "category_view", category.getName(), 0, "ProductCategoryDetailFragment");

        return view;
    }

    private void initComponents() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        final FloatingActionButton scrollTopFab = (FloatingActionButton) view.findViewById(R.id.scrollTopFab);
        scrollTopFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusableInTouchMode(true);
        recyclerView.requestFocus();
        final GridLayoutManager layoutManager = getLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(true));
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItem > 0) {
                    //Show FAB
                    scrollTopFab.setVisibility(View.VISIBLE);
                }
                else{
                    //Hide FAB
                    scrollTopFab.setVisibility(View.GONE);
                }

            }
        });
        setAdapter();
    }

    private void setAdapter() {
        adapter = new ProductCategoryDetailAdapter(getActivity(), category, new ArrayList<Product>());
        recyclerView.setAdapter(adapter);
    }

    private void fetchData() {
        ProductService.loadProductsForCategory(getActivity(), category, new ProductServiceCallback() {
            @Override
            public void onProductServiceCallback(List<Product> products, ProductCategory selectCategory, Exception exception) {
                if (exception == null) {
                    updateAdapter(products);
                } else {
                    Utils.showErrorAlert(getActivity(), exception);
                }
            }
        });
    }

    private void updateAdapter(List<Product> products) {
        this.products = products;
        adapter.addData(this.products);
    }

    public GridLayoutManager getLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
        return layoutManager;
    }

    private void getIntentData() {
        Bundle bundle = getArguments();
        category = (ProductCategory) bundle.getSerializable(B_PRODUCT_CATEGORY);
    }

}
