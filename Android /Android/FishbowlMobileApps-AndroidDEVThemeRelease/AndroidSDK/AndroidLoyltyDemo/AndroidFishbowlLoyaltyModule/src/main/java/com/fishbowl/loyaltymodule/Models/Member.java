package com.fishbowl.loyaltymodule.Models;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fishbowl.loyaltymodule.Services.FBThemeMobileSettingsService.customfielddatabasename;

/**
 **
 * Created by Digvijay Chauhan on 28/03/15.
 */
public class Member {
    public static  String CUSTOMER_ID="customerID";
    public static  String EMAIL="emailID";
    public static  String SMS_OPTIN="smsOpted";
    public static  String MAIL_OPTIN="emailOpted";
    public static  String PUSH_OPTIN="pushOpted";
    public static  String ADDRESSS_STATE="addressState";
    public static  String ADDRESS_CITY="addressCity";
    public static  String ADDRESS_STREET="addressLine1";
    public static  String PHONE= "cellPhone";
    public static  String FIRST_NAME="firstName";
    public static  String LAST_NAME="lastName";
    public static  String GENDER="customerGender";
    public static  String FAVOURITE_DEPARTMENT="favoriteDepartment";
    public static  String ZIP="addressZip";
    public static  String BIRTH_DATE="dateOfBirth";
    public static  String PASSWORD="loginPassword";
    public static  String HOME_STORE_ID="homeStoreID";;
    public static String LOYALITY_NO= "loyalityNo";
    public static String CREATED= "created";

    public static  Map<String,String> map= new HashMap<String, String>();


    public  int offercount;
    public  int rewardcount;

    public int getRewardcount() {
        return rewardcount;
    }

    public void setRewardcount(int rewardcount) {
        this.rewardcount = rewardcount;
    }

    public int getOffercount() {
        return offercount;
    }

    public void setOffercount(int offercount) {
        this.offercount = offercount;
    }

    public double getRewardpoint() {
        return rewardpoint;
    }

    public void setRewardpoint(double rewardpoint) {
        this.rewardpoint = rewardpoint;
    }

    public  double rewardpoint;

    public  String customerID;
    public String emailID;
    public String smsOpted;
    public  String emailOpted;
    public String pushOpted;
    public String addressState;
    public String addressCity;
    public String addressLine1;
    public String   cellPhone;
    public String   firstName;
    public String   lastName;
    public String   customerGender;
    public String   favoriteDepartment;
    public  String   addressZip;
    public String   dateOfBirth;
    public String   loginPassword;
    public String homeStoreID;
    public String loyalityNo;
    public String created;

    private static String custom_1;
    private String custom_2;
    private String custom_3;
    private String custom_4;
    private String custom_5;
    private String custom_6;
    private String custom_7;
    private String custom_8;
    private String custom_9;
    private String custom_10;
    private String custom_11;
    private String custom_12;
    private String custom_13;
    private String custom_14;
    private String custom_15;
    private String custom_16;
    private String custom_17;
    private String custom_18;
    private String custom_19;
    private String custom_20;
    private String custom_21;
    private String custom_22;
    private String custom_23;
    private String custom_24;
    private String custom_25;
    private String custom_26;

    private String custom_27;

    private String custom_28;

    private String custom_29;

    private String custom_30;

    private String custom_31;

    private String custom_32;

    private String custom_33;

    private String custom_34;

    private String custom_35;

    private String custom_36;

    private String custom_37;

    private String custom_38;

    private String custom_39;

    private String custom_40;

    private String custom_41;

    private String custom_42;

    private String custom_43;

    private String custom_44;

    private String custom_45;

    private String custom_46;

    private String custom_47;

    private String custom_48;

    private String custom_49;

    private String custom_50;

    private String custom_51;

    private String custom_52;

    private String custom_53;

    private String custom_54;

    private String custom_55;

    private String custom_56;

    private String custom_57;

    private String custom_58;

    private String custom_59;

    private String custom_60;

    private String custom_61;

    private String custom_62;

    private String custom_63;

    private String custom_64;

    private String custom_65;

    private String custom_66;

    private String custom_67;

    private String custom_68;

    private String custom_69;

    private String custom_70;

    private String custom_71;

    private String custom_72;

    private String custom_73;

    private String custom_74;

    private String custom_75;

    private String custom_76;

    private String custom_77;

    private String custom_78;

    private String custom_79;

    private String custom_80;

    private String custom_81;

    private String custom_82;

    private String custom_83;

    private String custom_84;

    private String custom_85;

    private String custom_86;

    private String custom_87;

    private String custom_88;

    private String custom_89;

    private String custom_90;

    private String custom_91;

    private String custom_92;

    private String custom_93;

