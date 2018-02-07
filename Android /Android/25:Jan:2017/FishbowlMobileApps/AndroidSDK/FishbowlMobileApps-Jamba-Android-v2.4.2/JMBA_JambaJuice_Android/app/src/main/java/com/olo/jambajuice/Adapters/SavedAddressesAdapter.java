package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.OrderType.AddDeliveryAddressActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.OrderType.SavedDeliveryAddressesActivity;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.DeliveryAddress;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;

import java.util.List;

/**
 * Created by vthink on 13/03/17.
 */

public class SavedAddressesAdapter extends ArrayAdapter<DeliveryAddress> {

    SavedDeliveryAddressesActivity context;
    DeliveryAddress rowItem;

    public SavedAddressesAdapter(SavedDeliveryAddressesActivity activity, int resourceid, List<DeliveryAddress> deliveryAddressesList) {
        super(activity, resourceid, deliveryAddressesList);
        this.context = activity;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        SavedAddressesAdapter.ViewHolder holder = null;


        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_saved_address_swipe, null);
            holder = new SavedAddressesAdapter.ViewHolder();

            holder.selectAddress = (TextView) convertView.findViewById(R.id.selectAddress);
            holder.selectAddressIcon = (ImageButton) convertView.findViewById(R.id.selectAddressIcon);
            holder.viewText = (TextView) convertView.findViewById(R.id.viewText);
            holder.addressDeleteLayout = (LinearLayout) convertView.findViewById(R.id.addressDeleteLayout);
            holder.savedAddressLayout = (RelativeLayout) convertView.findViewById(R.id.savedAddressLayout);

            convertView.setTag(holder);
        } else {

            holder = (SavedAddressesAdapter.ViewHolder) convertView.getTag();
        }

        rowItem = getItem(position);

        holder.selectAddress.setText(Utils.getFormatedAddress(rowItem.getStreetaddress(), rowItem.getCity(), rowItem.getBuilding(), rowItem.getZipcode()));

        if (DataManager.getInstance().getCurrentBasket().getDeliveryAddress() != null
                && DataManager.getInstance().getCurrentBasket().getDeliveryAddress().getId() == rowItem.getId()) {
            holder.selectAddressIcon.setImageResource(R.drawable.option_selected_red);
        } else {
            holder.selectAddressIcon.setImageResource(R.drawable.option_empty);
        }

        holder.savedAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeliveryAddress deliveryAddress = getItem(position);
                setDeliveryAddress(deliveryAddress);
            }
        });

        holder.viewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("IsAddNew", false);
                bundle.putInt("pos", position);
                TransitionManager.slideUp(context, AddDeliveryAddressActivity.class, bundle);
            }
        });

        holder.addressDeleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAddress(position);

            }
        });
        return convertView;
    }

    private void deleteAddress(final int pos) {
        final int id = getItem(pos).getId();
        context.enableScreen(false);
        BasketService.deleteAddress(context, id, new BasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(Basket basket, Exception e) {
                context.enableScreen(true);
                if (e != null) {
                    Utils.showErrorAlert(context, e);
                } else {
                    DataManager.getInstance().getDeliveryAddresses().remove(getItem(pos));
                    if (DataManager.getInstance().getCurrentBasket().getDeliveryAddress() != null
                            && DataManager.getInstance().getCurrentBasket().getDeliveryAddress().getId() == id) {
                        DataManager.getInstance().getCurrentBasket().setDeliveryAddress(null);
                        DataManager.getInstance().getCurrentBasket().setDeliveryCost(0);
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        Basket basket = DataManager.getInstance().getCurrentBasket();

        context.enableScreen(false);
        BasketService.dispatchAddress(context, deliveryAddress, basket.getId(), new BasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(Basket basket, Exception e) {
                context.enableScreen(true);
                if (e != null) {
                    Utils.showErrorAlert(context, e);
                } else {
                   // TransitionManager.transitFrom(context, BasketActivity.class, true);
                    showAlert(context,"Estimated delivery time is "+basket.getLeadtimeestimateminutes()+" mins","Message");
                }
                notifyDataSetChanged();
            }
        });
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView selectAddress;
        ImageButton selectAddressIcon;
        TextView viewText;
        LinearLayout addressDeleteLayout;
        RelativeLayout savedAddressLayout;

    }

    public void showAlert(final Activity context, String message, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                TransitionManager.transitFrom(context, BasketActivity.class, true);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

}
