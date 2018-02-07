package com.wearehathway.apps.incomm.Services;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wearehathway.apps.incomm.Interfaces.InCommBrandCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommBrandsCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommServiceCallback;
import com.wearehathway.apps.incomm.Models.InCommBrand;
import com.wearehathway.apps.incomm.Models.InCommCountry;
import com.wearehathway.apps.incomm.Models.InCommStates;
import com.wearehathway.apps.incomm.Utils.InCommUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nauman Afzaal on 13/08/15.
 */
public class InCommBrandService {

    private static String PROVINCE = "Province";
    private static String STATE = "State";
    private static String DISTRICT = "District";
    private static String TERRITORY = "Territory";
    private static String OUTLYING_AREA = "Outlying Area";

    public static void getBrandWithId(String brandId, final InCommBrandCallback callback) {
        String path = "Brands/" + brandId;
        InCommService.getInstance().get(path, null, new InCommServiceCallback() {
            @Override
            public void onServiceCallback(String response, Exception error) {
                InCommBrand brand = null;
                if (response != null) {
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    brand = gson.fromJson(response.toString(), InCommBrand.class);
                    if (brand == null) {
                        error = InCommUtils.getParsingError();
                    }
                }
                if (callback != null) {
                    callback.onBrandCallback(brand, error);
                }
            }
        });
    }

    public static void getAllBrands(final InCommBrandsCallback callback) {
        InCommService.getInstance().get("Brands", null, new InCommServiceCallback() {
            @Override
            public void onServiceCallback(String response, Exception error) {
                List<InCommBrand> brands = null;
                List<InCommCountry> countries = null;
                List<InCommStates> states = null;
                List<InCommStates> orderedStates = new ArrayList<>();

                if (response != null) {

                    //Getting whole brand
                    Gson gson = InCommUtils.getGsonForParsingDate();
                    brands = gson.fromJson(response, new TypeToken<ArrayList<InCommBrand>>() {
                    }.getType());
                    if (brands == null) {
                        error = InCommUtils.getParsingError();
                    }

                    //Getting Countries List
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                        JSONArray countryJsonString = jsonObject.getJSONArray("BillingCountries");
                        Gson gson1 = InCommUtils.getGsonForParsingDate();
                        countries = gson1.fromJson(countryJsonString.toString(), new TypeToken<ArrayList<InCommCountry>>() {
                        }.getType());
                        if (countries == null) {
                            error = InCommUtils.getParsingError();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Getting States List
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                        JSONArray stateJsonString = jsonObject.getJSONArray("BillingStates");
                        Gson gson1 = InCommUtils.getGsonForParsingDate();
                        states = gson1.fromJson(stateJsonString.toString(), new TypeToken<ArrayList<InCommStates>>() {
                        }.getType());
                        if (states == null) {
                            error = InCommUtils.getParsingError();
                        }

                        if (states != null) {
                            orderedStates = orderedStates(states);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (callback != null) {
                    callback.onAllBrandsCallback(brands, countries, orderedStates, error);
                }
            }
        });

    }

    private static List<InCommStates> orderedStates(List<InCommStates> states) {

        List<InCommStates> provinceTypeStates = new ArrayList<>();
        List<InCommStates> stateTypeStates = new ArrayList<>();
        List<InCommStates> districtTypeStates = new ArrayList<>();
        List<InCommStates> territoryTypeStates = new ArrayList<>();
        List<InCommStates> outlyingareaTypeStates = new ArrayList<>();

        List<InCommStates> orderedStates = new ArrayList<>();


        for (InCommStates inCommState : states) {
            if (inCommState.getType().equalsIgnoreCase(PROVINCE)) {
                provinceTypeStates.add(inCommState);
            }

            if (inCommState.getType().equalsIgnoreCase(STATE)) {
                stateTypeStates.add(inCommState);
            }

            if (inCommState.getType().equalsIgnoreCase(DISTRICT)) {
                districtTypeStates.add(inCommState);
            }

            if (inCommState.getType().equalsIgnoreCase(TERRITORY)) {
                territoryTypeStates.add(inCommState);
            }

            if (inCommState.getType().equalsIgnoreCase(OUTLYING_AREA)) {
                outlyingareaTypeStates.add(inCommState);
            }
        }

        orderedStates.addAll(stateTypeStates);
        orderedStates.addAll(districtTypeStates);
        orderedStates.addAll(territoryTypeStates);
        orderedStates.addAll(outlyingareaTypeStates);

        return orderedStates;

    }
}
