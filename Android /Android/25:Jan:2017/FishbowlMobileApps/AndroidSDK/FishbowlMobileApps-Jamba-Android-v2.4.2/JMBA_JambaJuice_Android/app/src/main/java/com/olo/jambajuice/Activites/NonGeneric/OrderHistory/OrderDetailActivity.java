package com.olo.jambajuice.Activites.NonGeneric.OrderHistory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fishbowl.basicmodule.Analytics.FBEventSettings;

import com.olo.jambajuice.Activites.Generic.BaseActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketActivity;
import com.olo.jambajuice.Activites.NonGeneric.OrderAhead.Basket.BasketFlagViewManager;
import com.olo.jambajuice.Activites.NonGeneric.Store.StoreLocator.StoreLocatorActivity;
import com.olo.jambajuice.Adapters.OrderDetailAdapter;
import com.olo.jambajuice.BusinessLogic.Analytics.AnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Analytics.JambaAnalyticsManager;
import com.olo.jambajuice.BusinessLogic.Interfaces.AllStoreMenuCallBack;
import com.olo.jambajuice.BusinessLogic.Interfaces.BasketServiceCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.FavoriteOrderCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.RecentOrderCallback;
import com.olo.jambajuice.BusinessLogic.Interfaces.StoreDetailCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.FavoriteOrder;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrder;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrderSummary;
import com.olo.jambajuice.BusinessLogic.Models.Store;
import com.olo.jambajuice.BusinessLogic.Services.BasketService;
import com.olo.jambajuice.BusinessLogic.Services.StoreService;
import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.TransitionManager;
import com.olo.jambajuice.Utils.Utils;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.wearehathway.apps.olo.Interfaces.OloBasketServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloOrderServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloRestaurantServiceCallback;
import com.wearehathway.apps.olo.Models.OloBasket;
import com.wearehathway.apps.olo.Models.OloOrderStatus;
import com.wearehathway.apps.olo.Models.OloRestaurant;
import com.wearehathway.apps.olo.Services.OloBasketService;
import com.wearehathway.apps.olo.Services.OloOrderService;
import com.wearehathway.apps.olo.Services.OloRestaurantService;

import java.util.List;
import java.util.Locale;

import static com.olo.jambajuice.Utils.Constants.GA_CATEGORY.ORDER_AHEAD;
import static com.olo.jambajuice.Utils.Constants.REQUEST_CODE_ASK_PERMISSIONS;

import static com.olo.jambajuice.Utils.Constants.GA_CATEGORY.ORDER_AHEAD;

