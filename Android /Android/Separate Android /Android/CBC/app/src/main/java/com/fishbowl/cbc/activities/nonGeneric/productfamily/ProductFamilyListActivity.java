package com.fishbowl.cbc.activities.nonGeneric.productfamily;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.fishbowl.cbc.R;
import com.fishbowl.cbc.adapters.ModifierAdapter;
import com.fishbowl.cbc.businesslogic.interfaces.ProductDetailCallback;
import com.fishbowl.cbc.businesslogic.interfaces.StoreMenuCallback;
import com.fishbowl.cbc.businesslogic.managers.DataManager;
import com.fishbowl.cbc.businesslogic.models.ProductOption;
import com.fishbowl.cbc.businesslogic.models.StoreMenuProduct;
import com.fishbowl.cbc.businesslogic.models.StoreMenuProductModifier;
import com.fishbowl.cbc.businesslogic.models.StoreMenuProductModifierOption;
import com.fishbowl.cbc.businesslogic.services.StoreService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.fishbowl.cbc.R.id.detailView;

public class ProductFamilyListActivity extends AppCompatActivity {

    private List<ProductOption> listDataHeader;
    private HashMap<Integer, List<ProductOption>> listDataChild;
    private ProductOption specialInstuction;
    LinearLayout linearLayout;
    //private CustomExpandableListView customExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_family_list);
        //fetchMenuIfRequired();
        fetchDataIfRequired();
        linearLayout = (LinearLayout) findViewById(detailView);
    }

    private void fetchMenuIfRequired() {
        StoreService.getStoreMenu(this, 15589, new StoreMenuCallback() {

            @Override
            public void onStoreMenuCallback(HashMap<Integer, StoreMenuProduct> storeProducts, Exception exception) {
                try {
                    updateModifierDatas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void fetchDataIfRequired() {
        StoreService.fetchProductModifiers(this, 1141991, new ProductDetailCallback() {
            @Override
            public void onProductDetailCallback(List<StoreMenuProductModifier> storeMenuProductModifiers, Exception exception) {
                try {
                    DataManager.getInstance().setStoreMenuProductModifiers(1141991, new ArrayList<StoreMenuProductModifier>(storeMenuProductModifiers));
                    updateModifierDatas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateModifierDatas() {
        try {
            List<StoreMenuProductModifier> storeMenuProductModifiers = DataManager.getInstance().getStoreMenuProductModifiers(1141991);
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();
            int i = 0;
//            //First level (Add boost, Add yummy extra,..)
            for (StoreMenuProductModifier modifier : storeMenuProductModifiers) {

                ProductOption headerOption = new ProductOption(modifier.getId(), modifier.getDescription(), 0, false);
                headerOption.setMinSelect(modifier.getMinSelects());
                headerOption.setMaxSelect(modifier.getMaxSelects());
                headerOption.setMinChoiceQuantity(modifier.getMinchoicequantity());
                headerOption.setMaxChoiceQuantity(modifier.getMaxchoicequantity());
                headerOption.setMaxAggregateQuantity(modifier.getMaxaggregatequantity());
                headerOption.setSupportschoicequantities(modifier.isSupportschoicequantities());

                ArrayList<StoreMenuProductModifierOption> storeMenuProductModifierOptions = modifier.getOptions();

                if (storeMenuProductModifierOptions != null) {
                    List<ProductOption> productOptions = new ArrayList<>();
                    int j = 0;
                    //Second Level (Choose boost, add juices , add fruits and vegies...)
                    for (StoreMenuProductModifierOption option : storeMenuProductModifierOptions) {
                        int id = option.getId();
                        int maxChoice1 = 0, minChoice = 0, maxAgregate = 0, maxChoiceQty = 0, minChoiceQty = 0;
                        boolean supportschoicequantities = false;
                        boolean isMandatory = false;
                        List<ProductOption> secLevProductOptions = null;
                        if (option.getModifiers() != null && option.getModifiers().size() > 0) {
                            isMandatory = true;
                        }

                        if (option.getModifiers() != null) {
                            List<StoreMenuProductModifier> secLevModifiers = option.getModifiers();
                            secLevProductOptions = new ArrayList<>();
                            for (StoreMenuProductModifier secModifier : secLevModifiers) {
                                List<StoreMenuProductModifierOption> secLevOptions = secModifier.getOptions();
                                if (secLevOptions != null) {

                                    //Third level
                                    for (StoreMenuProductModifierOption secLevOption : secLevOptions) {
                                        int secLevId = secLevOption.getId();
                                        boolean secLevIsMandatory = false;
                                        if (secLevOption.getModifiers() != null && secLevOption.getModifiers().size() > 0) {
                                            isMandatory = true;
                                        }

                                        ProductOption grandChildOption = new ProductOption(secLevId, secLevOption.getName(), secLevOption.getCost(), secLevOption.isSelected(), isMandatory);
                                        maxAgregate = secModifier.getMaxaggregatequantity();
                                        maxChoiceQty = secModifier.getMaxchoicequantity();
                                        minChoiceQty = secModifier.getMinchoicequantity();
                                        maxChoice1 = secModifier.getMaxSelects();
                                        minChoice = secModifier.getMinSelects();
                                        supportschoicequantities = secModifier.isSupportschoicequantities();
                                        secLevProductOptions.add(grandChildOption);
                                    }
                                }

                            }

                        }

                        ProductOption childOption = null;
                        childOption = new ProductOption(id, option.getName(), option.getCost(), option.isSelected(), isMandatory);
                        childOption.setMaxAggregateQuantity(maxAgregate);
                        childOption.setMaxChoiceQuantity(maxChoiceQty);
                        childOption.setMinChoiceQuantity(minChoiceQty);
                        childOption.setMaxSelect(maxChoice1);
                        childOption.setMinSelect(minChoice);
                        childOption.setSupportschoicequantities(supportschoicequantities);
                        childOption.setNestedOptionId(modifier.getOptionIdForUnnestedOption()); // For items with unnecessary nested option
                        childOption.setSubOptions(secLevProductOptions);
                        productOptions.add(childOption);

                        listDataChild.put(i, productOptions);
                        ++j;
                    }
                    listDataHeader.add(i, headerOption);
                    ++i;
                }
            }

            //listDataHeader.add(i, new ProductOption(0, "Add Special Instructions", 0, false));
            List<ProductOption> specialInstructionOptions = new ArrayList<>();
            specialInstuction = new ProductOption(0, "", 0, false);
            specialInstructionOptions.add(specialInstuction);
            listDataChild.put(i, specialInstructionOptions);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.modRecylerView);
            recyclerView.setHasFixedSize(true);
            ModifierAdapter adapter = new ModifierAdapter(linearLayout, storeMenuProductModifiers, listDataHeader, listDataChild);
            recyclerView.setAdapter(adapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(gridLayoutManager);

            //this.customExpandableListView = new CustomExpandableListView(this, linearLayout, listDataHeader, listDataChild);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePrice() {

    }
}