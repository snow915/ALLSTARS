package com.store;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.store.CarritoCompras.Carrito;
import com.store.Credenciales.Login;
import com.store.Register;
import com.store.ListaCategorias.Altavoces;
import com.store.ListaCategorias.Auriculares;
import com.store.ListaCategorias.Baterias;
import com.store.ListaCategorias.Botones;
import com.store.ListaCategorias.Camaras;
import com.store.ListaCategorias.Cristales;
import com.store.ListaCategorias.Display;
import com.store.ListaCategorias.Pantallas;
import com.store.ListaCategorias.Placas;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        index.OnFragmentInteractionListener,
        Categorias.OnFragmentInteractionListener,
        Carrusel.OnFragmentInteractionListener,
        Pantallas.OnFragmentInteractionListener,
        Cristales.OnFragmentInteractionListener,
        Display.OnFragmentInteractionListener,
        Baterias.OnFragmentInteractionListener,
        Placas.OnFragmentInteractionListener,
        Altavoces.OnFragmentInteractionListener,
        Botones.OnFragmentInteractionListener,
        Camaras.OnFragmentInteractionListener,
        Auriculares.OnFragmentInteractionListener,
        Carrito.OnFragmentInteractionListener,
        Profile.OnFragmentInteractionListener {

    Fragment mi_fragment = null;
    Fragment carousel = null;
    boolean fragment_seleccionado = false;
    ImageView user_image = null;
    String user = null;
    String pass = null;
    String username = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        load_preferences();
        String userid = getIntent().getStringExtra("user_id");
        if(user != null && pass != null){
            userid = user;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if(userid != null){
            NavigationView navView = findViewById(R.id.nav_view);
            View header = navView.getHeaderView(0);
            navView.getMenu().findItem(R.id.nav_signin).setVisible(false);
            navView.getMenu().findItem(R.id.nav_send).setVisible(true);
            navView.getMenu().findItem(R.id.nav_profile).setVisible(true);
        }
        carousel = new Carrusel();
        mi_fragment = new index();
        getSupportFragmentManager().beginTransaction().add(R.id.content_main, mi_fragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.content_main_carrusel, carousel).commit();
    }

    private void load_preferences(){
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        user = preferences.getString("user",null);
        pass = preferences.getString("pass",null);
        username = preferences.getString("username", null);
    }

    private void save_preferences(){
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("user",null);
        editor.putString("pass", null);
        editor.putString("username", null);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        String user_id = getIntent().getStringExtra("user_id");
        String user_name = getIntent().getStringExtra("user_name");
        if(user != null && pass != null){
            user_id = user;
            user_name = username;
        }
        if (user_id != null){

            String welcome_message = getString(R.string.welcome_message);
            TextView user_text = findViewById(R.id.no_logged_user_name);
            user_text.setText( welcome_message + " " +user_name);
        }
        return true;
    }

    public AlertDialog makeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.about_app);
        return builder.create();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog dialog = makeDialog();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        mi_fragment = null;
        fragment_seleccionado = false;

        if (id == R.id.nav_home) {
            carousel = new Carrusel();
            mi_fragment = new index();
            fragment_seleccionado = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main_carrusel, carousel).commit();
        } else if (id == R.id.nav_gallery) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
                mi_fragment = new Categorias();
                fragment_seleccionado = true;
            } else {
                mi_fragment = new Categorias();
                fragment_seleccionado = true;
            }
        } else if (id == R.id.nav_send) {
            save_preferences();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_carrito) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
                mi_fragment = new Carrito();
                fragment_seleccionado = true;
            } else {
                mi_fragment = new Carrito();
                fragment_seleccionado = true;
            }
        } else if (id == R.id.nav_signin) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }   else if (id == R.id.nav_profile) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
                mi_fragment = new Profile();
                String user_id = getIntent().getStringExtra("user_id");
                Bundle b = new Bundle();
                b.putString("user_id", user_id);
                mi_fragment.setArguments(b);
                fragment_seleccionado = true;
            } else {
                mi_fragment = new Profile();
                String user_id = getIntent().getStringExtra("user_id");
                Bundle b = new Bundle();
                b.putString("user_id", user_id);
                mi_fragment.setArguments(b);
                fragment_seleccionado = true;
            }
        }


        if (fragment_seleccionado) {
            //getSupportFragmentManager().popBackStack();
            //getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mi_fragment).addToBackStack(null).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mi_fragment).addToBackStack(null).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void show_error_message(ImageView user_image){
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Ouch!")
                        .setContentText("No pudes acceder al perfil si no has iniciado sesión!")
                        .hideConfirmButton()
                        .setCancelText("Ok")
                        .show();
            }
        });
    }
}
