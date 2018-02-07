package com.BasicApp.Fragments;

/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.BasicApp.Activites.NonGeneric.Home.DashboardModelActivity;
import com.BasicApp.Activites.NonGeneric.Menus.MenusLanding.MenuActivity;
import com.BasicApp.Analytic.FBAnalyticsManager;
import com.BasicApp.BusinessLogic.Models.Category;
import com.BasicApp.BusinessLogic.Models.MenuDrawerItem;
import com.BasicApp.BusinessLogic.Models.Product;
import com.BasicApp.BusinessLogic.Models.ProductList;
import com.BasicApp.ModelAdapters.SubCategoryAdapter;
import com.BasicApp.Utils.RecyclerItemClickListener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.GifWebView;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.basicmodule.sdk.R.id.detail_list;
import static com.estimote.sdk.EstimoteSDK.getApplicationContext;


public class SubCategoryFragment extends Fragment {
    public static List<MenuDrawerItem> dataList;
    public static List<MenuDrawerItem> dataList1;
    public static SubCategoryAdapter adapter;
    public View v;
    //TransparentProgressDialog pd;
    public static ListView lv;
    JSONObject wallObj;
    JSONObject wallOb1;
    Activity ref;
    JSONArray jsonArray;
    JSONArray jsonArray1;
    MenuDrawerItem item;
    LinearLayout llProgress;
    GifWebView view;
    private ViewGroup parentContainer = null;
    Activity thisActivity;
    LayoutInflater inflater;
    ListView listView;
    NetworkImageView networkImageView;
    ImageView backward;
    TextView catName;

    ViewGroup currentView = null;
    int catposition;
    private NetworkImageView topImage;
    private ImageLoader mImageLoader;
    public static Boolean checkback = true;
    ArrayList<Category> categories;
    public RecyclerView recyclerView;