    private String custom_94;

    private String custom_95;

    private String custom_96;

    private String custom_97;

    private String custom_98;

    public String getCustom_1() {

        return custom_1;


    }

    public void get(String name) {


    }

    public void setCustom_1(String custom_1) {
        map.put("custom_1",custom_1);

        this.custom_1 = custom_1;
    }

    public String getCustom_2() {
        return custom_2;
    }

    public void setCustom_2(String custom_2)
    {
        map.put("custom_2",custom_2);
        this.custom_2 = custom_2;
    }

    public String getCustom_3() {
        return custom_3;
    }

    public void setCustom_3(String custom_3)
    {
        map.put("custom_3",custom_3);
        this.custom_3 = custom_3;
    }

    public String getCustom_4() {
        return custom_4;
    }

    public void setCustom_4(String custom_4) {
        map.put("custom_4",custom_4);
        this.custom_4 = custom_4;
    }

    public String getCustom_5() {
        return custom_5;
    }

    public void setCustom_5(String custom_5) {
        map.put("custom_5",custom_5);
        this.custom_5 = custom_5;
    }

    public String getCustom_6() {
        return custom_6;
    }

    public void setCustom_6(String custom_6)
    {
        map.put("custom_6",custom_5);
        this.custom_6 = custom_6;
    }

    public String getCustom_7() {
        return custom_7;
    }

    public void setCustom_7(String custom_7)
    {
        map.put("custom_7",custom_7);
        this.custom_7 = custom_7;
    }

    public String getCustom_8() {
        return custom_8;
    }

    public void setCustom_8(String custom_8)
    {
        map.put("custom_8",custom_8);
        this.custom_8 = custom_8;
    }

    public String getCustom_9() {
        return custom_9;
    }

    public void setCustom_9(String custom_9) {
        this.custom_9 = custom_9;
    }

    public String getCustom_10() {
        return custom_10;
    }

    public void setCustom_10(String custom_10) {
        this.custom_10 = custom_10;
    }

    public String getCustom_11() {
        return custom_11;
    }

    public void setCustom_11(String custom_11) {
        this.custom_11 = custom_11;
    }

    public String getCustom_12() {
        return custom_12;
    }

    public void setCustom_12(String custom_12) {
        this.custom_12 = custom_12;
    }

    public String getCustom_13() {
        return custom_13;
    }

    public void setCustom_13(String custom_13) {
        this.custom_13 = custom_13;
    }

    public String getCustom_14() {
        return custom_14;
    }

    public void setCustom_14(String custom_14) {
        this.custom_14 = custom_14;
    }

    public String getCustom_15() {
        return custom_15;
    }

    public void setCustom_15(String custom_15) {
        this.custom_15 = custom_15;
    }

    public String getCustom_16() {
        return custom_16;
    }

    public void setCustom_16(String custom_16) {
        this.custom_16 = custom_16;
    }

    public String getCustom_17() {
        return custom_17;
    }

    public void setCustom_17(String custom_17) {
        this.custom_17 = custom_17;
    }

    public String getCustom_18() {
        return custom_18;
    }

    public void setCustom_18(String custom_18) {
        this.custom_18 = custom_18;
    }

    public String getCustom_19() {
        return custom_19;
    }

    public void setCustom_19(String custom_19) {
        this.custom_19 = custom_19;
    }

    public String getCustom_20() {
        return custom_20;
    }

    public void setCustom_20(String custom_20) {
        this.custom_20 = custom_20;
    }

    public String getCustom_21() {
        return custom_21;
    }

    public void setCustom_21(String custom_21) {
        this.custom_21 = custom_21;
    }

    public String getCustom_22() {
        return custom_22;
    }

    public void setCustom_22(String custom_22) {
        this.custom_22 = custom_22;
    }

    public String getCustom_23() {
        return custom_23;
    }

    public void setCustom_23(String custom_23) {
        this.custom_23 = custom_23;
    }

    public String getCustom_24() {
        return custom_24;
    }

    public void setCustom_24(String custom_24) {
        this.custom_24 = custom_24;
    }

    public String getCustom_25() {
        return custom_25;
    }

    public void setCustom_25(String custom_25) {
        this.custom_25 = custom_25;
    }

    public String getCustom_26() {
        return custom_26;
    }

    public void setCustom_26(String custom_26) {
        this.custom_26 = custom_26;
    }

    public String getCustom_27() {
        return custom_27;
    }

    public void setCustom_27(String custom_27) {
        this.custom_27 = custom_27;
    }

    public String getCustom_28() {
        return custom_28;
    }

    public void setCustom_28(String custom_28) {
        this.custom_28 = custom_28;
    }

    public String getCustom_29() {
        return custom_29;
    }

