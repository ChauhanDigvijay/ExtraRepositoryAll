package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.AddExistingGFCard;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard.NewCardActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard.ManageActivityGiftCard;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard.ReloadActivityGiftCard;
import com.olo.jambajuice.Adapters.GiftCardAdapters.MyGiftCardPageAdapter;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Services.IncommTokenService;
import com.olo.jambajuice.Fragments.GiftCardFragments.BottomsheetFragment;
import com.olo.jambajuice.Fragments.GiftCardFragments.MyGiftCardFragment;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldButton;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.squareup.picasso.Picasso;
import com.wearehathway.apps.incomm.Interfaces.InCommCardServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommGetAllCardServiceCallBack;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Services.InCommCardService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class GiftCardHomeListActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    public static GiftCardHomeListActivityGiftCard giftCardHomeListActivity;
    int footerHeight;
    BottomSheetDialogFragment bottomsheetFragment;
    private Context mContext;
    private SemiBoldButton btnPayAtCounter, btnAddOrBuyCards, btnManageAndReload;
    private RelativeLayout payAtCounterLayout, root_gift_card_home,manageAndReloadLayout,addOrBuyCardsLayout;
    private int pagerHeight, viewPagerClipWidth;
    private List<InCommCard> localInCommCardList;
    private ViewPager viewPager;
    private MyGiftCardPageAdapter adapter;
    private ImageButton CLAddButton, CLRefresh;
    private SemiBoldTextView tvBalanceAmount, dateAndTime, txtPageCount;
    private Calendar calendar;
    private int pos = 0;
    private float pagerPadding;
    private boolean isFromReload = false;

    //Pager Indicator declaration
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card_home_list);
        giftCardHomeListActivity = this;
        mContext = this;
        isShowBasketIcon = false;

        setUpToolBar(true,true);
        setBackButton(false,false);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(broadcastReceiver, new IntentFilter("BROADCAST_UPDATE_GF_HOME_ACTIVITY"));
        LocalBroadcastManager.getInstance(mContext).registerReceiver(broadcastReceiver, new IntentFilter("BROADCAST_GF_HOME_ACTIVITY_REFRESH_UI"));


        root_gift_card_home = (RelativeLayout) findViewById(R.id.root_gift_card_home);
        payAtCounterLayout = (RelativeLayout) findViewById(R.id.payAtCounterLayout);
