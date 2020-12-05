package com.pucmm.segundoparcial;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.pucmm.segundoparcial.Utilities.Utilities;

public class DBConnection extends SQLiteOpenHelper {


    public DBConnection(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(Utilities.CREATE_TABLE_CATEGORY);
            db.execSQL(Utilities.CREATE_TABLE_PRODUCT);
        }catch(SQLException e){
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Utilities.PRODUCT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ Utilities.CATEGORY_TABLE);

        onCreate(db);

    }
}
