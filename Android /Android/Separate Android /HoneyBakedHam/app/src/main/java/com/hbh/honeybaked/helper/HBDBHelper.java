package com.hbh.honeybaked.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.hbh.honeybaked.supportingfiles.Utility;

public class HBDBHelper {
    SQLiteDatabase hbha_db;
    Context mContext;

    public HBDBHelper(Context context) {
        this.mContext = context;
    }

    public void openDb() {
        this.hbha_db = this.mContext.openOrCreateDatabase("HBHADB", 0, null);
    }

    public void closeDb() {
        if (this.hbha_db != null && this.hbha_db.isOpen()) {
            this.hbha_db.close();
            this.hbha_db = null;
        }
    }

    public void createDatabase() {
        if (!this.mContext.getApplicationContext().getDatabasePath("HBHADB").exists()) {
            openDb();
            this.hbha_db.execSQL("CREATE TABLE IF NOT EXISTS hbha_menu_table(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,title TEXT NOT NULL,sub_title TEXT NOT NULL,icon_name TEXT NOT NULL,web_url TEXT NOT NULL);");
            this.hbha_db.execSQL("CREATE TABLE IF NOT EXISTS hbha_api_url_table(id INTEGER,sub_title TEXT NOT NULL,api_url_key TEXT NOT NULL,api_url_value TEXT NOT NULL);");
            this.hbha_db.execSQL("CREATE TABLE IF NOT EXISTS hbha_offer_table(campaignId INTEGER, campaignTitle TEXT, campaignDescription TEXT,validityEndDateTime TEXT,promotionID INTEGER, channelTypeID INTEGER, isPMOffer INTEGER,mailingId INTEGER,templateId INTEGER,channelId INTEGER, campaignType TEXT,couponURL TEXT,promotionCode TEXT)");
            this.hbha_db.execSQL("CREATE TABLE IF NOT EXISTS hbha_reward_table(campaignId INTEGER, campaignTitle TEXT, campaignDescription TEXT,validityEndDateTime TEXT,promotionID INTEGER, channelTypeID INTEGER, isPMOffer INTEGER,mailingId INTEGER,templateId INTEGER,channelId INTEGER, campaignType TEXT,couponURL TEXT,promotionCode TEXT)");
        }
    }

    public long insertMenu(String title, String sub_title, String icon_name, String web_url) {
        ContentValues menu_details = new ContentValues();
        menu_details.put("title", title);
        menu_details.put("sub_title", sub_title);
        menu_details.put("icon_name", icon_name);
        menu_details.put("web_url", web_url);
        return this.hbha_db.insert("hbha_menu_table", null, menu_details);
    }

    public long insertAPIUrl(int id, String sub_title, String api_url_key, String api_url_value) {
        ContentValues api_url_details = new ContentValues();
        api_url_details.put("id", Integer.valueOf(id));
        api_url_details.put("sub_title", sub_title);
        api_url_details.put("api_url_key", api_url_key);
        api_url_details.put("api_url_value", api_url_value);
        return this.hbha_db.insert("hbha_api_url_table", null, api_url_details);
    }

    public long insertRewardsDetails(int campaignId, String campaignTitle, String campaignDescription, String validityEndDateTime, int promotionID, int channelTypeID, int isPMOffer, int mailingId, int templateId, int channelId, String campaignType, String couponURL, String promotionCode) {
        ContentValues offer_deatils = new ContentValues();
        offer_deatils.put("campaignId", Integer.valueOf(campaignId));
        offer_deatils.put("campaignTitle", campaignTitle);
        offer_deatils.put("campaignDescription", campaignDescription);
        offer_deatils.put("validityEndDateTime", validityEndDateTime);
        offer_deatils.put("promotionID", Integer.valueOf(promotionID));
        offer_deatils.put("channelTypeID", Integer.valueOf(channelTypeID));
        offer_deatils.put("isPMOffer", Integer.valueOf(isPMOffer));
        offer_deatils.put("mailingId", Integer.valueOf(mailingId));
        offer_deatils.put("templateId", Integer.valueOf(templateId));
        offer_deatils.put("channelId", Integer.valueOf(channelId));
        offer_deatils.put("campaignType", campaignType);
        offer_deatils.put("couponURL", couponURL);
        offer_deatils.put("promotionCode", promotionCode);
        return this.hbha_db.insert("hbha_reward_table", null, offer_deatils);
    }

    public long insertOfferDetails(int campaignId, String campaignTitle, String campaignDescription, String validityEndDateTime, int promotionID, int channelTypeID, int isPMOffer, int mailingId, int templateId, int channelId, String campaignType, String couponURL, String promotionCode) {
        ContentValues offer_deatils = new ContentValues();
        offer_deatils.put("campaignId", Integer.valueOf(campaignId));
        offer_deatils.put("campaignTitle", campaignTitle);
        offer_deatils.put("campaignDescription", campaignDescription);
        offer_deatils.put("validityEndDateTime", validityEndDateTime);
        offer_deatils.put("promotionID", Integer.valueOf(promotionID));
        offer_deatils.put("channelTypeID", Integer.valueOf(channelTypeID));
        offer_deatils.put("isPMOffer", Integer.valueOf(isPMOffer));
        offer_deatils.put("mailingId", Integer.valueOf(mailingId));
        offer_deatils.put("templateId", Integer.valueOf(templateId));
        offer_deatils.put("channelId", Integer.valueOf(channelId));
        offer_deatils.put("campaignType", campaignType);
        offer_deatils.put("couponURL", couponURL);
        offer_deatils.put("promotionCode", promotionCode);
        return this.hbha_db.insert("hbha_offer_table", null, offer_deatils);
    }

    public Cursor getStringQuery(String query, String[] where) {
        return this.hbha_db.rawQuery(query, where);
    }

    public void updateTables(String query, String[] where) {
        if (Utility.isEmpty(where)) {
            this.hbha_db.execSQL(query);
        } else {
            this.hbha_db.execSQL(query, where);
        }
    }

    public void deleteTable(String name) {
        this.hbha_db.delete(name, null, null);
    }

    public void deleteTableWithWhere(String name, String condition, String[] where) {
        this.hbha_db.delete(name, condition, where);
    }

    public void dropTable(String name) {
        this.hbha_db.execSQL("drop table if exists '" + name + "'");
    }
}
