package com.raleys.app.android;

import java.util.ArrayList;

import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.raleys.app.android.models.ShoppingListName;
import com.raleys.libandroid.TextDialog;

public class ListNameAdapter extends BaseAdapter {
	private RaleysApplication _app;
	private Context _context;
	private Object _handler;
	private String _callback;
	private int _width;
	private int _height;
	private ArrayList<ShoppingListName> _listNames;
	private LayoutInflater _inflater;
	private Typeface _normalFont;
	public Boolean createNew;
	public ListNameCell addNewCell;
	public Boolean addNewAlertIsPresent;
	private int swipeDeletePosition;

	public ListNameAdapter(Context context,
			ArrayList<ShoppingListName> listNames, int width, int height,
			String callback) {
		_app = (RaleysApplication) context.getApplicationContext();
		_context = context;
		_handler = context;
		_callback = callback;
		_width = width;
		_height = height;
		_listNames = listNames;
		moveActiveShopListToTop();
		_inflater = LayoutInflater.from(context);
		_normalFont = _app.getNormalFont();
		createNew = false;
		addNewAlertIsPresent = false;
		swipeDeletePosition = -1;

		if (_app._listNamesCellBitmap == null)
			_app._listNamesCellBitmap = _app.getAppBitmap(
					"listname_table_cell", R.drawable.listname_table_cell,
					_width, _height);

		if (_app._listNamesCellSelectedBitmap == null)
			_app._listNamesCellSelectedBitmap = _app.getAppBitmap(
					"listname_table_cell_selected",
					R.drawable.listname_table_cell_selected, _width, _height);

	}

	// reorder the shopplist name. Active list will got to top
	public void moveActiveShopListToTop() {
		ArrayList<ShoppingListName> newList = new ArrayList<ShoppingListName>();
		String currentActiveListId;
		// ShoppingListName activeListName;
		currentActiveListId = "";
		currentActiveListId = _app.getActiveListId();

		for (int i = 0; i < _listNames.size(); i++) {
			ShoppingListName listName = _listNames.get(i);
			if (!currentActiveListId.isEmpty()
					&& currentActiveListId.equalsIgnoreCase(listName.listId)) {
				// activeListName = listName;
				newList.add(0, listName);
			} else {
				newList.add(listName);
			}
		}
		_listNames = newList;
	}

	public void changeListName(ArrayList<ShoppingListName> listNames) {
		_listNames = listNames;
		moveActiveShopListToTop();
	}

