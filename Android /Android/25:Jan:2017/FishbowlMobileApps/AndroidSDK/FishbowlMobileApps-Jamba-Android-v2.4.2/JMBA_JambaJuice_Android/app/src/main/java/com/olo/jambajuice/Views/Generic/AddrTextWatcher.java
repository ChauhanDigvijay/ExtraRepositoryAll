package com.olo.jambajuice.Views.Generic;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.olo.jambajuice.R;

/**
 * Created by vt02 on 11/8/16.
 */

public class AddrTextWatcher implements TextWatcher
{
    int mStart = 0;
    EditText editText;

    public AddrTextWatcher(EditText editText)
    {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        mStart = start + count;
    }

    @Override
    public void afterTextChanged(Editable s)
    {
        String capitalizedText = editText.getText().toString();
        if(capitalizedText.equals("")){
            return;
        }
        switch (editText.getId()){
            case R.id.streetAddress1:

                if(!capitalizedText.matches("[0-9a-zA-Z :'/\"()-]+")) {
                   editText.setText(capitalizedText.substring(0,capitalizedText.length()-1));
                   editText.setSelection(mStart);
                }
                break;
            case R.id.streetAddress2:
                if(!capitalizedText.matches("[0-9a-zA-Z :'/\"()-]+")) {
                    editText.setText(capitalizedText.substring(0,capitalizedText.length()-1));
                    editText.setSelection(mStart);
                }
                break;
            case R.id.city:
                if(!capitalizedText.matches("[a-zA-Z -]+")) {
                    editText.setText(capitalizedText.substring(0,capitalizedText.length()-1));
                    editText.setSelection(mStart);
                }
                break;
            default:
                break;
        }
    }
}

