package com.store;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.store.CarritoCompras.AdminSQLiteOpenHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InfoProducto extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button moreInfo;
    Button buy;
    Button add;
    TextView titulo;
    TextView precio;
    ImageView imagen;
    String nombreIntent;
    String precioIntent;
    RatingBar ratingStars;
    int image;
    int stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_producto);
        moreInfo = findViewById(R.id.detalles);
        buy = findViewById(R.id.comprar);
        add = findViewById(R.id.agregar);
        titulo = findViewById(R.id.titulo);
        precio = findViewById(R.id.precio);
        imagen = findViewById(R.id.imagen);
        ratingStars = findViewById(R.id.ratingStars);

        nombreIntent = getIntent().getStringExtra("nombre");
        precioIntent = getIntent().getStringExtra("precio");
        image = getIntent().getIntExtra("image",0);
        stars = getIntent().getIntExtra("stars",0);
        titulo.setText(nombreIntent);
        precio.setText(precioIntent);
        imagen.setImageResource(image);
        ratingStars.setRating(stars);

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlertDialog dialog = makeDialog();
                //dialog.show();
                new SweetAlertDialog(InfoProducto.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(nombreIntent)
                        .setContentText("Texto de su biografía.")
                        .show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarProducto(v);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Calendar.class);
                startActivity(intent);

            }
        });
    }

    public void agregarProducto(View v){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "carrito", null, 4);
        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("tituloProducto",nombreIntent);
        registro.put("precioProducto",precioIntent);
        registro.put("idImagen",image);
        registro.put("idStars", stars);
        db.insert("Carrito",null, registro);
        db.close();
        Toast.makeText(this,"Agregado al carrito",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public AlertDialog makeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.details_message);
        return builder.create();
    }

}
