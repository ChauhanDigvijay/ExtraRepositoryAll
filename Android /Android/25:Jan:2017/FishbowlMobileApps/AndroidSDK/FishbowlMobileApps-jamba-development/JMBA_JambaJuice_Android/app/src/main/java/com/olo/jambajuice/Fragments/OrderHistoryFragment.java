package com.olo.jambajuice.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.olo.jambajuice.Activites.NonGeneric.OrderHistory.OrderDetailActivity;
import com.olo.jambajuice.Adapters.OrderHistoryAdapter;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;

import java.util.ArrayList;
import java.util.List;

import static com.olo.jambajuice.Utils.Constants.B_RECENT_ORDER;

public class OrderHistoryFragment extends Fragment {
    private View view;
    private ListView ordersList;
    private List<RecentOrder> recentOrder;

    public static Fragment newInstance(List<RecentOrder> recentOrder) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        Bundle bundle = new Bundle();
        if (recentOrder == null) {
            bundle.putSerializable(Constants.B_RECENT_ORDER, new ArrayList<>(new ArrayList<RecentOrder>()));
        } else {
            bundle.putSerializable(Constants.B_RECENT_ORDER, new ArrayList<>(recentOrder));
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    public static OrderHistoryFragment getInstance(List<RecentOrder> recentOrder) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        Bundle bundle = new Bundle();
        if (recentOrder == null) {
            bundle.putSerializable(Constants.B_RECENT_ORDER, new ArrayList<>(new ArrayList<RecentOrder>()));
        } else {
            bundle.putSerializable(Constants.B_RECENT_ORDER, new ArrayList<>(recentOrder));
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_history, container, false);
        setUpIntentData();
        initComponents();
        return view;
    }

    private void initComponents() {
        ordersList = (ListView) view.findViewById(R.id.orders_list);
        if (recentOrder != null) {
            OrderHistoryAdapter adapter = new OrderHistoryAdapter(getActivity(), recentOrder);
            ordersList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            ordersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "recent");
                    bundle.putSerializable(Constants.B_ORDER_DETAIL, recentOrder.get(position));
                    TransitionManager.transitFrom(getActivity(), OrderDetailActivity.class, bundle);
                    //getActivity().finish();
                }
            });
        }
    }

    // getting values from newInstance Bundle and saving
    private void setUpIntentData() {
        Bundle bundle = getArguments();
        recentOrder = (ArrayList<RecentOrder>) bundle.getSerializable(B_RECENT_ORDER);
    }
}
