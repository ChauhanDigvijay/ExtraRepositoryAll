package com.BasicApp.ModelAdapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.BasicApp.Activites.NonGeneric.Menus.ProductDetail.NewModifierActivity;
import com.BasicApp.BusinessLogic.Models.NewMenuDrawer;
import com.BasicApp.BusinessLogic.Models.OrderItem;
import com.BasicApp.BusinessLogic.Models.OrderProductList;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.FBStoresItem;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * *
 * Created by Digvijay Chauhan on 1/12/15.
 */

public class ListOrderConfirmAdapter extends BaseAdapter {
    public static Double orderTotal = 0.00;
    View newView;
    Boolean checkduplicate = false;
    Activity context;
    Boolean historyflag = false;
    OrderItem order;
    EditText selectedText;
    String storelocation;
    ListView l1;
    FBStoresItem location;
    private ArrayList<NewMenuDrawer> AllDrawerItemList = null;

    public ListOrderConfirmAdapter(Activity context, NewMenuDrawer item, List<NewMenuDrawer> List, Boolean checkback, Boolean historyflag, OrderItem order, String storelocation, FBStoresItem location) {
        if (context == null) {
            throw new IllegalArgumentException("Context may not be null");
        } else {
            this.context = context;
        }

        if (order != null) {
            this.order = order;
            this.storelocation = storelocation;
        }

        this.historyflag = historyflag;
        this.location = location;

        if (checkback) {
            this.orderTotal = 0.00;
        }

        if (AllDrawerItemList == null || checkback || historyflag) {
            if (List != null) {
                AllDrawerItemList = new ArrayList<NewMenuDrawer>();
                this.AllDrawerItemList.addAll(List);
            }
        } else {
            for (NewMenuDrawer itemm : AllDrawerItemList) {
                if (item != null)
                    if (StringUtilities.isValidString(item.getProductName())) {
                        if (itemm.getProductName().equalsIgnoreCase(item.getProductName())) {
                            checkduplicate = true;
                            itemm.setQuantity(itemm.getQuantity());
                            itemm.setExt(itemm.getQuantity() * itemm.getPrice());
                        }
                    }
            }

            if (!checkduplicate && item != null) {
                this.AllDrawerItemList.add(item);
            } else {
                Double currPrice = 0.00;
                DecimalFormat df = new DecimalFormat("0.00##");
                if (AllDrawerItemList != null) {
                    for (NewMenuDrawer ext : AllDrawerItemList) {
                        currPrice = currPrice + ext.getExt();
                    }
                    TextView totalcount = (TextView) context.findViewById(R.id.total_count);
                    TextView totalsubtotal = (TextView) context.findViewById(R.id.total_subtotal);
                    TextView totalsalestax = (TextView) context.findViewById(R.id.total_salestax);
                    TextView totaltaxwith = (TextView) context.findViewById(R.id.total_taxwith);

                    totalcount.setText("$" + df.format(currPrice));
                    totalsubtotal.setText("$" + df.format(currPrice + 10));
                    Double subtotal = currPrice + 10;
                    totalsalestax.setText("$" + df.format(subtotal * .08));

                    Double totaltaxwithamount = subtotal * .08;
                    totaltaxwith.setText("$" + df.format(totaltaxwithamount + subtotal));
                }
            }
        }
    }


    @Override
    public boolean areAllItemsEnabled() {

        return true;
    }

    @Override
    public boolean isEnabled(int position) {

        return true;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        NewMenuDrawer expenceclaimList1 = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            newView = inflater.inflate(R.layout.list_test, parent, false);
            convertView = newView;

        }

        if (AllDrawerItemList != null) {

            expenceclaimList1 = AllDrawerItemList.get(position);

            populateView(context, convertView, expenceclaimList1, AllDrawerItemList, position);
        }
        parent.setTag(AllDrawerItemList);
        if (position % 2 == 1) {

            convertView.setBackgroundColor(Color.TRANSPARENT);
        } else {

            convertView.setBackgroundColor(Color.TRANSPARENT);
        }


