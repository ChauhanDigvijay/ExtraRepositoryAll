package com.wearehathway.apps.incomm;

import com.wearehathway.apps.incomm.Interfaces.InCommCardServiceCallback;
import com.wearehathway.apps.incomm.Interfaces.InCommOrderServiceCallback;
import com.wearehathway.apps.incomm.Models.InCommCard;
import com.wearehathway.apps.incomm.Models.InCommOrder;
import com.wearehathway.apps.incomm.Models.InCommSubmitOrder;
import com.wearehathway.apps.incomm.Services.InCommCardService;
import com.wearehathway.apps.incomm.Services.InCommOrderService;
import com.wearehathway.apps.incomm.Services.InCommService;

/**
 * Created by Nauman Afzaal on 11/08/15.
 */
public class InCommCardServiceTestCases extends BaseTests
{
    public void testGetCardById()
    {
        final InCommSubmitOrder order = InCommTestDataUtils.getSubmitOrder();
        InCommOrderService.submitOrder(order, new InCommOrderServiceCallback()
        {
            @Override
            public void onOrderServiceCallback(InCommOrder inCommOrderResponse, Exception error)
            {
                assertNull("Invalid submit response", error);
                assertNotNull("Invalid order response", inCommOrderResponse);
                final int cardId = inCommOrderResponse.getSubmittedOrderItemGiftCards().get(0).getGiftCardId();
                InCommCardService.getCardById(cardId, false, new InCommCardServiceCallback()
                {
                    @Override
                    public void onCardServiceCallback(InCommCard card, Exception exception)
                    {
                        assertNull("Get card by id failed", exception);
                        assertEquals(cardId, card.getCardId());
                        verifyCard(card);
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();
    }

    public void testGetCardByNumber()
    {
        final InCommSubmitOrder order = InCommTestDataUtils.getSubmitOrder();
        InCommOrderService.submitOrder(order, new InCommOrderServiceCallback()
        {
            @Override
            public void onOrderServiceCallback(InCommOrder inCommOrderResponse, Exception error)
            {
                assertNull("Invalid submit response", error);
                assertNotNull("Invalid order response", inCommOrderResponse);
                final String cardNumber = inCommOrderResponse.getSubmittedOrderItemGiftCards().get(0).getGiftCardNumber();
                final String cardPin = inCommOrderResponse.getSubmittedOrderItemGiftCards().get(0).getPin();
                InCommCardService.getCardByNumber(cardNumber, cardPin, false, new InCommCardServiceCallback()
                {
                    @Override
                    public void onCardServiceCallback(InCommCard card, Exception exception)
                    {
                        assertNull("Get card by id failed", exception);
                        assertEquals(cardNumber, card.getCardNumber());
                        verifyCard(card);
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();
    }

    private static void verifyCard(InCommCard card)
    {
        String brandId = InCommService.getInstance().getConfiguration().brandId;
        assertNotNull("Invalid card detail", card);
        assertEquals("Invalid Brand ID", brandId, card.getBrandId());
        assertTrue("Invalid card id", card.getCardId() > 0);
        assertNotNull("Invalid Brand Name", card.getBrandName());
        assertNotNull("Invalid Image Url", card.getImageUrl());
        assertNotNull("Invalid Thumbnail Url", card.getThumbnailImageUrl());
        assertNotNull("Invalid Terms and conditions", card.getTermsAndConditions());
    }
}
