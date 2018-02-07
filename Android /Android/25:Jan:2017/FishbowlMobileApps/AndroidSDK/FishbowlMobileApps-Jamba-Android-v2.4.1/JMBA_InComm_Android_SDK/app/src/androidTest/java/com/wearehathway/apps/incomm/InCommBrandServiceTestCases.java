package com.wearehathway.apps.incomm;

import com.wearehathway.apps.incomm.Interfaces.InCommBrandCallback;
import com.wearehathway.apps.incomm.Models.InCommBrand;
import com.wearehathway.apps.incomm.Models.InCommBrandCardImage;
import com.wearehathway.apps.incomm.Models.InCommBrandCreditCardType;
import com.wearehathway.apps.incomm.Models.InCommBrandDeliveryMethod;
import com.wearehathway.apps.incomm.Services.InCommBrandService;
import com.wearehathway.apps.incomm.Services.InCommService;

import static com.wearehathway.apps.incomm.Utils.InCommConstants.BrandCardImageType;
import static com.wearehathway.apps.incomm.Utils.InCommConstants.CreditCardType;

/**
 * Created by Nauman Afzaal on 13/08/15.
 */
public class InCommBrandServiceTestCases extends BaseTests
{
//    public void testGetAllBrands()
//    {
//        InCommBrandService.getAllBrands(new InCommBrandsCallback()
//        {
//            @Override
//            public void onAllBrandsCallback(List<InCommBrand> brands, Exception error)
//            {
//                assertNull("Get All Brand Failed", error);
//                assertNotNull("Invalid respose", brands);
//                assertTrue("Empty brands", brands.size() > 0);
//                for (InCommBrand brand : brands)
//                {
//                    validateBrand(brand);
//                }
//                latch.countDown();
//            }
//        });
//        waitForCompletion();
//    }

    public void testGetBrandWithid()
    {
        final String brandId = InCommService.getInstance().getConfiguration().brandId;
        InCommBrandService.getBrandWithId(brandId, new InCommBrandCallback()
        {
            @Override
            public void onBrandCallback(InCommBrand brand, Exception error)
            {
                assertNull("Get All Brand Failed", error);
                assertTrue(brand.getId().equalsIgnoreCase(brandId));
                validateBrand(brand);
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    private void validateBrand(InCommBrand brand)
    {
        assertNotNull("Invalid respose", brand);
        assertNotNull("Invalid BrandId", brand.getId());
        assertFalse("Brand Id Cannot be empty", brand.getId().isEmpty());
        assertNotNull(brand.getCardImages());
        assertTrue(brand.getCardImages().size() > 0);
        for(InCommBrandCardImage cardImage : brand.getCardImages())
        {
            assertNotNull(cardImage.getImageCode());
            assertNotNull(cardImage.getImageFileName());
            assertNotNull(cardImage.getImageUrl());
            assertNotNull(cardImage.getThumbnailImageUrl());
            assertTrue(cardImage.getImageType().equals(BrandCardImageType.Physical.val) || cardImage.getImageType().equals(BrandCardImageType.Virtual.val));

        }
        for(InCommBrandCreditCardType cardType : brand.getCreditCardTypes())
        {
            String creditCardType = cardType.getCreditCardType();
            assertTrue(creditCardType.equals(CreditCardType.AMEX.val) || creditCardType.equals(CreditCardType.Diners.val) || creditCardType.equals(CreditCardType.Discover.val) || creditCardType.equals(CreditCardType.JCB.val) || creditCardType.equals(CreditCardType.MasterCard.val) || creditCardType.equals(CreditCardType.PayPal.val) || creditCardType.equals(CreditCardType.VISA.val));
        }
        for(InCommBrandDeliveryMethod deliveryMethod : brand.getDeliveryMethods())
        {
            assertNotNull(deliveryMethod.getShippingMethod());
        }
        latch.countDown();
    }
}
