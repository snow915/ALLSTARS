package com.store;
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
        Carrito.OnFragmentInteractionListener {

    Fragment mi_fragment = null;
    Fragment carousel = null;
    boolean fragment_seleccionado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        carousel = new Carrusel();
        mi_fragment = new index();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mi_fragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main_carrusel, carousel).commit();
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
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main_carrusel, carousel).addToBackStack(null).commit();
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
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
            finish();
        }

        if (fragment_seleccionado) {
            //getSupportFragmentManager().popBackStack();
            //getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mi_fragment).addToBackStack(null).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mi_fragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
