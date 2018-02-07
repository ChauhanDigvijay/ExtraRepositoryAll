package com.olo.jambajuice.BusinessLogic.Models.GiftCardModels;



import com.wearehathway.apps.incomm.Models.InCommBrandCardImage;
import com.wearehathway.apps.incomm.Models.InCommOrderItem;
import com.wearehathway.apps.incomm.Models.InCommOrderPurchaser;
import com.wearehathway.apps.incomm.Models.InCommOrderRecipientDetails;
import com.wearehathway.apps.incomm.Models.InCommSubmitOrder;
import com.wearehathway.apps.incomm.Models.InCommSubmitPayment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vthink on 19/08/16.
 */
public class GiftCardCreate {
    private InCommSubmitOrder inCommSubmitOrder;
    private String selectedCardImageUrl;


    public GiftCardCreate() {
        this.inCommSubmitOrder = new InCommSubmitOrder();


        //Item
        InCommOrderItem firstOrderItem = new InCommOrderItem();
        ArrayList<InCommOrderItem> firstOrderItemList = new ArrayList<>();
        firstOrderItemList.add(firstOrderItem);

        InCommOrderRecipientDetails firstRecipient =new InCommOrderRecipientDetails();
        ArrayList<InCommOrderRecipientDetails> firstRecipientList = new ArrayList<>();
        firstRecipient.setItems(firstOrderItemList);
        firstRecipientList.add(firstRecipient);


        //Recipient
        inCommSubmitOrder.setRecipients(firstRecipientList);

        ///Payment
        inCommSubmitOrder.setPayment(new InCommSubmitPayment());

        //Purchaser
        inCommSubmitOrder.setPurchaser(new InCommOrderPurchaser());
    }

    public void setImageCode(InCommBrandCardImage inCommBrandCardImage){
        inCommSubmitOrder.getRecipients().get(0).getItems().get(0).setImageCode(inCommBrandCardImage.getImageCode());
        selectedCardImageUrl = inCommBrandCardImage.getImageUrl();
    }

    public String getSelectedCardImageUrl() {
        return selectedCardImageUrl;
    }

    public String getSelectedImageCode(){
        return inCommSubmitOrder.getRecipients().get(0).getItems().get(0).getImageCode();
    }

    public void setAmount(Double amount){
        inCommSubmitOrder.getRecipients().get(0).getItems().get(0).setAmount(amount);
    }

    public String getAmount(){
        return String.valueOf(inCommSubmitOrder.getRecipients().get(0).getItems().get(0).getAmount());
    }

    public void setRecipientEmailAddress(String recipientEmailAddress){
        inCommSubmitOrder.getRecipients().get(0).setEmailAddress(recipientEmailAddress);
    }

    public String getRecipientEmailAddress(){
        return inCommSubmitOrder.getRecipients().get(0).getEmailAddress();
    }

    public void setQuantity(int quantity){
        inCommSubmitOrder.getRecipients().get(0).getItems().get(0).setQuantity(quantity);
    }

    public int getQuantity(){
        return inCommSubmitOrder.getRecipients().get(0).getItems().get(0).getQuantity();
    }

    public void setTotalOrderAmount(Double totalOrderAmount){
        inCommSubmitOrder.getPayment().setAmount(totalOrderAmount);
    }

    public double getTotalOrderAmount(){
        return inCommSubmitOrder.getPayment().getAmount();
    }

    public void setPaymentInfo(InCommSubmitPayment paymentInfo){
        inCommSubmitOrder.setPayment(paymentInfo);
    }

    public InCommSubmitPayment getPaymentInfo(){
        return inCommSubmitOrder.getPayment();
    }

    public void setPurchaserInfo(InCommOrderPurchaser purchaserInfo){
        inCommSubmitOrder.setPurchaser(purchaserInfo);
    }

    public InCommOrderPurchaser getPurchaserInfo(){
        return inCommSubmitOrder.getPurchaser();
    }

    public void setRecipientInfo(List<InCommOrderRecipientDetails> recipientInfo){
        inCommSubmitOrder.setRecipients(recipientInfo);
    }

    public List<InCommOrderRecipientDetails> getRecipientInfo(){
        return inCommSubmitOrder.getRecipients();
    }

    public InCommSubmitOrder getInCommSubmitOrder() {
        return inCommSubmitOrder;
    }

    public void setInCommSubmitOrder(InCommSubmitOrder inCommSubmitOrder) {
        this.inCommSubmitOrder = inCommSubmitOrder;
    }

    public void resetPurchaserInfo(){
        inCommSubmitOrder.resetPurchaser();
    }
}
