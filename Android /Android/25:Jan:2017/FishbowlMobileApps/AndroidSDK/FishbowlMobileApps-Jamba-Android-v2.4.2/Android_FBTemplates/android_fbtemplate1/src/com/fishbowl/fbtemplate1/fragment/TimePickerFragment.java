package com.fishbowl.fbtemplate1.fragment;

import java.util.Calendar;

import com.fishbowl.fbtemplate1.R;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Created by dj on 12/10/15.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener 
        {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) 
    {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
    {
      
    	EditText tv1=(EditText) getActivity().findViewById(R.id.editText2);
        tv1.setText("Hour: "+view.getCurrentHour()+" Minute: "+view.getCurrentMinute());

    }
}