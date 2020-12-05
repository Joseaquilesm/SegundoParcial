package com.pucmm.segundoparcial;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pucmm.segundoparcial.Utilities.Utilities;

import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {
    AddCategoryActivity addCategory;
    Button btnAdd;
    Button btnSave;
    Spinner spinner;
    EditText nombre;
    EditText precio;
    long ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        nombre = findViewById(R.id.editTextName);
        precio = findViewById(R.id.editTextPrice);
        //llenando spinner con las categorias de la base de datos
        ArrayList<String> spinnerData = loadCategories();
        spinner = findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,spinnerData);
        spinner.setAdapter(adapter);

        //Evento Add Category
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProductActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });

        //Evento Guardar Producto
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save in database

                registrarProducto();
            }
        });


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //llenando spinner con las categorias de la base de datos
        ArrayList<String> spinnerData = loadCategories();
        spinner = findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,spinnerData);
        spinner.setAdapter(adapter);
    }

    public ArrayList<String> loadCategories(){
        //extracting database
        DBConnection connection = new DBConnection(this,"product_database",null,1);
        ArrayList<String> categorias = new ArrayList<String>();
        SQLiteDatabase db = connection.getReadableDatabase();

        //transaction to load categories from the category table
        db.beginTransaction();
        try {
            String query = "SELECT * FROM " + Utilities.CATEGORY;
            System.out.println("SELECT * FROM :" + Utilities.CATEGORY);
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String categoryName = cursor.getString(cursor.getColumnIndex("NAME"));
                    categorias.add(categoryName);
                }
            }
            db.setTransactionSuccessful();
        }catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            db.endTransaction();
            db.close();
        }
        return categorias;
    }

    public void registrarProducto(){
        //Conectando a la base de datos
        DBConnection connection = new DBConnection(this,"product_database",null,1);
        SQLiteDatabase db = connection.getWritableDatabase();

        //Seteando los campos que voy a insertar
        ContentValues campos = new ContentValues();

        //pseudo auto-increment
        ID = DatabaseUtils.queryNumEntries(db, Utilities.PRODUCT_TABLE);
        ID ++;

        //adding values
        campos.put(Utilities.ID,ID);
        campos.put(Utilities.NAME,nombre.getText().toString());
        campos.put(Utilities.PRICE,Integer.parseInt(precio.getText().toString()));
        campos.put(Utilities.CATEGORY,spinner.getSelectedItem().toString());

        long insertId = db.insert(Utilities.PRODUCT_TABLE,Utilities.ID, campos);
        Toast.makeText(getApplicationContext(),"Se ha agregado el producto: " + nombre.getText().toString() + " con un Id: "+ insertId, Toast.LENGTH_LONG).show();

        db.close();

    }
}