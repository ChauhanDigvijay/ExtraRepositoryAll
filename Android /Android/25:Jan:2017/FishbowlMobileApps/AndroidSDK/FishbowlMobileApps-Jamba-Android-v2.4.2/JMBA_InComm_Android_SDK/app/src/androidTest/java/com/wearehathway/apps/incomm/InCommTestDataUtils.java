package com.wearehathway.apps.incomm;

import com.wearehathway.apps.incomm.Models.InCommOrderItem;
import com.wearehathway.apps.incomm.Models.InCommOrderPurchaser;
import com.wearehathway.apps.incomm.Models.InCommOrderRecipientDetails;
import com.wearehathway.apps.incomm.Models.InCommSubmitOrder;
import com.wearehathway.apps.incomm.Models.InCommSubmitPayment;
import com.wearehathway.apps.incomm.Services.InCommService;
import com.wearehathway.apps.incomm.Utils.InCommConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nauman Afzaal on 17/08/15.
 */
public class InCommTestDataUtils
{
    public static InCommSubmitOrder getSubmitOrder()
    {
        InCommSubmitOrder order = new InCommSubmitOrder();

        InCommOrderPurchaser purchaser = new InCommOrderPurchaser();
        purchaser.setCountry("US");
        purchaser.setEmailAddress("androidTester@gmail.com");
        purchaser.setFirstName("Nauman");
        purchaser.setLastName("Afzaal");

        double amount = 10;
        String brandID = InCommService.getInstance().getConfiguration().brandId;
        InCommSubmitPayment payment = new InCommSubmitPayment();
        payment.setAmount(amount);
        payment.setCity("Newyork");
        payment.setCountry("US");
        payment.setStateProvince("NY");
        payment.setFirstName("Android");
        payment.setLastName("Test");
        payment.setZipPostalCode("22123");
        payment.setOrderPaymentMethod(InCommConstants.OrderPaymentMethod.NoFundsCollected.val);

        InCommOrderItem item = new InCommOrderItem();
        item.setAmount(amount);
        item.setBrandId(brandID);
        item.setQuantity(1);

        List<InCommOrderItem> items = new ArrayList<>();
        items.add(item);

        InCommOrderRecipientDetails recipient = new InCommOrderRecipientDetails();
        recipient.setFirstName("Nauman");
        recipient.setLastName("Afzaal");
        recipient.setEmailAddress("tester@gmail.com");
        recipient.setItems(items);

        order.setPayment(payment);
        order.setPurchaser(purchaser);
        List<InCommOrderRecipientDetails> recipients = new ArrayList<>();
        recipients.add(recipient);
        order.setRecipients(recipients);

        return order;
    }
}
