package com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager;


import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardCreate;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardReload;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.LocalPaymentInfo;
import com.wearehathway.apps.incomm.Models.InCommAutoReloadSavable;
import com.wearehathway.apps.incomm.Models.InCommAutoReloadSubmitOrder;
import com.wearehathway.apps.incomm.Models.InCommBrand;
import com.wearehathway.apps.incomm.Models.InCommBrandCardImage;
import com.wearehathway.apps.incomm.Models.InCommBrandCreditCardType;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Models.InCommCountry;
import com.wearehathway.apps.incomm.Models.InCommOrder;
import com.wearehathway.apps.incomm.Models.InCommStates;
import com.wearehathway.apps.incomm.Models.InCommUser;
import com.wearehathway.apps.incomm.Models.InCommUserPaymentAccount;
import com.wearehathway.apps.incomm.Models.InCommUserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nauman Afzaal on 14/05/15.
 */
public class GiftCardDataManager
{

    static GiftCardDataManager instance;
    InCommBrandCardImage selectedCardImage;
    InCommOrder inCommOrder;
    InCommCard inCommCard;
    List<InCommBrand> brands;
    List<InCommCountry> inCommCountries;
    List<InCommStates> inCommStates;
    List<InCommBrandCreditCardType> creditCardTypes;
    List<InCommCard> inCommCards;
    InCommUser inCommUser;
    InCommUserProfile inCommUserProfile;
    GiftCardCreate giftCardCreate;
    GiftCardReload giftCardReload;
    Boolean isSelf ;
    Boolean isChecked=false;
    Boolean isSaveNewPayment= false;
    HashMap<Integer,InCommCard> userAllCards;
    ArrayList<InCommUserPaymentAccount> accountList;
    LocalPaymentInfo localPaymentInfo;
    LocalPaymentInfo autoReloadLocalPaymentInfo;
    LocalPaymentInfo reloadLocalPaymentInfo;
    InCommAutoReloadSubmitOrder inCommAutoReloadSubmitOrder;
    InCommAutoReloadSavable autoReloadSavable;
    int paymentAccountID;
    boolean refreshGiftCards;
    boolean doNotifyDataSetChangedOnce = false;

    public int getPaymentAccountID() {
        return paymentAccountID;
    }

    public void setPaymentAccountID(int paymentAccountID) {
        this.paymentAccountID = paymentAccountID;
    }

    private GiftCardDataManager(){

    }

    public static GiftCardDataManager getInstance()
    {
        if (instance == null)
        {
            instance = new GiftCardDataManager();
        }
        return instance;
    }

    public void resetGiftCardDataManager()
    {
        instance=null;
    }

    public  void resetLocalPaymentInfo(){
        localPaymentInfo = null;
    }
    public void resetGiftCardCreate()
    {
        giftCardCreate = null;
    }
    public void resetGiftCardReload(){
        giftCardReload=null;
    }
    public void resetReloadLocalPaymentInfo(){
        reloadLocalPaymentInfo=null;
    }

    public InCommBrandCardImage getSelectedCardImage() {
        return selectedCardImage;
    }

    public void setSelectedCardImage(InCommBrandCardImage selectedCardImage) {
        this.selectedCardImage = selectedCardImage;
    }

    public InCommOrder getInCommOrder() {
        return inCommOrder;
    }

    public void setInCommOrder(InCommOrder inCommOrder) {
        this.inCommOrder = inCommOrder;
    }

    public InCommCard getInCommCard() {
        return inCommCard;
    }

    public void setInCommCard(InCommCard inCommCard) {
        this.inCommCard = inCommCard;
    }

    public List<InCommBrand> getBrands() {
        return brands;
    }

    public void setBrands(List<InCommBrand> brands) {
        this.brands = brands;
    }

    public InCommUser getInCommUser() {
        return inCommUser;
    }

    public void setInCommUser(InCommUser inCommUser) {
        this.inCommUser = inCommUser;
    }

    public InCommUserProfile getInCommUserProfile(){
        return inCommUserProfile;
    }

    public void setInCommUserProfile(InCommUserProfile inCommUserProfile){
        this.inCommUserProfile = inCommUserProfile;
    }

    public List<InCommCard> getInCommCards() {
        return inCommCards;
    }

    public void setInCommCards(List<InCommCard> inCommCards) {
        this.inCommCards = inCommCards;
    }

    public GiftCardCreate getGiftCardCreate() {
        return giftCardCreate;
    }

    public void setGiftCardCreate(GiftCardCreate giftCardCreate) {
        this.giftCardCreate = giftCardCreate;
    }

