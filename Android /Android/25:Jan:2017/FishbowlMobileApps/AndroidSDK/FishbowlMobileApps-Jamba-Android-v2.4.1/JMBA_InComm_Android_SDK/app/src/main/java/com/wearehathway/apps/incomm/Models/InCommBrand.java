package com.wearehathway.apps.incomm.Models;

import java.util.List;

/**
 * Created by Nauman Afzaal on 13/08/15.
 */
public class InCommBrand
{
    private String Alias;
    private boolean AnyDiscountActive;
    private String BarcodeType;
    private Object[] BillingCountries;
    private Object[] BillingStates;
    private String BrandWebsiteUrl;
    private String CardholderAgreementUrl;
    private String CardSampleUrl;
    private String CurrencyCultureCode;
    private String CustomImageTermsAndConditionsHtml;
    private String CustomImageTermsAndConditionsUrl;
    private String DarkColor;
    private String DateTimeCultureCode;
    private String DefaultCountryCode;
    private String Description;
    private String FaqUrl;
    private String Id;
    private String LanguageCultureCode;
    private String LegalDisclaimer;
    private String LightColor;
    private String LogoHorizontalImageUrl;
    private String LogoImageUrl;
    private String LogoThumbnailImageUrl;
    private String LogoVerticalImageUrl;
    private String MediumColor;
    private String Name;
    private int NumericId;
    private String PrivacyPolicyUrl;
    private double PurchaseFee;
    private int[] Quantities;
    private String ShoppingCartUrl;
    private String SupportEmailAddress;
    private String SupportPhoneNumber;
    private String SupportsVariableAmountDenominations;
    private String TermsAndConditionsUrl;
    private double VariableAmountDenominationMaximumValue;
    private double VariableAmountDenominationMinimumValue;
    private List<InCommBrandCardImage> CardImages;
    private List<InCommBrandCategory>Categories	;
    private List<InCommBrandCreditCardType>CreditCardTypes;
    private List<InCommBrandDeliveryMethod> DeliveryMethods;

    public String getAlias()
    {
        return Alias;
    }

    public boolean isAnyDiscountActive()
    {
        return AnyDiscountActive;
    }

    public String getBarcodeType()
    {
        return BarcodeType;
    }

    public Object[] getBillingCountries()
    {
        return BillingCountries;
    }

    public Object[] getBillingStates()
    {
        return BillingStates;
    }

    public String getBrandWebsiteUrl()
    {
        return BrandWebsiteUrl;
    }

    public String getCardholderAgreementUrl()
    {
        return CardholderAgreementUrl;
    }

    public String getCardSampleUrl()
    {
        return CardSampleUrl;
    }

    public String getCurrencyCultureCode()
    {
        return CurrencyCultureCode;
    }

    public String getCustomImageTermsAndConditionsHtml()
    {
        return CustomImageTermsAndConditionsHtml;
    }

    public String getCustomImageTermsAndConditionsUrl()
    {
        return CustomImageTermsAndConditionsUrl;
    }

    public String getDarkColor()
    {
        return DarkColor;
    }

    public String getDateTimeCultureCode()
    {
        return DateTimeCultureCode;
    }

    public String getDefaultCountryCode()
    {
        return DefaultCountryCode;
    }

    public String getDescription()
    {
        return Description;
    }

    public String getFaqUrl()
    {
        return FaqUrl;
    }

    public String getId()
    {
        return Id;
    }

    public String getLanguageCultureCode()
    {
        return LanguageCultureCode;
    }

    public String getLegalDisclaimer()
    {
        return LegalDisclaimer;
    }

    public String getLightColor()
    {
        return LightColor;
    }

    public String getLogoHorizontalImageUrl()
    {
        return LogoHorizontalImageUrl;
    }

    public String getLogoImageUrl()
    {
        return LogoImageUrl;
    }

    public String getLogoThumbnailImageUrl()
    {
        return LogoThumbnailImageUrl;
    }

    public String getLogoVerticalImageUrl()
    {
        return LogoVerticalImageUrl;
    }

    public String getMediumColor()
    {
        return MediumColor;
    }

    public String getName()
    {
        return Name;
    }

    public int getNumericId()
    {
        return NumericId;
    }

    public String getPrivacyPolicyUrl()
    {
        return PrivacyPolicyUrl;
    }

    public double getPurchaseFee()
    {
        return PurchaseFee;
    }

    public int[] getQuantities()
    {
        return Quantities;
    }

    public String getShoppingCartUrl()
    {
        return ShoppingCartUrl;
    }

    public String getSupportEmailAddress()
    {
        return SupportEmailAddress;
    }

    public String getSupportPhoneNumber()
    {
        return SupportPhoneNumber;
    }

    public String getSupportsVariableAmountDenominations()
    {
        return SupportsVariableAmountDenominations;
    }

    public String getTermsAndConditionsUrl()
    {
        return TermsAndConditionsUrl;
    }

    public double getVariableAmountDenominationMaximumValue()
    {
        return VariableAmountDenominationMaximumValue;
    }

    public double getVariableAmountDenominationMinimumValue()
    {
        return VariableAmountDenominationMinimumValue;
    }

    public List<InCommBrandCardImage> getCardImages()
    {
        return CardImages;
    }

    public List<InCommBrandCategory> getCategories()
    {
        return Categories;
    }

    public List<InCommBrandCreditCardType> getCreditCardTypes()
    {
        return CreditCardTypes;
    }

    public List<InCommBrandDeliveryMethod> getDeliveryMethods()
    {
        return DeliveryMethods;
    }
}
