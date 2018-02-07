package com.olo.jambajuice.Activites.NonGeneric.Menu.ProductDetail;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Spinner;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.Fragments.ProductDetailFragment;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Nauman Afzaal on 29/06/15.
 */
public class CustomExpandableListView implements View.OnClickListener {
    LayoutInflater infalInflater;
    LinearLayout parent;
    ProductDetailFragment parentFragment;
    private List<ProductOption> listDataHeader; // header titles
    private HashMap<Integer, List<ProductOption>> listDataChild;// child data in format of header title, child title

    public CustomExpandableListView(Context context, ProductDetailFragment fragment, LinearLayout parent, List<ProductOption> listDataHeader, HashMap<Integer, java.util.List<ProductOption>> listDataChild) {
        this.listDataHeader = new ArrayList<>(listDataHeader);
        this.listDataChild = new HashMap<>(listDataChild);
        this.parent = parent;
        parentFragment = fragment;
        this.parent.removeAllViews();
        infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fillGroupView();
    }

    private void fillGroupView() {
        for (int i = 0; i < getGroupCount(); i++) {
            ProductOption head = (ProductOption) getGroup(i);
            if (!isSpecialInstruction(i) || isSpecialInstructionSupported(i)) {
                View groupView = getGroupView(i);
                LinearLayout childViewParent = (LinearLayout) groupView.findViewById(R.id.childViews);
                for (int j = 0; j < getChildrenCount(i); j++) {
                    ProductOption child = (ProductOption) getChild(i, j);
                    if (head.getName().contains("Add Yummy Extras") || head.getName().contains("add yummy extras") || head.getName().contains("Add yummy extra") || head.getName().contains("add yummy extra")) {
                        int subOption = 0;
                        View childView = createChildView(i, j, subOption, (j + 1 == getChildrenCount(i)));
                        childViewParent.addView(childView);
                        LinearLayout grandChild = (LinearLayout) childView.findViewById(R.id.grandChildViews);
                        for (int k = 0; k < getGrandChildCount(i, j); k++) {
                            View grandChildView = createGrandChildView(i, j, k, (k + 1 == getGrandChildCount(i, j)));
                            grandChild.addView(grandChildView);
                        }

                    } else {
                        if (child.getSubOptions() != null) {
                            for (int k = 0; k < child.getSubOptions().size(); k++) {
                                View childView = createChildView(i, j, k, (j + 1 == getChildrenCount(i)));
                                childViewParent.addView(childView);
                            }
                        } else {
                            int subOption = 0;
                            View childView = createChildView(i, j, subOption, (j + 1 == getChildrenCount(i)));
                            childViewParent.addView(childView);
                        }

                    }
                }
                this.parent.addView(groupView);
            }
        }
    }

    private void groupSelectedAtPosition(View groupView, int groupPosition) {
        ProductOption header = (ProductOption) getGroup(groupPosition);
        header.setIsSelected(!header.isSelected());
        LinearLayout childViewParent = (LinearLayout) groupView.findViewById(R.id.childViews);
        ImageView img_selected = (ImageView) groupView.findViewById(R.id.img_selected);
        if (header.isSelected()) {
            //Show child views
            childViewParent.setVisibility(View.VISIBLE);
            img_selected.setImageResource(R.drawable.minus_brown);
        } else {
            //Hide child views
            childViewParent.setVisibility(View.GONE);
            img_selected.setImageResource(R.drawable.plus_brown);
        }
        img_selected.invalidate();
    }


    private void parentChildSelectedAtPosition(View childView, int groupPosition, int childPosition) {
        final ProductOption child = (ProductOption) getChild(groupPosition, childPosition);
        LinearLayout grandChildViews = (LinearLayout) childView.findViewById(R.id.grandChildViews);
        ImageView imgParentImg = (ImageView) childView.findViewById(R.id.img_parent_selected);
        if (grandChildViews.getVisibility() == View.GONE) {
            //Show child views
            grandChildViews.setVisibility(View.VISIBLE);
            imgParentImg.setImageResource(R.drawable.exp_down_gray);
        } else {
            //Hide child views
            grandChildViews.setVisibility(View.GONE);
            imgParentImg.setImageResource(R.drawable.exp_up_gray);
        }
        imgParentImg.invalidate();
    }