	@Override
	public int getCount() {
		int size = _listNames.size();
		if (createNew == true) {
			size += 1;
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		return _listNames.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		ListNameCell cell;
		LayoutParams layoutParams;

		try {

			// abandon current focus to avoid app crash
			// View currentFocus = ((Activity) _context).getCurrentFocus();
			// if (currentFocus != null) {
			// currentFocus.clearFocus();
			// hideKeyboard();
			// }
			//

			if (createNew == true && getCount() - 1 == position) {

				// convert_view_shoppingnamelist_addnew_layout
				convertView = _inflater.inflate(
						R.layout.convert_view_shoppingnamelist_addnew_layout,
						null);

				cell = new ListNameCell();
				cell._layout = (LinearLayout) convertView
						.findViewById(R.id.shoppingnamelist_holder_layout);
				layoutParams = cell._layout.getLayoutParams();
				layoutParams.height = _height;
				layoutParams.width = _width;
				cell._layout.setLayoutParams(layoutParams);

				cell._addnew = (EditText) convertView.findViewById(R.id.addnew);
				layoutParams = cell._addnew.getLayoutParams();
				layoutParams.width = _width;
				cell._addnew.setLayoutParams(layoutParams);
				cell._addnew
						.setOnEditorActionListener(new OnEditorActionListener() {
							@Override
							public boolean onEditorAction(TextView v,
									int actionId, KeyEvent event) {
								if (actionId == EditorInfo.IME_ACTION_DONE) {

									addNewList();
									return true;
								}
								return false;
							}
						});

				cell._addnew.setFocusableInTouchMode(true);
				cell._addnew.requestFocus();
				cell._addnew
						.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
				// cell._addnew.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

				if (addNewCell != null) {
					cell._addnew.setText(addNewCell._addnew.getText());
				}

				addNewCell = cell;

				// cell._addnew.setOnFocusChangeListener(new
				// OnFocusChangeListener() {
				//
				// @Override
				// public void onFocusChange(View v, boolean hasFocus) {
				// if(!hasFocus){
				// hideKeyboard(v);
				// }
				//
				// }
				// });
				// LinearLayout.LayoutParams params;
				return convertView;
			}

			final ShoppingListName listName = _listNames.get(position);

			// if (convertView == null) {
			convertView = _inflater.inflate(
					R.layout.convert_view_shoppingnamelist_layout, null);

			cell = new ListNameCell();
			cell._layout = (LinearLayout) convertView
					.findViewById(R.id.shoppingnamelist_holder_layout);
			layoutParams = cell._layout.getLayoutParams();
			layoutParams.height = _height;
			cell._layout.setVisibility(View.VISIBLE);
			layoutParams.width = _width;
			cell._layout.setLayoutParams(layoutParams);
			LinearLayout.LayoutParams params;

			// list name
			Button name = (Button) convertView
					.findViewById(R.id._listNameButton);
			params = (LinearLayout.LayoutParams) name.getLayoutParams();
			params.width = parent.getWidth() - (int) (parent.getWidth() * 0.30); // _app._screenWidth;
			int margin = (int) (parent.getWidth() * 0.01);
			params.setMargins(margin, 0, margin, 0);
			name.setLayoutParams(params);
			//

			// set active button
			Button setactive = (Button) convertView
					.findViewById(R.id._activate);
			params = (LinearLayout.LayoutParams) setactive.getLayoutParams();
			params.width = (int) (parent.getWidth() * 0.25); // _app._screenWidth;
			params.setMargins(0, 0, margin * 2, 0);
			setactive.setLayoutParams(params);
			//

			// Delete
			Button del = (Button) convertView.findViewById(R.id._delete);
			params = (LinearLayout.LayoutParams) del.getLayoutParams();
			params.width = (int) (parent.getWidth() * 0.25); // _app._screenWidth;
			params.setMargins(0, 0, 0, 0);
			del.setLayoutParams(params);
			//

			// horizontalscrollview - pagination
			HorizontalScrollView hscroll = (HorizontalScrollView) convertView
					.findViewById(R.id.horizontalscrollview);
			hscroll.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (checkAddNewIsCompleted() == false) {
						if (swipeDeletePosition > 0) {
							notifyDataSetChanged();
							// refresh single row
							// int wantedPosition = swipeDeletePosition; //
							// Whatever position you're looking for
							// int firstPosition =
							// ((ListView)parent).getFirstVisiblePosition() -
							// ((ListView)parent).getHeaderViewsCount(); // This
							// is the same as child #0
							// int wantedChild = wantedPosition - firstPosition;
							// // Say, first visible position is 8, you want
							// position 10, wantedChild will now be 2
							// // So that means your view is child #2 in the
							// ViewGroup:
							// if (wantedChild < 0 || wantedChild >=
							// ((ListView)parent).getChildCount()) {
							// Log.w("",
							// "Unable to get view for desired position, because it's not being displayed on screen.");
							// return true;
							// }
							// // Could also check if wantedPosition is between
							// listView.getFirstVisiblePosition() and
							// listView.getLastVisiblePosition() instead.
							// View wantedView =
							// ((ListView)parent).getChildAt(wantedChild);
							swipeDeletePosition = -1;
							return true;
						}

						if (event.getAction() == MotionEvent.ACTION_UP
								|| event.getAction() == MotionEvent.ACTION_CANCEL) {
							int scrollX = v.getScrollX();
							int itemWidth = v.getMeasuredWidth();
							int activeItem = 0;
							if (scrollX > (itemWidth * 0.20)) {// = ((scrollX +
																// itemWidth /
																// 2) /
																// itemWidth);)
								activeItem = 1;
								swipeDeletePosition = position;
							}
							v.setTag(activeItem);
							int scrollTo = activeItem * itemWidth;
							// smoothScrollTo(scrollTo, 0);
							ObjectAnimator animator = ObjectAnimator.ofInt(v,
									"scrollX", scrollTo);
							animator.start();

							return true;
						} else {
							return false;
						}
					} else {
						return true;
					}
				}
			});
			//

