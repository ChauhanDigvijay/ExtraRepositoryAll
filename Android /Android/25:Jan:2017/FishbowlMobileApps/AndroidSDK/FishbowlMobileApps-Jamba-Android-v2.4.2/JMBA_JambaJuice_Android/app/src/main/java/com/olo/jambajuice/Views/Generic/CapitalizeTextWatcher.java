package com.olo.jambajuice.Views.Generic;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.olo.jambajuice.Utils.Utils;

/**
 * Created by Nauman Afzaal on 25/06/15.
 */
public class CapitalizeTextWatcher implements TextWatcher {
    int mStart = 0;
    EditText editText;

    public CapitalizeTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mStart = start + count;
    }

    @Override
    public void afterTextChanged(Editable s) {
        String capitalizedText = Utils.capFirstLetter(editText.getText().toString());

        if(capitalizedText==null || capitalizedText.equals("")){
            return;
        }
        if(!capitalizedText.matches("[a-zA-Z]+")) {
            editText.setText(capitalizedText.substring(0,capitalizedText.length()-1));
            editText.setSelection(mStart);
        }
        else if (!capitalizedText.equals(editText.getText().toString()))
        {
                editText.setText(capitalizedText);
                editText.setSelection(mStart);
        }
    }
}
