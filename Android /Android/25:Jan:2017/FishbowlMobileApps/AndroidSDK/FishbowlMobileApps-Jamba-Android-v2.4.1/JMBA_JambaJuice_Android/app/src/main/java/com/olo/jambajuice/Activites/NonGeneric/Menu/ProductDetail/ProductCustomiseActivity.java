package com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketFlagViewManager;
import com.olo.jambajuice.Adapters.SubstitutionExpandableListViewAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProductModifier;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProductModifierOption;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductCustomiseActivity extends BaseActivity implements View.OnClickListener {


    SubstitutionExpandableListViewAdapter expdListAdapter;
    Context context;
    int productId;
    Toolbar toolbar;
    LinearLayout footerToolbar;
    ScrollView scrollView;
    RelativeLayout customize_root;
    Button tvModifierApply;
    int commonHeight = 0;
    private Button continueShoppingBtn;
    private SemiBoldTextView headerModifierTitle, subModifierTitle, removeModifierTitle;
    private LinearLayout subDetailView;
    private ListView listView_modifier_remove;
    private ExpandableListView lvExp;
    private List<StoreMenuProductModifier> localStoreMenuProductModifiers;
    private List<StoreMenuProductModifierOption> mainParentOption;
    private ArrayList<StoreMenuProductModifierOption> substituteListDataHeader;
    private HashMap<StoreMenuProductModifierOption, ArrayList<StoreMenuProductModifierOption>> substituteListDataChild;
    private ArrayList<StoreMenuProductModifierOption> RemoveListDataChild;
    private CustomExpandableListView customExpandableListView;
    private RelativeLayout customiseMainLayout, subTitle, removeTitle;
    private boolean isModified = false;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_product_customise);
            context = this;
            Intent intent = getIntent();
            productId = intent.getIntExtra(Constants.B_MODIFIER_PRODUCT_ID, 0);
            DataManager.getInstance().groupHeight = 0;
            DataManager.getInstance().childHeight = 0;

            customiseMainLayout = (RelativeLayout) findViewById(R.id.customiseMainLayout);
            subTitle = (RelativeLayout) findViewById(R.id.subTitle);
            removeTitle = (RelativeLayout) findViewById(R.id.removeTitle);
            headerModifierTitle = (SemiBoldTextView) findViewById(R.id.headerModifierTitle);
            subModifierTitle = (SemiBoldTextView) findViewById(R.id.subModifierTitle);
            removeModifierTitle = (SemiBoldTextView) findViewById(R.id.removeModifierTitle);
            lvExp = (ExpandableListView) findViewById(R.id.lvExp);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            footerToolbar = (LinearLayout) findViewById(R.id.footerToolbar);
            scrollView = (ScrollView) findViewById(R.id.expScrollView);
            customize_root = (RelativeLayout) findViewById(R.id.customize_root);
            listView_modifier_remove = (ListView) findViewById(R.id.listView_modifier_remove);
            tvModifierApply = (Button) findViewById(R.id.tvModifierApply);
            tvModifierApply.setOnClickListener(this);
            back = (ImageButton)findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });


            // preparing list data
            prepareListData();
            updateExpandableList();
            updateListView();

            resizeView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHeaderHeight() {
        if (BasketFlagViewManager.getInstance().basketHeight > 0) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            params.height = BasketFlagViewManager.getInstance().basketHeight;
            toolbar.setLayoutParams(params);
        }
    }

    private void resizeView() {
        ViewTreeObserver vto = customize_root.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    customize_root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    customize_root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                setHeaderHeight();
                if (lvExp != null) {
                    setExpListViewHeight();
                }
                if (listView_modifier_remove != null) {
                    setListViewHeight();
                }

                int toolbarHeight, footerHeight, scrollHeight, rootHeight, subTitleHeight, removeTitleHeight, expListHeight, listHeight;
                rootHeight = customize_root.getHeight();
                toolbarHeight = toolbar.getHeight();
                footerHeight = footerToolbar.getHeight();
                subTitleHeight = subTitle.getHeight();
                removeTitleHeight = removeTitle.getHeight();
                expListHeight = lvExp.getHeight();
                listHeight = listView_modifier_remove.getHeight();

                scrollHeight = rootHeight - (toolbarHeight + footerHeight);

                //Scroll View Height
                RelativeLayout.LayoutParams scrollParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
                LinearLayout.LayoutParams expListParams = (LinearLayout.LayoutParams) lvExp.getLayoutParams();

                if ((subTitleHeight + expListHeight + removeTitleHeight + listHeight) >= scrollHeight) {
                    scrollHeight = subTitleHeight + expListHeight + removeTitleHeight + listHeight;
//            expListHeight = (scrollHeight)-(removeTitleHeight+listHeight);
                    scrollParams.height = scrollHeight;
                    scrollView.setLayoutParams(scrollParams);
                    expListParams.height = expListHeight;
                    lvExp.setLayoutParams(expListParams);
                } else {
                    scrollParams.height = scrollHeight;
                    scrollView.setLayoutParams(scrollParams);
                }

            }
        });
    }

    public void setExpListViewHeightOnExpand(ExpandableListView listView,
                                             int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        Boolean isChildExits = false;
        int childHeight = 0;

        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            if (DataManager.getInstance().groupHeight == 0) {
                View groupItem = listAdapter.getGroupView(i, false, listView.getChildAt(i), listView);
                groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += groupItem.getMeasuredHeight();
            } else {
                totalHeight += DataManager.getInstance().groupHeight;
            }


            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    isChildExits = true;
//                    View listItem = listAdapter.getChildView(i, j, false, listView.getSelectedView(),
//                            listView);
//                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    if (DataManager.getInstance().childHeight == 0) {
                        totalHeight += DataManager.getInstance().groupHeight;
                        childHeight = DataManager.getInstance().groupHeight;
                    } else {
                        totalHeight += DataManager.getInstance().childHeight;
                        childHeight = DataManager.getInstance().childHeight;
                    }


                }
            }
        }


        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        if (isChildExits == true) {
            height = height + childHeight;
        }
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }


    private void setExpListViewHeight() {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) lvExp.getExpandableListAdapter();
        if (listAdapter != null) {
            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(lvExp.getWidth(),
                    View.MeasureSpec.EXACTLY);
            for (int i = 0; i < listAdapter.getGroupCount(); i++) {


                if (DataManager.getInstance().groupHeight == 0) {
                    View groupItem = listAdapter.getGroupView(i, false, lvExp.getChildAt(i), lvExp);
                    groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += groupItem.getMeasuredHeight();
                    commonHeight = groupItem.getMeasuredHeight();
                } else {
                    totalHeight += DataManager.getInstance().groupHeight;
                    commonHeight = DataManager.getInstance().groupHeight;
                }

            }

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lvExp.getLayoutParams();
            int height = totalHeight
                    + (lvExp.getDividerHeight() * (listAdapter.getGroupCount() - 1));
            if (height < 10)
                height = 200;
            params.height = height;
            lvExp.setLayoutParams(params);
            lvExp.requestLayout();
        }
    }

    private void setListViewHeight() {
        ListAdapter listAdapter = (ListAdapter) listView_modifier_remove.getAdapter();
        if (listAdapter != null) {
            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView_modifier_remove.getWidth(),
                    View.MeasureSpec.EXACTLY);
            for (int i = 0; i < listAdapter.getCount(); i++) {
//            View groupItem = listAdapter.getView(i, listView_modifier_remove.getChildAt(i), listView_modifier_remove);
//            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += commonHeight;
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView_modifier_remove.getLayoutParams();
            int height = totalHeight
                    + (listView_modifier_remove.getDividerHeight() * (listAdapter.getCount() - 1));
            if (height < 10)
                height = 200;
            params.height = height;
            listView_modifier_remove.setLayoutParams(params);
            listView_modifier_remove.requestLayout();
        }
    }

    private void updateExpandableList() {
        if (substituteListDataHeader != null) {
            try {
                subTitle.setVisibility(View.VISIBLE);
                expdListAdapter = new SubstitutionExpandableListViewAdapter(this, substituteListDataHeader,
                        substituteListDataChild);
                // setting list adapter
                lvExp.setAdapter(expdListAdapter);
                lvExp.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        int newHeight = v.getHeight();

                        if (v.getHeight() > DataManager.getInstance().groupHeight) {
                            DataManager.getInstance().groupHeight = v.getHeight();
                        }

                        int count = 0;
                        for (int i = 0; i < substituteListDataHeader.size(); i++) {
                            if (!substituteListDataHeader.get(i).isSelected()) {
                                count++;
                            }
                        }

                        if (count == substituteListDataHeader.size()) {
                            DataManager.getInstance().groupHeight = v.getHeight();
                        }

                        setExpListViewHeightOnExpand(parent, groupPosition);
                        // Toast.makeText(v.getContext(), substituteListDataHeader.get(groupPosition).getName(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                lvExp.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                        isModified = true;
                        ImageView ci = (ImageView) v.findViewById(R.id.img_selected);
                        if (substituteListDataHeader.get(groupPosition).isSelected()) {
                            if (substituteListDataChild.get(substituteListDataHeader.get(groupPosition)).get(childPosition).isSelected()) {
                                substituteListDataHeader.get(groupPosition).setIsSelected(false);
                                substituteListDataChild.get(substituteListDataHeader.get(groupPosition)).get(childPosition).setIsSelected(false);
                                ci.setVisibility(View.GONE);
                            } else {
                                ExpandableListAdapter listAdapter = (ExpandableListAdapter) lvExp.getExpandableListAdapter();
                                for (int j = 0; j < listAdapter.getChildrenCount(groupPosition); j++) {

                                    View listItem = listAdapter.getChildView(groupPosition, j, false, v, parent);
                                    ImageView nci = (ImageView) listItem.findViewById(R.id.img_selected);
                                    substituteListDataHeader.get(groupPosition).setIsSelected(false);
                                    substituteListDataChild.get(substituteListDataHeader.get(groupPosition)).get(j).setIsSelected(false);
                                    nci.setVisibility(View.GONE);
                                }
                                substituteListDataHeader.get(groupPosition).setIsSelected(true);
                                substituteListDataChild.get(substituteListDataHeader.get(groupPosition)).get(childPosition).setIsSelected(true);
                                ci.setVisibility(View.VISIBLE);
                            }

                        } else {

                            substituteListDataHeader.get(groupPosition).setIsSelected(true);
                            substituteListDataChild.get(substituteListDataHeader.get(groupPosition)).get(childPosition).setIsSelected(true);
                            ci.setVisibility(View.VISIBLE);
                        }

                        expdListAdapter.notifyDataSetChanged();
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void updateListView() {
        if (RemoveListDataChild != null) {
            removeTitle.setVisibility(View.VISIBLE);
            listView_modifier_remove = (ListView) findViewById(R.id.listView_modifier_remove);
            final RemovalListAdapter customAdapter = new RemovalListAdapter(context, RemoveListDataChild);
            listView_modifier_remove.setAdapter(customAdapter);

            listView_modifier_remove.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    isModified = true;
                    ImageView imageView = (ImageView) view.findViewById(R.id.img_rmv_selected);
                    if (RemoveListDataChild.get(position).isSelected()) {
                        imageView.setVisibility(View.GONE);

                        RemoveListDataChild.get(position).setIsSelected(false);
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        RemoveListDataChild.get(position).setIsSelected(true);
                    }

                    customAdapter.notifyDataSetChanged();

                }
            });
        }
    }

    private void prepareListData() {
        try {


            localStoreMenuProductModifiers = new ArrayList<>();
            mainParentOption = new ArrayList<>();
            substituteListDataHeader = new ArrayList<>();
            substituteListDataChild = new HashMap<StoreMenuProductModifierOption, ArrayList<StoreMenuProductModifierOption>>();
            StoreMenuProductModifier modifier = null;
            if (DataManager.getInstance().getStoreMenuProductModifiers(productId) != null) {
//            localStoreMenuProductModifiers = new ArrayList<>(DataManager.getInstance().getStoreMenuProductModifiers(productId));

                for (StoreMenuProductModifier instanceMod : DataManager.getInstance().getStoreMenuProductModifiers(productId)) {
                    StoreMenuProductModifier newMod = new StoreMenuProductModifier(instanceMod);
                    localStoreMenuProductModifiers.add(newMod);
                }
                //DataManager.getInstance().getStoreMenuProductModifiers(productId);

                for (int i = 0; i < localStoreMenuProductModifiers.size(); i++) {
                    if (localStoreMenuProductModifiers.get(i).isCustomizeModifer()) {
                        try {
                            modifier = localStoreMenuProductModifiers.get(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }

            ArrayList<StoreMenuProductModifierOption> secLevStoreMenuProductModifierOptions;

            ArrayList<StoreMenuProductModifierOption> storeMenuProductModifierOptions = modifier.getOptions();
            for (StoreMenuProductModifierOption firstLevelOptions : storeMenuProductModifierOptions) {
                mainParentOption.add(firstLevelOptions);
                List<StoreMenuProductModifier> secLevStoreMenuProductModifiers = firstLevelOptions.getModifiers();
                for (StoreMenuProductModifier secLevModifiers : secLevStoreMenuProductModifiers) {
                    secLevStoreMenuProductModifierOptions = secLevModifiers.getOptions();
                    if (secLevStoreMenuProductModifierOptions != null && secLevStoreMenuProductModifierOptions.size() > 0) {
                        if ((secLevStoreMenuProductModifierOptions.get(0).getModifiers() != null && secLevStoreMenuProductModifierOptions.get(0).getModifiers().size() > 0) || secLevModifiers.getDescription().equalsIgnoreCase("Substitutions") || secLevModifiers.getDescription().equalsIgnoreCase("Substitutes") || secLevModifiers.getDescription().contains("Sub") || secLevModifiers.getDescription().contains("sub")) {

                            //substiture
                            for (StoreMenuProductModifierOption headerOption : secLevStoreMenuProductModifierOptions) {
//                            if(headerOption!=null && headerOption.getModifiers()!=null && headerOption.getModifiers().size()>0 && headerOption.getModifiers().get(0).getOptions()!=null &&  headerOption.getModifiers().get(0).getOptions().size()>0){
                                substituteListDataHeader.add(headerOption);
                                List<StoreMenuProductModifier> thirdLevStoreMenuProductModifierList = headerOption.getModifiers();
                                for (StoreMenuProductModifier thirdLevModifier : thirdLevStoreMenuProductModifierList) {
                                    ArrayList<StoreMenuProductModifierOption> thirdLevStoreMenuProductModifierOptionList = thirdLevModifier.getOptions();
                                    substituteListDataChild.put(headerOption, thirdLevStoreMenuProductModifierOptionList);
                                }
                                if (thirdLevStoreMenuProductModifierList == null || thirdLevStoreMenuProductModifierList.size() == 0) {
                                    ArrayList<StoreMenuProductModifierOption> dummyThirdLevStoreMenuProductModifierOptionList = new ArrayList<>();
                                    substituteListDataChild.put(headerOption, dummyThirdLevStoreMenuProductModifierOptionList);
                                }

//                            }
                            }
                        } else {
                            //removal
                            RemoveListDataChild = secLevStoreMenuProductModifierOptions;
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvModifierApply) {
            //Apply modifier changes
            DataManager.getInstance().setStoreMenuProductModifiers(productId, localStoreMenuProductModifiers);
            DataManager.getInstance().setMainParentOption(productId, mainParentOption);
            DataManager.getInstance().setSubstituteListDataHeader(productId, substituteListDataHeader);
            DataManager.getInstance().setSubstituteListDataChild(productId, substituteListDataChild);
            DataManager.getInstance().setRemoveListDataChild(productId, RemoveListDataChild);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        moveBasketToTop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class RemovalListHolder {
        TextView txtRemove;
        ImageView clickImg;
    }

    public class RemovalListAdapter extends BaseAdapter {

        RemovalListHolder listHolder = null;
        private Context context;
        private ArrayList<StoreMenuProductModifierOption> removeList;

        public RemovalListAdapter(Context context, ArrayList<StoreMenuProductModifierOption> removeList) {
            this.context = context;
            this.removeList = removeList;
        }

        @Override
        public int getCount() {
            return removeList.size();
        }

        @Override
        public Object getItem(int position) {
            return removeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            listHolder = new RemovalListHolder();
            if (v == null) {
                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = mInflater.inflate(R.layout.list_item, null);
                listHolder.txtRemove = (TextView) v.findViewById(R.id.label);
                listHolder.clickImg = (ImageView) v.findViewById(R.id.img_rmv_selected);
                v.setTag(listHolder);
            } else {
                listHolder = (RemovalListHolder) v.getTag();
            }

            if (removeList.get(position).isSelected()) {
                listHolder.clickImg.setVisibility(View.VISIBLE);
            } else {
                listHolder.clickImg.setVisibility(View.GONE);
            }

            String removeItem = removeList.get(position).getName();
            listHolder.txtRemove.setText(removeItem);
            return v;
        }
    }
}