        return convertView;
    }


    private void populateView(final Context context, View view, final NewMenuDrawer expenceclaimList, final List<NewMenuDrawer> AllDrawerItemList, int position) {

        DecimalFormat df = new DecimalFormat("0.00##");
        TextView fromDate = (TextView) view.findViewById(R.id.expenseType);
        TextView expenseName = (TextView) view.findViewById(R.id.todayDate);
        TextView next = (TextView) view.findViewById(R.id.btn_next);
        TextView percost = (TextView) view.findViewById(R.id.desc);
        TextView desc = (TextView) view.findViewById(R.id.todayDate);
        TextView totalDollar = (TextView) view.findViewById(R.id.totalDollar);
        EditText quantity = (EditText) view.findViewById(R.id.field_valueaas);
        quantity.setFocusable(false);
        TextView edit = (TextView) view.findViewById(R.id.edit_txt);
        TextView remove = (TextView) view.findViewById(R.id.remove_txt);
        final ListView l1 = (ListView) this.context.findViewById(R.id.mobile_listdf);
        final TextView totalcount1 = (TextView) this.context.findViewById(R.id.total_count);


        final TextView totalsubtotal = (TextView) this.context.findViewById(R.id.total_subtotal);


        final TextView totalsalestax = (TextView) this.context.findViewById(R.id.total_salestax);


        final TextView totaltaxwith = (TextView) this.context.findViewById(R.id.total_taxwith);


        final TextView gradient = (TextView) view.findViewById(R.id.field_gradient);


        Button B1 = (Button) this.context.findViewById(R.id.addmore);
        Button B2 = (Button) this.context.findViewById(R.id.cont_btn);
        Button B3 = (Button) this.context.findViewById(R.id.confirm_btn);
        TextView T1 = (TextView) this.context.findViewById(R.id.text_location);
        TextView T2 = (TextView) this.context.findViewById(R.id.text_locationname);
        TextView T3 = (TextView) this.context.findViewById(R.id.text_gen);
        View V1 = (View) this.context.findViewById(R.id.view1);
        //TextView T4=(TextView)this.context.findViewById(R.id.text_orderno);
        TextView T5 = (TextView) this.context.findViewById(R.id.date_txt);
        TextView T6 = (TextView) this.context.findViewById(R.id.time_txt);
        TextView T7 = (TextView) this.context.findViewById(R.id.type_txt);

        if (expenceclaimList != null) {
            //itemeditclick
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewModifierActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable("draweritem1", expenceclaimList);
                    extras.putBoolean("checkback", false);
                    intent.putExtras(extras);

                    OrderProductList.sharedInstance().currentAdded = expenceclaimList;
                    int index = OrderProductList.sharedInstance().orderProductList.indexOf(expenceclaimList);
                    AllDrawerItemList.remove(expenceclaimList);
                    OrderProductList.sharedInstance().orderProductList.remove(expenceclaimList);
                    notifyDataSetChanged();

                    context.startActivity(intent);
                }
            });
            //itemremoveclick
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)

                {
                    AllDrawerItemList.remove(expenceclaimList);
                    AllDrawerItemList.remove(expenceclaimList);
                    OrderProductList.sharedInstance().orderProductList.remove(expenceclaimList);
                    notifyDataSetChanged();

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            DecimalFormat df = new DecimalFormat("0.00##");
                            Double currPrice = 0.00;
                            notifyDataSetChanged();
                            l1.invalidateViews();
                            l1.refreshDrawableState();
                            if (AllDrawerItemList != null) {
                                for (NewMenuDrawer ext : AllDrawerItemList) {
                                    currPrice = currPrice + ext.getExt();
                                }
                            }

                            if (currPrice > 0) {
                                totalcount1.setText("$" + df.format(currPrice));
                            } else {
                                totalcount1.setText("$" + df.format(0));

                                TextView totalcount = (TextView) ((Activity) context).findViewById(R.id.total_count);
                                TextView totalsubtotal = (TextView) ((Activity) context).findViewById(R.id.total_subtotal);
                                TextView totalsalestax = (TextView) ((Activity) context).findViewById(R.id.total_salestax);
                                TextView totaltaxwith = (TextView) ((Activity) context).findViewById(R.id.total_taxwith);
                                TextView serviceTax = (TextView) ((Activity) context).findViewById(R.id.total_countDJ);
                                totalcount.setText("$" + df.format(currPrice));
                                serviceTax.setText("$ 0");
                                totalsubtotal.setText("$ 0");
                                Double subtotal = currPrice + 0;

                                totalsalestax.setText("$0");
                                Double totaltaxwithamount = subtotal * 0;
                                totaltaxwith.setText("$0");
                            }

                        }

                    });
                }
            });


            //itemcallbacktextwatcher
            if (quantity != null && !historyflag) {
                quantity.addTextChangedListener(new MyTextWatcher(view));

                quantity.setTag(expenceclaimList);
            } else if (historyflag) {
                quantity.setKeyListener(null);
            }

            //itemquantity
            if (expenceclaimList.getQuantity() != 0) {
                quantity.setText(String.valueOf(expenceclaimList.getQuantity()));
            } else {
                quantity.setText("1");
            }

            //itemdescription
            if (StringUtilities.isValidString(expenceclaimList.getProductLongDescription())) {
                desc.setText(expenceclaimList.getProductLongDescription());
            }

            //itempercost
            if (expenceclaimList.getPrice() != 0) {
                percost.setText("$" + df.format(expenceclaimList.getPrice()));
            }


            //itemcostwithquantity
            if (expenceclaimList.getQuantity() == 0) {
                totalDollar.setText("$" + df.format(expenceclaimList.getPrice()));
            } else {
                Double extPrice = expenceclaimList.getQuantity() * expenceclaimList.getPrice();
                expenceclaimList.setExt(extPrice);
                totalDollar.setText("$" + df.format(expenceclaimList.getExt()));
            }


            //sequence
            if (StringUtilities.isValidString(expenceclaimList.getProductName())) {
                fromDate.setText(expenceclaimList.getProductName());
                int next_postion = position;
                next.setText((next_postion + 1) + "");
            }


            //total amount
            Double currPrice = 0.00;
            if (AllDrawerItemList != null) {
                for (NewMenuDrawer ext : AllDrawerItemList) {
                    currPrice = currPrice + ext.getExt();

                }

                if (currPrice > 0) {
                    TextView totalcount = (TextView) this.context.findViewById(R.id.total_count);
                    totalcount.setText("$" + df.format(currPrice));
                    totalsubtotal.setText("$" + df.format(currPrice + 10));

                    Double subtotal = currPrice + 10;

                    totalsalestax.setText("$" + df.format(subtotal * .08));

                    Double totaltaxwithamount = subtotal * .08;

                    totaltaxwith.setText("$" + df.format(totaltaxwithamount + subtotal));
                }

            }

            //order come back again
            if (order != null || historyflag) {
                B1.setVisibility(View.INVISIBLE);
                B2.setVisibility(View.INVISIBLE);
                /*	if(order.getOrderId()>0)
				{
					B3.setVisibility(View.GONE);
					T4.setText("YOUR ORDER NO"+" "+":"+" "+order.getOrderId());
				}
				else
				{
					B3.setVisibility(View.GONE);

				}*/
                T1.setVisibility(View.VISIBLE);
                T2.setVisibility(View.VISIBLE);
                T5.setVisibility(View.GONE);
                T6.setVisibility(View.GONE);
                T7.setVisibility(View.VISIBLE);
                T3.setVisibility(View.GONE);
                V1.setVisibility(View.VISIBLE);

                if (StringUtilities.isValidString(order.getStoreID())) {

                    T1.setText(location.getAddress());
                    T2.setText(location.getStoreName());
                    T5.setText(order.getOrdereDateNTime());
                    T6.setText(order.getOrderTime());
                    T7.setText(order.getOrderType());
                }
            } else {
                B1.setVisibility(View.GONE);
                B2.setVisibility(View.GONE);
                T3.setVisibility(View.VISIBLE);
                B3.setVisibility(View.INVISIBLE);
                T1.setVisibility(View.GONE);
                T2.setVisibility(View.GONE);
                V1.setVisibility(View.GONE);
                T5.setVisibility(View.GONE);
                T6.setVisibility(View.GONE);
                T7.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }


    @Override
    public int getCount() {
        if (AllDrawerItemList != null) {
            return AllDrawerItemList.size();
        } else

            return 0;
    }


    @Override
    public Object getItem(int position) {
        Object b = AllDrawerItemList.get(position);
        return AllDrawerItemList.get(position);

    }


    @Override
    public long getItemId(int position) {

        return position;
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {

            DecimalFormat df = new DecimalFormat("0.00##");

            String qtyString = s.toString().trim();
            long quantity = qtyString.equals("") ? 0 : Long.valueOf(qtyString);
            EditText qtyView = (EditText) view.findViewById(R.id.field_valueaas);
            NewMenuDrawer expenceclaimList = (NewMenuDrawer) qtyView.getTag();
            Double currPrice = 0.00;
            Double priceDiff = 0.00;

            if (s.toString().length() < 10) {
                if (expenceclaimList.getQuantity() != quantity) {
                    Double extPrice = quantity * expenceclaimList.getProductbasePrice();
                    expenceclaimList.setQuantity(quantity);
                    expenceclaimList.setExt(extPrice);

                    if (AllDrawerItemList != null) {
                        for (NewMenuDrawer ext : AllDrawerItemList) {
                            currPrice = currPrice + ext.getExt();

                        }
                    }
                    TextView ext = (TextView) view.findViewById(R.id.totalDollar);
                    if (expenceclaimList.getQuantity() != 0) {
                        ext.setText("$" + df.format(expenceclaimList.getExt()));
                    } else {
                        ext.setText("");
                    }

                    if (expenceclaimList.getQuantity() != 0) {
                        qtyView.setText(String.valueOf(expenceclaimList.getQuantity()));
                    } else {
                        qtyView.setText("");
                    }

                    if (currPrice > 0) {

                        TextView totalcount = (TextView) context.findViewById(R.id.total_count);
                        TextView totalsubtotal = (TextView) context.findViewById(R.id.total_subtotal);
                        TextView totalsalestax = (TextView) context.findViewById(R.id.total_salestax);
                        TextView totaltaxwith = (TextView) context.findViewById(R.id.total_taxwith);
                        totalcount.setText("$" + df.format(currPrice));
                        totalsubtotal.setText("$" + df.format(currPrice + 10));
                        Double subtotal = currPrice + 10;
                        totalsalestax.setText("$" + df.format(subtotal * .08));
                        Double totaltaxwithamount = subtotal * .08;
                        totaltaxwith.setText("$" + df.format(totaltaxwithamount + subtotal));

                    } else {
                        TextView totalcount = (TextView) context.findViewById(R.id.total_count);
                        TextView totalsubtotal = (TextView) context.findViewById(R.id.total_subtotal);
                        TextView totalsalestax = (TextView) context.findViewById(R.id.total_salestax);
                        TextView totaltaxwith = (TextView) context.findViewById(R.id.total_taxwith);
                        totalcount.setText("$" + df.format(0));
                        totalsubtotal.setText("$" + df.format(0));
                        totalsalestax.setText("$" + df.format(0));
                        totaltaxwith.setText("$" + df.format(0));
                    }
                }

            }
            return;
        }
    }

}
