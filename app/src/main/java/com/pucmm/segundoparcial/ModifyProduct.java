package com.pucmm.segundoparcial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.pucmm.segundoparcial.Utilities.Utilities;

import java.util.ArrayList;

public class ModifyProduct extends AppCompatActivity {
    Button  btnAdd;
    Button btnUpdate;
    Button btnDelete;
    Spinner spinner;
    String productId;
    EditText Nombre;
    EditText Precio;
    DBConnection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_product);
        productId = getIntent().getStringExtra("Product_Id");
        connection = new DBConnection(this,"product_database",null,1);
        Nombre = findViewById(R.id.editTextNameModify);
        Precio = findViewById(R.id.editTextPriceModify);
        spinner = findViewById(R.id.spinnerCategory);

        ArrayList<String> spinnerData = loadCategories();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,spinnerData);
        spinner.setAdapter(adapter);

        buscarProducto();





        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyProduct.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               //Update
            SQLiteDatabase db = connection.getWritableDatabase();
            String[] params = {productId};
            ContentValues campos = new ContentValues();
            campos.put(Utilities.NAME,Nombre.getText().toString());
            campos.put(Utilities.PRICE,Integer.parseInt(Precio.getText().toString()));
            campos.put(Utilities.CATEGORY,spinner.getSelectedItem().toString());
            db.update(Utilities.PRODUCT_TABLE, campos, Utilities.ID+ " =? ", params );

                Intent intent = new Intent(ModifyProduct.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               //Delete
                mostrarDialogo();

            }
        });


    }

    private void mostrarDialogo() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage("Are you sure you want to delete this Product?");
        alerta.setCancelable(false);
        alerta.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = connection.getWritableDatabase();
                String[] params = {productId};
                ContentValues campos = new ContentValues();
                campos.put(Utilities.NAME,Nombre.getText().toString());
                campos.put(Utilities.PRICE,Integer.parseInt(Precio.getText().toString()));
                campos.put(Utilities.CATEGORY,spinner.getSelectedItem().toString());
                db.delete(Utilities.PRODUCT_TABLE, Utilities.ID+ " =? ", params );
                db.close();
                Intent intent = new Intent(ModifyProduct.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alerta.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alerta.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> spinnerData = loadCategories();

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
            Cursor cursor = db.rawQuery(query, null);
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

    public void buscarProducto(){
        SQLiteDatabase db = connection.getReadableDatabase();
        String[] params = {productId};
        System.out.println("EL ID QUE RECIBO EN BUSCAR PRODUCTO ES: "+ productId);
        try{
            Cursor cursor = db.rawQuery("SELECT " + Utilities.NAME + " , " + Utilities.PRICE + " , " + Utilities.CATEGORY +
                            " FROM " + Utilities.PRODUCT_TABLE + " WHERE " + Utilities.ID + "=? ", params);
             cursor.moveToFirst();
             Nombre.setText(cursor.getString(0));
             Precio.setText(cursor.getString(1));
             buscarCategoria(cursor.getString(2));
        }catch(SQLException e){
            System.out.println(e);
        }
        db.close();

    }
    public void buscarCategoria(String categoria){
        SQLiteDatabase db = connection.getReadableDatabase();
        String[] params = {categoria};
        try{
            //SELECT CATEGORY_ID FROM CATEGORY WHERE CATEGORY_NAME = categoria
            Cursor cursor = db.rawQuery("SELECT "+ Utilities.CATEGORY_ID + " FROM "+ Utilities.CATEGORY_TABLE +
                    " WHERE "+ Utilities.CATEGORY_NAME + " =? ", params);
            cursor.moveToFirst();
            System.out.println("EL ID QUE ACABO DE ENCONTRAR ES: "+ cursor.getInt(0));
            spinner.setSelection(cursor.getInt(0) - 1);
        }catch(SQLException e){
            System.out.println(e);
        }
        db.close();
    }

}


/*

* */