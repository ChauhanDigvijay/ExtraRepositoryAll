package com.olo.jambajuice.Activites.NonGeneric.Menu.ProductSearch;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator.StoreLocatorActivity;
import com.olo.jambajuice.Adapters.ProductSearchAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;

import com.olo.jambajuice.BusinessLogic.Interfaces.ProductServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;
import com.olo.jambajuice.BusinessLogic.Models.ProductSearch;
import com.olo.jambajuice.BusinessLogic.Services.ProductService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchActivity extends BaseActivity implements View.OnClickListener {

    List<ProductSearch> productSearches = new ArrayList<>();
    List<ProductCategory> productCategories = new ArrayList<>();
    List<String> names = new ArrayList<>();
    private EditText pSearchEditText;
    private TextView pCannotFind;
    private ImageView pCancelImage;
    private ScrollView scrollView;
    private ProductSearchAdapter productSearchAdapter;
    private RecyclerView productSearchRecyclerView;
    private SemiBoldButton productSearchChooseStore;
    private Context context;
    private RelativeLayout noProductAvailableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        context = this;

        AnalyticsManager.getInstance().trackScreen("ProductSearchView");


        DataManager manager = DataManager.getInstance();

        this.productCategories = manager.getSelectedStoreCategories();

        ImageView product_selection_jambaLogo = (ImageView) findViewById(R.id.product_selection_jambaLogo);
        product_selection_jambaLogo.setImageResource(R.drawable.signed_in_logo);
        noProductAvailableView = (RelativeLayout)findViewById(R.id.noProductAvailableView);

        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setUpListView();
        setUpSearchView();
        setUpButtonsView();
        setUpAllSearchableProducts();
    }

    private void setUpListView() {
        productSearchRecyclerView = (RecyclerView) findViewById(R.id.productSearchRecyclerView);
        productSearchRecyclerView.setNestedScrollingEnabled(false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        productSearchRecyclerView.setLayoutManager(linearLayoutManager);
        productSearchRecyclerView.setHasFixedSize(true);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    final FloatingActionButton scrollTopFab = (FloatingActionButton) findViewById(R.id.scrollTopFab);
//                    if(v.getTop()==scrollY){
//                        // reaches the top end
//                        scrollTopFab.setVisibility(View.GONE);
//                    }else{
//                        scrollTopFab.setVisibility(View.VISIBLE);
//                    }
//                }
//            });
//        }

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView
                int scrollX = scrollView.getScrollX(); // For HorizontalScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES
                final FloatingActionButton scrollTopFab = (FloatingActionButton) findViewById(R.id.scrollTopFab);
                if(scrollView.getTop()==scrollY){
                    // reaches the top end
                    scrollTopFab.setVisibility(View.GONE);
                }else{
                    scrollTopFab.setVisibility(View.VISIBLE);
                }
            }
        });
        final FloatingActionButton scrollTopFab = (FloatingActionButton) findViewById(R.id.scrollTopFab);
        scrollTopFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // productSearchRecyclerView.smoothScrollToPosition(0);
                scrollView.smoothScrollTo(0,0);

            }
        });