//        manageAndReloadLayout = (RelativeLayout) findViewById(R.id.manageAndReloadLayout);
//        addOrBuyCardsLayout = (RelativeLayout) findViewById(R.id.addOrBuyCardsLayout);


        btnPayAtCounter = (SemiBoldButton) findViewById(R.id.btnPayAtCounter);
        btnAddOrBuyCards = (SemiBoldButton) findViewById(R.id.btnAddOrBuyCards);
        btnManageAndReload = (SemiBoldButton) findViewById(R.id.btnManageAndReload);

        CLAddButton = (ImageButton) findViewById(R.id.CLAddButton);
        CLRefresh = (ImageButton) findViewById(R.id.CLRefresh);

        tvBalanceAmount = (SemiBoldTextView) findViewById(R.id.tvBalanceAmount);
        dateAndTime = (SemiBoldTextView) findViewById(R.id.dateAndTime);

        //txtPageCount = (SemiBoldTextView) findViewById(R.id.txtPageCount);

        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        viewPager = (ViewPager) findViewById(R.id.cardViewPager);
        viewPager.addOnPageChangeListener(this);

        bottomsheetFragment = BottomsheetFragment.newInstance("Modal Bottom Sheet");

        btnPayAtCounter.setOnClickListener(this);
        CLAddButton.setOnClickListener(this);
        btnAddOrBuyCards.setOnClickListener(this);
        btnManageAndReload.setOnClickListener(this);
        CLRefresh.setOnClickListener(this);


        initToolbar();
        resizeView();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null)
            setIntent(intent);
    }


    private void resizeView() {
        root_gift_card_home.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {

                //Remove Listener
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    root_gift_card_home.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    root_gift_card_home.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                int width = 0;
                width = root_gift_card_home.getWidth();

                footerHeight = payAtCounterLayout.getHeight();

                viewPagerClipWidth = (int) ((0.10) * width);
                pagerPadding = Utils.convertPixelsToDp(viewPagerClipWidth, mContext);
                pagerHeight = (int) (0.63 * (width - pagerPadding * 2));

                if (GiftCardDataManager.getInstance().getUserAllCards() != null) {
                    if (GiftCardDataManager.getInstance().getUserAllCards().size() > 0) {
                        setUI(); // Setting only values from Data manager if it is already having all gift card details
                    } else {
                        getAllGiftCards(); //Getting all gift card details for this user by using service
                    }
                } else {
                    getAllGiftCards();//Getting all gift card details for this user by using service
                }

            }
        });

    }

    private void initToolbar() {
        setTitle("Jamba Cards");
        toolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(mContext, android.R.color.darker_gray));
    }

    //Getting all gift card details for this user by using service
    private void getAllGiftCards() {
        enableScreen(false);
        loaderText("Refreshing...");
        if(GiftCardDataManager.getInstance().getInCommUser() != null) {
            String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
            InCommCardService.getAllCard(userId, new InCommGetAllCardServiceCallBack() {
                @Override
                public void onGetAllCardServiceCallback(List<InCommCard> allCards, Exception exception) {
                    enableScreen(true);
                    if (exception == null) {
                        if (allCards != null && allCards.size() > 0) {
                            HashMap<Integer, InCommCard> inCommCardHashMap = new HashMap<Integer, InCommCard>();
                            for (int i = 0; i < allCards.size(); i++) {
                                if (allCards.get(i) != null) {
                                    inCommCardHashMap.put(allCards.get(i).getCardId(), allCards.get(i));
                                }
                            }
                            GiftCardDataManager.getInstance().setUserAllCards(new HashMap<Integer, InCommCard>(inCommCardHashMap));
                            GiftCardDataManager.getInstance().setInCommCards(new ArrayList<InCommCard>(allCards));
                            calendar = Calendar.getInstance();
                            String date = convertDate(calendar.getTime());
                            String time = convertTime(calendar.getTime());
                            DataManager.getInstance().setLastCheckedDateAndtime(date + " at " + time);
                            setUI();
                        } else {
                            enableScreen(true);
                            TransitionManager.transitFrom(GiftCardHomeListActivityGiftCard.this, NewCardActivityGiftCard.class);
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
                                    getAllGiftCards();
                                }

                            }
                        });
                    } else if (exception != null) {
                        enableScreen(true);
                        if (Utils.getErrorDescription(exception) != null) {
                            Utils.alert(mContext, null, Utils.getErrorDescription(exception));
                        } else {
                            Utils.alert(mContext, null, Utils.responseErrorNull());
                        }
                    }
                }
            });
        }
    }

    private void setUI() {
        try {
            //ViewPager
            List<Fragment> fragments = new ArrayList<>(getFragments());
            localInCommCardList = new ArrayList<>(GiftCardDataManager.getInstance().getInCommCards());

            if (localInCommCardList != null && localInCommCardList.size() > 0) {
                if (viewPager == null) {
                    viewPager = (ViewPager) findViewById(R.id.cardViewPager);
                } else {
                    viewPager.setAdapter(null);
                }
                adapter = new MyGiftCardPageAdapter(getSupportFragmentManager());
                RelativeLayout.LayoutParams viewPagerParams = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
                viewPagerParams.height = pagerHeight;
                viewPager.setLayoutParams(viewPagerParams);
                viewPager.setClipToPadding(false);
                viewPager.setPadding((int) pagerPadding, (int) pagerPadding, (int) pagerPadding, 0);
                viewPager.setClipToPadding(false);
                adapter.notifyDataSetChanged();
                viewPager.setAdapter(adapter);
                if (pos < fragments.size()) {
                    viewPager.setCurrentItem(pos);
                    //txtPageCount.setText(String.valueOf(pos + 1).concat("/").concat(String.valueOf(localInCommCardList.size())));
                    setUiPageViewController(pos);
                } else {
                    viewPager.setCurrentItem(0);
                    setUiPageViewController(0);
                    txtPageCount.setText(String.valueOf(1).concat("/").concat(String.valueOf(localInCommCardList.size())));
                }
                String balance = String.format("%.2f", GiftCardDataManager.getInstance().getUserAllCards().get(localInCommCardList.get(viewPager.getCurrentItem()).getCardId()).getBalance());
                tvBalanceAmount.setText(balance);
                dateAndTime.setText(DataManager.getInstance().getLastCheckedDateAndtime());
               // viewPager.getAdapter().notifyDataSetChanged();
                enableScreen(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }

    }
    //Set Pager Indicator
    private void setUiPageViewController(int pos) {
        dotsCount = adapter.getCount();
        pager_indicator.setVisibility(View.VISIBLE);
        if (dotsCount <= 1) {
            pager_indicator.removeAllViews();
            pager_indicator.setVisibility(View.INVISIBLE);
        }
        if (dotsCount > 10) {
            dotsCount = 10;
        }
        if (pos > 9) {
            pos = 9;
        }
        dots = new ImageView[dotsCount];
        if (pager_indicator != null) {
            pager_indicator.removeAllViews();
        }
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nonselecteditem_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            pager_indicator.addView(dots[i], params);
        }
        dots[pos].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selecteditem_dot));
    }


    //Hardcoded images
    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        localInCommCardList = new ArrayList<>(GiftCardDataManager.getInstance().getInCommCards());
        for (int i = 0; i < localInCommCardList.size(); i++) {
            fragments.add(MyGiftCardFragment.newInstance(GiftCardDataManager.getInstance().getUserAllCards().get(localInCommCardList.get(i).getCardId()), i));
        }
        return fragments;
    }

    private String convertDate(Date dateString) {
        SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = output.format(dateString);
        return formattedDate;
    }

    private String convertTime(Date dateString) {
        SimpleDateFormat output = new SimpleDateFormat("hh:mm a");
        String formattedTime = output.format(dateString);
        return formattedTime;
    }


    private void createDialog() {
        bottomsheetFragment.show(getSupportFragmentManager(), bottomsheetFragment.getTag());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CLAddButton:
                createDialog();
                break;
            case R.id.CLRefresh:
                pos = viewPager.getCurrentItem();
                refreshGiftCard();
                //getAllGiftCards();
                break;
            case R.id.btnPayAtCounter:
                Bundle payNow = new Bundle();
                payNow.putInt("cardId", localInCommCardList.get(viewPager.getCurrentItem()).getCardId());
                TransitionManager.slideUp(GiftCardHomeListActivityGiftCard.this, PayNowActivity.class, payNow);
                break;
//            case R.id.btnReload:
//                Bundle reload = new Bundle();
//                reload.putInt("cardId", localInCommCardList.get(viewPager.getCurrentItem()).getCardId());
//                reload.putInt("dataIndex", viewPager.getCurrentItem());
//                TransitionManager.slideUp(GiftCardHomeListActivityGiftCard.this, ReloadActivityGiftCard.class, reload);
//                break;
            case R.id.btnManageAndReload:
                Bundle bundle = new Bundle();
                bundle.putInt("cardId", localInCommCardList.get(viewPager.getCurrentItem()).getCardId());
                bundle.putInt("dataIndex", viewPager.getCurrentItem());
                TransitionManager.slideUp(GiftCardHomeListActivityGiftCard.this, ManageActivityGiftCard.class, bundle);
                break;
            case R.id.btnAddOrBuyCards:
                createDialog();
                break;
        }

    }

    private void refreshGiftCard() {
        String userId = GiftCardDataManager.getInstance().getInCommUser().getUserId();
        final int cardId = localInCommCardList.get(viewPager.getCurrentItem()).getCardId();
        enableScreen(false);
        InCommCardService.getCardInfo(userId, cardId, new InCommCardServiceCallback() {
            @Override
            public void onCardServiceCallback(InCommCard card, Exception exception) {
                if (card != null && exception == null) {
                    GiftCardDataManager.getInstance().getUserAllCards().get(cardId).setBalance(card.getBalance());
                    calendar = Calendar.getInstance();
                    String date = convertDate(calendar.getTime());
                    String time = convertTime(calendar.getTime());
                    DataManager.getInstance().setLastCheckedDateAndtime(date + " at " + time);
                    setUI();

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
                                refreshGiftCard();
                            }
                        }
                    });
                } else if (exception != null) {
                    if (Utils.getErrorDescription(exception) != null) {
                        enableScreen(true);
                        Utils.alert(mContext,"Failure",Utils.getErrorDescription(exception));
                        getAllGiftCards();
                    } else {
                        enableScreen(true);
                        Utils.alert(mContext,"Failure","Unexpected error occurred while processing your request.");
                        getAllGiftCards();
                    }
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        String balance = String.format("%.2f", GiftCardDataManager.getInstance().getUserAllCards().get(localInCommCardList.get(position).getCardId()).getBalance());
//        tvBalanceAmount.setText(balance);
//        txtPageCount.setText(String.valueOf(position+1) + "/" + localInCommCardList.size());
    }

    @Override
    public void onPageSelected(int position) {
        String balance = String.format("%.2f", GiftCardDataManager.getInstance().getUserAllCards().get(localInCommCardList.get(position).getCardId()).getBalance());
        tvBalanceAmount.setText(balance);
        //txtPageCount.setText(String.valueOf(position + 1) + "/" + localInCommCardList.size());

        int pos = position;
        if (pos > 9) {
            return;
        }
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nonselecteditem_dot));
        }
        dots[pos].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void handleUpdateGiftCardLists(Intent intent) {
        boolean position = intent.getBooleanExtra("pos", false);
        if (position) {
            pos = viewPager.getCurrentItem();
        } else {
            pos = 0;//show first card
        }
        getAllGiftCards();
    }

    @Override
    protected void handleUIRefreshGiftCardLists(Intent intent) {
        setUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFromReload = getIntent().getBooleanExtra("isFromReload", false);
        //If this screen navigated from ReloadActivity screen
        if (isFromReload || GiftCardDataManager.getInstance().isRefreshGiftCards()) {
            pos = viewPager.getCurrentItem();
            getAllGiftCards();
            getIntent().putExtra("isFromReload", false);//Resetting intent after refreshing gift cards
            GiftCardDataManager.getInstance().setRefreshGiftCards(false);
        }
    }
}
