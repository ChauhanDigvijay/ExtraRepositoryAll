package com.olo.jambajuice.Adapters.GiftCardAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olo.jambajuice.R;
import com.wearehathway.apps.incomm.Models.InCommTransactionHistory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {
    private List<InCommTransactionHistory> transactionHistory;


    public TransactionHistoryAdapter(List<InCommTransactionHistory> transactionList) {
        //Receiving Transaction History data.
        this.transactionHistory = transactionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transaction_history, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (transactionHistory != null) {
            final InCommTransactionHistory name = transactionHistory.get(position);

            //Set the details for Transaction History.
            holder.tvTransPrice.setText("$" + String.format("%.2f",name.getCardBalance()));
            holder.tvTransDate.setText(setDate(name.getTransactionDate()));
            holder.tvTransType.setText(name.getTransactionDescription());
        }
    }

    @Override
    public int getItemCount() {
        return transactionHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvTransType, tvTransPrice, tvTransDate;

        public ViewHolder(View v) {
            super(v);
            tvTransPrice = (TextView) v.findViewById(R.id.tvTransPrice);
            tvTransDate = (TextView) v.findViewById(R.id.tvTransDate);
            tvTransType = (TextView) v.findViewById(R.id.tvTransType);

        }
    }

    private String setDate(Date date) {
        //Set date by how many days ago.
        String reformattedStr = "";
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(currentDate);
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayNow = cal.get(Calendar.DAY_OF_MONTH);

        int yearsDiff = yearNow - year;
        int monthsDiff = monthNow - month;
        int daysDiff = dayNow - day;

        if (yearsDiff > 0) {
            reformattedStr = reformattedStr + yearsDiff + " year";
            reformattedStr = reformattedStr + getEndingString(yearsDiff);
        } else if (monthsDiff > 0) {
            reformattedStr = reformattedStr + monthsDiff + " month";
            reformattedStr = reformattedStr + getEndingString(monthsDiff);

        } else if (daysDiff > 0) {
            reformattedStr = reformattedStr + daysDiff + " day";
            reformattedStr = reformattedStr + getEndingString(daysDiff);
        } else {
            reformattedStr = reformattedStr + " today";
        }
        return reformattedStr;
    }

    private String getEndingString(int value) {
        if (value > 1) {
            return "s ago";
        } else {
            return " ago";
        }
    }
}
