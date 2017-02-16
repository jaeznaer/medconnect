package com.mycompany.medsconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joel on 22-02-2016.
 */
public class MedsConnectDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "MedConnect7";
    private static final int DB_VERSION = 1;

    MedsConnectDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        update(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        update(db,oldVersion,newVersion);
    }

    private void update(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<1){
            db.execSQL("CREATE TABLE PRODUCT(_id INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,COST TEXT,TYPE1 TEXT,TYPE2 TEXT,IMAGE TEXT);");
            insertProduct(db, "PLAYBOY MALIBU BODY SPRAY 150ML", "190", "Body Care", "Deodrants", "http://www.netmeds.com/media/catalog/product/small/playboy_malibu_body_spray_150ml.jpg");
            insertProduct(db,"OLD SPICE  DEODORANT SPRAY","175","Body Care","Deodrants","http://www.netmeds.com/media/catalog/product/small/old_spice_after_party_deodorant_spray_150ml.jpg");
            insertProduct(db,"NIVEA FRESH ACTIVE RUSH","180","Body Care","Deodrants","http://www.netmeds.com/media/catalog/product/small/nivea_fresh_active_rush_deodorant_150ml.jpg");
            insertProduct(db,"COLGATE TOTAL ADVANCED HEALTH 70G","55","Body Care","Oral Care","http://www.netmeds.com/media/catalog/product/small/colgate_total_advanced_health_70g.jpg");
            insertProduct(db,"COLGATE PLAX FRESHMINT 100ML","50","Body Care","Oral Care","http://www.netmeds.com/media/catalog/product/small/colgate_plax_freshmint_100ml.jpg");
            insertProduct(db,"LISTERINE COOLMINT MOUTHWASH 250ML","100","Body Care","Oral Care","http://www.netmeds.com/media/catalog/product/small/listerine_coolmint_mouthwash_250ml.jpg");
            insertProduct(db,"PEPSODENT GERMI CHECK GUM EXPERT SOFT","50","Body Care","Oral Care","http://www.netmeds.com/media/catalog/product/small/pepsodent_germi_check_gum_expert_soft.jpg");
            insertProduct(db,"BAND AID WASHPROOF 100'S","240","Body Care","First Aid","http://www.netmeds.com/media/catalog/product/small/band_aid_washproof_100_s.jpg");
            insertProduct(db,"DETTOL ANTISEPTIC LIQUID 500ML","115","Body Care","First Aid","http://www.netmeds.com/media/catalog/product/small/dettol_antiseptic_liquid_500ml.jpg");
            insertProduct(db,"VICKS ACTION EXTRA 500MG","28","Body Care","First Aid","http://www.netmeds.com/media/catalog/product/small/vicks_action_extra_500mg_10_s.jpg");
            insertProduct(db,"VOLINI SPRAY 100G","210","Body Care","First Aid","http://www.netmeds.com/media/catalog/product/small/volini_spray_100g.jpg");
            insertProduct(db,"MAMY POKO SOFY BODY FIT","35","Body Care","Baby Care","http://www.netmeds.com/media/catalog/product/small/mamy_poko_sofy_body_fit_overnight_xl_6_s.jpg");
            insertProduct(db,"JOHNSON'S BABY POWDER BLOSSOMS 50G","30","Body Care","Baby Care","http://www.netmeds.com/media/catalog/product/small/johnson_s_baby_powder_blossoms_50g.jpg");
            insertProduct(db,"JOHNSON'S BABY TOOTH BRUSH","30","Body Care","Baby Care","http://www.netmeds.com/media/catalog/product/small/johnson_s_baby_tooth_brush.jpg");
            insertProduct(db,"JOHNSON'S BABY SOAP 50G","50","Body Care","Baby Care","http://www.netmeds.com/media/catalog/product/small/johnson_s_baby_soap_50g.jpg");

            db.execSQL("CREATE TABLE CART(_id INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,COST TEXT,PRODUCT_QTY TEXT,TOTALCOST TEXT,IMAGE TEXT);");
            db.execSQL("CREATE TABLE COUNTER(_id INTEGER PRIMARY KEY AUTOINCREMENT, NOVALUE TEXT);");
        }
    }

    private static void insertProduct(SQLiteDatabase db, String name, String cost, String type1, String type2, String image) {
        ContentValues alues = new ContentValues();
        alues.put("NAME", name);
        alues.put("COST", cost);
        alues.put("TYPE1", type1);
        alues.put("TYPE2", type2);
        alues.put("IMAGE", image);
        db.insert("PRODUCT", null, alues);
    }
}
