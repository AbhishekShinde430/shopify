package com.infinitesoftsolution.shoppinglist.shopify;

import android.provider.BaseColumns;

public class TableData {

    public TableData() {
    }

    public  static String TABLE_NAME="reg_info";

    public static abstract class TableInfo implements BaseColumns{


        public static final String NAME="name";
        public final static String QTY="qty";
        public final static String VAL="val";
        public final static String MEASURE="measures";
        public final static String CHECK="z";
        public final static String DATABASE_NAME="prodct_info";


    }
}
