package com.olo.jambajuice.BusinessLogic.Models.GiftCardModels;


import com.wearehathway.apps.incomm.Models.InCommOrderPurchaser;
import com.wearehathway.apps.incomm.Models.InCommReloadOrder;
import com.wearehathway.apps.incomm.Models.InCommSubmitPayment;

/**
 * Created by vt010 on 9/6/16.
 */
public class GiftCardReload {
    private InCommReloadOrder inCommReloadOrder;

    public GiftCardReload(){
        inCommReloadOrder=new InCommReloadOrder();

        inCommReloadOrder.setPayment(new InCommSubmitPayment());
        inCommReloadOrder.setPurchaser(new InCommOrderPurchaser());
    }

    public void setAmount(double amount){
        inCommReloadOrder.setAmount(amount);
        if(inCommReloadOrder.getPayment()!=null){
            inCommReloadOrder.getPayment().setAmount(amount);
        }
    }
    public double getAmount(){
        return inCommReloadOrder.getAmount();
    }

    public void setBrandId(String brandId){
        inCommReloadOrder.setBrandId(brandId);
    }
    public String getBrandId(){
        return inCommReloadOrder.getBrandId();
    }
    public void setCardId(int cardId){
        inCommReloadOrder.setCardId(cardId);
    }
    public int getCardId(){
        return inCommReloadOrder.getCardId();
    }
    public void setCardPin(String cardPin){
        inCommReloadOrder.setCardPin(cardPin);
    }
    public String getCardPin(){
        return inCommReloadOrder.getCardPin();
    }
    public void setIsTestMode(boolean isTestMode){
        inCommReloadOrder.setIsTestMode(isTestMode);
    }
    public boolean isTestMode(){
        return inCommReloadOrder.isTestMode();
    }
    public void setPurchaserInfo(InCommOrderPurchaser purchaser){
        inCommReloadOrder.setPurchaser(purchaser);
    }
    public void resetPurchaserInfo(){
        inCommReloadOrder.resetPurchaser();
    }
    public InCommOrderPurchaser getPurchaserInfo(){
        return inCommReloadOrder.getPurchaser();
    }
    public void setPaymentInfo(InCommSubmitPayment payment)
    {
        inCommReloadOrder.setPayment(payment);
    }
    public InCommSubmitPayment getPaymentInfo(){
        return inCommReloadOrder.getPayment();
    }
}
