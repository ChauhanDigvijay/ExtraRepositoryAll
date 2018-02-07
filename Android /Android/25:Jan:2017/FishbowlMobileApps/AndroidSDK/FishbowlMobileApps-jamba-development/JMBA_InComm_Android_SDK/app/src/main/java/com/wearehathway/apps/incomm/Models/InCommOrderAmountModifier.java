package com.wearehathway.apps.incomm.Models;

/**
 * Created by Nauman Afzaal on 18/08/15.
 */
public class InCommOrderAmountModifier
{
    private double Amount;
    private String AuxData1;
    private String AuxData2;
    private String AuxData3;
    private String AuxData4;
    private String ChargeTypeCode;
    private double Multiplier;
    private int Quantity;

    public double getAmount()
    {
        return Amount;
    }

    public String getAuxData1()
    {
        return AuxData1;
    }

    public String getAuxData2()
    {
        return AuxData2;
    }

    public String getAuxData3()
    {
        return AuxData3;
    }

    public String getAuxData4()
    {
        return AuxData4;
    }

    public String getChargeTypeCode()
    {
        return ChargeTypeCode;
    }

    public double getMultiplier()
    {
        return Multiplier;
    }

    public int getQuantity()
    {
        return Quantity;
    }
}
