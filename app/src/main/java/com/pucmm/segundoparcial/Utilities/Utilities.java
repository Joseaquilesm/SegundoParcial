package com.pucmm.segundoparcial.Utilities;

public class Utilities {
    //PRODUCT TABLE
    public static final String PRODUCT_TABLE = "product";
    public static final String ID = "id";
    public static final String NAME = "NAME";
    public static final String PRICE = "PRICE";
    public static final String CATEGORY = "CATEGORY";

    //CATEGORY TABLE
    public static final String CATEGORY_TABLE = "category";
    public static final String CATEGORY_ID = "id";
    public static final String CATEGORY_NAME = "NAME";

    public static final String    CREATE_TABLE_PRODUCT="CREATE TABLE "+PRODUCT_TABLE+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" TEXT, "+PRICE+" INTEGER, "+CATEGORY+" TEXT)";
    public static final String    CREATE_TABLE_CATEGORY="CREATE TABLE "+CATEGORY_TABLE+" ("+CATEGORY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+CATEGORY_NAME+" TEXT)";
}
