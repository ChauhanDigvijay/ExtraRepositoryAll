package com.wearehathway.apps.incomm;

import com.wearehathway.apps.incomm.Interfaces.InCommOrderServiceCallback;
import com.wearehathway.apps.incomm.Models.InCommOrder;
import com.wearehathway.apps.incomm.Models.InCommOrderItem;
import com.wearehathway.apps.incomm.Models.InCommOrderPurchaser;
import com.wearehathway.apps.incomm.Models.InCommOrderRecipientDetails;
import com.wearehathway.apps.incomm.Models.InCommReloadOrder;
import com.wearehathway.apps.incomm.Models.InCommSubmitOrder;
import com.wearehathway.apps.incomm.Models.InCommSubmitPayment;
import com.wearehathway.apps.incomm.Models.InCommSubmittedOrderItemGiftCards;
import com.wearehathway.apps.incomm.Services.InCommOrderService;
import com.wearehathway.apps.incomm.Services.InCommService;
import com.wearehathway.apps.incomm.Utils.InCommConstants;

import java.util.List;

/**
 * Created by Nauman Afzaal on 11/08/15.
 */
public class InCommOrderServiceTestCases extends BaseTests
{
    public void testSubmitOrder()
    {
        final InCommSubmitOrder order = InCommTestDataUtils.getSubmitOrder();
        InCommOrderService.submitOrder(order, new InCommOrderServiceCallback()
        {
            @Override
            public void onOrderServiceCallback(InCommOrder inCommOrderResponse, Exception error)
            {
                assertNull("Invalid submit response", error);
                assertNotNull("Invalid order response", inCommOrderResponse);
                verifyOrderResponse(inCommOrderResponse);

                latch.countDown();
            }
        });
        waitForCompletion();
    }

    //TODO Order reload not working
    public void notestOrderReload()
    {
        InCommOrderService.submitOrder(InCommTestDataUtils.getSubmitOrder(), new InCommOrderServiceCallback()
        {
            @Override
            public void onOrderServiceCallback(InCommOrder inCommOrderResponse, Exception error)
            {
                assertNull("Invalid submit response", error);
                assertNotNull("Invalid order response", inCommOrderResponse);

                List<InCommSubmittedOrderItemGiftCards> giftCards = inCommOrderResponse.getSubmittedOrderItemGiftCards();
                assertTrue("Invalid Gift Cards", !giftCards.isEmpty());
                InCommSubmittedOrderItemGiftCards giftCard = giftCards.get(0);
                assertNotNull(giftCard);

                double amount = 20;

                InCommSubmitPayment payment = new InCommSubmitPayment();
                payment.setAmount(amount);
                payment.setCity("Newyork");
                payment.setCountry("US");
                payment.setStateProvince("NY");
                payment.setFirstName("Android");
                payment.setLastName("Test");
                payment.setZipPostalCode("22123");
                payment.setOrderPaymentMethod(InCommConstants.OrderPaymentMethod.NoFundsCollected.val);

                InCommOrderPurchaser purchaser = new InCommOrderPurchaser();
                purchaser.setFirstName("Android");
                purchaser.setLastName("Tester");
                purchaser.setEmailAddress("androidtester@gmail.com");
                purchaser.setCountry("US");

                InCommReloadOrder reloadOrder = new InCommReloadOrder();
                reloadOrder.setAmount(amount);
                reloadOrder.setCardId(giftCard.getGiftCardId());
                reloadOrder.setCardPin(giftCard.getPin());
                reloadOrder.setIsTestMode(true);
                reloadOrder.setPayment(payment);
                reloadOrder.setPurchaser(purchaser);

                InCommOrderService.submitOrderReload(reloadOrder, new InCommOrderServiceCallback()
                {
                    @Override
                    public void onOrderServiceCallback(InCommOrder inCommOrderResponse, Exception error)
                    {
                        assertNull("Invalid submit response", error);
                        assertNotNull("Invalid reload response", inCommOrderResponse);
                        latch.countDown();

                    }
                });
            }
        });
        waitForCompletion();
    }

    private void verifyOrderResponse(InCommOrder inCommOrder)
    {
        assertNotNull("Invalid reload response", inCommOrder);
        assertEquals("Invalid Application Code", inCommOrder.getApplicationCode(), InCommService.getInstance().getConfiguration().clientId);
        assertNotNull("Invalid Order Id", inCommOrder.getId());
        assertNotNull("Gift card should not be null", inCommOrder.getSubmittedOrderItemGiftCards());
        for(InCommSubmittedOrderItemGiftCards giftCard : inCommOrder.getSubmittedOrderItemGiftCards())
        {
            assertTrue("Invalid, cardId", giftCard.getGiftCardId() > 0);
            assertNotNull("Invalid card number", giftCard.getGiftCardNumber());
            assertNotNull("Invalid token", giftCard.getToken());
        }
        assertEquals(inCommOrder.getResult(), InCommConstants.OrderStatus.Success.val);
        assertNotNull("Invlaid Created on", inCommOrder.getCreatedOn());
        assertNotNull("Payment should not be null", inCommOrder.getPayment());
        assertNotNull("Purchaser should not be null", inCommOrder.getPurchaser());

        assertNotNull("Invalid purchaser country", inCommOrder.getPurchaser().getCountry());
        assertNotNull("Invalid purchaser first name", inCommOrder.getPurchaser().getFirstName());
        assertNotNull("Invalid purchaser last name", inCommOrder.getPurchaser().getLastName());
        assertNotNull("Invalid purchaser email address", inCommOrder.getPurchaser().getEmailAddress());
        assertNotNull("Invalid recipient", inCommOrder.getRecipients());
        for(InCommOrderRecipientDetails recipientDetail : inCommOrder.getRecipients())
        {
            assertNotNull("Invalid recipient detail id", recipientDetail.getId());
            assertNotNull("Invalid gift items", recipientDetail.getItems());
            assertNotNull("Invalid deliver on", recipientDetail.getDeliverOn());
            for(InCommOrderItem item : recipientDetail.getItems())
            {
                assertNotNull("Invalid gift item cardId", item.getId());
            }
        }
    }
}
