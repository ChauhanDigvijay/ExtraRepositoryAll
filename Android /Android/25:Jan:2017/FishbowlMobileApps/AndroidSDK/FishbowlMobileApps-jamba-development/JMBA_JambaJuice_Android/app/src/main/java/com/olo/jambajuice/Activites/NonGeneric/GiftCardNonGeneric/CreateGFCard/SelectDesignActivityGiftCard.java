package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Adapters.GiftCardAdapters.ItemOffSetDecoration;
import com.olo.jambajuice.Adapters.GiftCardAdapters.RecyclerViewAdapter;
import com.olo.jambajuice.BusinessLogic.Interfaces.GiftCardInterFaces.TemplateSelectionInterfaces;
import com.olo.jambajuice.BusinessLogic.Interfaces.IncommTokenServiceCallback;
import com.olo.jambajuice.BusinessLogic.Managers.DataManager;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardCreate;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardTemplate;
import com.olo.jambajuice.BusinessLogic.Services.IncommTokenService;
import com.olo.jambajuice.JambaApplication;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Constants;
import com.olo.jambajuice.Utils.Utils;
import com.wearehathway.apps.incomm.Interfaces.InCommBrandsCallback;
import com.wearehathway.apps.incomm.Models.InCommBrand;
import com.wearehathway.apps.incomm.Models.InCommBrandCardImage;
import com.wearehathway.apps.incomm.Models.InCommCountry;
import com.wearehathway.apps.incomm.Models.InCommStates;
import com.wearehathway.apps.incomm.Services.InCommBrandService;

import java.util.ArrayList;
import java.util.List;

/*
    Created by jeeva
 */
public class SelectDesignActivityGiftCard extends GiftCardBaseActivity implements TemplateSelectionInterfaces, View.OnClickListener {

