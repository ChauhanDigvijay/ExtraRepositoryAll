package com.wearehathway.apps.olo.Models;

/**
 * Created by Nauman Afzaal on 27/04/15.
 */
public class OloUser
{
    private String firstname;
    private String lastname;
    private String emailaddress;
    private String authtoken;
    private String contactnumber;
    private String reference;
    private String basketid;
    private String cardsuffix;

    public OloUser(String firstName, String lastName, String emailaddress, String cardsuffix)
    {
        this.firstname = firstName;
        this.lastname = lastName;
        this.emailaddress = emailaddress;
        this.cardsuffix = cardsuffix;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public String getEmailaddress()
    {
        return emailaddress;
    }

    public String getAuthtoken()
    {
        return authtoken;
    }

    public String getContactnumber()
    {
        return contactnumber;
    }

    public String getReference()
    {
        return reference;
    }

    public String getBasketid()
    {
        return basketid;
    }

    public String getCardsuffix()
    {
        return cardsuffix;
    }
}
