package com.olo.jambajuice.Activites.NonGeneric.Settings;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.BillingAccountsCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.BillingAccount;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;
import com.wearehathway.apps.olo.Interfaces.OloServiceCallback;
import com.wearehathway.apps.olo.Services.OloBasketService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BillingAccountsActivity extends BaseActivity {

    List<BillingAccountHolder> cardViewHolderList;
    ArrayList<BillingAccount> savedBillingAccounts;
    Context context;
    LinearLayout listCardContainer;
    TextView noSavedCards;
    SemiBoldButton tv_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_accounts);
        context = this;
        initToolbar();
        setUpView();
        if(DataManager.getInstance().getCurrentBasket() != null) {
            getBillingAccountsAndAddAccountCards();
        }else{
            createBasketAndGetBillAccounts();
        }

    }

    private void createBasketAndGetBillAccounts(){
        if(DataManager.getInstance().getCurrentSelectedStore() != null) {
            enableScreen(false);
            int storeId = DataManager.getInstance().getCurrentSelectedStore().getRestaurantId();
            BasketService.createBasket(this, storeId, new BasketServiceCallback() {
                @Override
                public void onBasketServiceCallback(Basket basket, Exception e) {
                    enableScreen(true);
                    getBillingAccountsAndAddAccountCards();
                }
            });
        }
    }

    private void initToolbar() {
        isShowBasketIcon = false;
        setUpToolBar(true, true);
        setTitle("Cards On File");
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange_color));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(getResources().getColor(android.R.color.white));
        setBackButton(false, true);
    }

    private void setUpView() {
        listCardContainer = (LinearLayout) findViewById(R.id.list_cards_info);
        noSavedCards = (TextView) findViewById(R.id.noSavedCards);
        tv_done = (SemiBoldButton) findViewById(R.id.tv_done);
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getBillingAccountsAndAddAccountCards() {
        enableScreen(false);
        UserService.getBillingAccounts(new BillingAccountsCallback() {
            @Override
            public void onBillingAccountsCallback(ArrayList<BillingAccount> billingAccounts, Exception error) {
                enableScreen(true);
                if (billingAccounts != null && billingAccounts.size() > 0) {
                    savedBillingAccounts = new ArrayList<BillingAccount>(billingAccounts);
                    addExistingCards();
                } else {
                    if(error != null){
                        Utils.alert(context,"Error",Utils.getErrorDescription(error));
                    }
                    noSavedCards.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void addExistingCards() {
        boolean isSavedCardsAvailable = false;
        LayoutInflater layoutInflater = getLayoutInflater();
        cardViewHolderList = new ArrayList<>();

        if(savedBillingAccounts != null && savedBillingAccounts.size() > 0) {
            for (BillingAccount billingAccount : savedBillingAccounts) {
                if (!billingAccount.getAccounttype().equalsIgnoreCase("payinstore")) {
                    isSavedCardsAvailable = true;
                    View cardDetailView = layoutInflater.inflate(R.layout.row_billing_account_card, listCardContainer, false);
                    BillingAccountHolder cardViewHolder = new BillingAccountHolder(this, cardDetailView, billingAccount);
                    cardViewHolderList.add(cardViewHolder);
                    listCardContainer.addView(cardDetailView);
                }
            }
        }else{
            noSavedCards.setVisibility(View.VISIBLE);
        }

        if(!isSavedCardsAvailable){
            noSavedCards.setVisibility(View.VISIBLE);
        }
    }

    public void deleteCard(final BillingAccount billingAccount) {
        enableScreen(false);
        String successMessage = "Card removed successfully.";
        if (billingAccount.getAccounttype().equalsIgnoreCase("creditcard")) {
            successMessage = "Credit card removed successfully.";
        } else if (billingAccount.getAccounttype().equalsIgnoreCase("Jamba Cards")) {
            successMessage = "Jamba card removed successfully.";
        }
        final String finalSuccessMessage = successMessage;
        OloBasketService.deleteSavedBillingAccount(billingAccount.getAccountid(), new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                enableScreen(true);
                if (error == null && response == null) {
                    if (cardViewHolderList != null) {
                        cardViewHolderList.clear();
                    }
                    listCardContainer.removeAllViews();
                    savedBillingAccounts.remove(billingAccount);
                    addExistingCards();
                    Utils.showAlert(context, finalSuccessMessage, "Success!");
                } else {
                    Utils.showErrorAlert(BillingAccountsActivity.this, error);
                }
            }
        });
    }

    public void showDeleteCardConfirmationAlert(final BillingAccount billingAccount) {
        String title = "Remove Card";
        String message = "Are you sure you want to delete this saved card?";
        if (billingAccount.getAccounttype().equalsIgnoreCase("creditcard")) {
            title = "Remove Credit Card";
            message = "Are you sure you want to delete this saved credit card?";
        } else if (billingAccount.getAccounttype().equalsIgnoreCase("Jamba Cards")) {
            title = "Remove Jamba Card";
            message = "Are you sure you want to delete this saved jamba card?";
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                deleteCard(billingAccount);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
