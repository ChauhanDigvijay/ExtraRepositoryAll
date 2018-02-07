package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Adapters.GiftCardAdapters.TransactionHistoryAdapter;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Services.IncommTokenService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.incomm.Interfaces.InCommTransactionHistoryCallback;
import com.wearehathway.apps.incomm.Models.InCommCardTransactionHistory;
import com.wearehathway.apps.incomm.Models.InCommTransactionHistory;
import com.wearehathway.apps.incomm.Services.InCommTransactionHistoryService;

import java.util.List;

public class TransactionHistory extends GiftCardBaseActivity {

    Context mContext;
    int cardId;
    String userId;
    private RecyclerView transRecyclerListView;
    private List<InCommTransactionHistory> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        mContext = this;

        isShowBasketIcon = false;

        setUpToolBar(true);
        setBackButton(true,false);

        transRecyclerListView = (RecyclerView) findViewById(R.id.transRecyclerListView);
        //Set cardID and userID.
        cardId = getIntent().getIntExtra("cardId", 0);
        userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();

        initToolbar();
        getTransactionHistoryData();

    }

    private void initToolbar() {
        setTitle("Transaction History");
        toolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(mContext,android.R.color.darker_gray));
    }

    private void setLayout() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        transRecyclerListView.setLayoutManager(mLayoutManager);
        //Send api response to set adapter.
        RecyclerView.Adapter transactionAdapter = new TransactionHistoryAdapter(transactionList);
        transRecyclerListView.setAdapter(transactionAdapter);
        enableScreen(true);
    }

    private void getTransactionHistoryData() {
        enableScreen(false);
        loaderText("Retrieving transaction history....");
        InCommTransactionHistoryService.getAllTransactionHistories(userId, String.valueOf(cardId), new InCommTransactionHistoryCallback() {
            @Override
            public void onTransactionHistoryServiceCallback(InCommCardTransactionHistory inCommCardTransactionHistory, Exception exception) {
                enableScreen(true);
                if(Utils.getErrorCode(exception) == Constants.InCommFailure_GiftCardProcessorFailure){
                    Utils.alert(mContext, "Message", "There is no transaction history");
                }
                else if (inCommCardTransactionHistory != null && exception == null) {
                    //If exception is null set Adapter.
                    transactionList = inCommCardTransactionHistory.getInCommTransactionHistory();
                    setLayout();
                }else if (Utils.getErrorCode(exception) == Constants.InCommFailure_Unauthorized || Utils.getVolleyErrorDescription(exception).contains(Constants.VolleyFailure_UnAuthorizedMessage)) {
                    enableScreen(false);
                    IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                        @Override
                        public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                            enableScreen(true);
                            if (successFlag) {
                                DataManager.getInstance().setInCommToken(tokenSummary);
                                ((JambaApplication) mContext.getApplicationContext()).initializeInCommSDK();
                                enableScreen(false);
                                getTransactionHistoryData();
                            }
                        }
                    });
                }
                else if (exception != null) {
                    if (Utils.getErrorDescription(exception) != null) {
                        alertWithTryAgain(mContext, "Failure", Utils.getErrorDescription(exception));
                    } else {
                        alertWithTryAgain(mContext, "Failure", "There is some problem in Network Connection. Please Try Again.");
                    }
                } else {
                    alertWithTryAgain(mContext, "Failure", Utils.responseErrorNull());
                }
            }
        });
    }

    private void alertWithTryAgain(final Context context, String Title, final String Message) {
        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getTransactionHistoryData();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                onBackPressed();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

}
