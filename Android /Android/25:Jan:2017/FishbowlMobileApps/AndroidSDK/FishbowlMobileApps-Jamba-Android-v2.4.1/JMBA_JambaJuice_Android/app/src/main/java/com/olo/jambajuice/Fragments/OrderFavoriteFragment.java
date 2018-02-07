package com.olo.jambajuice.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.olo.jambajuice.Activites.NonGeneric.OrderHistory.OrderDetailActivity;
import com.olo.jambajuice.Adapters.OrderFavoriteAdapter;
import com.olo.jambajuice.BusinessLogic.Models.FavoriteOrder;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;

import java.util.ArrayList;
import java.util.List;

import static com.olo.jambajuice.Utils.Constants.B_NEW_FAVORITE;
import static com.olo.jambajuice.Utils.Constants.B_RECENT_FAVORITE;

/**
 * Created by VT017 on 3/14/2017.
 */

public class OrderFavoriteFragment extends Fragment {
    TextView nofavorites;
    private View view;
    private ListView ordersList;
    private List<FavoriteOrder> favOrders;

    public static Fragment newInstance(List<FavoriteOrder> favOrder, List<RecentOrder> newOrder) {
        OrderFavoriteFragment fragment = new OrderFavoriteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.B_RECENT_FAVORITE, new ArrayList<FavoriteOrder>(favOrder));
        bundle.putSerializable(Constants.B_NEW_FAVORITE, new ArrayList<RecentOrder>(newOrder));
        fragment.setArguments(bundle);
        return fragment;
    }

    public static OrderFavoriteFragment getInstance(List<FavoriteOrder> favOrder) {
        OrderFavoriteFragment fragment = new OrderFavoriteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.B_RECENT_FAVORITE, new ArrayList<FavoriteOrder>(favOrder));
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
        nofavorites = (TextView) view.findViewById(R.id.nofavorites);
        if (favOrders != null && favOrders.size() > 0) {
            OrderFavoriteAdapter adapter = new OrderFavoriteAdapter(getActivity(), favOrders);
            ordersList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            ordersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "favorite");
                    bundle.putSerializable(Constants.B_ORDER_DETAIL, favOrders.get(position));
                    TransitionManager.transitFrom(getActivity(), OrderDetailActivity.class, bundle);
                    //getActivity().finish();
                }
            });
        } else {
            nofavorites.setVisibility(View.VISIBLE);
        }
    }

    private void setUpIntentData() {
        Bundle bundle = getArguments();
        favOrders = (ArrayList<FavoriteOrder>) bundle.getSerializable(B_RECENT_FAVORITE);
    }


}
