//package com.google.android.gms.samples.wallet;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.wallet.Cart;
//import com.google.android.gms.wallet.CreateWalletObjectsRequest;
//import com.google.android.gms.wallet.FullWallet;
//import com.google.android.gms.wallet.FullWalletRequest;
//import com.google.android.gms.wallet.LineItem;
//import com.google.android.gms.wallet.MaskedWallet;
//import com.google.android.gms.wallet.MaskedWalletRequest;
//import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
//import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
//import com.google.android.gms.wallet.PaymentMethodTokenizationType;
//import com.google.android.gms.wallet.Wallet;
//import com.google.android.gms.wallet.WalletConstants;
//import com.google.android.gms.wallet.fragment.SupportWalletFragment;
//import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
//import com.google.android.gms.wallet.fragment.WalletFragmentMode;
//import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
//import com.google.android.gms.wallet.fragment.WalletFragmentStyle;
//
//public class MainActivity extends AppCompatActivity implements
//        GoogleApiClient.OnConnectionFailedListener{
//
//
//    private GoogleApiClient mGoogleApiClient;
//    private SupportWalletFragment mXmlWalletFragment;
//    private SupportWalletFragment mWalletFragment;
//
//    private MaskedWallet mMaskedWallet;
//    private FullWallet mFullWallet;
//
//
//
//    public static final int MASKED_WALLET_REQUEST_CODE=888;
//    public static final int FULL_WALLET_REQUEST_CODE=889;
//
//
//    public static final String WALLET_FRAGMENT_ID = "wallet_fragment";
//
//
//    GoogleApiClient googleApiClient;
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_main);
//
//        // Check if WalletFragment exists
//        mWalletFragment = (SupportWalletFragment) getSupportFragmentManager()
//                .findFragmentByTag(WALLET_FRAGMENT_ID);
//
//        if (mWalletFragment == null) {
//            // Wallet fragment style
//            WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
//                    .setBuyButtonText(WalletFragmentStyle.BuyButtonText.BUY_WITH)
//                    .setBuyButtonWidth(WalletFragmentStyle.Dimension.MATCH_PARENT);
//
//            // Wallet fragment options
//            WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
//                    .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
//                    .setFragmentStyle(walletFragmentStyle)
//                    .setTheme(WalletConstants.THEME_LIGHT)
//                    .setMode(WalletFragmentMode.BUY_BUTTON)
//                    .build();
//
//            // Initialize the WalletFragment
//            WalletFragmentInitParams.Builder startParamsBuilder =
//                    WalletFragmentInitParams.newBuilder()
//                            .setMaskedWalletRequest(generateMaskedWalletRequest())
//                            .setMaskedWalletRequestCode(MASKED_WALLET_REQUEST_CODE)
//                            .setAccountName("djusa51435@gmail.com");
//            mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);
//            mWalletFragment.initialize(startParamsBuilder.build());
//
//            // Add the WalletFragment to the UI
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.wallet_button_holder, mWalletFragment, WALLET_FRAGMENT_ID)
//                    .commit();
//        }
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addOnConnectionFailedListener(this)
//                .enableAutoManage(this, 0, this)
//                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
//                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
//                        .setTheme(WalletConstants.THEME_LIGHT)
//                        .build())
//                .build();
//
//        googleApiClient = createGoogleApiClient();
//
//
//    }
//
//    private GoogleApiClient createGoogleApiClient() {
//        return new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
//                        .setEnvironment(WalletConstants.ENVIRONMENT_PRODUCTION)
//                        .setTheme(WalletConstants.THEME_HOLO_DARK).build()).build();
//    }
//
//    private MaskedWalletRequest generateMaskedWalletRequest() {
//        // This is just an example publicKey for the purpose of this codelab.
//        // To learn how to generate your own visit:
//        // https://github.com/android-pay/androidpay-quickstart
//        String publicKey = "BO39Rh43UGXMQy5PAWWe7UGWd2a9YRjNLPEEVe+zWIbdIgALcDcnYCuHbmrrzl7h8FZjl6RCzoi5/cDrqXNRVSo=";
//        PaymentMethodTokenizationParameters parameters =
//                PaymentMethodTokenizationParameters.newBuilder()
//                        .setPaymentMethodTokenizationType(
//                                PaymentMethodTokenizationType.NETWORK_TOKEN)
//                        .addParameter("publicKey", publicKey)
//                        .build();
//
//        MaskedWalletRequest maskedWalletRequest =
//                MaskedWalletRequest.newBuilder()
//                        .setMerchantName("djusa51435@gmail.com")
//                        .setPhoneNumberRequired(true)
//                        .setShippingAddressRequired(true)
//                        .setCurrencyCode("USD")
//                        .setCart(Cart.newBuilder()
//                                .setCurrencyCode("USD")
//                                .setTotalPrice("10.00")
//                                .addLineItem(LineItem.newBuilder()
//                                        .setCurrencyCode("USD")
//                                        .setDescription("Google I/O Sticker")
//                                        .setQuantity("1")
//                                        .setUnitPrice("10.00")
//                                        .setTotalPrice("10.00")
//                                        .build())
//                                .build())
//                        .setEstimatedTotalPrice("15.00")
//                        .setPaymentMethodTokenizationParameters(parameters)
//                        .build();
//        return maskedWalletRequest;
//
//    }
//
//
//
//    @Override
//    public void onStart() {
//
//        super.onStart();
//        googleApiClient.connect();
//    }
//    protected void onStop() {
//        googleApiClient.disconnect();
//        super.onStop();
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case MASKED_WALLET_REQUEST_CODE:
//                switch (resultCode) {
//                    case RESULT_OK:
//                        mMaskedWallet =  data
//                                .getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
//                        Toast.makeText(this, "Got Masked Wallet", Toast.LENGTH_SHORT).show();
//                        break;
//                    case RESULT_CANCELED:
//                        // The user canceled the operation
//                        break;
//                    case WalletConstants.RESULT_ERROR:
//                        Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                break;
//
//
//            case FULL_WALLET_REQUEST_CODE:
//                switch (resultCode) {
//                    case RESULT_OK:
//                        mFullWallet = data
//                                .getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
//                        // Show the credit card number
//                        Toast.makeText(this,
//                                "Got Full Wallet, Done!",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case WalletConstants.RESULT_ERROR:
//                        Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                break;
//
//        }
//    }
//
//
//
//public static NotifyTransactionStatusRequest generateNotifyTransactionStatusRequest(String googleTranscationID,int status)
//    {
//        return NotifyTransactionStatusRequest.newBuilder()
//                .setGoogleTransactionId(googleTranscationID).setStatus(status).build();
//    }
//
//    private FullWalletRequest generateFullWalletRequest(String googleTransactionId) {
//        FullWalletRequest fullWalletRequest = FullWalletRequest.newBuilder()
//                .setGoogleTransactionId(googleTransactionId)
//                .setCart(Cart.newBuilder()
//                        .setCurrencyCode("USD")
//                        .setTotalPrice("10.10")
//                        .addLineItem(LineItem.newBuilder()
//                                .setCurrencyCode("USD")
//                                .setDescription("Google I/O Sticker")
//                                .setQuantity("1")
//                                .setUnitPrice("10.00")
//                                .setTotalPrice("10.00")
//                                .build())
//                        .addLineItem(LineItem.newBuilder()
//                                .setCurrencyCode("USD")
//                                .setDescription("Tax")
//                                .setRole(LineItem.Role.TAX)
//                                .setTotalPrice(".10")
//                                .build())
//                        .build())
//                .build();
//        return fullWalletRequest;
//    }
//
//
//
//    public void requestFullWallet(View view) {
//        if (mMaskedWallet == null) {
//            Toast.makeText(this, "No masked wallet, can't confirm", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Wallet.Payments.loadFullWallet(mGoogleApiClient,
//                generateFullWalletRequest(mMaskedWallet.getGoogleTransactionId()),
//                FULL_WALLET_REQUEST_CODE);
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//    }
//}