    public void setCustom_29(String custom_29) {
        this.custom_29 = custom_29;
    }

    public String getCustom_30() {
        return custom_30;
    }

    public void setCustom_30(String custom_30) {
        this.custom_30 = custom_30;
    }

    public String getCustom_31() {
        return custom_31;
    }

    public void setCustom_31(String custom_31) {
        this.custom_31 = custom_31;
    }

    public String getCustom_32() {
        return custom_32;
    }

    public void setCustom_32(String custom_32) {
        this.custom_32 = custom_32;
    }

    public String getCustom_33() {
        return custom_33;
    }

    public void setCustom_33(String custom_33) {
        this.custom_33 = custom_33;
    }

    public String getCustom_34() {
        return custom_34;
    }

    public void setCustom_34(String custom_34) {
        this.custom_34 = custom_34;
    }

    public String getCustom_35() {
        return custom_35;
    }

    public void setCustom_35(String custom_35) {
        this.custom_35 = custom_35;
    }

    public String getCustom_36() {
        return custom_36;
    }

    public void setCustom_36(String custom_36) {
        this.custom_36 = custom_36;
    }

    public String getCustom_37() {
        return custom_37;
    }

    public void setCustom_37(String custom_37) {
        this.custom_37 = custom_37;
    }

    public String getCustom_38() {
        return custom_38;
    }

    public void setCustom_38(String custom_38) {
        this.custom_38 = custom_38;
    }

    public String getCustom_39() {
        return custom_39;
    }

    public void setCustom_39(String custom_39) {
        this.custom_39 = custom_39;
    }

    public String getCustom_40() {
        return custom_40;
    }

    public void setCustom_40(String custom_40) {
        this.custom_40 = custom_40;
    }

    public String getCustom_41() {
        return custom_41;
    }

    public void setCustom_41(String custom_41) {
        this.custom_41 = custom_41;
    }

    public String getCustom_42() {
        return custom_42;
    }

    public void setCustom_42(String custom_42) {
        this.custom_42 = custom_42;
    }

    public String getCustom_43() {
        return custom_43;
    }

    public void setCustom_43(String custom_43) {
        this.custom_43 = custom_43;
    }

    public String getCustom_44() {
        return custom_44;
    }

    public void setCustom_44(String custom_44) {
        this.custom_44 = custom_44;
    }

    public String getCustom_45() {
        return custom_45;
    }

    public void setCustom_45(String custom_45) {
        this.custom_45 = custom_45;
    }

    public String getCustom_46() {
        return custom_46;
    }

    public void setCustom_46(String custom_46) {
        this.custom_46 = custom_46;
    }

    public String getCustom_47() {
        return custom_47;
    }

    public void setCustom_47(String custom_47) {
        this.custom_47 = custom_47;
    }

    public String getCustom_48() {
        return custom_48;
    }

    public void setCustom_48(String custom_48) {
        this.custom_48 = custom_48;
    }

    public String getCustom_49() {
        return custom_49;
    }

    public void setCustom_49(String custom_49) {
        this.custom_49 = custom_49;
    }

    public String getCustom_50() {
        return custom_50;
    }

    public void setCustom_50(String custom_50) {
        this.custom_50 = custom_50;
    }

    public String getCustom_51() {
        return custom_51;
    }

    public void setCustom_51(String custom_51) {
        this.custom_51 = custom_51;
    }

    public String getCustom_52() {
        return custom_52;
    }

    public void setCustom_52(String custom_52) {
        this.custom_52 = custom_52;
    }

    public String getCustom_53() {
        return custom_53;
    }

    public void setCustom_53(String custom_53) {
        this.custom_53 = custom_53;
    }

    public String getCustom_54() {
        return custom_54;
    }

    public void setCustom_54(String custom_54) {
        this.custom_54 = custom_54;
    }

    public String getCustom_55() {
        return custom_55;
    }

    public void setCustom_55(String custom_55) {
        this.custom_55 = custom_55;
    }

    public String getCustom_56() {
        return custom_56;
    }

    public void setCustom_56(String custom_56) {
        this.custom_56 = custom_56;
    }

    public String getCustom_57() {
        return custom_57;
    }

    public void setCustom_57(String custom_57) {
        this.custom_57 = custom_57;
    }

    public String getCustom_58() {
        return custom_58;
    }

    public void setCustom_58(String custom_58) {
        this.custom_58 = custom_58;
    }

    public String getCustom_59() {
        return custom_59;
    }

    public void setCustom_59(String custom_59) {
        this.custom_59 = custom_59;
    }

    public String getCustom_60() {
        return custom_60;
    }

    public void setCustom_60(String custom_60) {
        this.custom_60 = custom_60;
    }

