package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreditCard;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Preferences.FBPreferences;
import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard.AddNewPaymentActivityGiftCard;
import com.olo.jambajuice.Adapters.GiftCardAdapters.CardListAdapater;
import com.olo.jambajuice.BusinessLogic.Interfaces.FBSDKLoginServiceCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.GiftCardInterFaces.PaymentSelectionInterfaceCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Services.IncommTokenService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.wearehathway.apps.incomm.Interfaces.InCommUserPaymentAccountCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommUserServiceCallBack;
import com.wearehathway.apps.incomm.Models.InCommUser;
import com.wearehathway.apps.incomm.Models.InCommUserPaymentAccount;
import com.wearehathway.apps.incomm.Services.InCommUserPaymentAccountService;
import com.wearehathway.apps.incomm.Services.InCommUserService;

import java.util.ArrayList;

public class ExistingSavedCreditCard extends GiftCardBaseActivity implements PaymentSelectionInterfaceCallBack {
    Context mContext;
    private RelativeLayout creditCardFooter;
    private RecyclerView recycler_view;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<InCommUserPaymentAccount> cardList = new ArrayList<InCommUserPaymentAccount>();
    private PaymentSelectionInterfaceCallBack interfaceCallBack;
    private SemiBoldButton addNewCCBtn;
    private SemiBoldTextView noListText;
    private boolean isFromReload = false;
    private Boolean isItFromAutoReloadScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_saved_credit_card);
        mContext = this;
        isShowBasketIcon = false;
        setUpToolBar(true);
        setBackButton(true,false);

        //Getting bundle
        isFromReload = getIntent().getBooleanExtra("isFromReload", false);

        creditCardFooter = (RelativeLayout) findViewById(R.id.creditCardFooter);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        addNewCCBtn = (SemiBoldButton) findViewById(R.id.addNewCCBtn);
        noListText = (SemiBoldTextView) findViewById(R.id.noListText);

        recycler_view.setHasFixedSize(true);

        interfaceCallBack = this;

        creditCardFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromReload", isFromReload);
                bundle.putBoolean("isItFromAutoReload", isItFromAutoReload());
                TransitionManager.transitFrom(ExistingSavedCreditCard.this, AddNewPaymentActivityGiftCard.class, bundle);
            }
        });


        initToolbar();
        if (isFromReload) {
            getReloadCardData();
        } else if (isItFromAutoReload()) {
            getAutoReloadCreditCardData();
        } else {
            getCreateCardData();
        }
    }

    private void initToolbar() {
        setTitle("My Cards");
        toolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.background_white));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(mContext,android.R.color.darker_gray));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Setting data based on bundle
        if (isFromReload) {
            setReloadCardData();
        } else if (isItFromAutoReload()) {
            setAutoReloadData();
        } else {
            setCreateCardData();
        }
    }

    private Boolean isItFromAutoReload() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isItFromAutoReloadScreen = bundle.getBoolean("FromAutoReload");
            if (isItFromAutoReloadScreen) {
                return isItFromAutoReloadScreen;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void setAutoReloadData() {
        if (GiftCardDataManager.getInstance().getAccountList() != null) {
            cardList = new ArrayList<InCommUserPaymentAccount>(GiftCardDataManager.getInstance().getAccountList());
            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            recycler_view.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new CardListAdapater(cardList, interfaceCallBack, 0);
            recycler_view.setAdapter(mAdapter);

            if (GiftCardDataManager.getInstance().getAutoReloadLocalPaymentInfo().getCreditCardNumber() != null) {
                addNewCCBtn.setText("Edit Credit Card");
            } else {
                addNewCCBtn.setText("Add New Credit Card");
            }
            if (cardList.size() > 0) {
                noListText.setVisibility(View.GONE);
            } else {
                noListText.setVisibility(View.VISIBLE);
            }
            enableScreen(true);
        }
        if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder() != null) {
            if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getPaymentAccountId() != 0) {
                if (GiftCardDataManager.getInstance().getAccountList() != null && GiftCardDataManager.getInstance().getAccountList().size() > 0) {
                    for (int i = 0; i < GiftCardDataManager.getInstance().getAccountList().size(); i++) {
                        if (GiftCardDataManager.getInstance().getInCommAutoReloadSubmitOrder().getPaymentAccountId() == GiftCardDataManager.getInstance().getAccountList().get(i).getId()) {
                            addNewCCBtn.setText("Add New Credit Card");
                            cardList = new ArrayList<InCommUserPaymentAccount>(GiftCardDataManager.getInstance().getAccountList());
                            // use a linear layout manager
                            mLayoutManager = new LinearLayoutManager(this);
                            recycler_view.setLayoutManager(mLayoutManager);

                            // specify an adapter (see also next example)
                            mAdapter = new CardListAdapater(cardList, interfaceCallBack, cardList.get(i).getId());
                            recycler_view.setAdapter(mAdapter);

                            enableScreen(true);
                        }
                    }
                }
            } else if (GiftCardDataManager.getInstance().getAutoReloadLocalPaymentInfo() != null) {
                if (GiftCardDataManager.getInstance().getAutoReloadLocalPaymentInfo().getCreditCardNumber() != null) {
                    addNewCCBtn.setText("Edit Credit Card");
                } else {
                    addNewCCBtn.setText("Add New Credit Card");
                }
            } else {
                addNewCCBtn.setText("Add New Credit Card");
            }
        }
    }

    private void setCreateCardData() {
        if (GiftCardDataManager.getInstance().getAccountList() != null) {
            cardList = new ArrayList<InCommUserPaymentAccount>(GiftCardDataManager.getInstance().getAccountList());
            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            recycler_view.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new CardListAdapater(cardList, interfaceCallBack, 0);
            recycler_view.setAdapter(mAdapter);
            GiftCardDataManager.getInstance().setPaymentAccountID(0);

            if (GiftCardDataManager.getInstance().getLocalPaymentInfo().getCreditCardNumber() != null) {
                addNewCCBtn.setText("Edit Credit Card");
            } else {
                addNewCCBtn.setText("Add New Credit Card");
            }
            if (cardList.size() > 0) {
                noListText.setVisibility(View.GONE);
            } else {
                noListText.setVisibility(View.VISIBLE);
            }
            enableScreen(true);
        }
        if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
            if (GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo() != null) {
                if (GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getPaymentAccountId() != 0) {
                    if (GiftCardDataManager.getInstance().getAccountList() != null && GiftCardDataManager.getInstance().getAccountList().size() > 0) {
                        for (int i = 0; i < GiftCardDataManager.getInstance().getAccountList().size(); i++) {
                            if (GiftCardDataManager.getInstance().getGiftCardCreate().getPaymentInfo().getPaymentAccountId() == GiftCardDataManager.getInstance().getAccountList().get(i).getId()) {
                                addNewCCBtn.setText("Add New Credit Card");
                                cardList = new ArrayList<InCommUserPaymentAccount>(GiftCardDataManager.getInstance().getAccountList());
                                // use a linear layout manager
                                mLayoutManager = new LinearLayoutManager(this);
                                recycler_view.setLayoutManager(mLayoutManager);

                                // specify an adapter (see also next example)
                                mAdapter = new CardListAdapater(cardList, interfaceCallBack, cardList.get(i).getId());
                                recycler_view.setAdapter(mAdapter);

                                enableScreen(true);
                            }
                        }
                    }
                } else if (GiftCardDataManager.getInstance().getLocalPaymentInfo() != null) {
                    if (GiftCardDataManager.getInstance().getLocalPaymentInfo().getCreditCardNumber() != null) {
                        addNewCCBtn.setText("Edit Credit Card");
                    } else {
                        addNewCCBtn.setText("Add New Credit Card");
                    }
                } else {
                    addNewCCBtn.setText("Add New Credit Card");
                }
            }
        }
    }

    private void setReloadCardData() {
        if (GiftCardDataManager.getInstance().getAccountList() != null) {
            cardList = new ArrayList<InCommUserPaymentAccount>(GiftCardDataManager.getInstance().getAccountList());
            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            recycler_view.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new CardListAdapater(cardList, interfaceCallBack, 0);
            recycler_view.setAdapter(mAdapter);
            GiftCardDataManager.getInstance().setPaymentAccountID(0);

            if (GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().getCreditCardNumber() != null) {
                addNewCCBtn.setText("Edit Credit Card");
            } else {
                addNewCCBtn.setText("Add New Credit Card");
            }
            if (cardList.size() > 0) {
                noListText.setVisibility(View.GONE);
            } else {
                noListText.setVisibility(View.VISIBLE);
            }
            enableScreen(true);
        }
        if (GiftCardDataManager.getInstance().getGiftCardReload() != null) {
            if (GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo() != null) {
                if (GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getPaymentAccountId() != 0) {
                    if (GiftCardDataManager.getInstance().getAccountList() != null && GiftCardDataManager.getInstance().getAccountList().size() > 0) {
                        for (int i = 0; i < GiftCardDataManager.getInstance().getAccountList().size(); i++) {
                            if (GiftCardDataManager.getInstance().getGiftCardReload().getPaymentInfo().getPaymentAccountId() == GiftCardDataManager.getInstance().getAccountList().get(i).getId()) {
                                addNewCCBtn.setText("Add New Credit Card");
                                cardList = new ArrayList<InCommUserPaymentAccount>(GiftCardDataManager.getInstance().getAccountList());
                                // use a linear layout manager
                                mLayoutManager = new LinearLayoutManager(this);
                                recycler_view.setLayoutManager(mLayoutManager);

                                // specify an adapter (see also next example)
                                mAdapter = new CardListAdapater(cardList, interfaceCallBack, cardList.get(i).getId());
                                recycler_view.setAdapter(mAdapter);

                                enableScreen(true);
                            }
                        }
                    }
                } else if (GiftCardDataManager.getInstance().getReloadLocalPaymentInfo() != null) {
                    if (GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().getCreditCardNumber() != null) {
                        addNewCCBtn.setText("Edit Credit Card");
                    } else {
                        addNewCCBtn.setText("Add New Credit Card");
                    }
                } else {
                    addNewCCBtn.setText("Add New Credit Card");
                }
            }
        }
    }

    private void getCreateCardData() {
        enableScreen(false);
        loaderText("Retrieving your saved payments...");
        String userId = "";
        if(GiftCardDataManager.getInstance().getInCommUser() != null) {
            userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
            InCommUserPaymentAccountService.getAllUserPaymentAccount(userId, new InCommUserPaymentAccountCallback() {
                @Override
                public void onPaymentAccountListServiceCallback(ArrayList<InCommUserPaymentAccount> accountList, Exception exception) {
                    if (exception == null) {
                        if (accountList != null) {
                            if (accountList.size() > 0) {
                                GiftCardDataManager.getInstance().setAccountList(accountList);
                                setCreateCardData();
                            } else {
                                enableScreen(true);
                                noListText.setVisibility(View.VISIBLE);
                                if (GiftCardDataManager.getInstance().getLocalPaymentInfo().getCreditCardNumber() != null) {
                                    addNewCCBtn.setText("Edit Credit Card");
                                } else {
                                    addNewCCBtn.setText("Add New Credit Card");
                                }
                            }
                        } else {
                            enableScreen(true);
                            noListText.setVisibility(View.VISIBLE);
                            if (GiftCardDataManager.getInstance().getLocalPaymentInfo().getCreditCardNumber() != null) {
                                addNewCCBtn.setText("Edit Credit Card");
                            } else {
                                addNewCCBtn.setText("Add New Credit Card");
                            }
                        }
                    } else if (Utils.getErrorCode(exception) == Constants.InCommFailure_Unauthorized || Utils.getVolleyErrorDescription(exception).contains(Constants.VolleyFailure_UnAuthorizedMessage)) {
                        enableScreen(false);
                        IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                            @Override
                            public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                                enableScreen(true);
                                if (successFlag) {
                                    DataManager.getInstance().setInCommToken(tokenSummary);
                                    ((JambaApplication) mContext.getApplicationContext()).initializeInCommSDK();
                                    enableScreen(false);
                                    getCreateCardData();
                                }
                            }
                        });
                    } else {
                        enableScreen(true);
                        if (Utils.getErrorDescription(exception) != null) {
                            alertWithTryAgain(mContext, "Failure", Utils.getErrorDescription(exception), "giftCardCreate");
                        } else {
                            alertWithTryAgain(mContext, "Failure", "There is some problem is loading your credit cards", "giftCardCreate");
                        }
                    }
                }
            });
        }else{
            getIncommUser();
        }
    }

    private void getIncommUser() {
        enableScreen(false);
        InCommUserService.getAccessTokenWithUserId(new InCommUserServiceCallBack() {
            @Override
            public void onUserServiceCallback(InCommUser inCommUser, Exception exception) {
                enableScreen(true);
                if (inCommUser != null) {
                    GiftCardDataManager.getInstance().setInCommUser(inCommUser);
                    getCreateCardData();
                } else if (exception != null) {
                    if (Utils.getErrorDescription(exception) != null) {
                        Utils.alert(mContext, "Failure", Utils.getErrorDescription(exception));
                    } else {
                        Utils.alert(mContext, "Failure", "Unexpected error occurred while processing your request. Please try again.");
                    }
                } else {
                    Utils.alert(mContext, "Failure", "Unexpected error occurred while processing your request. Please try again.");
                }
            }
        });
    }

    private void getReloadCardData() {
        enableScreen(false);
        loaderText("Retrieving your saved payments...");
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        InCommUserPaymentAccountService.getAllUserPaymentAccount(userId, new InCommUserPaymentAccountCallback() {
            @Override
            public void onPaymentAccountListServiceCallback(ArrayList<InCommUserPaymentAccount> accountList, Exception exception) {
                if (exception == null) {
                    if (accountList != null) {
                        if (accountList.size() > 0) {
                            GiftCardDataManager.getInstance().setAccountList(accountList);
                            setReloadCardData();
                        } else {
                            enableScreen(true);
                            noListText.setVisibility(View.VISIBLE);
                            if (GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().getCreditCardNumber() != null) {
                                addNewCCBtn.setText("Edit Credit Card");
                            } else {
                                addNewCCBtn.setText("Add New Credit Card");
                            }
                        }
                    } else {
                        enableScreen(true);
                        noListText.setVisibility(View.VISIBLE);
                        if (GiftCardDataManager.getInstance().getReloadLocalPaymentInfo().getCreditCardNumber() != null) {
                            addNewCCBtn.setText("Edit Credit Card");
                        } else {
                            addNewCCBtn.setText("Add New Credit Card");
                        }
                    }
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
                                getReloadCardData();
                            }
                        }
                    });
                }
                else {
                    enableScreen(true);
                    if (Utils.getErrorDescription(exception) != null) {
                        alertWithTryAgain(mContext, "Failure", Utils.getErrorDescription(exception), "reload");
                    } else {
                        alertWithTryAgain(mContext, "Failure", "There is some problem is loading your credit cards", "reload");
                    }
                }
            }
        });
    }

    private void getAutoReloadCreditCardData() {
        enableScreen(false);
        loaderText("Retrieving your saved payments...");
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        InCommUserPaymentAccountService.getAllUserPaymentAccount(userId, new InCommUserPaymentAccountCallback() {
            @Override
            public void onPaymentAccountListServiceCallback(ArrayList<InCommUserPaymentAccount> accountList, Exception exception) {
                if (exception == null) {
                    if (accountList != null) {
                        if (accountList.size() > 0) {
                            GiftCardDataManager.getInstance().setAccountList(accountList);
                            setAutoReloadData();
                        } else {
                            enableScreen(true);
                            noListText.setVisibility(View.VISIBLE);
                            if (GiftCardDataManager.getInstance().getAutoReloadLocalPaymentInfo().getCreditCardNumber() != null) {
                                addNewCCBtn.setText("Edit Credit Card");
                            } else {
                                addNewCCBtn.setText("Add New Credit Card");
                            }
                        }
                    } else {
                        enableScreen(true);
                        noListText.setVisibility(View.VISIBLE);
                        if (GiftCardDataManager.getInstance().getAutoReloadLocalPaymentInfo().getCreditCardNumber() != null) {
                            addNewCCBtn.setText("Edit Credit Card");
                        } else {
                            addNewCCBtn.setText("Add New Credit Card");
                        }
                    }
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
                                getAutoReloadCreditCardData();
                            }
                        }
                    });
                }
                else {
                    enableScreen(true);
                    if (Utils.getErrorDescription(exception) != null) {
                        alertWithTryAgain(mContext, "Failure", Utils.getErrorDescription(exception), "autoReload");
                    } else {
                        alertWithTryAgain(mContext, "Failure", "There is some problem is loading your credit cards", "autoReload");
                    }
                }
            }
        });
    }

    private void alertWithTryAgain(final Context context, String Title, final String Message, final String fromWhere) {
        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (fromWhere.equalsIgnoreCase("giftCardCreate")) {
                    getCreateCardData();
                } else if (fromWhere.equalsIgnoreCase("reload")) {
                    getReloadCardData();
                } else if (fromWhere.equalsIgnoreCase("autoReload")) {
                    getAutoReloadCreditCardData();
                }
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

    @Override
    public void onSelection(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", pos);
        bundle.putBoolean("isItFromAutoReloadScreen", isItFromAutoReload());
        bundle.putBoolean("isFromReload", isFromReload);
        TransitionManager.transitFrom(ExistingSavedCreditCard.this, PaymentDetailsActivityGiftCard.class, bundle);
    }
}
