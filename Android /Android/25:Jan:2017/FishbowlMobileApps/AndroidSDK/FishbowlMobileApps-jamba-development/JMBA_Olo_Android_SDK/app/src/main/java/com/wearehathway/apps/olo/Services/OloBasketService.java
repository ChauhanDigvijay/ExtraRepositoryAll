package com.wearehathway.apps.olo.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wearehathway.apps.olo.Interfaces.OloBasketLoyaltyRewardsCallback;
import com.wearehathway.apps.olo.Interfaces.OloBasketLoyaltySchemesCallback;
import com.wearehathway.apps.olo.Interfaces.OloBasketServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloBasketSubmitServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloBasketValidationCallback;
import com.wearehathway.apps.olo.Interfaces.OloBatchProductBasketServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloBillingSchemeServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloErrorCallback;
import com.wearehathway.apps.olo.Interfaces.OloOrderServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloSavedDispatchAddressesCallBack;
import com.wearehathway.apps.olo.Interfaces.OloServiceCallback;
import com.wearehathway.apps.olo.Interfaces.OloSwitchBasketServiceCallback;
import com.wearehathway.apps.olo.Models.OloBasket;
import com.wearehathway.apps.olo.Models.OloBasketChoice;
import com.wearehathway.apps.olo.Models.OloBasketProduct;
import com.wearehathway.apps.olo.Models.OloBasketProductBatchResult;
import com.wearehathway.apps.olo.Models.OloBasketValidation;
import com.wearehathway.apps.olo.Models.OloBillingField;
import com.wearehathway.apps.olo.Models.OloBillingScheme;
import com.wearehathway.apps.olo.Models.OloChoiceCustomFieldValue;
import com.wearehathway.apps.olo.Models.OloDeliveryAddress;
import com.wearehathway.apps.olo.Models.OloLoyaltyReward;
import com.wearehathway.apps.olo.Models.OloLoyaltyScheme;
import com.wearehathway.apps.olo.Models.OloOrderInfo;
import com.wearehathway.apps.olo.Models.OloOrderStatus;
import com.wearehathway.apps.olo.Models.OloSwitchBasket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public class OloBasketService {
    private final static int SubmitBasketTimeOut = 120000; // 2 Minutes.

    public static void getBasketWithId(String basketId, final OloBasketServiceCallback callback) {
        String path = "baskets/" + basketId;
        OloService.getInstance().get(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void removeAddress(int addressId, String authtoken, final OloBasketServiceCallback callback) {
        String path = "/users/" + authtoken + "/userdeliveryaddresses/" + addressId;
        OloService.getInstance().delete(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                callback.onBasketServiceCallback(null, error);
            }
        });
    }

    public static void dispatchAddress(String basketId, OloDeliveryAddress oloDeliveryAddress, final OloBasketServiceCallback callback) {
        String path = "baskets/" + basketId + "/dispatchaddress";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", oloDeliveryAddress.getId());
        parameters.put("building", oloDeliveryAddress.getBuilding());
        parameters.put("streetaddress", oloDeliveryAddress.getStreetaddress());
        parameters.put("city", oloDeliveryAddress.getCity());
        parameters.put("zipcode", oloDeliveryAddress.getZipcode());
        parameters.put("phonenumber", oloDeliveryAddress.getPhonenumber());
        parameters.put("specialinstructions", oloDeliveryAddress.getSpecialinstructions());
        OloService.getInstance().put(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void deliveryMode(String basketId, String deliverymode, final OloBasketServiceCallback callback) {
        String path = "baskets/" + basketId + "/deliverymode";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("deliverymode", deliverymode);
        OloService.getInstance().put(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void createBasket(int vendorId, final OloBasketServiceCallback callback) {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        String path = "baskets/create";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("vendorid", vendorId);
        if (authToken != null) {
            parameters.put("authtoken", authToken);
        }
        OloService.getInstance().post(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void switchBasket(int vendorId, String basketId, final OloSwitchBasketServiceCallback callback) {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        String path = "baskets/" + String.valueOf(basketId) + "/transfer";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("vendorid", vendorId);
        if (authToken != null) {
            parameters.put("authtoken", authToken);
        }
        OloService.getInstance().post(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                switchBasketResponse(response, error, callback);
            }
        });
    }


    public static void addProductToBasket(String basketId, OloBasketProduct product, final OloBasketServiceCallback callback) {
        String path = "baskets/" + basketId + "/products";
        HashMap<String, Object> parameters = getParameters(product);
        OloService.getInstance().post(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void addProductToBasketBatch(String basketId, OloBasketProduct product, final OloBatchProductBasketServiceCallback callback) {
        String path = "baskets/" + basketId + "/products/batch";
        HashMap<String, Object> parameters = new HashMap<>();

        JSONArray productArray = new JSONArray();
        JSONObject prodObj = new JSONObject();
        try {
            if (product.getSpecialInstructions() != null) {
                prodObj.put("specialinstructions", product.getSpecialInstructions());
            } else {
                prodObj.put("specialinstructions", "");
            }
            prodObj.put("productid", product.getProductId());
            prodObj.put("quantity", product.getQuantity());
            ArrayList<OloBasketChoice> choices = product.getChoices();
            if (choices != null) {
                JSONArray choicesArray = new JSONArray();
                for (OloBasketChoice choice : choices) {
                    JSONObject choiceObj = new JSONObject();
                    choiceObj.put("choiceid", choice.getId());
                    choiceObj.put("quantity", choice.getQuantity());
                    choicesArray.put(choiceObj);
                }
                prodObj.put("choices", choicesArray);
            }
            productArray.put(prodObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parameters.put("products", productArray);
        OloService.getInstance().post(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseBatchResponse(response, error, callback);
            }
        });
    }

    public static void addMultipleProducts(String basketId, ArrayList<OloBasketProduct> products, final OloBatchProductBasketServiceCallback callback) {
        String path = "baskets/" + basketId + "/products/batch";
        HashMap<String, Object> parameters = new HashMap<>();

        JSONArray productArray = new JSONArray();
        for (OloBasketProduct product : products) {
            JSONObject prodObj = new JSONObject();
            try {
                prodObj.put("productId", product.getProductId());
                prodObj.put("options", product.getOptions());
                prodObj.put("quantity", product.getQuantity());
                prodObj.put("specialinstructions", product.getSpecialInstructions());
                ArrayList<OloBasketChoice> choices = product.getChoices();
                if (choices != null) {
                    JSONArray choicesArray = new JSONArray();
                    for (OloBasketChoice choice : choices) {
                        JSONObject choiceObj = new JSONObject();
                        choiceObj.put("choiceid", choice.getId());
                        choiceObj.put("quantity", choice.getQuantity());
                        ArrayList<OloChoiceCustomFieldValue> customfields = choice.getCustomfields();
                        if (customfields != null) {
                            JSONArray customArray = new JSONArray();
                            for (OloChoiceCustomFieldValue customFieldValue : customfields) {
                                JSONObject customObj = new JSONObject();
                                customObj.put("fieldid", customFieldValue.getFieldId());
                                customObj.put("value", customFieldValue.getValue());
                                customArray.put(customObj);
                            }
                            choiceObj.put("customfields", customArray);
                        }
                        choicesArray.put(choiceObj);
                    }
                    prodObj.put("choicecustomfields", choicesArray);
                }
                productArray.put(prodObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        parameters.put("products", productArray);

        OloService.getInstance().post(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseBatchResponse(response, error, callback);
            }
        });
    }

    public static void updateProduct(String basketId, OloBasketProduct product, final OloBasketServiceCallback callback) {
        String path = "baskets/" + basketId + "/products/" + product.getId();
        HashMap<String, Object> parameters = getParameters(product);
        OloService.getInstance().put(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void setTimeWanted(String basketId, boolean ismanualfire, int year, int month, int day, int hour, int minute, final OloBasketServiceCallback callback) {
        String path = "baskets/" + basketId + "/timewanted";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("year", year);
        parameters.put("month", month);
        parameters.put("day", day);
        parameters.put("hour", hour);
        parameters.put("minute", minute);
        parameters.put("ismanualfire", ismanualfire);
        OloService.getInstance().put(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void deleteTimeWanted(String basketId, final OloBasketServiceCallback callback) {
        String path = "baskets/" + basketId + "/timewanted";
        OloService.getInstance().delete(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void deleteProduct(String basketId, int basketProductId, final OloBasketServiceCallback callback) {
        String path = "baskets/" + basketId + "/products/" + basketProductId;
        OloService.getInstance().delete(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void submitOrder(OloOrderInfo orderInfo, final OloBasketSubmitServiceCallback callback) {
        String path = "baskets/" + orderInfo.getBasketId() + "/submit";
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("billingmethod", orderInfo.getBillingMethod());
        parameters.put("billingaccountid", orderInfo.getBillingAccountId());
        parameters.put("usertype", orderInfo.getUserType());
        parameters.put("cardnumber", orderInfo.getCardnumber());
        parameters.put("expiryyear", orderInfo.getExpiryyear());
        parameters.put("expirymonth", orderInfo.getExpirymonth());
        parameters.put("cvv", orderInfo.getCvv());
        parameters.put("zip", orderInfo.getZip());
        parameters.put("saveonfile", orderInfo.getSaveonfile());
        parameters.put("orderref", orderInfo.getOrderref());
        parameters.put("firstname", orderInfo.getFirstName());
        parameters.put("lastname", orderInfo.getLastName());
        parameters.put("emailaddress", orderInfo.getEmail());
        parameters.put("contactnumber", orderInfo.getContactNumber());
        parameters.put("reference", orderInfo.getReference());
        parameters.put("usertype", orderInfo.getUserType());


        List<OloBillingField> localOloBillingFieldList = orderInfo.getBillingFields();

        try {
            if (localOloBillingFieldList != null && localOloBillingFieldList.size() > 0) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < localOloBillingFieldList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", localOloBillingFieldList.get(i).getName());
                    jsonObject.put("value", localOloBillingFieldList.get(i).getValue());
                    jsonArray.put(jsonObject);
                }
                parameters.put("billingfields", jsonArray);
            }
            if (orderInfo != null) {
                parameters.put("billingschemeid", orderInfo.getBillingSchemeId());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (authToken != null) {
            parameters.put("authtoken", authToken);
        }

        OloService.getInstance().post(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseSubmitResponse(response, error, callback);
            }
        }, SubmitBasketTimeOut);
    }

    public static void cancelOrder(String orderId,final OloOrderServiceCallback callback){
        String path = "/orders/"+orderId+"/cancel";
        String authToken = OloSessionService.currentSesstion.getAuthToken();

        HashMap<String, Object> parameters = new HashMap<>();

        OloService.getInstance().post(path, authToken, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseOrderCancelResponse(response, error, callback);
            }
        });
    }

    public static void createFromOrder(String id, String orderref, final OloBasketServiceCallback callback) {
        String path = "baskets/createfromorder";
        String authToken = OloSessionService.currentSesstion.getAuthToken();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("orderref", orderref);

        OloService.getInstance().post(path, authToken, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }
    public static void createFromFavorite(String id, final OloBasketServiceCallback callback) {
        String path = "/baskets/createfromfave";
        String authToken = OloSessionService.currentSesstion.getAuthToken();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("faveid", id);

        OloService.getInstance().post(path, authToken, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }
    public static void validateBasket(String basketId, final OloBasketValidationCallback callback) {
        //String path = "baskets/" + basketId + "/validate";
        String path = "baskets/" + basketId + "/validate?checkupsell=true";
        OloService.getInstance().post(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseValidationResponse(response, error, callback);
            }
        });
    }

    public static void addUpsell(String basketId, JSONObject reqItem, final OloBasketServiceCallback callback) {
        String path = "baskets/" + basketId + "/upsell";

        try {
            HashMap e = new HashMap();
            e.put("items", reqItem.getJSONArray("items"));

            Log.d("e",e.toString());

            OloService.getInstance().post(path, e, new OloServiceCallback() {
                public void onServiceCallback(JSONObject response, Exception error) {
                    parseResponse(response, error, callback);
                }
            });
        } catch (JSONException var5) {
            var5.printStackTrace();
        }

    }

    public static void applyCoupon(String couponCode, String basketId, final OloBasketServiceCallback callback) {
        String path = "/baskets/" + basketId + "/coupon";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("couponcode", couponCode);
        OloService.getInstance().put(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void deleteCoupon(String basketId, final OloBasketServiceCallback callback) {
        String path = "/baskets/" + basketId + "/coupon";
        OloService.getInstance().delete(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void getBillingSchemes(String basketId, final OloBillingSchemeServiceCallback callback) {
        String path = "/baskets/" + basketId + "/billingschemes";
        OloService.getInstance().get(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                ArrayList<OloBillingScheme> billingSchemes = null;
                if (response != null) {
                    try {
                        Gson gson = new Gson();
                        JSONArray bilSchemes = (JSONArray) response.get("billingschemes");
                        billingSchemes = gson.fromJson(bilSchemes.toString(), new TypeToken<ArrayList<OloBillingScheme>>() {
                        }.getType());
                    } catch (Exception ex) {
                    }
                    if (billingSchemes == null) {
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                callback.onBillingSchemeServiceCallback(billingSchemes, error);
            }
        });
    }

    public static void getSavedDispatchAddress(String authToken, final OloSavedDispatchAddressesCallBack callBack) {
        String path = "/users/" + authToken + "/userdeliveryaddresses";
        OloService.getInstance().get(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                ArrayList<OloDeliveryAddress> oloDeliveryAddresses = null;
                if (response != null) {
                    try {
                        Gson gson = new Gson();
                        JSONArray bilSchemes = (JSONArray) response.get("deliveryaddresses");
                        oloDeliveryAddresses = gson.fromJson(bilSchemes.toString(), new TypeToken<ArrayList<OloDeliveryAddress>>() {
                        }.getType());
                    } catch (Exception ex) {
                    }
                    if (oloDeliveryAddresses == null) {
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                callBack.onSavedDispatchAddresses(oloDeliveryAddresses, error);
            }
        });
    }


    public static void configureLoyaltySchemes(String basketId, int schemeId, String phoneNumber, final OloErrorCallback callback) {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null) {
            callback.onErrorCallback(new Exception("User is not authenticated"));
            return;
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("authtoken", authToken);
        parameters.put("schemeid", schemeId);
        parameters.put("membershipnumber", phoneNumber);
        String path = "/baskets/" + basketId + "/loyaltyschemes";
        OloService.getInstance().post(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                // Ignore response, just return error if any
                callback.onErrorCallback(error);
            }
        });
    }

    public static void getLoyaltySchemes(String basketId, final OloBasketLoyaltySchemesCallback callback) {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null) {
            callback.onLoyaltySchemesCallback(null, new Exception("User is not authenticated"));
            return;
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("authtoken", authToken);
        String path = "/baskets/" + basketId + "/loyaltyschemes";
        OloService.getInstance().get(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                ArrayList<OloLoyaltyScheme> oloLoyaltySchemes = null;
                if (response != null) {
                    try {
                        Gson gson = new Gson();
                        JSONArray schemes = response.getJSONArray("schemes");
                        oloLoyaltySchemes = gson.fromJson(schemes.toString(), new TypeToken<ArrayList<OloLoyaltyScheme>>() {
                        }.getType());
                    } catch (Exception e) {
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null) {
                    callback.onLoyaltySchemesCallback(oloLoyaltySchemes, error);
                }
            }
        });
    }

    public static void getLoyaltyRewards(String basketId, int memberShipId, final OloBasketLoyaltyRewardsCallback callback) {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null) {
            callback.onBasketLoyaltyRewardsCallback(null, new Exception("User is not authenticated"));
            return;
        }

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("authtoken", authToken);
        parameters.put("membershipid", memberShipId);
        String path = "/baskets/" + basketId + "/loyaltyrewards/qualifying";
        OloService.getInstance().get(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                ArrayList<OloLoyaltyReward> oloLoyaltyRewards = null;
                if (response != null) {
                    try {
                        Gson gson = new Gson();
                        JSONArray rewards = response.getJSONArray("rewards");
                        oloLoyaltyRewards = gson.fromJson(rewards.toString(), new TypeToken<ArrayList<OloLoyaltyReward>>() {
                        }.getType());
                    } catch (Exception e) {
                        error = new Exception("Error occurred while parsing data.");
                    }
                }
                if (callback != null) {
                    callback.onBasketLoyaltyRewardsCallback(oloLoyaltyRewards, error);
                }
            }
        });
    }

    public static void applyReward(String basketId, int membershipId, String rewardReference, final OloBasketServiceCallback callback) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("membershipid", membershipId);
        JSONArray references = new JSONArray();
        references.put(rewardReference);
        parameters.put("references", references);
        String path = "/baskets/" + basketId + "/loyaltyrewards/byref";
        OloService.getInstance().put(path, parameters, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void removeReward(String basketId, int rewardId, final OloBasketServiceCallback callback) {
        String path = "/baskets/" + basketId + "/loyaltyrewards/" + rewardId;
        OloService.getInstance().delete(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                parseResponse(response, error, callback);
            }
        });
    }

    public static void deleteSavedBillingAccount( int billingAccountId, final OloServiceCallback callback) {
        String authToken = OloSessionService.currentSesstion.getAuthToken();
        if (authToken == null) {
            callback.onServiceCallback(null, new Exception("User is not authenticated"));
            return;
        }
        String path = "/users/"+authToken+"/billingaccounts/"+billingAccountId;
        OloService.getInstance().delete(path, null, new OloServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error) {
                callback.onServiceCallback(response,error);
               // parseResponse(response, error, callback);
            }
        });
    }

    private static HashMap<String, Object> getParameters(OloBasketProduct product) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("productid", product.getProductId());
        parameters.put("quantity", product.getQuantity());
        parameters.put("options", product.getOptions());
        parameters.put("specialinstructions", product.getSpecialInstructions());
        //        parameters.put("recipient", "");
        ArrayList<OloBasketChoice> choices = product.getChoices();
        if (choices != null) {
            JSONArray choicesArray = new JSONArray();
            for (OloBasketChoice choice : choices) {
                JSONObject choiceObj = new JSONObject();
                try {
                    choiceObj.put("choiceid", choice.getId());
                    choiceObj.put("quantity", choice.getQuantity());
                    ArrayList<OloChoiceCustomFieldValue> customfields = choice.getCustomfields();
                    if (customfields != null) {
                        JSONArray customArray = new JSONArray();
                        for (OloChoiceCustomFieldValue customFieldValue : customfields) {
                            JSONObject customObj = new JSONObject();
                            customObj.put("fieldid", customFieldValue.getFieldId());
                            customObj.put("value", customFieldValue.getValue());
                            customArray.put(customObj);
                        }
                        choiceObj.put("customfields", customArray);
                    }
                    choicesArray.put(choiceObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            parameters.put("choicecustomfields", choicesArray);
        }
        return parameters;
    }

    private static void parseSubmitResponse(JSONObject response, Exception error, OloBasketSubmitServiceCallback callback) {
        OloOrderStatus status = null;
        if (response != null) {
            try {
                Gson gson = new Gson();
                status = gson.fromJson(response.toString(), OloOrderStatus.class);
            } catch (Exception e) {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onOloBasketSubmitServiceCallback(status, error);
        }
    }

    private static void parseValidationResponse(JSONObject response, Exception error, OloBasketValidationCallback callback) {
        OloBasketValidation validation = null;
        if (response != null) {
            try {
                Gson gson = new Gson();
                validation = gson.fromJson(response.toString(), OloBasketValidation.class);
            } catch (Exception e) {
            }
            if (validation == null) {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onOloBasketValidationCallback(validation, error);
        }
    }

    private static void parseBatchResponse(JSONObject response, Exception error, OloBatchProductBasketServiceCallback callback) {
        OloBasketProductBatchResult result = null;
        if (response != null) {
            try {
                Gson gson = new Gson();
                result = gson.fromJson(response.toString(), OloBasketProductBatchResult.class);
            } catch (Exception e) {
            }
            if (result == null) {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onBatchProductBasketServiceCallback(result, error);
        }
    }

    private static void parseResponse(JSONObject response, Exception error, OloBasketServiceCallback callback) {
        OloBasket basket = null;
        if (response != null) {
            try {
                Gson gson = new Gson();
                basket = gson.fromJson(response.toString(), OloBasket.class);
            } catch (Exception e) {
            }
            if (basket == null) {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onBasketServiceCallback(basket, error);
        }
    }

    private static void switchBasketResponse(JSONObject response, Exception error, OloSwitchBasketServiceCallback callback) {
        OloSwitchBasket switchBasket = null;
        if (response != null) {
            try {
                Gson switchbasketGson = new Gson();
                switchBasket = switchbasketGson.fromJson(response.toString(), OloSwitchBasket.class);
            } catch (Exception e) {
                error = new Exception("Error occurred while parsing data.");
            }
            if (switchBasket == null) {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onSwitchBasketServiceCallback(switchBasket, error);
        }
    }

    private static void parseOrderCancelResponse(JSONObject response,Exception error,OloOrderServiceCallback callback){
        OloOrderStatus oloOrderStatus = null;
        if (response != null) {
            try {
                Gson orderStausGson = new Gson();
                oloOrderStatus = orderStausGson.fromJson(response.toString(), OloOrderStatus.class);
            } catch (Exception e) {
                error = new Exception("Error occurred while parsing data.");
            }
            if (oloOrderStatus == null) {
                error = new Exception("Error occurred while parsing data.");
            }
        }
        if (callback != null) {
            callback.onOrderServiceCallback(oloOrderStatus, error);
        }
    }
}