    public String getCustom_61() {
        return custom_61;
    }

    public void setCustom_61(String custom_61) {
        this.custom_61 = custom_61;
    }

    public String getCustom_62() {
        return custom_62;
    }

    public void setCustom_62(String custom_62) {
        this.custom_62 = custom_62;
    }

    public String getCustom_63() {
        return custom_63;
    }

    public void setCustom_63(String custom_63) {
        this.custom_63 = custom_63;
    }

    public String getCustom_64() {
        return custom_64;
    }

    public void setCustom_64(String custom_64) {
        this.custom_64 = custom_64;
    }

    public String getCustom_65() {
        return custom_65;
    }

    public void setCustom_65(String custom_65) {
        this.custom_65 = custom_65;
    }

    public String getCustom_66() {
        return custom_66;
    }

    public void setCustom_66(String custom_66) {
        this.custom_66 = custom_66;
    }

    public String getCustom_67() {
        return custom_67;
    }

    public void setCustom_67(String custom_67) {
        this.custom_67 = custom_67;
    }

    public String getCustom_68() {
        return custom_68;
    }

    public void setCustom_68(String custom_68) {
        this.custom_68 = custom_68;
    }

    public String getCustom_69() {
        return custom_69;
    }

    public void setCustom_69(String custom_69) {
        this.custom_69 = custom_69;
    }

    public String getCustom_70() {
        return custom_70;
    }

    public void setCustom_70(String custom_70) {
        this.custom_70 = custom_70;
    }

    public String getCustom_71() {
        return custom_71;
    }

    public void setCustom_71(String custom_71) {
        this.custom_71 = custom_71;
    }

    public String getCustom_72() {
        return custom_72;
    }

    public void setCustom_72(String custom_72) {
        this.custom_72 = custom_72;
    }

    public String getCustom_73() {
        return custom_73;
    }

    public void setCustom_73(String custom_73) {
        this.custom_73 = custom_73;
    }

    public String getCustom_74() {
        return custom_74;
    }

    public void setCustom_74(String custom_74) {
        this.custom_74 = custom_74;
    }

    public String getCustom_75() {
        return custom_75;
    }

    public void setCustom_75(String custom_75) {
        this.custom_75 = custom_75;
    }

    public String getCustom_76() {
        return custom_76;
    }

    public void setCustom_76(String custom_76) {
        this.custom_76 = custom_76;
    }

    public String getCustom_77() {
        return custom_77;
    }

    public void setCustom_77(String custom_77) {
        this.custom_77 = custom_77;
    }

    public String getCustom_78() {
        return custom_78;
    }

    public void setCustom_78(String custom_78) {
        this.custom_78 = custom_78;
    }

    public String getCustom_79() {
        return custom_79;
    }

    public void setCustom_79(String custom_79) {
        this.custom_79 = custom_79;
    }

    public String getCustom_80() {
        return custom_80;
    }

    public void setCustom_80(String custom_80) {
        this.custom_80 = custom_80;
    }

    public String getCustom_81() {
        return custom_81;
    }

    public void setCustom_81(String custom_81) {
        this.custom_81 = custom_81;
    }

    public String getCustom_82() {
        return custom_82;
    }

    public void setCustom_82(String custom_82) {
        this.custom_82 = custom_82;
    }

    public String getCustom_83() {
        return custom_83;
    }

    public void setCustom_83(String custom_83) {
        this.custom_83 = custom_83;
    }

    public String getCustom_84() {
        return custom_84;
    }

    public void setCustom_84(String custom_84) {
        this.custom_84 = custom_84;
    }

    public String getCustom_85() {
        return custom_85;
    }

    public void setCustom_85(String custom_85) {
        this.custom_85 = custom_85;
    }

    public String getCustom_86() {
        return custom_86;
    }

    public void setCustom_86(String custom_86) {
        this.custom_86 = custom_86;
    }

    public String getCustom_87() {
        return custom_87;
    }

    public void setCustom_87(String custom_87) {
        this.custom_87 = custom_87;
    }

    public String getCustom_88() {
        return custom_88;
    }

    public void setCustom_88(String custom_88) {
        this.custom_88 = custom_88;
    }

    public String getCustom_89() {
        return custom_89;
    }

    public void setCustom_89(String custom_89) {
        this.custom_89 = custom_89;
    }

    public String getCustom_90() {
        return custom_90;
    }

    public void setCustom_90(String custom_90) {
        this.custom_90 = custom_90;
    }

    public String getCustom_91() {
        return custom_91;
    }

    public void setCustom_91(String custom_91) {
        this.custom_91 = custom_91;
    }

    public String getCustom_92() {
        return custom_92;
    }

