package com.olo.jambajuice.Activites.NonGeneric.OrderAhead.OrderType;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.DeliveryAddress;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.StringUtilities;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;

public class AddDeliveryAddressActivity extends BaseActivity implements View.OnClickListener {
    EditText streetAddress, buildingName, city, zip, other;
    EditText contactNumber;
    int addressId;
    Button apply;
    Boolean isAddNew = false;
    int selectedPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_address);

        Intent intent = getIntent();
        isAddNew = intent.getBooleanExtra("IsAddNew", false);
        selectedPos = intent.getIntExtra("pos", -1);
        setUpView();
        if (selectedPos != -1) {
            setText();
        }
        enableOrDisableEditText();
    }

    private void setText() {
        DeliveryAddress deliveryAddress = DataManager.getInstance().getDeliveryAddresses().get(selectedPos);
        buildingName.setText(deliveryAddress.getBuilding());
        streetAddress.setText(deliveryAddress.getStreetaddress());
        city.setText(deliveryAddress.getCity());
        zip.setText(deliveryAddress.getZipcode());
        contactNumber.setText(deliveryAddress.getPhonenumber());
        contactNumber.setVisibility(View.GONE);
        other.setText(deliveryAddress.getSpecialinstructions());
        addressId = deliveryAddress.getId();
    }

    private void enableOrDisableEditText() {
        if (isAddNew) {
            streetAddress.setEnabled(true);
            buildingName.setEnabled(true);
            city.setEnabled(true);
            zip.setEnabled(true);
            other.setEnabled(true);
            contactNumber.setEnabled(true);
        } else {
            streetAddress.setEnabled(false);
            buildingName.setEnabled(false);
            city.setEnabled(false);
            zip.setEnabled(false);
            other.setEnabled(false);
            contactNumber.setEnabled(false);
        }
    }

    private void setUpView() {
        isShowBasketIcon = false;
        createToolBar();
        streetAddress = (EditText) findViewById(R.id.streetAddress);
        buildingName = (EditText) findViewById(R.id.buildingName);
        city = (EditText) findViewById(R.id.city);
        zip = (EditText) findViewById(R.id.zip);
        other = (EditText) findViewById(R.id.other);
        apply = (Button) findViewById(R.id.tv_apply);
        contactNumber = (EditText) findViewById(R.id.contactNumber);
        if (isAddNew) {
            contactNumber.setText(UserService.getUser().getContactnumber());
        }
        apply.setOnClickListener(this);
    }

    private void createToolBar() {
        setUpToolBar(true, true);
        if(isAddNew) {
            setTitle("New Address", getResources().getColor(android.R.color.white));
        }else{
            setTitle("Delivery Address", getResources().getColor(android.R.color.white));
        }
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
        setBackButton(false, true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_apply:
                saveAddress();
                break;
        }
    }

    private void enableEditing() {
        streetAddress.setEnabled(true);
        buildingName.setEnabled(true);
        city.setEnabled(true);
        zip.setEnabled(true);
        other.setEnabled(true);
        contactNumber.setEnabled(true);
    }

    private void setDeliveryAddress() {
        Basket basket = DataManager.getInstance().getCurrentBasket();

        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress.setBuilding(buildingName.getText().toString());
        deliveryAddress.setCity(city.getText().toString());
        deliveryAddress.setZipcode(zip.getText().toString());
        deliveryAddress.setStreetaddress(streetAddress.getText().toString());
        deliveryAddress.setSpecialinstructions(other.getText().toString());
        deliveryAddress.setPhonenumber(contactNumber.getText().toString());
        deliveryAddress.setId(addressId);

        enableScreen(false);
        BasketService.dispatchAddress(AddDeliveryAddressActivity.this, deliveryAddress, basket.getId(), new BasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(Basket basket, Exception e) {
                enableScreen(true);
                if (e != null) {
                    Utils.showErrorAlert(AddDeliveryAddressActivity.this, e);
                } else {
                    DataManager.getInstance().setDeliveryAddresses(null);
                    showAlert(AddDeliveryAddressActivity.this,"Estimated delivery time is "+basket.getLeadtimeestimateminutes()+" mins","Message");
                   // TransitionManager.transitFrom(AddDeliveryAddressActivity.this, BasketActivity.class, true);
                }
            }
        });
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

    private void saveAddress() {
        String addrStr = streetAddress.getText().toString();
        String cityStr = city.getText().toString();
        String zipStr = zip.getText().toString();

        boolean isTransitionAllowed = true;
        if (!StringUtilities.isValidString(addrStr)) {
            streetAddress.requestFocus();
            streetAddress.setError("Enter valid street address");
            isTransitionAllowed = false;
        }
        if (!StringUtilities.isValidString(cityStr)) {
            city.requestFocus();
            city.setError("Enter valid city");
            isTransitionAllowed = false;
        }
        if (!StringUtilities.isValidString(zipStr)) {
            if (isTransitionAllowed) {
                zip.requestFocus();
            }
            zip.setError("Enter valid zip code.");
            isTransitionAllowed = false;
        }

        if (isTransitionAllowed) {
            setDeliveryAddress();
        }
    }
}
