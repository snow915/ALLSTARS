package com.store;
import android.app.Activity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import androidx.fragment.app.FragmentTransaction;

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
        Profile.OnFragmentInteractionListener,
        EditProfile.OnFragmentInteractionListener{

    Fragment mi_fragment = null;
    Fragment carousel = null;
    boolean fragment_seleccionado = false;
    String user = null;
    String pass = null;
    String username = null;
    String user_id = null;
    String artist_name = null;
    String artist_id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        load_preferences();
        load_artist_preferences();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(user != null){
            user_id = user;
            NavigationView navView = findViewById(R.id.nav_view);
            View header = navView.getHeaderView(0);
            navView.getMenu().findItem(R.id.nav_signin).setVisible(false);
            navView.getMenu().findItem(R.id.nav_send).setVisible(true);
            navView.getMenu().findItem(R.id.see_profile).setVisible(true);
            navView.getMenu().findItem(R.id.edit_profile).setVisible(true);
            navView.getMenu().findItem(R.id.delete_account).setVisible(true);
            navView.getMenu().findItem(R.id.nav_profile).setVisible(true);
            navView.getMenu().findItem(R.id.requests).setVisible(false);
        } else if(artist_id != null) {
            NavigationView navView = findViewById(R.id.nav_view);
            View header = navView.getHeaderView(0);
            navView.getMenu().findItem(R.id.nav_signin).setVisible(false);
            navView.getMenu().findItem(R.id.nav_send).setVisible(true);
            navView.getMenu().findItem(R.id.see_profile).setVisible(true);
            navView.getMenu().findItem(R.id.edit_profile).setVisible(true);
            navView.getMenu().findItem(R.id.delete_account).setVisible(true);
            navView.getMenu().findItem(R.id.nav_profile).setVisible(true);
            navView.getMenu().findItem(R.id.requests).setVisible(true);
        }
        navigationView.setNavigationItemSelectedListener(this);
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
        user_id = preferences.getString("user_id", null);
    }
    private void load_artist_preferences() {
        SharedPreferences preferences = getSharedPreferences("artist_credentials", Context.MODE_PRIVATE);
        pass = preferences.getString("pass",null);
        artist_name = preferences.getString("artist_name", null);
        artist_id = preferences.getString("artist_id", null);
    }

    private void save_preferences() {
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
//        SharedPreferences.Editor editor=preferences.edit();
//        editor.putString("user",null);
//        editor.putString("pass", null);
//        editor.putString("username", null);
//        editor.commit();
    }
    private void save_artist_preferences() {
        SharedPreferences preferences = getSharedPreferences("artist_credentials", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();

        //        SharedPreferences.Editor editor=preferences.edit();
//        editor.putString("artist_name",null);
//        editor.putString("pass", null);
//        editor.putString("artist_id", null);
//        editor.commit();
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
        load_artist_preferences();
        load_preferences();
        if(user != null && pass != null && artist_id == null){
            user_id = user;
            user_name = username;
        }else if (artist_id != null){
            String welcome_message = getString(R.string.welcome_message);
            TextView user_text = findViewById(R.id.no_logged_user_name);
            user_text.setText( welcome_message + " " +artist_id);
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
            if (artist_id != null){
                Toast.makeText(getApplicationContext(), "Artist", Toast.LENGTH_SHORT).show();
                save_artist_preferences();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
            }
            else if (user_id != null){
                Toast.makeText(getApplicationContext(), "User", Toast.LENGTH_SHORT).show();
                save_preferences();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
            }
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
        }   else if (id == R.id.see_profile) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
                mi_fragment = new Profile();
                Bundle b = new Bundle();
                if (user_id != null){
                    b.putString("user_id", user_id);
                    mi_fragment.setArguments(b);
                } else {
                    mi_fragment = new ArtistProfile();
                    b.putString("artist_id", artist_id);
                    mi_fragment.setArguments(b);
                }
                fragment_seleccionado = true;
            } else {
                mi_fragment = new Profile();
                Bundle b = new Bundle();
                if (user_id != null){
                    b.putString("user_id", user_id);
                    mi_fragment.setArguments(b);
                } else {
                    mi_fragment = new ArtistProfile();
                    b.putString("artist_id", artist_id);
                    mi_fragment.setArguments(b);
                }
                fragment_seleccionado = true;
            }
        } else if (id == R.id.edit_profile) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
                mi_fragment = new EditProfile();
                Bundle b = new Bundle();
                if (user_id != null){
                    b.putString("user_id", user_id);
                    mi_fragment.setArguments(b);
                } else {
                    mi_fragment = new EditArtistProfile();
                    b.putString("artist_id", artist_id);
                    mi_fragment.setArguments(b);
                }
                fragment_seleccionado = true;
            } else {
                mi_fragment = new EditProfile();
                Bundle b = new Bundle();
                if (user_id != null){
                    b.putString("user_id", user_id);
                    mi_fragment.setArguments(b);
                } else {
                    mi_fragment = new EditArtistProfile();
                    b.putString("artist_id", artist_id);
                    mi_fragment.setArguments(b);
                }
                fragment_seleccionado = true;
            }
        } else if (id == R.id.delete_account) {
            new SweetAlertDialog( MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure that you want delete this?")
                    .setContentText("This action cannot be undone")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.setCanceledOnTouchOutside(false);
                            sDialog.dismissWithAnimation();
                            deleteUserData(user_id);
                        }
                    })
                    .show();
        } else if (id == R.id.requests) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
                mi_fragment = new FragmentSolicitud();
                fragment_seleccionado = true;
            } else {
                mi_fragment = new EditProfile();
                fragment_seleccionado = true;
            }
        }

        if (fragment_seleccionado) {
            //getSupportFragmentManager().popBackStack();
            //getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mi_fragment).addToBackStack(null).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mi_fragment, "Current").addToBackStack(null).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void deleteUserData(String id){
        DatabaseReference df = FirebaseDatabase.getInstance().getReference("Usuarios").child(id);
        df.removeValue();
        save_preferences();
        new SweetAlertDialog( MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Done")
                .setContentText("Count deleted successfully!")
                .setConfirmText("Got it")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setCanceledOnTouchOutside(false);
                        sDialog.dismissWithAnimation();
                        reload();
                    }
                })
                .show();
    }

    public void reload(){
        Activity current_context = MainActivity.this;
        Intent intent = new Intent(current_context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