    public GiftCardReload getGiftCardReload(){
        return giftCardReload;
    }
    public void setGiftCardReload(GiftCardReload giftCardReload){
        this.giftCardReload=giftCardReload;
    }
    public Boolean getIsSelf() {
        return isSelf;
    }

    public void setIsSelf(Boolean self) {
        isSelf = self;
    }

    public HashMap<Integer, InCommCard> getUserAllCards() {
        return userAllCards;
    }

    public void setUserAllCards(HashMap<Integer, InCommCard> userAllCards) {
        this.userAllCards = userAllCards;
    }

    public List<InCommCountry> getInCommCountries() {
        if(inCommCountries == null){
            inCommCountries = new ArrayList<>();
        }
        return inCommCountries;
    }

    public void setInCommCountries(List<InCommCountry> inCommCountries) {
        this.inCommCountries = inCommCountries;
    }

    public List<InCommStates> getInCommStates() {
        if(inCommStates == null){
            inCommStates = new ArrayList<>();
        }
        return inCommStates;
    }

    public void setInCommStates(List<InCommStates> inCommStates) {
        this.inCommStates = inCommStates;
    }

    public List<InCommBrandCreditCardType> getCreditCardTypes() {
        if(creditCardTypes == null){
            creditCardTypes = new ArrayList<>();
        }
        return creditCardTypes;
    }

    public void setCreditCardTypes(List<InCommBrandCreditCardType> creditCardTypes) {
        this.creditCardTypes = creditCardTypes;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean checked) {
        isChecked = checked;
    }

    public Boolean getIsSaveNewPayment() {
        return isSaveNewPayment;
    }

    public void setIsSaveNewPayment(Boolean saveNewPayment) {
        isSaveNewPayment = saveNewPayment;
    }

    public ArrayList<InCommUserPaymentAccount> getAccountList() {
        return accountList;
    }

    public void setAccountList(ArrayList<InCommUserPaymentAccount> accountList) {
        this.accountList = accountList;
    }

    public LocalPaymentInfo getLocalPaymentInfo() {
        if(localPaymentInfo == null){
            localPaymentInfo = new LocalPaymentInfo();
        }
        return localPaymentInfo;
    }

    public void setLocalPaymentInfo(LocalPaymentInfo localPaymentInfo) {
        this.localPaymentInfo = localPaymentInfo;
    }

    public InCommAutoReloadSubmitOrder getInCommAutoReloadSubmitOrder() {
        return inCommAutoReloadSubmitOrder;
    }

    public void setInCommAutoReloadSubmitOrder(InCommAutoReloadSubmitOrder inCommAutoReloadSubmitOrder) {
        this.inCommAutoReloadSubmitOrder = inCommAutoReloadSubmitOrder;
    }

    public LocalPaymentInfo getAutoReloadLocalPaymentInfo() {
        if(autoReloadLocalPaymentInfo == null){
            autoReloadLocalPaymentInfo = new LocalPaymentInfo();
        }
        return autoReloadLocalPaymentInfo;
    }

    public void setAutoReloadLocalPaymentInfo(LocalPaymentInfo autoReloadLocalPaymentInfo) {
        this.autoReloadLocalPaymentInfo = autoReloadLocalPaymentInfo;
    }
    public LocalPaymentInfo getReloadLocalPaymentInfo() {
        if(reloadLocalPaymentInfo == null){
            reloadLocalPaymentInfo = new LocalPaymentInfo();
        }
        return reloadLocalPaymentInfo;
    }

    public void setReloadLocalPaymentInfo(LocalPaymentInfo reloadLocalPaymentInfo) {
        this.reloadLocalPaymentInfo = reloadLocalPaymentInfo;
    }

    public InCommAutoReloadSavable getAutoReloadSavable() {
        return autoReloadSavable;
    }

    public void setAutoReloadSavable(InCommAutoReloadSavable autoReloadSavable) {
        this.autoReloadSavable = autoReloadSavable;
    }

    public boolean isRefreshGiftCards() {
        return refreshGiftCards;
    }

    public void setRefreshGiftCards(boolean refreshGiftCards) {
        this.refreshGiftCards = refreshGiftCards;
    }

    public boolean isDoNotifyDataSetChangedOnce() {
        return doNotifyDataSetChangedOnce;
    }

    public void setDoNotifyDataSetChangedOnce(boolean doNotifyDataSetChangedOnce) {
        this.doNotifyDataSetChangedOnce = doNotifyDataSetChangedOnce;
    }
}