    public void setCustom_92(String custom_92) {
        this.custom_92 = custom_92;
    }

    public String getCustom_93() {
        return custom_93;
    }

    public void setCustom_93(String custom_93) {
        this.custom_93 = custom_93;
    }

    public String getCustom_94() {
        return custom_94;
    }

    public void setCustom_94(String custom_94) {
        this.custom_94 = custom_94;
    }

    public String getCustom_95() {
        return custom_95;
    }

    public void setCustom_95(String custom_95) {
        this.custom_95 = custom_95;
    }

    public String getCustom_96() {
        return custom_96;
    }

    public void setCustom_96(String custom_96) {
        this.custom_96 = custom_96;
    }

    public String getCustom_97() {
        return custom_97;
    }

    public void setCustom_97(String custom_97) {
        this.custom_97 = custom_97;
    }

    public String getCustom_98() {
        return custom_98;
    }

    public void setCustom_98(String custom_98) {
        this.custom_98 = custom_98;
    }

    public String getCustom_99() {
        return custom_99;
    }

    public void setCustom_99(String custom_99) {
        this.custom_99 = custom_99;
    }

    public String getCustom_100() {
        return custom_100;
    }

    public void setCustom_100(String custom_100) {
        this.custom_100 = custom_100;
    }

    private String custom_99;

    private String custom_100;


    String[] customfieldarray = new String[100];
    String[] memberfieldarray = new String[100];
    List<String> customfield = new ArrayList<String>(100);

