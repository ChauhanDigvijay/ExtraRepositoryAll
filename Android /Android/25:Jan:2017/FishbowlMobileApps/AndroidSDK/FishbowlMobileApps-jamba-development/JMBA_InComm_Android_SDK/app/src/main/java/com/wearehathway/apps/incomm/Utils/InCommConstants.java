package com.wearehathway.apps.incomm.Utils;

/**
 * Created by Nauman Afzaal on 22/04/15.
 */
public class InCommConstants
{

    public static final String TimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public enum ResultCodes
    {
        Success(0), GeneralFailure(100), InvalidOperation(101), CardProcessorFailure(102), ValidationError(200), InvalidCredentials(300), InvalidSessionToken(301), UserNameAlreadyExists(302),
        RedirectRequired(303), InvalidCard(400), CardAlreadyRegistered(401);

        public int value;

        private ResultCodes(int val)
        {
            value = val;
        }
    }

    public enum BrandCardImageType
    {
        Physical("Physical"),
        Virtual("Virtual");

        public String val;

        BrandCardImageType(String val)
        {
            this.val = val;
        }
    }

    public enum CreditCardType
    {
        AMEX("AMEX"),
        Diners("Diners"),
        Discover("Discover"),
        JCB("JCB"),
        MasterCard("MasterCard"),
        PayPal("PayPal"),
        VISA("VISA");

        public String val;

        CreditCardType(String val)
        {
            this.val = val;
        }
    }

    public enum OrderPaymentMethod
    {
        ACH("ACH"),
        Cash("Cash"),
        Check("Check"),
        CorporateAccountBalancePayment("CorporateAccountBalancePayment"),
        CreditCard("CreditCard"),
        CreditCardAlternative("CreditCardAlternative"),
        CreditCardManualEntry("CreditCardManualEntry"),
        CustomerCredit("CustomerCredit"),
        EmployeePayment("EmployeePayment"),
        GeneralLedger("GeneralLedger"),
        NoFundsCollected("NoFundsCollected"),
        Other("Other"),
        Promotional("Promotional"),
        Reissue("Reissue"),
        ResellerIssued("ResellerIssued"),
        Return("Return"),
        Terms("Terms"),
        Upload("Upload");

        public String val;

        OrderPaymentMethod(String val)
        {
            this.val = val;
        }
    }

    public enum GiftCardStatus
    {
        Activated("Activated"),
        Cancelled("Cancelled"),
        Delayed("Delayed"),
        Error("Error"),
        Pending("Pending"),
        PendingBulk("PendingBulk"),
        Processing("Processing");

        public String val;

        GiftCardStatus(String val)
        {
            this.val = val;
        }
    }

    public enum OrderStatus
    {
        Cancelled("Cancelled"),
        GeneralOrderFailure("GeneralOrderFailure"),
        OrderPendingApproval("OrderPendingApproval"),
        OrderPendingInventory("OrderPendingInventory"),
        OrderPendingPayment("OrderPendingPayment"),
        Success("Success");

        public String val;

        OrderStatus(String val)
        {
            this.val = val;
        }
    }

    public enum TransactionPaymentStatus
    {
        AwaitingPayment("AwaitingPayment"),
        NoPaymentNeeded("NoPaymentNeeded"),
        Paid("Paid"),
        PendingVesta("PendingVesta"),
        Untracked("Untracked");
        
        public String val;

        TransactionPaymentStatus(String val)
        {
            this.val = val;
        }
    }

}
