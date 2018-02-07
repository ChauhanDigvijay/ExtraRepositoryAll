package com.fishbowl.fbtemplate2;

/**
 **
 * Created by Digvijay Chauhan on 1/12/15.
 */
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class List_OrderConfirmAdapter extends BaseAdapter {
	View newView ;

	public static Double orderTotal = 0.00;

	Boolean checkduplicate=false;
	Activity context;


	EditText selectedText;

	private static ArrayList<DetailItem> AllDrawerItemList=null;

	public List_OrderConfirmAdapter(Activity context,DetailItem item,List<DetailItem> List,Boolean checkback ) {


		if (context == null) {
			throw new IllegalArgumentException("Context may not be null");
		}
		this.context = context;
		if(checkback)
		{
			this.orderTotal = 0.00;
		}
		if(AllDrawerItemList==null||checkback){
			AllDrawerItemList=new 	ArrayList<DetailItem>(); 			
			this.AllDrawerItemList.add(item);
		}else
		{
			for(DetailItem itemm:AllDrawerItemList)
			{
				if(itemm.getTitle().equalsIgnoreCase(item.getTitle()))
				{
					checkduplicate=true;
					itemm.setQuantity(itemm.getQuantity()+1);
					itemm.setExt(itemm.getQuantity()*itemm.getPrice());
				}

			}

			if(!checkduplicate)
			{


				this.AllDrawerItemList.add(item);

			}
			else
			{
				Double currPrice=0.00;
				DecimalFormat df = new DecimalFormat("0.00##");
				if(AllDrawerItemList!=null)
				{
					for(DetailItem ext:AllDrawerItemList)
					{
						currPrice=currPrice+ext.getExt();
					}


					TextView totalcount=(TextView)context.findViewById(R.id.total_count);
					totalcount.setText("$" +df.format(currPrice));
				}




			}
		}
	}


	@Override
	public boolean areAllItemsEnabled() {

		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		DetailItem	expenceclaimList1 = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			newView = inflater.inflate(R.layout.list_orderconfirm, parent, false);
			convertView = newView;
		}
		if(AllDrawerItemList!=null){
			expenceclaimList1 = AllDrawerItemList.get(position);
		}
		populateView(context, convertView, expenceclaimList1,AllDrawerItemList,position);

		return convertView;
	}

	private void populateView(Context context, View view, DetailItem expenceclaimList,List<DetailItem> AllDrawerItemList,int position) {


		DecimalFormat df = new DecimalFormat("0.00##");
		TextView fromDate = (TextView) view.findViewById(R.id.expenseType);
		TextView expenseName = (TextView) view.findViewById(R.id.todayDate);
		TextView next = (TextView) view.findViewById(R.id.btn_next);
		TextView percost = (TextView) view.findViewById(R.id.desc);


		TextView totalDollar = (TextView) view.findViewById(R.id.totalDollar);
		EditText quantity = (EditText) view.findViewById(R.id.field_valueaas);


		if(quantity!=null){


			quantity.addTextChangedListener(new MyTextWatcher(view));
			quantity.setTag(expenceclaimList);




		}
		if(expenceclaimList.getQuantity() != 0){
			quantity.setText(String.valueOf(expenceclaimList.getQuantity()));
		}
		else {
			quantity.setText("1");
		}

		/*TextView items = (TextView) view.findViewById(R.id.txtItems);
		ImageView statusIcon = (ImageView) view.findViewById(R.id.status_icon_imgs);
		 */	if(expenceclaimList!=null)

		 {

		 }


		 if(expenceclaimList.getPrice() != 0){
			 percost.setText("$" + df.format(expenceclaimList.getPrice()));
		 }
	

		 if(expenceclaimList.getQuantity() == 0){
			 totalDollar.setText("$" + df.format(expenceclaimList.getPrice()));
		 }else {
			 totalDollar.setText("$" + df.format(expenceclaimList.getExt()));
		 }
		 /*if(StringUtilities.isValidString(expenceclaimList.getPrice()))*/{

			 //	 totalDollar.setText(expenceclaimList.getTitle());

			 // totalDollar.setText("$" + df.format(expenceclaimList.getPrice()));



		 }

		 if(StringUtilities.isValidString(expenceclaimList.getTitle())){

			 fromDate.setText(expenceclaimList.getTitle());

			 int next_postion=position;
			 next.setText((next_postion+1) + ""  );



		 }}

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
		// TODO Auto-generated method stub

	}
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}


	@Override
	public int getCount() {
		if(AllDrawerItemList!=null)
		{
			return AllDrawerItemList.size();
		}
		else
			// TODO Auto-generated method stub
			return 1;
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub

		Object b = AllDrawerItemList.get(position);
		return AllDrawerItemList.get(position);

	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	@Override
	public boolean isEmpty() {

		return AllDrawerItemList.isEmpty();

	}



	OnFocusChangeListener focus_list = new View.OnFocusChangeListener() 
	{

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub

			EditText t = (EditText) v;


			if (hasFocus) {
				selectedText = t;
				{
					String editamount = t.getText().toString();
					if (!StringUtilities.isValidString(editamount)) {

						editamount = "";
					} 
					if(!editamount.equalsIgnoreCase("")){

						t.setText(editamount);

					}

				}
			}	
			else
			{/*

				String s = t.getText().toString();
				if (StringUtilities.isValidString(s)) {



					totalDollar.setText("dj");

				}
				selectedText=null;
			 */}

		}

	};








	/*TextWatcher text_watcher = new TextWatcher()
	{


		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(selectedText.getTag().equals(position)){

			{


				{	
					String str=	s.toString();	
					if(str!=null)
					{	

						TextView totalDollar = (TextView) newView.findViewById(R.id.totalDollar);

					//	int Calculatedamount=2*Integer.parseInt(str);
					totalDollar.setText(str);
			//		notifyDataSetChanged();

					//  expenseClaimItem.getExpenseData().getAmt().setCurrency( expenseClaimItem.getExpenseData().getUnitAmt().getCurrency());
				}
				}


			}
		}
		}


	};
	 */

	private class MyTextWatcher implements TextWatcher{

		private View view;
		private MyTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//do nothing
		}
		public void onTextChanged(CharSequence s, int start, int before, int count) { 
			//do nothing
		}
		public void afterTextChanged(Editable s) {

			DecimalFormat df = new DecimalFormat("0.00##");
			String qtyString = s.toString().trim();
			int quantity = qtyString.equals("") ? 0:Integer.valueOf(qtyString);
			EditText qtyView = (EditText) view.findViewById(R.id.field_valueaas);

			//	EditText qtyView = (EditText) view.findViewById(R.id.totalDollar);
			DetailItem expenceclaimList = (DetailItem) qtyView.getTag();

			/*
			if(quantity!=0)
			{*/
			Double currPrice=0.00;
			Double priceDiff=0.00;
			if(expenceclaimList.getQuantity() != quantity  ){

				// Double currPrice = expenceclaimList.getExt();




				Double extPrice = quantity * expenceclaimList.getPrice();				
				expenceclaimList.setQuantity(quantity);
				expenceclaimList.setExt(extPrice);



				if(AllDrawerItemList!=null)
				{
					for(DetailItem ext:AllDrawerItemList)
					{
						currPrice=currPrice+ext.getExt();
					}
				}

				/*if(extPrice>currPrice){
				 priceDiff = Double.valueOf(df.format(extPrice - currPrice));
				}else
				{
				 priceDiff = Double.valueOf(df.format(currPrice-extPrice));	
				}*/


				TextView ext = (TextView) view.findViewById(R.id.totalDollar);
				if(expenceclaimList.getQuantity() != 0){
					ext.setText("$" + df.format(expenceclaimList.getExt()));
				}
				else {
					ext.setText("");
				}

				if(expenceclaimList.getQuantity() != 0){
					qtyView.setText(String.valueOf(expenceclaimList.getQuantity()));
				}
				else {
					qtyView.setText("");
				}

				if(currPrice>0){
					//orderTotal += priceDiff;
					TextView totalcount=(TextView)context.findViewById(R.id.total_count);
					totalcount.setText("$" +df.format(currPrice));
				}
				else
				{
					TextView totalcount=(TextView)context.findViewById(R.id.total_count);
					totalcount.setText("$" +df.format(0));
				}
			}
			/*}
			else
			{

				TextView ext = (TextView) view.findViewById(R.id.totalDollar);
				TextView totalcount=(TextView)context.findViewById(R.id.total_count);
				totalcount.setText("$" +df.format(0));
				ext.setText("$" +df.format(0));
			}*/
			/*qtyView.setText("");




			qtyView.setText(qtyString);	*/

			return;
		}
	}

}