    public void initWithJson(JSONObject jsonObj) {

        try {


            if (jsonObj.has(CUSTOMER_ID) && !jsonObj.isNull(CUSTOMER_ID)) {
                customerID = jsonObj.getString(CUSTOMER_ID);
            }

            if (jsonObj.has(EMAIL) && !jsonObj.isNull(EMAIL)) {
                emailID = jsonObj.getString(EMAIL);
            }
            if (jsonObj.has(SMS_OPTIN) && !jsonObj.isNull(SMS_OPTIN)) {
                smsOpted = jsonObj.getString(SMS_OPTIN);
            }

            if (jsonObj.has(MAIL_OPTIN) && !jsonObj.isNull(MAIL_OPTIN)) {
                emailOpted = jsonObj.getString(MAIL_OPTIN);
            }

            if (jsonObj.has(PUSH_OPTIN) && !jsonObj.isNull(PUSH_OPTIN)) {
                pushOpted = jsonObj.getString(PUSH_OPTIN);
            }



            if (jsonObj.has(ADDRESSS_STATE) && !jsonObj.isNull(ADDRESSS_STATE)) {
                addressState = jsonObj.getString(ADDRESSS_STATE);


            }

            if (jsonObj.has(ADDRESS_CITY) && !jsonObj.isNull(ADDRESS_CITY)) {
                addressCity = jsonObj.getString(ADDRESS_CITY);
            }
            if (jsonObj.has(ADDRESS_STREET) && !jsonObj.isNull(ADDRESS_STREET)) {
                addressLine1 = jsonObj.getString(ADDRESS_STREET);
            }
            if (jsonObj.has(PHONE) && !jsonObj.isNull(PHONE)) {
                cellPhone = jsonObj.getString(PHONE);
            }

            if (jsonObj.has(FIRST_NAME) && !jsonObj.isNull(FIRST_NAME)) {
                firstName = jsonObj.getString(FIRST_NAME);
            }
            if (jsonObj.has(LAST_NAME) && !jsonObj.isNull(LAST_NAME)) {
                lastName = jsonObj.getString(LAST_NAME);
            }

            if (jsonObj.has(GENDER) && !jsonObj.isNull(GENDER)) {
                customerGender = jsonObj.getString(GENDER);
            }

            if (jsonObj.has(FAVOURITE_DEPARTMENT) && !jsonObj.isNull(FAVOURITE_DEPARTMENT)) {
                favoriteDepartment = jsonObj.getString(FAVOURITE_DEPARTMENT);
            }

            if (jsonObj.has(ZIP) && !jsonObj.isNull(ZIP)) {
                addressZip = jsonObj.getString(ZIP);
            }

            if (jsonObj.has(BIRTH_DATE) && !jsonObj.isNull(BIRTH_DATE)) {
                dateOfBirth = jsonObj.getString(BIRTH_DATE);
            }

            if (jsonObj.has(PASSWORD) && !jsonObj.isNull(PASSWORD)) {
                loginPassword = jsonObj.getString(PASSWORD);
            }
            if (jsonObj.has(HOME_STORE_ID) && !jsonObj.isNull(HOME_STORE_ID) && Integer.valueOf(jsonObj.getString(HOME_STORE_ID)) > 0) {
                homeStoreID = jsonObj.getString(HOME_STORE_ID);
            }

            if (jsonObj.has(LOYALITY_NO) && !jsonObj.isNull(LOYALITY_NO)) {
                loyalityNo = jsonObj.getString(LOYALITY_NO);
            }

            if (jsonObj.has(CREATED) && !jsonObj.isNull(CREATED)) {
                created = jsonObj.getString(CREATED);
            }


            if(customfielddatabasename.size()>0)
            {

                for (int i = 0; i < customfielddatabasename.size(); i++)
                {
                    String data = customfielddatabasename.get(i);
                    if (jsonObj.has(data)) {

                        map.put(data,jsonObj.getString(data));
                    }

                }
            }


            setCustom_1(jsonObj.getString("Custom_1") == null ? null : String.valueOf(jsonObj.getString("Custom_1")));
            setCustom_2(jsonObj.getString("Custom_2") == null ? null : String.valueOf(jsonObj.getString("Custom_2")));
            setCustom_3(jsonObj.getString("Custom_3") == null ? null : String.valueOf(jsonObj.getString("Custom_3")));
            setCustom_4(jsonObj.getString("Custom_4") == null ? null : String.valueOf(jsonObj.getString("Custom_4")));
            setCustom_5(jsonObj.getString("Custom_5") == null ? null : String.valueOf(jsonObj.getString("Custom_5")));
            setCustom_6(jsonObj.getString("Custom_6") == null ? null : String.valueOf(jsonObj.getString("Custom_6")));
            setCustom_7(jsonObj.getString("Custom_7") == null ? null : String.valueOf(jsonObj.getString("Custom_7")));
            setCustom_8(jsonObj.getString("Custom_8") == null ? null : String.valueOf(jsonObj.getString("Custom_8")));
            setCustom_9(jsonObj.getString("Custom_9") == null ? null : String.valueOf(jsonObj.getString("Custom_9")));
            setCustom_10(jsonObj.getString("Custom_10") == null ? null : String.valueOf(jsonObj.getString("Custom_10")));
            setCustom_11(jsonObj.getString("Custom_11") == null ? null : String.valueOf(jsonObj.getString("Custom_11")));
            setCustom_12(jsonObj.getString("Custom_12") == null ? null : String.valueOf(jsonObj.getString("Custom_12")));
            setCustom_13(jsonObj.getString("Custom_13") == null ? null : String.valueOf(jsonObj.getString("Custom_13")));
            setCustom_14(jsonObj.getString("Custom_14") == null ? null : String.valueOf(jsonObj.getString("Custom_14")));
            setCustom_15(jsonObj.getString("Custom_15") == null ? null : String.valueOf(jsonObj.getString("Custom_15")));
            setCustom_16(jsonObj.getString("Custom_16") == null ? null : String.valueOf(jsonObj.getString("Custom_16")));
            setCustom_17(jsonObj.getString("Custom_17") == null ? null : String.valueOf(jsonObj.getString("Custom_17")));
            setCustom_18(jsonObj.getString("Custom_18") == null ? null : String.valueOf(jsonObj.getString("Custom_18")));
            setCustom_19(jsonObj.getString("Custom_19") == null ? null : String.valueOf(jsonObj.getString("Custom_19")));
            setCustom_20(jsonObj.getString("Custom_20") == null ? null : String.valueOf(jsonObj.getString("Custom_20")));
            setCustom_21(jsonObj.getString("Custom_21") == null ? null : String.valueOf(jsonObj.getString("Custom_21")));
            setCustom_22(jsonObj.getString("Custom_22") == null ? null : String.valueOf(jsonObj.getString("Custom_22")));
            setCustom_23(jsonObj.getString("Custom_23") == null ? null : String.valueOf(jsonObj.getString("Custom_23")));
            setCustom_24(jsonObj.getString("Custom_24") == null ? null : String.valueOf(jsonObj.getString("Custom_24")));
            setCustom_25(jsonObj.getString("Custom_25") == null ? null : String.valueOf(jsonObj.getString("Custom_25")));
            setCustom_26(jsonObj.getString("Custom_26") == null ? null : String.valueOf(jsonObj.getString("Custom_26")));

            setCustom_27(jsonObj.getString("Custom_27") == null ? null : String.valueOf(jsonObj.getString("Custom_27")));

            setCustom_28(jsonObj.getString("Custom_28") == null ? null : String.valueOf(jsonObj.getString("Custom_28")));

            setCustom_29(jsonObj.getString("Custom_29") == null ? null : String.valueOf(jsonObj.getString("Custom_29")));

            setCustom_30(jsonObj.getString("Custom_30") == null ? null : String.valueOf(jsonObj.getString("Custom_30")));

            setCustom_31(jsonObj.getString("Custom_31") == null ? null : String.valueOf(jsonObj.getString("Custom_31")));

            setCustom_32(jsonObj.getString("Custom_32") == null ? null : String.valueOf(jsonObj.getString("Custom_32")));

            setCustom_33(jsonObj.getString("Custom_33") == null ? null : String.valueOf(jsonObj.getString("Custom_33")));

            setCustom_34(jsonObj.getString("Custom_34") == null ? null : String.valueOf(jsonObj.getString("Custom_34")));

            setCustom_35(jsonObj.getString("Custom_35") == null ? null : String.valueOf(jsonObj.getString("Custom_35")));

            setCustom_36(jsonObj.getString("Custom_36") == null ? null : String.valueOf(jsonObj.getString("Custom_36")));

            setCustom_37(jsonObj.getString("Custom_37") == null ? null : String.valueOf(jsonObj.getString("Custom_37")));

            setCustom_38(jsonObj.getString("Custom_38") == null ? null : String.valueOf(jsonObj.getString("Custom_38")));

            setCustom_39(jsonObj.getString("Custom_39") == null ? null : String.valueOf(jsonObj.getString("Custom_39")));

            setCustom_40(jsonObj.getString("Custom_40") == null ? null : String.valueOf(jsonObj.getString("Custom_40")));

            setCustom_41(jsonObj.getString("Custom_41") == null ? null : String.valueOf(jsonObj.getString("Custom_41")));

            setCustom_42(jsonObj.getString("Custom_42") == null ? null : String.valueOf(jsonObj.getString("Custom_42")));

            setCustom_43(jsonObj.getString("Custom_43") == null ? null : String.valueOf(jsonObj.getString("Custom_43")));

            setCustom_44(jsonObj.getString("Custom_44") == null ? null : String.valueOf(jsonObj.getString("Custom_44")));

            setCustom_45(jsonObj.getString("Custom_45") == null ? null : String.valueOf(jsonObj.getString("Custom_45")));

            setCustom_46(jsonObj.getString("Custom_46") == null ? null : String.valueOf(jsonObj.getString("Custom_46")));

            setCustom_47(jsonObj.getString("Custom_47") == null ? null : String.valueOf(jsonObj.getString("Custom_47")));

            setCustom_48(jsonObj.getString("Custom_48") == null ? null : String.valueOf(jsonObj.getString("Custom_48")));

            setCustom_49(jsonObj.getString("Custom_49") == null ? null : String.valueOf(jsonObj.getString("Custom_49")));

            setCustom_50(jsonObj.getString("Custom_50") == null ? null : String.valueOf(jsonObj.getString("Custom_50")));

            setCustom_51(jsonObj.getString("Custom_51") == null ? null : String.valueOf(jsonObj.getString("Custom_51")));

            setCustom_52(jsonObj.getString("Custom_52") == null ? null : String.valueOf(jsonObj.getString("Custom_52")));

            setCustom_53(jsonObj.getString("Custom_53") == null ? null : String.valueOf(jsonObj.getString("Custom_53")));

            setCustom_54(jsonObj.getString("Custom_54") == null ? null : String.valueOf(jsonObj.getString("Custom_54")));

            setCustom_55(jsonObj.getString("Custom_55") == null ? null : String.valueOf(jsonObj.getString("Custom_55")));

            setCustom_56(jsonObj.getString("Custom_56") == null ? null : String.valueOf(jsonObj.getString("Custom_56")));

            setCustom_57(jsonObj.getString("Custom_57") == null ? null : String.valueOf(jsonObj.getString("Custom_57")));

            setCustom_58(jsonObj.getString("Custom_58") == null ? null : String.valueOf(jsonObj.getString("Custom_58")));

            setCustom_59(jsonObj.getString("Custom_59") == null ? null : String.valueOf(jsonObj.getString("Custom_59")));

            setCustom_60(jsonObj.getString("Custom_60") == null ? null : String.valueOf(jsonObj.getString("Custom_60")));

            setCustom_61(jsonObj.getString("Custom_61") == null ? null : String.valueOf(jsonObj.getString("Custom_61")));

            setCustom_62(jsonObj.getString("Custom_62") == null ? null : String.valueOf(jsonObj.getString("Custom_62")));

            setCustom_63(jsonObj.getString("Custom_63") == null ? null : String.valueOf(jsonObj.getString("Custom_63")));

            setCustom_64(jsonObj.getString("Custom_64") == null ? null : String.valueOf(jsonObj.getString("Custom_64")));

            setCustom_65(jsonObj.getString("Custom_65") == null ? null : String.valueOf(jsonObj.getString("Custom_65")));

            setCustom_66(jsonObj.getString("Custom_66") == null ? null : String.valueOf(jsonObj.getString("Custom_66")));

            setCustom_67(jsonObj.getString("Custom_67") == null ? null : String.valueOf(jsonObj.getString("Custom_67")));

            setCustom_68(jsonObj.getString("Custom_68") == null ? null : String.valueOf(jsonObj.getString("Custom_68")));

            setCustom_69(jsonObj.getString("Custom_69") == null ? null : String.valueOf(jsonObj.getString("Custom_69")));

            setCustom_70(jsonObj.getString("Custom_70") == null ? null : String.valueOf(jsonObj.getString("Custom_70")));

            setCustom_71(jsonObj.getString("Custom_71") == null ? null : String.valueOf(jsonObj.getString("Custom_71")));

            setCustom_72(jsonObj.getString("Custom_72") == null ? null : String.valueOf(jsonObj.getString("Custom_72")));

            setCustom_73(jsonObj.getString("Custom_73") == null ? null : String.valueOf(jsonObj.getString("Custom_73")));

            setCustom_74(jsonObj.getString("Custom_74") == null ? null : String.valueOf(jsonObj.getString("Custom_74")));

            setCustom_75(jsonObj.getString("Custom_75") == null ? null : String.valueOf(jsonObj.getString("Custom_75")));

            setCustom_76(jsonObj.getString("Custom_76") == null ? null : String.valueOf(jsonObj.getString("Custom_76")));

            setCustom_77(jsonObj.getString("Custom_77") == null ? null : String.valueOf(jsonObj.getString("Custom_77")));

            setCustom_78(jsonObj.getString("Custom_78") == null ? null : String.valueOf(jsonObj.getString("Custom_78")));

            setCustom_79(jsonObj.getString("Custom_79") == null ? null : String.valueOf(jsonObj.getString("Custom_79")));

            setCustom_80(jsonObj.getString("Custom_80") == null ? null : String.valueOf(jsonObj.getString("Custom_80")));

            setCustom_81(jsonObj.getString("Custom_81") == null ? null : String.valueOf(jsonObj.getString("Custom_81")));

            setCustom_82(jsonObj.getString("Custom_82") == null ? null : String.valueOf(jsonObj.getString("Custom_82")));

            setCustom_83(jsonObj.getString("Custom_83") == null ? null : String.valueOf(jsonObj.getString("Custom_83")));

            setCustom_84(jsonObj.getString("Custom_84") == null ? null : String.valueOf(jsonObj.getString("Custom_84")));

            setCustom_85(jsonObj.getString("Custom_85") == null ? null : String.valueOf(jsonObj.getString("Custom_85")));

            setCustom_86(jsonObj.getString("Custom_86") == null ? null : String.valueOf(jsonObj.getString("Custom_86")));

            setCustom_87(jsonObj.getString("Custom_87") == null ? null : String.valueOf(jsonObj.getString("Custom_87")));

            setCustom_88(jsonObj.getString("Custom_88") == null ? null : String.valueOf(jsonObj.getString("Custom_88")));

            setCustom_89(jsonObj.getString("Custom_89") == null ? null : String.valueOf(jsonObj.getString("Custom_89")));

            setCustom_90(jsonObj.getString("Custom_90") == null ? null : String.valueOf(jsonObj.getString("Custom_90")));

            setCustom_91(jsonObj.getString("Custom_91") == null ? null : String.valueOf(jsonObj.getString("Custom_91")));

            setCustom_92(jsonObj.getString("Custom_92") == null ? null : String.valueOf(jsonObj.getString("Custom_92")));

            setCustom_93(jsonObj.getString("Custom_93") == null ? null : String.valueOf(jsonObj.getString("Custom_93")));

            setCustom_94(jsonObj.getString("Custom_94") == null ? null : String.valueOf(jsonObj.getString("Custom_94")));

            setCustom_95(jsonObj.getString("Custom_95") == null ? null : String.valueOf(jsonObj.getString("Custom_95")));

            setCustom_96(jsonObj.getString("Custom_96") == null ? null : String.valueOf(jsonObj.getString("Custom_96")));

            setCustom_97(jsonObj.getString("Custom_97") == null ? null : String.valueOf(jsonObj.getString("Custom_97")));

            setCustom_98(jsonObj.getString("Custom_98") == null ? null : String.valueOf(jsonObj.getString("Custom_98")));

            setCustom_99(jsonObj.getString("Custom_99") == null ? null : String.valueOf(jsonObj.getString("Custom_99")));

            setCustom_100(jsonObj.getString("Custom_100") == null ? null : String.valueOf(jsonObj.getString("Custom_100")));










        }catch (Exception e){
            e.printStackTrace();

        }

    }


}
