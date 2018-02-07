package com.olo.jambajuice.BusinessLogic.Models;

import com.wearehathway.apps.olo.Models.OloProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nauman Afzaal on 03/06/15.
 */
public class StoreMenuProduct
{
    private int id;//Olo Product Id.
    private String name;
    private float productCost;
    private String description;
    private int chainProductId;
    private boolean isSizeManadatory;
    private boolean hasPopulatedModifiers;
    private StoreMenuProductModifier productSizeModifier;
    private List<StoreMenuProductModifier> productModifiers;

    public StoreMenuProduct(OloProduct oloProduct)
    {
        id = oloProduct.getId();
        name = oloProduct.getName();
        productCost = oloProduct.getCost();
        description = oloProduct.getDescription();
        chainProductId = oloProduct.getChainproductid();
    }

    public void populateStoreMenuProductModifiers(List<StoreMenuProductModifier> storeMenuProductModifiers)
    {
        productSizeModifier = null;
        productModifiers = new ArrayList<>();
        hasPopulatedModifiers = true;
        for (StoreMenuProductModifier modifier : storeMenuProductModifiers)
        {
            if (modifier.isTopLevelTypeOrSizeModifier())
            {
                productSizeModifier = modifier;
                isSizeManadatory = true;// If there is any "select size" or "select smoothie type" then size is mandatory.
            }
            else
            {
                ArrayList<StoreMenuProductModifier> unnesterModifiers = modifier.returnModifiersByRemovingNeedlessNesting();
                productModifiers.addAll(unnesterModifiers);
            }
        }
    }

    public int getId()
    {
        return id;
    }

    public StoreMenuProductModifier getProductSizeModifier()
    {
        return productSizeModifier;
    }

    public StoreMenuProductModifier getProductSizeModifier(int pos)
    {
        StoreMenuProductModifier storeMenuProductModifier = null;
        StoreMenuProductModifierOption option = getStoreMenuProductOptionAt(pos);
        if (option != null)
        {
            List<StoreMenuProductModifier> allModifiers = option.getModifiers();
            if (allModifiers != null)
            {
                storeMenuProductModifier = allModifiers.get(0);
            }
        }
        return storeMenuProductModifier;
    }

    public List<StoreMenuProductModifier> getProductSecondLevelModifier(int pos)
    {
        List<StoreMenuProductModifier> list = new ArrayList<>();
        StoreMenuProductModifier storeMenuProductModifier = null;
        StoreMenuProductModifierOption option = getStoreMenuProductOptionAt(pos);
        if (option != null)
        {
            List<StoreMenuProductModifier> allModifiers = option.getModifiers();
            if (allModifiers != null)
            {
//                storeMenuProductModifier = allModifiers.get(0);
                list = new ArrayList<>(allModifiers);
            }
        }
        return list;
    }

    public StoreMenuProductModifierOption getStoreMenuProductOptionAt(int pos)
    {
        StoreMenuProductModifierOption storeMenuProductModifierOption = null;
        if (productSizeModifier != null)
        {
            ArrayList<StoreMenuProductModifierOption> storeMenuProductModifierOptions = productSizeModifier.getOptions();
            if (storeMenuProductModifierOptions != null && pos < storeMenuProductModifierOptions.size())
            {
                storeMenuProductModifierOption = storeMenuProductModifierOptions.get(pos);
            }
        }
        return storeMenuProductModifierOption;
    }

    public List<StoreMenuProductModifier> getProductModifiers()
    {
        return productModifiers;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public int getChainProductId()
    {
        return chainProductId;
    }

    public boolean isSmoothieTypesAvailable()
    {
        return productSizeModifier != null && productSizeModifier.isASmoothieTypeAndSizeModifier();
    }
    public boolean hasSecondLevelModifiers()
    {
        return productSizeModifier != null && productSizeModifier.hasSizeModifierOn2ndLevelButNotMakeItLightChoice();
    }

    public boolean hasSmoothieTypesOrSecondLevelModifiers()
    {
        return isSmoothieTypesAvailable() || hasSecondLevelModifiers();
    }
    public boolean isSizeManadatory()
    {
        return isSizeManadatory;
    }

    public boolean isHasPopulatedModifiers()
    {
        return hasPopulatedModifiers;
    }

    public float getProductCost()
    {
        return productCost;
    }
}