    private void childSelectedAtPosition(final View childView, final int groupPosition, final int childPosition, final int grandChildPosition) {
        final ProductOption childSelected = (ProductOption) getGrandChild(groupPosition, childPosition, grandChildPosition);
        final ProductOption parent = (ProductOption) getChild(groupPosition, childPosition);
        if (!childSelected.isSelected() && (!parentFragment.isMoreOptionSelectionAllowed(groupPosition, parent) || !parentFragment.isMoreQuantitySelectionAllowed(groupPosition, childPosition))) {
            return;
        }

        final ImageView check = (ImageView) childView.findViewById(R.id.img_selected);
        final RelativeLayout modifierQuantityLayout = (RelativeLayout) childView.findViewById(R.id.modifierQuantityLayout);
        final TextView tv_price_quantity = (TextView) childView.findViewById(R.id.tv_price_quantity);
        final Spinner modifierQuantity = (Spinner) childView.findViewById(R.id.modifierQuantity);
        final int length = parentFragment.getSpinnerLength(groupPosition, childPosition);
        String items[] = new String[length];
        for (int i = 1; i <= length; i++) {
            items[i - 1] = String.valueOf(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(childView.getContext(), R.layout.modifier_quantity_spinner_textview, items);
        modifierQuantity.setAdapter(adapter);
        if (!parentFragment.isSpinnerAvailable(groupPosition, childPosition)) {
            modifierQuantity.setEnabled(false);
            modifierQuantityLayout.setVisibility(View.GONE);
            tv_price_quantity.setVisibility(View.GONE);
        }
        modifierQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                childSelected.setQuantity(Integer.parseInt(modifierQuantity.getSelectedItem().toString()));
                if (parentFragment.checkMaxChoiceQuantity(groupPosition, childPosition)) {
                    tv_price_quantity.setText(childSelected.getQuantity() + " x");
                    parentFragment.updatePrice();
                } else {
                    check.setVisibility(View.INVISIBLE);
                    childSelected.setQuantity(1);
                    modifierQuantityLayout.setVisibility(View.GONE);
                    tv_price_quantity.setVisibility(View.GONE);
                    childSelected.setIsSelected(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tv_price_quantity.setText(childSelected.getQuantity() + " x");
        if (childSelected.isSelected()) {
            JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(childSelected.getId()), childSelected.getName(), FBEventSettings.REMOVE_BOOST);
            check.setVisibility(View.INVISIBLE);
            childSelected.setQuantity(1);
            modifierQuantityLayout.setVisibility(View.GONE);
            tv_price_quantity.setVisibility(View.GONE);
        } else {
            check.setVisibility(View.VISIBLE);
            childSelected.setQuantity(1);
            if (modifierQuantity.isEnabled()) {
                modifierQuantityLayout.setVisibility(View.VISIBLE);
                tv_price_quantity.setVisibility(View.VISIBLE);
            }
            JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(childSelected.getId()),childSelected.getName(), FBEventSettings.ADD_BOOST);
        }
        childSelected.setIsSelected(!childSelected.isSelected());
        parentFragment.updatePrice();
    }

    private void childSelectedAtPosition(View childView, final int groupPosition, final int childPosition) {
        final ProductOption child = (ProductOption) getChild(groupPosition, childPosition);
        if (!child.isSelected() && (!parentFragment.isMoreOptionSelectionAllowed(groupPosition, child) || !parentFragment.isMoreQuantitySelectionAllowed(groupPosition, childPosition))) {
            return;
        }

        final ImageView check = (ImageView) childView.findViewById(R.id.img_selected);
        final RelativeLayout modifierQuantityLayout = (RelativeLayout) childView.findViewById(R.id.modifierQuantityLayout);
        final TextView tv_price_quantity = (TextView) childView.findViewById(R.id.tv_price_quantity);
        final Spinner modifierQuantity = (Spinner) childView.findViewById(R.id.modifierQuantity);
        int length = parentFragment.getSpinnerLength(groupPosition, childPosition);
        String items[] = new String[length];
        for (int i = 1; i <= length; i++) {
            items[i - 1] = String.valueOf(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(childView.getContext(), R.layout.modifier_quantity_spinner_textview, items);
        modifierQuantity.setAdapter(adapter);
        if (!parentFragment.isSpinnerAvailable(groupPosition, childPosition)) {
            modifierQuantity.setEnabled(false);
            modifierQuantityLayout.setVisibility(View.GONE);
            tv_price_quantity.setVisibility(View.GONE);
        }
        modifierQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                child.setQuantity(Integer.parseInt(modifierQuantity.getSelectedItem().toString()));
                if (parentFragment.checkMaxChoiceQuantity(groupPosition, childPosition)) {
                    tv_price_quantity.setText(child.getQuantity() + " x");
                    parentFragment.updatePrice();
                } else {
                    check.setVisibility(View.INVISIBLE);
                    child.setQuantity(1);
                    modifierQuantityLayout.setVisibility(View.GONE);
                    tv_price_quantity.setVisibility(View.GONE);
                    child.setIsSelected(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tv_price_quantity.setText(child.getQuantity() + " x");
        if (child.isSelected()) {
            check.setVisibility(View.INVISIBLE);
            child.setQuantity(1);
            modifierQuantityLayout.setVisibility(View.GONE);
            tv_price_quantity.setVisibility(View.GONE);
            JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(child.getId()), child.getName(), FBEventSettings.REMOVE_BOOST);
        } else {
            check.setVisibility(View.VISIBLE);
            child.setQuantity(1);
            if (modifierQuantity.isEnabled()) {
                modifierQuantityLayout.setVisibility(View.VISIBLE);
                tv_price_quantity.setVisibility(View.VISIBLE);
            }
            JambaAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(child.getId()),child.getName(), FBEventSettings.ADD_BOOST);

        }
        child.setIsSelected(!child.isSelected());
        parentFragment.updatePrice();


    }

    private View createGrandChildView(int groupPosition, final int childPosition, int grandChildPosition, boolean isLastChild) {

        final ProductOption grandChilds = (ProductOption) getGrandChild(groupPosition, childPosition, grandChildPosition);

        View convertView = infalInflater.inflate(R.layout.row_boost, parent, false);
        TextView txtListChild = (TextView) convertView.findViewById(R.id.tv_boost_detail);
        TextView amount = (TextView) convertView.findViewById(R.id.tv_price);
        ImageView check = (ImageView) convertView.findViewById(R.id.img_selected);
        RelativeLayout modifierQuantityLayout = (RelativeLayout) convertView.findViewById(R.id.modifierQuantityLayout);
        Spinner modifierQuantity = (Spinner) convertView.findViewById(R.id.modifierQuantity);
        final TextView tv_price_quantity = (TextView) convertView.findViewById(R.id.tv_price_quantity);
        //View divider = convertView.findViewById(R.id.divider);
        final EditText specialInst = (EditText) convertView.findViewById(R.id.etxt_special);
        final TextView charLimit = (TextView) convertView.findViewById(R.id.charLimit);

        convertView.setTag(groupPosition + "_" + childPosition + "_" + grandChildPosition);
        convertView.setOnClickListener(this);
        amount.setText(Utils.formatPrice(grandChilds.getCost()));
        txtListChild.setText(grandChilds.getName());
        if (grandChilds.isSelected()) {
            check.setVisibility(View.VISIBLE);
            if (modifierQuantity.isEnabled()) {
                modifierQuantityLayout.setVisibility(View.VISIBLE);
                tv_price_quantity.setVisibility(View.VISIBLE);
            }
        } else {
            check.setVisibility(View.INVISIBLE);
            modifierQuantityLayout.setVisibility(View.GONE);
            tv_price_quantity.setVisibility(View.GONE);
        }

        amount.setVisibility(View.VISIBLE);
        //divider.setVisibility(View.VISIBLE);
        txtListChild.setVisibility(View.VISIBLE);
        specialInst.setVisibility(View.INVISIBLE);

        return convertView;
    }

    private View createChildView(int groupPosition, final int childPosition, int subPosition, boolean isLastChild) {
        View convertView = infalInflater.inflate(R.layout.row_boost, parent, false);

        RelativeLayout txtChildLayout = (RelativeLayout) convertView.findViewById(R.id.textViews);
        TextView txtListChild = (TextView) convertView.findViewById(R.id.tv_boost_detail);
        TextView amount = (TextView) convertView.findViewById(R.id.tv_price);
        ImageView check = (ImageView) convertView.findViewById(R.id.img_selected);
        RelativeLayout modifierQuantityLayout = (RelativeLayout) convertView.findViewById(R.id.modifierQuantityLayout);
        Spinner modifierQuantity = (Spinner) convertView.findViewById(R.id.modifierQuantity);
        final TextView tv_price_quantity = (TextView) convertView.findViewById(R.id.tv_price_quantity);
        //View divider = convertView.findViewById(R.id.divider);
        final EditText specialInst = (EditText) convertView.findViewById(R.id.etxt_special);
        final TextView charLimit = (TextView) convertView.findViewById(R.id.charLimit);
        final ProductOption child = (ProductOption) getChild(groupPosition, childPosition);
        final ProductOption head = (ProductOption) getGroup(groupPosition);
        TextView tvParentText = (TextView) convertView.findViewById(R.id.tv_parent_text);
        RelativeLayout expChildLayout = (RelativeLayout) convertView.findViewById(R.id.expChildViews);
        ImageView childParent = (ImageView) convertView.findViewById(R.id.img_parent_selected);
        if (child.getSubOptions() != null) {
            if (head.getName().contains("Add Yummy Extras")) {
                amount.setVisibility(View.INVISIBLE);
                txtListChild.setVisibility(View.GONE);
                txtChildLayout.setVisibility(View.GONE);
                check.setVisibility(View.INVISIBLE);
                modifierQuantityLayout.setVisibility(View.GONE);
                tv_price_quantity.setVisibility(View.GONE);
                specialInst.setVisibility(View.INVISIBLE);
                charLimit.setVisibility(View.INVISIBLE);
                tvParentText.setVisibility(View.VISIBLE);
                expChildLayout.setVisibility(View.VISIBLE);
                childParent.setVisibility(View.VISIBLE);

                convertView.setTag(groupPosition + "_" + childPosition);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag = (String) v.getTag();
                        String[] arrayTag = tag.split("_");
                        parentChildSelectedAtPosition(v, Integer.parseInt(arrayTag[0]), Integer.parseInt(arrayTag[1]));
                    }
                });
                tvParentText.setText(child.getName());
                if (child.isSelected()) {
                    childParent.setImageResource(R.drawable.exp_down_gray);
                } else {
                    childParent.setImageResource(R.drawable.exp_up_gray);
                }
            } else {
                convertView.setTag(groupPosition + "_" + childPosition + "_" + subPosition);
                convertView.setOnClickListener(this);
                amount.setText(Utils.formatPrice(child.getSubOptions().get(subPosition).getCost()));
                txtListChild.setText(child.getSubOptions().get(subPosition).getName());
                if (child.isSelected()) {
                    if (modifierQuantity.isEnabled()) {
                        modifierQuantityLayout.setVisibility(View.VISIBLE);
                        tv_price_quantity.setVisibility(View.VISIBLE);
                    }
                    check.setVisibility(View.VISIBLE);
                } else {
                    check.setVisibility(View.INVISIBLE);
                    modifierQuantityLayout.setVisibility(View.GONE);
                    tv_price_quantity.setVisibility(View.GONE);
                }

                amount.setVisibility(View.VISIBLE);
                //divider.setVisibility(View.VISIBLE);
                txtListChild.setVisibility(View.VISIBLE);
                txtChildLayout.setVisibility(View.VISIBLE);
                specialInst.setVisibility(View.INVISIBLE);
                tvParentText.setVisibility(View.INVISIBLE);
                expChildLayout.setVisibility(View.INVISIBLE);
                childParent.setVisibility(View.INVISIBLE);
            }
        } else {


            if ((groupPosition + 1 == getGroupCount()) && isLastChild) {
                amount.setVisibility(View.INVISIBLE);
                txtListChild.setVisibility(View.GONE);
                txtChildLayout.setVisibility(View.GONE);
                check.setVisibility(View.INVISIBLE);
                modifierQuantityLayout.setVisibility(View.GONE);
                tv_price_quantity.setVisibility(View.GONE);
                specialInst.setVisibility(View.VISIBLE);
                charLimit.setVisibility(View.VISIBLE);
                tvParentText.setVisibility(View.INVISIBLE);
                expChildLayout.setVisibility(View.INVISIBLE);
                childParent.setVisibility(View.INVISIBLE);
                //divider.setVisibility(View.INVISIBLE);
                String inst = child.getName();
                if (!inst.isEmpty()) {
                    specialInst.setText(inst);
                    charLimit.setText(inst.length() + "/" + 16);
                }
                specialInst.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        int size = specialInst.getText().toString().length();
                        if (size > 0) {
                            charLimit.setText(size + "/" + 16);
                        } else {
                            charLimit.setText("");
                        }
                        child.setName(specialInst.getText().toString());
                    }
                });
                if (!child.getName().equals("")) {
                    specialInst.setText(child.getName());
                }
            } else {
                convertView.setTag(groupPosition + "_" + childPosition);
                convertView.setOnClickListener(this);
                amount.setText(Utils.formatPrice(child.getCost()));
                txtListChild.setText(child.getName());
                if (child.isSelected()) {
                    if (modifierQuantity.isEnabled()) {
                        modifierQuantityLayout.setVisibility(View.VISIBLE);
                        tv_price_quantity.setVisibility(View.VISIBLE);
                    }

                    check.setVisibility(View.VISIBLE);
                    if (modifierQuantity.isEnabled()) {
                        modifierQuantityLayout.setVisibility(View.VISIBLE);
                        tv_price_quantity.setVisibility(View.VISIBLE);
                    }
                } else {
                    modifierQuantityLayout.setVisibility(View.GONE);
                    tv_price_quantity.setVisibility(View.GONE);
                    check.setVisibility(View.INVISIBLE);
                    modifierQuantityLayout.setVisibility(View.GONE);
                    tv_price_quantity.setVisibility(View.GONE);
                }

                amount.setVisibility(View.VISIBLE);
                //divider.setVisibility(View.VISIBLE);
                txtListChild.setVisibility(View.VISIBLE);
                txtChildLayout.setVisibility(View.VISIBLE);
                specialInst.setVisibility(View.INVISIBLE);
                tvParentText.setVisibility(View.INVISIBLE);
                expChildLayout.setVisibility(View.INVISIBLE);
                childParent.setVisibility(View.INVISIBLE);
            }

        }

        return convertView;

    }

    private boolean isCustomize(int groupPosition) {
        return ((ProductOption) getGroup(groupPosition)).getName().equalsIgnoreCase("Customize");
    }

    private boolean isExtras(int groupPosition) {
        return ((ProductOption) getGroup(groupPosition)).getName().equalsIgnoreCase("Extras");
    }

    private View getGroupView(int groupPosition) {
        ProductOption header = (ProductOption) getGroup(groupPosition);
        View convertView = infalInflater.inflate(R.layout.row_boost_header, parent, false);
        convertView.setTag(groupPosition + "");
        convertView.setOnClickListener(this);
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.description);
        lblListHeader.setText(header.getName());
        ImageView check = (ImageView) convertView.findViewById(R.id.img_selected);
        check.setSelected(header.isSelected());
        if (header.isSelected()) {
            check.setImageResource(R.drawable.minus_brown);
        } else {
            check.setImageResource(R.drawable.plus_brown);
        }
        return convertView;
    }


    //Helper Functions
    private Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(groupPosition).get(childPosition);
    }

    private int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(groupPosition).size();
    }

    private int getGrandChildCount(int groupPosition, int childPosition) {
        if (this.listDataChild.get(groupPosition).get(childPosition).getSubOptions() == null) {
            return 0;
        } else {
            return this.listDataChild.get(groupPosition).get(childPosition).getSubOptions().size();
        }
    }

    private Object getGrandChild(int groupPosition, int childPosition, int grandchildPosition) {
        return this.listDataChild.get(groupPosition).get(childPosition).getSubOptions().get(grandchildPosition);
    }

    private Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    private int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        String[] arrayTag = tag.split("_");
        if (arrayTag.length == 3) {
            // Child
            childSelectedAtPosition(v, Integer.parseInt(arrayTag[0]), Integer.parseInt(arrayTag[1]), Integer.parseInt(arrayTag[2]));
        } else if (arrayTag.length == 2) {
            childSelectedAtPosition(v, Integer.parseInt(arrayTag[0]), Integer.parseInt(arrayTag[1]));
        } else {
            //Header
            groupSelectedAtPosition(v, Integer.parseInt(arrayTag[0]));
        }
    }

    private boolean isSpecialInstructionSupported(int groupId) {
        if (isSpecialInstruction(groupId)) {
            Store store = DataManager.getInstance().getCurrentSelectedStore();
            if (store != null) {
                return store.isSupportsSpecialInstructions();
            }
        }
        return false;
    }

    private boolean isSpecialInstruction(int groupId) {
        return groupId + 1 == getGroupCount();
    }
}
