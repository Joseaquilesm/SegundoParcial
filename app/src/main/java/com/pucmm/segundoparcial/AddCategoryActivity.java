package com.pucmm.segundoparcial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pucmm.segundoparcial.Utilities.Utilities;

public class AddCategoryActivity extends AppCompatActivity {
    Button btnSaveCategory;
    EditText nombre;
    public long ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        nombre = findViewById(R.id.editTextCategory);
        btnSaveCategory = findViewById(R.id.btnSaveCategory);
        btnSaveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarCategoria();
                finish();
            }
        });
    }

    public void registrarCategoria(){
        DBConnection connection = new DBConnection(this,"product_database",null,1);
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues campos = new ContentValues();
        //pseudo auto-increment
        ID = DatabaseUtils.queryNumEntries(db, Utilities.CATEGORY_TABLE);
        ID ++;

        //adding values
        campos.put(Utilities.CATEGORY_ID,ID);
        campos.put(Utilities.CATEGORY_NAME,nombre.getText().toString());

        long insertId = db.insert(Utilities.CATEGORY_TABLE,Utilities.CATEGORY_ID, campos);
        Toast.makeText(getApplicationContext(),"Se ha agregado la categoría: " + nombre.getText().toString() + " con un Id: "+ insertId, Toast.LENGTH_LONG).show();

        db.close();

    }
}