    private static int numOfGridInRow = 2;
    private RelativeLayout SDFooterLayout, SelectDesignRootLayout;
    private RecyclerView recyclerView;
    private int cellWidth;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<GiftCardTemplate> rowListItem;
    private TemplateSelectionInterfaces templateSelectionInterfaces;
    private int currentPos = 0;
    private List<InCommBrandCardImage> inCommBrandCardImages;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_design);

        context = this;
        isShowBasketIcon = false;

        setUpToolBar(true,true);
        setBackButton(false,false);

        SelectDesignRootLayout = (RelativeLayout) findViewById(R.id.SelectDesignRootLayout);

        SDFooterLayout = (RelativeLayout) findViewById(R.id.SDFooter);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(SelectDesignActivityGiftCard.this, numOfGridInRow);

        templateSelectionInterfaces = this;

        SDFooterLayout.setEnabled(false);

        SDFooterLayout.setOnClickListener(this);

        ViewTreeObserver observer = SelectDesignRootLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    SelectDesignRootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    SelectDesignRootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                cellWidth = (SelectDesignRootLayout.getMeasuredWidth() / 2) - (int) (Utils.convertPixelsToDp(20, context));
                prepareData();
                initToolbar();
            }
        });

    }

    private void initToolbar() {
        setTitle("Select Design");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.giftcardToolBarBackGround));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context,android.R.color.darker_gray));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void prepareData() {
        rowListItem = new ArrayList<>();

        if (GiftCardDataManager.getInstance().getBrands() != null) {

            enableScreen(false);
            if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
                inCommBrandCardImages = GiftCardDataManager.getInstance().getBrands().get(0).getCardImages();
                SDFooterLayout.setEnabled(true);
                setPosition();
                setUI();
            } else {
                inCommBrandCardImages = GiftCardDataManager.getInstance().getBrands().get(0).getCardImages();
                SDFooterLayout.setEnabled(true);
                setUI();
            }
        } else {
            enableScreen(false);
            loaderText("Getting designs...");
            InCommBrandService.getAllBrands(new InCommBrandsCallback() {
                @Override
                public void onAllBrandsCallback(List<InCommBrand> brands, List<InCommCountry> countries, List<InCommStates> states, Exception error) {
                    if (brands != null && brands.size() > 0 && brands.get(0).getCardImages() != null) {
                        GiftCardDataManager.getInstance().setBrands(brands);
                        inCommBrandCardImages = brands.get(0).getCardImages();
                        SDFooterLayout.setEnabled(true);
                        setUI();
                    }else if (Utils.getErrorCode(error) == Constants.InCommFailure_Unauthorized || Utils.getVolleyErrorDescription(error).contains(Constants.VolleyFailure_UnAuthorizedMessage)) {
                        enableScreen(false);
                        IncommTokenService.getIncommTokenServices(new IncommTokenServiceCallback() {
                            @Override
                            public void onIncommTokenServiceCallback(String tokenSummary, Boolean successFlag, String error) {
                                enableScreen(true);
                                if (successFlag) {
                                    DataManager.getInstance().setInCommToken(tokenSummary);
                                    ((JambaApplication) context.getApplicationContext()).initializeInCommSDK();
                                    enableScreen(false);
                                    prepareData();
                                }
                            }
                        });
                    }
                    else if (error != null) {
                        enableScreen(true);
                        if (Utils.getErrorDescription(error) != null) {
                            alertWithTryAgain(context, "Failure", Utils.getErrorDescription(error));
                        } else {
                            alertWithTryAgain(context, "Failure", "Unable to download gift card designs");
                        }
                    } else {
                        alertWithTryAgain(context, "Failure", Utils.responseErrorNull());
                    }
                }
            });
        }
    }

    private void alertWithTryAgain(Context context, String Title, String Message) {
        if (Message == null) {
            Message = "Error";
        }
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                prepareData();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void setPosition() {
        String selectedImageCode = GiftCardDataManager.getInstance().getGiftCardCreate().getSelectedImageCode();
        if (selectedImageCode != null) {
            for (int i = 0; i < inCommBrandCardImages.size(); i++) {
                if (inCommBrandCardImages.get(i).getImageCode().equalsIgnoreCase(selectedImageCode)) {
                    currentPos = i;
                }
            }
        }
    }

    private void setUI() {
        for (int i = 0; i < inCommBrandCardImages.size(); i++) {
            GiftCardTemplate giftCardTemplate = new GiftCardTemplate(false, inCommBrandCardImages.get(i));
            rowListItem.add(giftCardTemplate);
        }

        if (rowListItem != null && rowListItem.size() > 0) {
            rowListItem.get(currentPos).setSelected(true);


            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(gridLayoutManager);
            ItemOffSetDecoration itemDecoration = new ItemOffSetDecoration(context, R.dimen.gridSpace);
            recyclerView.addItemDecoration(itemDecoration);

            RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(SelectDesignActivityGiftCard.this, rowListItem, templateSelectionInterfaces, cellWidth);
            recyclerView.setAdapter(rcAdapter);
            enableScreen(true);
        }

    }


    @Override
    public void onSelection(int pos) {
        currentPos = pos;
        for (int i = 0; i < rowListItem.size(); i++) {
            rowListItem.get(i).setSelected(false);
        }
        rowListItem.get(currentPos).setSelected(true);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SDFooter:
                if (GiftCardDataManager.getInstance().getGiftCardCreate() != null) {
                    GiftCardDataManager.getInstance().getGiftCardCreate().setImageCode(rowListItem.get(currentPos).getInCommBrandCardImage());
                    onBackPressed();
                } else {
                    GiftCardCreate giftCardCreate = new GiftCardCreate();
                    giftCardCreate.setImageCode(rowListItem.get(currentPos).getInCommBrandCardImage());
                    GiftCardDataManager.getInstance().setGiftCardCreate(giftCardCreate);
                    onBackPressed();
                }
                break;
        }
    }
}
