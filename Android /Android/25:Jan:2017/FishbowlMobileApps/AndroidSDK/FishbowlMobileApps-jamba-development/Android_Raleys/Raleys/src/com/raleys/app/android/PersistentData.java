package com.raleys.app.android;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.raleys.app.android.models.AccountRequest;
import com.raleys.app.android.models.Login;
import com.raleys.app.android.models.ShoppingList;
import com.raleys.app.android.models.Store;

public class PersistentData implements Serializable {
	private static final long serialVersionUID = -7193466922565614670L;
	private transient Context _context;
	private String _fileName;
	public long _lastOfferUpdateTime;
	public long _lastProductCategoryCacheTime;
	public long _lastPromoCategoryCacheTime;
	public Login _login;
	public AccountRequest account;
	public Raleys _Raleys;

	public ArrayList<Store> _storeList;
	public String _currentListId;
	public String _activeListId;
	public ShoppingList _activeShoppingList;

	public PersistentData(Context context, String fileName) {
		_context = context;
		_fileName = fileName;
	}

	public void save() {
		try {
			FileOutputStream fos = _context.openFileOutput(_fileName,
					Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(this);
			os.close();
		} catch (Exception ex) {
			Log.e(getClass().getSimpleName(), "Failed to write " + _fileName
					+ ", " + ex.toString());
		}
	}

	public void refresh() {
		PersistentData data = null;

		try {
			FileInputStream fis = _context.openFileInput(_fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			data = (PersistentData) is.readObject();
			this._login = data._login;
			this._storeList = data._storeList;
			this._currentListId = data._currentListId;
			this._activeListId = data._activeListId;
			this._lastOfferUpdateTime = data._lastOfferUpdateTime;
			this._lastProductCategoryCacheTime = data._lastProductCategoryCacheTime;
			this._lastPromoCategoryCacheTime = data._lastPromoCategoryCacheTime;
			this._Raleys=data._Raleys;
			this.account=data.account;
			this._activeShoppingList = data._activeShoppingList;
			is.close();
		} catch (Exception ex) {
			Log.e(getClass().getSimpleName(), "Failed to read " + _fileName
					+ ", " + ex.toString());
		}
	}
}
