package com.olo.jambajuice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.StoreMenuProductModifierOption;
import com.olo.jambajuice.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jeeva on 08-04-2016.
 */
public class SubstitutionExpandableListViewAdapter extends BaseExpandableListAdapter {

    int selectedParentPosition = 0, selectedChildPosition = -1;
    int click = 0;
    private Context _context;
    private ArrayList<StoreMenuProductModifierOption> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<StoreMenuProductModifierOption, ArrayList<StoreMenuProductModifierOption>> _listDataChild;

    public SubstitutionExpandableListViewAdapter(Context context, ArrayList<StoreMenuProductModifierOption> listDataHeader,
                                                 HashMap<StoreMenuProductModifierOption, ArrayList<StoreMenuProductModifierOption>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        ChildHolder childHolder = new ChildHolder();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_row_item, null);
            childHolder.txtListChild = (TextView) convertView
                    .findViewById(R.id.tvChildName);
            childHolder.imgSelected = (ImageView) convertView.findViewById(R.id.img_selected);
            childHolder.llChildDataLayout = (RelativeLayout) convertView.findViewById(R.id.llChildDataLayout);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
            DataManager.getInstance().childHeight = convertView.getHeight();
        }
        String substituteName = (String) ((StoreMenuProductModifierOption) getChild(groupPosition, childPosition)).getName();
        childHolder.txtListChild.setText(substituteName);


        if (_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).isSelected()) {
            childHolder.imgSelected.setVisibility(View.VISIBLE);
        } else {
            childHolder.imgSelected.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        //final int currentPosition = groupPosition;
        GroupHolder groupHolder = new GroupHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row_group, null);

            groupHolder.lblListHeader = (TextView) convertView
                    .findViewById(R.id.tvName);
            groupHolder.imgGrpSelected = (ImageView) convertView.findViewById(R.id.img_group_selected);
            groupHolder.imgCheckSelected = (ImageView) convertView.findViewById(R.id.img_check_selected);
            groupHolder.llParentDataLayout = (RelativeLayout) convertView.findViewById(R.id.llParentDataLayout);
            groupHolder.clickedChildName = (TextView) convertView.findViewById(R.id.childTvName);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        if (isExpanded) {
            groupHolder.imgGrpSelected.setImageResource(R.drawable.expand_icon);
        } else {
            groupHolder.imgGrpSelected.setImageResource(R.drawable.expand_icon);

        }

        for (int i = 0; i < _listDataChild.get(_listDataHeader.get(groupPosition)).size(); i++) {
            if (_listDataChild.get(_listDataHeader.get(groupPosition)).get(i).isSelected()) {
                String clickedChildText = _listDataChild.get(_listDataHeader.get(groupPosition)).get(i).getName();
                groupHolder.clickedChildName.setText("And ".concat(clickedChildText));
                groupHolder.clickedChildName.setVisibility(View.VISIBLE);
                break;
            } else {
                groupHolder.clickedChildName.setText("");
                groupHolder.clickedChildName.setVisibility(View.GONE);
            }
        }

        notifyDataSetChanged();

        int newHeight = convertView.getHeight();

        if (convertView.getHeight() > DataManager.getInstance().groupHeight) {
            DataManager.getInstance().groupHeight = convertView.getHeight();
        }

        String headerTitle = (String) ((StoreMenuProductModifierOption) getGroup(groupPosition)).getName();
        groupHolder.lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ChildHolder {
        TextView txtListChild;
        RelativeLayout llChildDataLayout;
        ImageView imgSelected;
    }

    public class GroupHolder {
        TextView lblListHeader, clickedChildName;
        RelativeLayout llParentDataLayout;
        ImageView imgGrpSelected, imgCheckSelected;
    }


}