    public MyAdapter mAdapter;
    public ViewPager mPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_sub_category_fragment, container, false);
        networkImageView = (NetworkImageView) v.findViewById(R.id.menu_img);
        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getActivity())
                .getImageLoader();
        Bundle bundle = getArguments();
        catposition = bundle.getInt("position");

        topImage=(NetworkImageView)v.findViewById(R.id.menu_img);

        //   catName=(TextView)v.findViewById(R.id.categoryName);


		/*
		 * backward.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) {
		 *
		 *
		 * } });
		 */

        this.inflater = inflater;
        v.setTag("USER_TAGS_TAB");
        parentContainer = container;
        thisActivity = getActivity();
        setHasOptionsMenu(true);
        //	pd = new TransparentProgressDialog(getActivity());

        return v;
    }

    public void onCustomBackPressed() {

        getFragmentManager().popBackStack();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        // Instantiate the RequestQueue.

        mImageLoader = CustomVolleyRequestQueue.getInstance(getActivity()).getImageLoader();
        //Image URL - This can point to any image file supported by Android

        loadNewItemsCount();

    }



    public void setHeaderImage(String url){

        //	ImageLoader imageLoader = FishbowlTemplate1App.getInstance().getImageLoader();
        //     mImageLoader.get(url, ImageLoader.getImageListener(topImage,R.drawable.ic_launcher, android.R.drawable.ic_dialog_alert));
        //     topImage.setImageUrl(url,mImageLoader);

        // mImageLoader.get(url, ImageLoader.getImageListener(topImage,R.drawable.headerone, android.R.drawable.ic_dialog_alert));
        //topImage.setImageUrl(url, mImageLoader);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }


    public void loadNewItemsCount() {

        categories=new  ArrayList<Category>();
        final Category category= ProductList.sharedInstance().categories.get(catposition);
        //   catName.setText(category.category);
        getActionBar().setTitle(category.category.toUpperCase());
        getActionBar().setTitle((Html.fromHtml("<font color=\"#000\">" + category.category.toUpperCase() + "</font>")));



        setHeaderImage(category.imageurl);
        mAdapter = new MyAdapter(getActivity());
        mPager = (ViewPager) v.findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(catposition);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                reloadadapter(position);
            }


            public void onPageSelected(int position) {
                // Check if this is the page you want.

            }
        });
        adapter = new SubCategoryAdapter(getActivity(),category,catposition);
        // lv = (ListView) v.findViewById(R.id.detail_list);
        recyclerView =( RecyclerView)v.findViewById(detail_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        // do whatever
//                        Intent intent = new Intent(getActivity(),ModifierActivity.class);
//
//
//                        Product product =category.products.get(position);
//
//                        MenuDrawerItem	item = new MenuDrawerItem(product.iname,R.drawable.plussign,Integer.valueOf(product.itemid),10.0, product.idesc, product.imageurl);
//                        Bundle extras = new Bundle();
//
//                        extras.putInt("ProductPosition", position);
//                        extras.putInt("CategoryPosition", catposition);
//                        extras.putBoolean("checkback",checkback);
//
//                        OrderProductList.sharedInstance().setCurrentAdded(item);
//
//                        intent.putExtras(extras);
//                        startActivityForResult(intent,2);
//                    }
//
//                    @Override public void onLongItemClick(View view, int position) {
//                        // do whatever
//                    }
//                })
//        );





    }
    public ActionBar getActionBar() {
        return ((MenuActivity) getActivity()).getSupportActionBar();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed here it is 2
        if (requestCode == 2 && resultCode == 2) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                dataList1 = null;
                this.checkback = true;
            }
        } else {
            // checkback=false;
            dataList1 = null;
        }

    }


    public void getItemValue(int pos, Activity ref) {
        this.ref = ref;

        int finalpos = 0;
        int i;
        for (i = 0; i < dataList.size(); i++) {
            MenuDrawerItem dItem = dataList.get(i);

            int comp = dItem.getImgResID();
            if (comp == pos) {
                finalpos = i;
                break;
            }
        }

        lv.setSelection(finalpos);

        lv.smoothScrollToPosition(finalpos);

        finalpos = 0;
    }


    public class MyAdapter extends PagerAdapter {
        private ImageLoader mImageLoader;

        LayoutInflater inflater;
        private int postion;
        Activity ctx;
        public MyAdapter(Activity ctx){
            this.ctx=ctx;
            inflater=LayoutInflater.from(ctx);
            mImageLoader = CustomVolleyRequestQueue.getInstance(ctx)
                    .getImageLoader();
            //instantiate your views list
        }
        public  void release(){


        }

        @Override
        public int getCount() {

            if(ProductList.sharedInstance().categories!=null){
                if(ProductList.sharedInstance().categories.size()!=0)
                {
                    return ProductList.sharedInstance().categories.size();
                }}

            return 0;
        }

        public Object instantiateItem(ViewGroup container, int position) {


            Category item=null;
            //   Log.e("MyViewPagerAdapter","instantiateItem for "+position);

            this.postion=catposition;

            item=ProductList.sharedInstance().categories.get(catposition);

            //  Log.e("MyViewPagerAdapter","instantiateItem need to create the View");

            currentView= (ViewGroup) inflater.inflate(R.layout.image_layout,container,false);
            //  Log.i("MyViewPagerAdapter", "instantiateItem() [position: " + position + "]" + " childCount:" + container.getChildCount());
            setView(currentView,item,catposition);


            ((ViewPager) container).addView(currentView);

            //        reloadadapter(position);

            return currentView;
        }

        public void setView(ViewGroup currentView, Category item, final int position)
        {


            NetworkImageView imageView = (NetworkImageView) currentView.findViewById(R.id.imageView_viewPager);
            TextView categoryName=(TextView)currentView.findViewById(R.id.categoryName);

            if(StringUtilities.isValidString(item.imageurl)) {


                imageView.setImageUrl(item.imageurl,mImageLoader);

            }
            else
            {
                imageView.setBackgroundResource(R.drawable.happyhour);
            }
            categoryName.setText(item.category);
            // reloadadapter(item,position);
            //   reloadadapter(position);

        }



        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
            Log.i("MyViewPagerAdapter", "destroyItem() [position: " + position + "]" + " childCount:" + container.getChildCount());
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view==((View)object);
        }
    }

    public void reloadadapter(final int position)


    {
        catposition = position;


        final Category category= ProductList.sharedInstance().categories.get(catposition);


        TextView categoryName=(TextView)currentView.findViewById(R.id.categoryName);
        NetworkImageView imageView = (NetworkImageView) currentView.findViewById(R.id.imageView_viewPager);

        categories=new  ArrayList<Category>();


        if(StringUtilities.isValidString(category.imageurl)) {


            imageView.setImageUrl(category.imageurl,mImageLoader);

        }
        else
        {
            imageView.setBackgroundResource(R.drawable.happyhour);
        }

        categoryName.setText(category.category);
        adapter = new SubCategoryAdapter(getActivity(),category,position);
        adapter. notifyDataSetChanged();
        // lv = (ListView) v.findViewById(R.id.detail_list);
        recyclerView =( RecyclerView)v.findViewById(detail_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                       // Intent intent = new Intent(getActivity(),ModifierActivity.class);

                         Intent intent = new Intent(getActivity(),DashboardModelActivity.class);
                        final Category category= ProductList.sharedInstance().categories.get(catposition);
                        final Product product =category.products.get(position);


                        MenuDrawerItem	item = new MenuDrawerItem(product.iname,R.drawable.plussign,Integer.valueOf(product.itemid),10.0, product.idesc, product.imageurl);
                        Bundle extras = new Bundle();
                        extras.putInt("ProductPosition", position);
                        extras.putInt("CategoryPosition", catposition);
                        extras.putBoolean("checkback",checkback);

                     //   OrderProductList.sharedInstance().setCurrentAdded(item);
                        FBAnalyticsManager.sharedInstance().track_ItemWith(product.itemid,product.iname, FBEventSettings.SUB_CATEGORY_CLICK);
                        intent.putExtras(extras);
                        startActivityForResult(intent,2);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

}