package com.pucmm.segundoparcial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.pucmm.segundoparcial.Models.ModeloProducto;
import com.pucmm.segundoparcial.Utilities.Utilities;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FragmentTransaction transaction;
    Fragment productos;
    ListView listaProductos;
    ImageButton add;
    ArrayList<ModeloProducto> productList;
    ArrayList<String> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBConnection connection = new DBConnection(this,"product_database",null,1);
        listaProductos = findViewById(R.id.listViewProductos);
        getProductos(connection);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,info);
        listaProductos.setAdapter(adapter);

        listaProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ModifyProduct.class);
                position ++;

                intent.putExtra("Product_Id", Integer.toString(buscarIdProducto(position)));
                System.out.println("THE ID IM SENDING IS: "+ position);
                startActivity(intent);
            }
        });


        add = findViewById(R.id.btnAddProduct);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
               startActivity(intent);

            }
        });
    }

    private int buscarIdProducto(int position) {
       int actualId=0;
        for(int i = 0; i < position ;i++){
           actualId =  productList.get(i).getId();
        }
        return actualId;
    }

    private void getProductos(DBConnection conn) {
        SQLiteDatabase db = conn.getReadableDatabase();
        ModeloProducto producto = null;
        productList = new ArrayList<ModeloProducto>();

        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM "+ Utilities.PRODUCT_TABLE,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    producto = new ModeloProducto();
                    producto.setId(cursor.getInt(0));
                    producto.setNombre(cursor.getString(1));
                    producto.setPrecio(cursor.getInt(2));
                    producto.setCategoria(cursor.getString(3));

                    productList.add(producto);
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
        getList();
    }

    private void getList() {
        info = new ArrayList<String>();

        for (int i = 0; i < productList.size(); i++){
            info.add("\n" + productList.get(i).getNombre()+ "\n\n " + " Precio: " + productList.get(i).getPrecio() + "\t\t\t" + productList.get(i).getCategoria()+"\n");
        }
    }


}