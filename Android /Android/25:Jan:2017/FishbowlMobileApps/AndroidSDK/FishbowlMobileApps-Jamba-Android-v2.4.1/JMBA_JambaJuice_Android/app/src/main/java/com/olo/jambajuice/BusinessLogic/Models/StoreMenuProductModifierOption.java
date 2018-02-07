package com.olo.jambajuice.BusinessLogic.Models;

import com.wearehathway.apps.olo.Models.OloModifier;
import com.wearehathway.apps.olo.Models.OloOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nauman Afzaal on 01/06/15.
 */
public class StoreMenuProductModifierOption implements Serializable
{
    private int id;
    private String name;
    private float cost;
    private List<StoreMenuProductModifier> modifiers;
    private boolean isSelected;
    private int quantity;

    public StoreMenuProductModifierOption()
    {
    }

    public StoreMenuProductModifierOption(OloOption oloOption)
    {
        id = oloOption.getId();
        name = oloOption.getName();
        cost = oloOption.getCost();
        List<OloModifier> oloModifiers = oloOption.getModifiers();
        if (oloModifiers != null)
        {
            modifiers = new ArrayList<>();
            for (OloModifier oloModifier : oloModifiers)
            {
                modifiers.add(new StoreMenuProductModifier(oloModifier));
            }
        }
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public float getCost()
    {
        return cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<StoreMenuProductModifier> getModifiers()
    {
        return modifiers;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setModifiers(List<StoreMenuProductModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public StoreMenuProductModifierOption(StoreMenuProductModifierOption option) {
        this.id = option.getId();
        this.name = option.getName();
        this.cost = option.getCost();
        this.isSelected = option.isSelected();
        this.quantity = option.getQuantity();
        this.modifiers = new ArrayList<>();

        if(option.getModifiers()!=null){
            for(StoreMenuProductModifier modifier : option.getModifiers()){
                this.modifiers.add(new StoreMenuProductModifier(modifier));
            }
        }
//        this.modifiers = option.getModifiers();

    }
}
