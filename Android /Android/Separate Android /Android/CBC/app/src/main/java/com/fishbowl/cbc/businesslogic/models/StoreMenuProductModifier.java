package com.fishbowl.cbc.businesslogic.models;

import com.fishbowl.apps.olo.Models.OloModifier;
import com.fishbowl.apps.olo.Models.OloOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoreMenuProductModifier implements Serializable {
    private int id;
    private boolean mandatory;
    private String description;
    private int minSelects;
    private int maxSelects;
    private int maxchoicequantity;
    private int minchoicequantity;
    private int optionIdForUnnestedOption;
    private int maxaggregatequantity;
    private boolean supportschoicequantities;
    private ArrayList<StoreMenuProductModifierOption> options;

    public StoreMenuProductModifier() {
    }

    public StoreMenuProductModifier(OloModifier oloModifier) {
        id = oloModifier.getId();
        String modifierName = oloModifier.getDescription();
        if (modifierName.toLowerCase().contains("add whole food boost")) {
            modifierName = "Add Whole Food Boosts";
        } else if (modifierName.toLowerCase().contains("add boost")) {
            modifierName = "Add Boosts";
        } else if (modifierName.contains("?")) {
            modifierName = modifierName.replace("?", "");
        } else if (modifierName.contains(":")) {
            modifierName = modifierName.replace(":", "");
        }
        description = modifierName;
        mandatory = oloModifier.isMandatory();
        String min = oloModifier.getMinselects();
        String max = oloModifier.getMaxselects();
        String minChoice = oloModifier.getMinchoicequantity();
        String maxChoice = oloModifier.getMaxchoicequantity();
        String maxAggregate = oloModifier.getMaxaggregatequantity();
        supportschoicequantities = oloModifier.isSupportschoicequantities();
        if (min != null && !min.equals("")) {
            minSelects = Integer.parseInt(min);
        }
        if (max != null && !max.equals("")) {
            maxSelects = Integer.parseInt(max);
        }
        if (minChoice != null && !minChoice.equals("")) {
            minchoicequantity = Integer.parseInt(minChoice);
        }
        if (maxChoice != null && !maxChoice.equals("")) {
            maxchoicequantity = Integer.parseInt(maxChoice);
        }
        if (maxAggregate != null && !maxAggregate.equals("")) {
            maxaggregatequantity = Integer.parseInt(maxAggregate);
        }
        options = new ArrayList<>();
        List<OloOption> oloOptions = oloModifier.getOptions();
        if (oloOptions != null) {
            for (OloOption option : oloOptions) {
                options.add(new StoreMenuProductModifierOption(option));
            }
        }
    }

    public boolean isASimpleSizeModifier() {
        return description.toLowerCase().contains("select size");
    }

    public boolean isASmoothieTypeAndSizeModifier() {
        return description.toLowerCase().contains("select smoothie type");
    }

    public boolean hasSizeModifierOn2ndLevel() {
        if (isASimpleSizeModifier()) {
            return false;
        }
        for (StoreMenuProductModifierOption option : options) {
            if (option.getModifiers() != null) {
                for (StoreMenuProductModifier secondLevelModifier : option.getModifiers()) {
                    if (secondLevelModifier.isASimpleSizeModifier()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasSizeModifierOn2ndLevelButNotMakeItLightChoice() {
        return hasSizeModifierOn2ndLevel() && !isASmoothieTypeAndSizeModifier();
    }

    public boolean isTopLevelTypeOrSizeModifier() {
        return isASimpleSizeModifier() || hasSizeModifierOn2ndLevel();
    }

    //See case of steel-cut oatmeal
    private boolean hasNeedlesslyNestedModifiers() {
        return false;
//        return options.size() == 1 && options.get(0).getModifiers().size() > 0;
    }

    //Only does it to first level
    public ArrayList<StoreMenuProductModifier> returnModifiersByRemovingNeedlessNesting() {
        ArrayList<StoreMenuProductModifier> modifiers = new ArrayList<>();
        if (hasNeedlesslyNestedModifiers()) {
            List<StoreMenuProductModifier> modifier = options.get(0).getModifiers();
            int size = modifier.size();
            for (int i = 0; i < size; i++) {
                StoreMenuProductModifier storeMenuProductModifier = modifier.get(i);
                storeMenuProductModifier.optionIdForUnnestedOption = options.get(0).getId();
                modifiers.add(storeMenuProductModifier);
            }
        } else {
            modifiers.add(this); // Return this if modifier do not have needless nesting.
        }
        return modifiers;
    }

    public int classicOptionId() {
        if (isASmoothieTypeAndSizeModifier()) {
            for (StoreMenuProductModifierOption option : options) {
                if (option.getName().toLowerCase().contains("classic")) {
                    return option.getId();
                }
            }
        }
        return -1;
    }

    public int makeItLightOptionId() {
        if (isASmoothieTypeAndSizeModifier()) {
            for (StoreMenuProductModifierOption option : options) {
                if (option.getName().toLowerCase().contains("make it light")) {
                    return option.getId();
                }
            }
        }
        return -1;
    }


    public int getId() {
        return id;
    }

    public ArrayList<StoreMenuProductModifierOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<StoreMenuProductModifierOption> options) {
        this.options = options;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinSelects() {
        return minSelects;
    }

    public int getMaxSelects() {
        return maxSelects;
    }

    public int getMaxchoicequantity() {
        return maxchoicequantity;
    }

    public int getMinchoicequantity() {
        return minchoicequantity;
    }

    public int getMaxaggregatequantity() {
        return maxaggregatequantity;
    }

    public boolean isSupportschoicequantities() {
        return supportschoicequantities;
    }

    public int getOptionIdForUnnestedOption() {
        return optionIdForUnnestedOption;
    }

    public boolean isCustomizeModifer() {
        if (description.toLowerCase().contains("Would you like to customize more".toLowerCase()) || description.toLowerCase().contains("Not Perfect Yet".toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    public StoreMenuProductModifier(StoreMenuProductModifier mod) {
        this.id = mod.getId();
        this.mandatory = mod.isMandatory();
        this.description = mod.getDescription();
        this.minSelects = mod.getMinSelects();
        this.maxSelects = mod.getMaxSelects();
        this.minchoicequantity = mod.getMinchoicequantity();
        this.maxchoicequantity = mod.getMaxchoicequantity();
        this.maxaggregatequantity = mod.getMaxaggregatequantity();
        this.supportschoicequantities = mod.isSupportschoicequantities();
        this.optionIdForUnnestedOption = mod.getOptionIdForUnnestedOption();
        this.options = new ArrayList<>();
        if (mod.getOptions() != null) {
            for (StoreMenuProductModifierOption option : mod.getOptions()) {
                this.options.add(new StoreMenuProductModifierOption(option));
            }

        }
        //this.options = mod.getOptions();
    }
}