/**
 * Created by Ihsanulhaq on 28/05/15.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener, BasketServiceCallback {

    String type, basketId;
    private OrderDetailAdapter adapter;
    private ListView listView;
    private Button favButton, orderAgainButton, cancelOrderButton;
    private RecentOrder recentOrder;
    private FavoriteOrder favoriteOrder;
    private Store recentOrderStore;
    private Bundle bundle;
    private Context context;
    private RelativeLayout favLayout;
    private TextView favename;
    private ProgressBar loadingOrderDetailBar;
    private SemiBoldTextView itemTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        context = this;
        setUpToolBar(true);
        setTitle("Order Detail");
        setBackButton(true, false);
        bundle = getIntent().getExtras();
        initComponents();
        setClickListeners();

        // getting type : recent or favorite
        type = bundle.getString("type");
        if (type != null && type.equalsIgnoreCase("favorite")) {
            favoriteOrder = (FavoriteOrder) getDataFromIntent();
            favLayout.setVisibility(View.VISIBLE);
            favename.setText(favoriteOrder.getName());
        } else {
            favLayout.setVisibility(View.GONE);
            recentOrder = (RecentOrder) getDataFromIntent();
            if (recentOrder.getProducts().size() > 1) {
                itemTitle.setText("Items Ordered");
            } else {
                itemTitle.setText("Item Ordered");
            }
        }
        if (recentOrder != null) {
            if (recentOrder.getDeliverymode().equalsIgnoreCase("dispatch")) {
                getDeliveryStatus();
            }
            getStoreInfo();

            if (recentOrder.getVendorextref() != null) {
                orderAgainButton.setVisibility(View.VISIBLE);
            } else {
                orderAgainButton.setVisibility(View.GONE);
            }

            checkStatus();
        }
        if (favoriteOrder != null) {
            getFavoriteInfo();
            cancelOrderButton.setVisibility(View.GONE);
        }
    }

    private void checkStatus() {
        if (recentOrder.iseditable()) {
            cancelOrderButton.setVisibility(View.VISIBLE);
        } else {
            cancelOrderButton.setVisibility(View.GONE);
        }
    }

    // getting delivery status for user view
    private void getDeliveryStatus() {
        UserService.getDeliveryStatus(recentOrder.getId(), new RecentOrderCallback() {
            @Override
            public void onOrderCallback(List<RecentOrder> status, Exception e) {
                if (e == null) {
                    setDeliveryValues(status.get(0));
                } else {
                    Utils.showErrorAlert(OrderDetailActivity.this, e);
                }
            }
        });
    }

    private void setDeliveryValues(RecentOrder neworder) {
        RecentOrderSummary sum = neworder.getSummary();
        recentOrder.getSummary().setDeliveryid(sum.getDeliveryid());
        recentOrder.getSummary().setDrivername(sum.getDrivername());
        recentOrder.getSummary().setDeliveryservice(sum.getDeliveryservice());
        recentOrder.getSummary().setDeliverystatus(sum.getDeliverystatus());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            recentOrder.getSummary().setDriverphonenumber(PhoneNumberUtils.formatNumber(sum.getDriverphonenumber(), "US"));
        } else {
            //Deprecated method
            recentOrder.getSummary().setDriverphonenumber(PhoneNumberUtils.formatNumber(sum.getDriverphonenumber()));
        }
        // recentOrder.getSummary().setDriverphonenumber(PhoneNumberUtils.formatNumber(sum.getDriverphonenumber()));

    }

    private void setClickListeners() {
        favButton.setOnClickListener(this);
        orderAgainButton.setOnClickListener(this);
        cancelOrderButton.setOnClickListener(this);
    }

    //  setting recent adapter
    private void setAdapter() {
        adapter = new OrderDetailAdapter(this, recentOrder);
        listView.setAdapter(adapter);
    }

    //  setting favorite adapter
    private void setFavoriteAdapter() {
        adapter = new OrderDetailAdapter(this, recentOrder);
        listView.setAdapter(adapter);
    }

    private void initComponents() {
        loadingOrderDetailBar = (ProgressBar) findViewById(R.id.loadingOrderDetailBar);
        listView = (ListView) findViewById(R.id.list_details);
        favButton = (Button) findViewById(R.id.saveFavorite);
        orderAgainButton = (Button) findViewById(R.id.orderAgainBtn);
        cancelOrderButton = (Button) findViewById(R.id.cancelOrderBtn);
        favLayout = (RelativeLayout) findViewById(R.id.fav_layout);
        favename = (TextView) findViewById(R.id.favename);
        itemTitle = (SemiBoldTextView) findViewById(R.id.itemTitle);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveFavorite:
                if (type.equalsIgnoreCase("favorite")) {
                    removeFavorite();
                } else {
                    getBasketId();
                }
                break;
            case R.id.orderAgainBtn:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(context)) {
                        openDrawLayoutSettings(OrderDetailActivity.this);
                    } else {
                        addToBasket();
                    }
                } else {
                    addToBasket();
                }

                break;
            case R.id.cancelOrderBtn:
                showConfirmationAlert();
                break;

        }

    }

    private void showConfirmationAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to cancel this order?");
        alertDialogBuilder.setTitle("Cancel Order");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                cancelOrder();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(Dialog.BUTTON_NEGATIVE).setTypeface(null, Typeface.BOLD);
            }
        });
        alertDialog.show();
    }

    private void cancelOrder() {
        loadingOrderDetailBar.setVisibility(View.VISIBLE);
        BasketService.cancelOrder(this, recentOrder, new OloOrderServiceCallback() {
            @Override
            public void onOrderServiceCallback(final OloOrderStatus orderStatus, final Exception error) {
                loadingOrderDetailBar.setVisibility(View.GONE);
                if (error != null) {
                    Utils.showErrorAlert(OrderDetailActivity.this, error);
                }
                if (orderStatus != null) {
                    if (orderStatus.getStatus().equalsIgnoreCase("Canceled")) {
                        Utils.showAlert(context, "Order cancelled successfully.", "Success!");
                        recentOrder.setStatus(orderStatus.getStatus());
                        recentOrder.setIseditable(orderStatus.iseditable());
                        if (orderStatus.getDeliverymode().equalsIgnoreCase("dispatch")) {
                            loadingOrderDetailBar.setVisibility(View.VISIBLE);
                            UserService.getDeliveryStatus(orderStatus.getId(), new RecentOrderCallback() {
                                @Override
                                public void onOrderCallback(List<RecentOrder> status, Exception e) {
                                    loadingOrderDetailBar.setVisibility(View.GONE);
                                    if (e == null) {
                                        recentOrder.getSummary().setDeliverystatus(status.get(0).getSummary().getDeliverystatus());

                                    } else {
                                        recentOrder.getSummary().setDeliverystatus(orderStatus.getStatus());
                                    }
                                    setAdapter();
                                    adapter.setStoreInfo(recentOrderStore);
                                    checkStatus();
                                    DataManager.getInstance().setOrderCancelFlag(true);
                                }
                            });
                        } else {
                            setAdapter();
                            adapter.setStoreInfo(recentOrderStore);
                            checkStatus();
                            DataManager.getInstance().setOrderCancelFlag(true);
                        }

                    }
                }
            }
        });
    }

    private void addToBasket() {
        if (DataManager.getInstance().isBasketPresent()) {
            if (recentOrderStore != null) {
                if (recentOrderStore != null && DataManager.getInstance().getCurrentSelectedStore() != null && recentOrderStore.getRestaurantId() != DataManager.getInstance().getCurrentSelectedStore().getRestaurantId()) {
                    showDeleteBasketAndChangeStoreConfirmation();
                } else {
                    showDeleteBasketConfirmation();
                }
            } else {
                showDeleteBasketConfirmation();
            }
        } else {
            if (recentOrderStore != null) {
                if (recentOrderStore != null && DataManager.getInstance().getCurrentSelectedStore() != null && recentOrderStore.getRestaurantId() != DataManager.getInstance().getCurrentSelectedStore().getRestaurantId()) {
                    showStoreChangeAlert();
                } else {
                    orderAgain();
                }
            } else {
                orderAgain();
            }
        }
    }

    private void openDrawLayoutSettings(final Activity activity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("We need your permission to display Basket icon on top of this App. Please grant permission!");
        alertDialogBuilder.setTitle("Basket Permission");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, Constants.REQUEST_CODE_ASK_PERMISSIONS);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(context)) {
                    openDrawLayoutSettings(OrderDetailActivity.this);
                } else {
                    addToBasket();
                }
            } else {
                addToBasket();
            }
        }
    }

    private void showStoreChangeAlert() {
        String newStoreName = recentOrderStore.getName();
        if (newStoreName != null) {
            newStoreName = recentOrderStore.getName();
        } else {
            newStoreName = "";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Start a new order at " + newStoreName + "?");
        alertDialogBuilder.setTitle("Start Order");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                orderAgain();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // deleting basket and creating new basket and store information.
    private void showDeleteBasketAndChangeStoreConfirmation() {
        String newStoreName = recentOrderStore.getName();
        if (newStoreName != null) {
            newStoreName = " to ".concat(recentOrderStore.getName());
        } else {
            newStoreName = "";
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Starting a new order will empty the basket and change the store" + newStoreName + ". Continue?");
        alertDialogBuilder.setTitle("Start Order");
        alertDialogBuilder.setPositiveButton("Start Order", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                orderAgain();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // showing information to delete basket
    private void showDeleteBasketConfirmation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Re-ordering this order will delete the current basket. Continue?");
        alertDialogBuilder.setTitle("Empty Basket");
        alertDialogBuilder.setPositiveButton("Delete Basket", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                orderAgain();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void orderAgain() {
        final Activity activity = this;
        trackButtonWithName("Order Again");
        DataManager.getInstance().resetBasket();
        BasketFlagViewManager.getInstance().removeBasketFlag();
        enableScreen(false);

        // ordering from favorite or recent order detail
        if (type.equalsIgnoreCase("favorite")) {
            JambaAnalyticsManager.sharedInstance().track_ItemWith(favoriteOrder.getId(), "", FBEventSettings.ORDER_AGAIN);
            // creating basket from favorite
            BasketService.createBasketFromFavorite(this, favoriteOrder, new AllStoreMenuCallBack() {
                @Override
                public void onAllStoreMenuCallback(Exception exception) {
                    enableScreen(true);
                    if (exception == null) {
                        TransitionManager.transitFrom(activity, BasketActivity.class);
                    } else {
                        Utils.showErrorAlert(activity, exception);
                    }

                }

                @Override
                public void onAllStoreMenuErrorCallback(Exception exception) {
                    enableScreen(true);
                    if (exception != null) {
                        Utils.showErrorAlert(activity, exception);
                    }
                }
            });
        } else {

            JambaAnalyticsManager.sharedInstance().track_ItemWith(recentOrder.getId(), "", FBEventSettings.ORDER_AGAIN);
            AnalyticsManager.getInstance().trackEvent(ORDER_AHEAD.value, "order_again", recentOrder.getReference());
            // creating order from recent order
            BasketService.createBasketFromOrder(this, recentOrder, new AllStoreMenuCallBack() {
                @Override
                public void onAllStoreMenuCallback(Exception exception) {
                    enableScreen(true);
                    if (exception == null) {
                        TransitionManager.transitFrom(activity, BasketActivity.class);
                    } else {
                        Utils.showErrorAlert(activity, exception);
                    }
                }

                @Override
                public void onAllStoreMenuErrorCallback(Exception exception) {
                    enableScreen(true);
                    if (exception != null) {
                        Utils.showErrorAlert(activity, exception);
                    }
                }
            });
        }
    }

    // getting data which is received in bundle from previouse activity
    public Object getDataFromIntent() {
        if (bundle != null) {
            return bundle.getSerializable(Constants.B_ORDER_DETAIL);
        }
        return null;
    }

    @Override
    public void onBasketServiceCallback(Basket basket, Exception e) {
        enableScreen(true);
        if (e == null) {
            TransitionManager.transitFrom(this, BasketActivity.class);
        } else {
            Utils.showErrorAlert(this, e);
        }
    }

    @Override
    public void enableScreen(boolean isEnabled) {
        super.enableScreen(isEnabled);
        orderAgainButton.setEnabled(isEnabled);
        if (isEnabled) {
            orderAgainButton.setText("Order again");
        } else {
            orderAgainButton.setText("Please wait...");
        }
    }

    // getting store information datas
    public void getStoreInfo() {
        StoreService.getStoreInformation(recentOrder.getVendorextref(), new StoreDetailCallback() {
            @Override
            public void onStoreDetailCallback(final Store store, Exception exception) {
                loadingOrderDetailBar.setVisibility(View.GONE);
                if (store != null) {
                    setAdapter();
                    adapter.setStoreInfo(store);
                    recentOrderStore = store;
                } else {
                    Utils.showErrorAlert(OrderDetailActivity.this, exception);
                }
            }
        });
    }

    // getting favorite order information datas
    private void getFavoriteInfo() {
        favButton.setText("Remove Favorite");
        UserService.getFavoriteOrderDetail(favoriteOrder.getId(), new RecentOrderCallback() {
            @Override
            public void onOrderCallback(List<RecentOrder> status, Exception e) {
                loadingOrderDetailBar.setVisibility(View.GONE);
                if (e == null) {
                    recentOrder = status.get(0);
                    if (recentOrder.getProducts().size() > 1) {
                        itemTitle.setText("Items Ordered");
                    } else {
                        itemTitle.setText("Item Ordered");
                    }
                    loadingOrderDetailBar.setVisibility(View.VISIBLE);
                    getRestaurantById();
                } else {
                    Utils.showErrorAlert(OrderDetailActivity.this, e);
                }
            }
        });

    }

    // getting restaurant details for favorite using restaurant id
    private void getRestaurantById() {
        OloRestaurantService.getRestaurantById(Integer.parseInt(favoriteOrder.getVendorid()), new OloRestaurantServiceCallback() {
            @Override
            public void onRestaurantServiceCallback(OloRestaurant[] restaurants, Exception exception) {
                loadingOrderDetailBar.setVisibility(View.GONE);
                Store stores = null;
                if (exception == null) {
                    if (restaurants != null && restaurants.length > 0) {
                        stores = new Store(restaurants[0]);
                    }
                    setFavoriteAdapter();
                    adapter.setStoreInfo(stores);
                    recentOrderStore = stores;
                } else {
                    Utils.showErrorAlert(OrderDetailActivity.this, exception);
                }
            }
        });
    }

    // removing favorite and directing to history page
    private void removeFavorite() {

        // removing favorite from favorite orders
        UserService.removeFavoriteOrders(favoriteOrder.getId(), new FavoriteOrderCallback() {
            @Override
            public void onFavoriteCallback(List<FavoriteOrder> status, Exception e) {
                if (e == null) {
//                    if (UserService.favOrder != null) {
//                        UserService.favOrder.clear();
//                    }
//                    OrderHistoryActivity.getInstance().finish();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("from", "Detail");
//                    TransitionManager.transitFrom(OrderDetailActivity.this, OrderHistoryActivity.class, bundle);
                    if (DataManager.getInstance().getNewRecentOrder() != null && DataManager.getInstance().getNewRecentOrder().size() > 0) {
                        DataManager.getInstance().getNewRecentOrder().clear();
                    }
                    if (DataManager.getInstance().getFromFavorite() != null) {
                        DataManager.getInstance().setFromFavorite(true);
                    }
                    if (!((Activity) context).isFinishing()) {
                        showAlert(context, "Order successfully removed from favorites.", "Success");
                    }
                } else {
                    Utils.showErrorAlert(OrderDetailActivity.this, e);
                }
            }
        });
    }

    // adding favorite and redirecting to history page
    private void addFavorite() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lp.leftMargin = 40;
            lp.rightMargin = 40;
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setTitle("Alert Message");
            alertDialog.setMessage("Enter Favorite Name");
            alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    // adding favorites from recent orders
                    UserService.addFavoriteOrders(basketId, input.getText().toString(), new FavoriteOrderCallback() {
                        @Override
                        public void onFavoriteCallback(List<FavoriteOrder> status, Exception e) {
                            if (e == null) {
//                            if (UserService.favOrder != null) {
//                                UserService.favOrder.clear();
//                            }
//                            OrderHistoryActivity.getInstance().finish();
//                            Bundle bundle = new Bundle();
//                            bundle.putString("from", "Detail");
//                            TransitionManager.transitFrom(OrderDetailActivity.this, OrderHistoryActivity.class, bundle);
                                if (DataManager.getInstance().getNewRecentOrder() != null && DataManager.getInstance().getNewRecentOrder().size() > 0) {
                                    DataManager.getInstance().getNewRecentOrder().clear();
                                }
                                if (DataManager.getInstance().getFromFavorite() != null) {
                                    DataManager.getInstance().setFromFavorite(true);
                                }
                                if (!((Activity) context).isFinishing()) {
                                    showAlert(context, "Order added to favorites.", "Success");
                                }
                            } else {
                                Utils.showErrorAlert(OrderDetailActivity.this, e);
                            }
                        }
                    });
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog dialog = alertDialog.create();
            //To raise keyboard
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            dialog.show();
        } catch (Exception e) {
        }
    }

    public void showAlert(Context context, String message, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                onBackPressed();
            }
        });
        alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                onBackPressed();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // just creating order, getting basket id and using it to add favorite
    private void getBasketId() {
        OloBasketService.createFromOrder(recentOrder.getId(), recentOrder.getReference(), new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket oloBasket, Exception error) {
                if (oloBasket != null) {
                    basketId = oloBasket.getId();
                    addFavorite();
                } else {
                    Utils.showErrorAlert(OrderDetailActivity.this, error);
                }
            }
        });
    }

}