			// list name
			cell._listNameButton = (Button) convertView
					.findViewById(R.id._listNameButton);
			cell._listNameButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listnameButtonPressed(listName);
				}
			});

			// activate
			cell._activate = (Button) convertView.findViewById(R.id._activate);
			cell._activate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setActiveButtonPressed(listName);
				}
			});

			// cell._layout.addView(cell._activate, layoutParams);

			// delete
			cell._delete = (Button) convertView.findViewById(R.id._delete);
			cell._delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					deleteButtonPressed(listName);
				}
			});
			// cell._layout.addView(cell._delete, layoutParams);

			// convertView.setTag(cell);
			// } else {
			// cell = (ListNameCell) convertView.getTag();
			// }

			cell._listNameButton.setText(listName.name);
			cell._listNameButton.setVisibility(View.VISIBLE);
			// cell._listNameButton.setOnClickListener(new
			// View.OnClickListener() {
			// public void onClick(View v) {
			// listnameButtonPressed(listName);
			// }
			// });

			cell._layout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					listnameButtonPressed(listName);
				}
			});

			// normal
			// cell._activate.setBackgroundResource(color.Shop_SetActive);
			// cell._activate.setBackgroundColor(drawable.shop_setactive);
			int sdk = android.os.Build.VERSION.SDK_INT;
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				cell._activate.setBackgroundDrawable(_context.getResources()
						.getDrawable(R.drawable.shop_setactive));
			} else {
				cell._activate.setBackground(_context.getResources()
						.getDrawable(R.drawable.shop_setactive));
			}

			cell._activate.setTextColor(Color.BLACK);
			cell._activate.setText("Set Active");

			String currentListId = _app.getActiveListId();
			if (currentListId != null && currentListId.equals(listName.listId)) {
				// active
				// cell._activate.setBackgroundResource(color.Shop_Active);
				if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					cell._activate
							.setBackgroundDrawable(_context.getResources()
									.getDrawable(R.drawable.shop_active));
				} else {
					cell._activate.setBackground(_context.getResources()
							.getDrawable(R.drawable.shop_active));
				}
				cell._activate.setTextColor(Color.WHITE);
				cell._activate.setText("Active");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return convertView;
	}

	// private void hideKeyboard(View text) {
	// InputMethodManager imm =
	// (InputMethodManager)_context.getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
	// }

	public void hideKeyboard() {
		View v = ((ShoppingScreen) _context).getWindow().getCurrentFocus();
		if (v != null) {
			InputMethodManager imm = (InputMethodManager) ((ShoppingScreen) _context)
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	public void addNewList() {
		try {
			if (addNewCell == null) {
				return;
			}
			String listName = addNewCell._addnew.getText().toString();
			if (listName == null || listName.equalsIgnoreCase("")) {
				TextDialog errorDialog = new TextDialog(_context,
						_app._dialogBackground, _app.getBoldFont(),
						_normalFont, "Input Error",
						"List Name field can't be blank");
				errorDialog.show();
				return;
			} else if (listName.length() < 4) {
				TextDialog errorDialog = new TextDialog(_context,
						_app._dialogBackground, _app.getBoldFont(),
						_normalFont, "Input Error",
						"List Name must contain at least 4 characters.");
				errorDialog.show();
				return;
			}

			cancelAddNew();
			((ShoppingScreen) _context).addNewList(listName);
		} catch (Exception ex) {

		}
	}

	public void cancelAddNew() {
		createNew = false;
		notifyDataSetChanged();
		hideKeyboard();
		addNewCell._addnew.setText("");
		addNewAlertIsPresent = false;
		swipeDeletePosition = -1;
	}

	public Boolean checkAddNewIsCompleted() {
		if (createNew == true) {
			if (addNewAlertIsPresent == false) {
				addNewAlertIsPresent = true;
				String message = "Do you want to save the list?";
				((ShoppingScreen) _handler).showTextDialog(_context, "",
						message, "addNewListConfirm", "addNewCancel");
			}
		}
		return createNew;
	}

	private void listnameButtonPressed(ShoppingListName listName) {
		try {
			// _handler.getClass().getMethod(_callback, ShoppingListName.class)
			// .invoke(_handler, listName);
			if (swipeDeletePosition >= 0) {
				swipeDeletePosition = -1;
				notifyDataSetChanged();
				return;
			}
			if (checkAddNewIsCompleted() == false) {
				try {
					// CLP SDK Menu opened - Create new list
					JSONObject data = new JSONObject();
					data.put("event_name", "MenuOpened");
					data.put("link_clicked", "List Open");
					data.put("event_time", _app.clpsdkObj.formatedCurrentDate());
					_app.clpsdkObj.updateAppEvent(data);
					//
				} catch (Exception e) {
					e.printStackTrace();
				}
				((ShoppingScreen) _handler).setActiveList(listName);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}


	}

	private void deleteButtonPressed(ShoppingListName listName) {
		try {
			// _handler.getClass().getMethod("deleteList",
			// ShoppingListName.class)
			// .invoke(_handler, listName);
			// ((ShoppingScreen)_handler).deleteList(listName);
			if (checkAddNewIsCompleted() == false) {
				((ShoppingScreen) _handler).deleteConfirmation(listName);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void setActiveButtonPressed(ShoppingListName listName) {
		try {
			// _handler.getClass().getMethod(_callback, ShoppingListName.class)
			// .invoke(_handler, listName);
			if (checkAddNewIsCompleted() == false) {
				((ShoppingScreen) _handler).changeActiveList(listName);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static class ListNameCell {
		LinearLayout _layout;
		// SizedImageTextButton _listNameButton;
		Button _listNameButton;
		Button _activate, _delete;
		ScrollView _horizontalScroller;
		EditText _addnew;
	}
}