//        productSearchRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
//
//                if (firstVisibleItem > 0) {
//                    //Show FAB
//                    scrollTopFab.setVisibility(View.VISIBLE);
//                }
//                else{
//                    //Hide FAB
//                    scrollTopFab.setVisibility(View.GONE);
//                }
//
//                int visibleItemCount = linearLayoutManager.getChildCount();
//                int totalItemCount = linearLayoutManager.getItemCount();
//                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
//                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
//                    //End of list
//                    noProductAvailableView.setVisibility(View.VISIBLE);
//                }else{
//                    noProductAvailableView.setVisibility(View.GONE);
//                }
//
//            }
//        });

    }

    private void setUpSearchView() {
        pSearchEditText = (EditText) findViewById(R.id.pSearchEditText);
        pSearchEditText.requestFocus();
        pCancelImage = (ImageView) findViewById(R.id.pCancelImage);
        pCancelImage.setOnClickListener(this);
        pCancelImage.setVisibility(View.INVISIBLE);

        pSearchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.scrollTo(0, pSearchEditText.getBottom());
            }
        });


        pSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (pSearchEditText.getText().equals("") || pSearchEditText.getText() == null) {
                    invalidText();
                } else {
                    if (keyEvent != null) {
                        JambaAnalyticsManager.sharedInstance().track_ItemWith("", pSearchEditText.getText().toString(), FBEventSettings.MENU_SEARCH);
                    }

                    searchProduct(pSearchEditText.getText().toString());
                }
                return false;
            }
        });
        pSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    pCancelImage.setVisibility(View.VISIBLE);
                } else {
                    pCancelImage.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUpButtonsView() {
        productSearchChooseStore = (SemiBoldButton) findViewById(R.id.productSearchChooseStore);
        productSearchChooseStore.setOnClickListener(this);
    }

    private void setUpAllSearchableProducts() {
        if (productSearches.size() > 0) {
            productSearches.clear();
            names.clear();
        }
        if (pCannotFind != null && pCannotFind.getVisibility() == View.VISIBLE) {
            pCannotFind.setVisibility(View.GONE);
        }
        if (productCategories != null) {
            for (int i = 0; i < productCategories.size(); i++) {
                ProductService.loadProductsForCategory((Activity) context, productCategories.get(i), new ProductServiceCallback() {
                    @Override
                    public void onProductServiceCallback(List<Product> productLists, ProductCategory selectCategory, Exception exception) {
                        if (exception == null) {
                            for (Product product : productLists) {
                                if (!names.contains(product.getName())) {
                                    ProductSearch productSearch = new ProductSearch();
                                    productSearch.setProduct(product);
                                    productSearch.setProductCategory(selectCategory.getName());
                                    productSearches.add(productSearch);
                                    names.add(product.getName());
                                    updateList();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.pCancelImage:
                clearSearchText();
                setUpAllSearchableProducts();
                break;
            case R.id.productSearchChooseStore:
                TransitionManager.slideUp(ProductSearchActivity.this, StoreLocatorActivity.class);
                break;
        }
    }

    private void clearSearchText() {
        if (pSearchEditText == null) {
            pSearchEditText = (EditText) findViewById(R.id.pSearchEditText);
        }
        pSearchEditText.setText("");
    }

    public void searchProduct(String searchWord) {

        AnalyticsManager.getInstance().trackEvent("search", "product_search", searchWord);

        final String searchWords = searchWord;
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(pSearchEditText.getWindowToken(), 0);
        if (!searchWords.equals("")) {
            clearProductData();
            pCannotFind = (TextView) findViewById(R.id.pCannotFind);

            if (productCategories != null) {
                for (int i = 0; i < productCategories.size(); i++) {
                    ProductService.loadProductsForCategory((Activity) context, productCategories.get(i), new ProductServiceCallback() {
                        @Override
                        public void onProductServiceCallback(List<Product> productLists, ProductCategory selectCategory, Exception exception) {
                            if (exception == null) {
                                for (Product product : productLists) {
                                    ProductSearch productSearch = new ProductSearch();
                                    if (!names.contains(product.getName())) {
                                        if (product.getName().toLowerCase().contains(searchWords.toLowerCase())) {
                                            pCannotFind.setVisibility(View.GONE);
                                            productSearch.setProduct(product);
                                            productSearch.setProductCategory(selectCategory.getName());
                                            names.add(product.getName());
                                            productSearches.add(productSearch);
                                        }
                                        updateList();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    private void clearProductData() {
        productSearches.clear();
        names.clear();
    }

    private void updateList() {
        if (productSearchAdapter == null) {
            productSearchAdapter = new ProductSearchAdapter(productSearches);
            productSearchRecyclerView.setAdapter(productSearchAdapter);
            if (productSearchRecyclerView.getAdapter().getItemCount() == 0) {
                pCannotFind = (TextView) findViewById(R.id.pCannotFind);
                pCannotFind.setVisibility(View.VISIBLE);
            }

        }
        if (productSearchAdapter != null) {
            productSearchAdapter.notifyDataSetChanged();
            if (productSearchRecyclerView.getAdapter().getItemCount() == 0) {
                pCannotFind = (TextView) findViewById(R.id.pCannotFind);
                pCannotFind.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void invalidText() {

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Invalid Text");
        alertDialogBuilder.setMessage("Please! Enter a valid name");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (pSearchEditText != null) {
                    pSearchEditText.requestFocus();
                }
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

//    @Override
//    public void onScrollChange(View view, int l, int t, int oldl, int oldt) {
//        final FloatingActionButton scrollTopFab = (FloatingActionButton) findViewById(R.id.scrollTopFab);
//        if(view.getTop()==t){
//            // reaches the top end
//            scrollTopFab.setVisibility(View.GONE);
//        }else{
//            scrollTopFab.setVisibility(View.VISIBLE);
//        }
//    }
}

