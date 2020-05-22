package com.store;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.store.CarritoCompras.Carrito;
import com.store.ListaCategorias.Categorias;
import com.store.credentials.Login;
import com.store.ListaCategorias.Altavoces;
import com.store.ListaCategorias.Auriculares;
import com.store.ListaCategorias.Baterias;
import com.store.ListaCategorias.Botones;
import com.store.ListaCategorias.Camaras;
import com.store.ListaCategorias.Cristales;
import com.store.ListaCategorias.Display;
import com.store.ListaCategorias.Pantallas;
import com.store.ListaCategorias.Placas;
import com.store.famous.AddArtistService;
import com.store.famous.ArtistProfile;
import com.store.famous.EditArtistProfile;
import com.store.famous.FragmentDeleteService;
import com.store.famous.FragmentSolicitud;
import com.store.famous.FragmentViewServices;
import com.store.user.EditProfile;
import com.store.user.FragmentSolicitudUsuario;
import com.store.user.Profile;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.Menu;
import android.widget.TextView;
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

    private Fragment myFragment = null;
    private Fragment carousel = null;
    private boolean fragmentSelected = false;
    private String userID = null;
    private String username = null;
    private String userFirstName = null;
    private String artistUsername = null;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private SharedPreferencesApp sharedPreferencesApp;
    private MenuItem search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesApp = new SharedPreferencesApp(getApplicationContext());
        sharedPreferencesApp.loadPreferences();
        userID = sharedPreferencesApp.getUserID();
        username = sharedPreferencesApp.getUsername();
        userFirstName = sharedPreferencesApp.getUserFirstName();
        artistUsername = sharedPreferencesApp.getArtistUsername();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(username != null || artistUsername != null){
            navigationView.getMenu().findItem(R.id.nav_signin).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_send).setVisible(true);
            navigationView.getMenu().findItem(R.id.see_profile).setVisible(true);
            navigationView.getMenu().findItem(R.id.edit_profile).setVisible(true);
            navigationView.getMenu().findItem(R.id.delete_account).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);

            navigationView.getMenu().findItem(R.id.nav_requests).setVisible(true);
            navigationView.getMenu().findItem(R.id.requests).setVisible(true);
            navigationView.getMenu().findItem(R.id.accepted_requests).setVisible(true);
            navigationView.getMenu().findItem(R.id.rejected_requests).setVisible(true);

            if(artistUsername != null) {
                navigationView.getMenu().findItem(R.id.nav_services).setVisible(true);
                navigationView.getMenu().findItem(R.id.add_service).setVisible(true);
                navigationView.getMenu().findItem(R.id.see_services).setVisible(true);
                navigationView.getMenu().findItem(R.id.edit_services).setVisible(true);
                navigationView.getMenu().findItem(R.id.delete_services).setVisible(true);
            }
        }
        navigationView.setNavigationItemSelectedListener(this);
        carousel = new Carrusel();
        myFragment = new index();
        //Here i put the carousel, in the content_main.xml
        getSupportFragmentManager().beginTransaction().add(R.id.content_main, myFragment).commit();
        //Here i put index, in the content_main.xml
        getSupportFragmentManager().beginTransaction().add(R.id.content_main_carrusel, carousel).commit();
        search = findViewById(R.id.searchIcon);
    }

    @Override
    public void onBackPressed() {
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
        if (artistUsername != null){
            String welcome_message = getString(R.string.welcome_message);
            TextView user_text = findViewById(R.id.no_logged_user_name);
            user_text.setText( welcome_message + " " + artistUsername);
        }
        if (username != null){
            String welcome_message = getString(R.string.welcome_message);
            TextView user_text = findViewById(R.id.no_logged_user_name);
            user_text.setText( welcome_message + " " +userFirstName);
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
        } else if(id == R.id.searchIcon) {

            Intent intent = new Intent(getApplicationContext(), ActivitySearchResult.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        myFragment = null;
        fragmentSelected = false;

        if (id == R.id.nav_home) {
            carousel = new Carrusel();
            myFragment = new index();
            fragmentSelected = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main_carrusel, carousel).commit();
        } else if (id == R.id.nav_gallery) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
                myFragment = new Categorias();
                fragmentSelected = true;
            } else {
                myFragment = new Categorias();
                fragmentSelected = true;
            }
        } else if (id == R.id.nav_send) {
            if (artistUsername != null){
                sharedPreferencesApp.cleanPreferences();
                reload();
            }
            else if (username != null){
                FirebaseAuth.getInstance().signOut(); //Close session for Firebase and Google
                LoginManager.getInstance().logOut(); //Close session for Facebook
                sharedPreferencesApp.cleanPreferences();
                reload();
            }
        } else if(id == R.id.nav_carrito) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
                myFragment = new Carrito();
                fragmentSelected = true;
            } else {
                myFragment = new Carrito();
                fragmentSelected = true;
            }

        } else if (id == R.id.nav_signin) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }   else if (id == R.id.see_profile) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
            }
            myFragment = new Profile();
            Bundle b = new Bundle();
            if (username != null){
                b.putString("userID", userID);
                myFragment.setArguments(b);
            } else {
                myFragment = new ArtistProfile();
                b.putString("artistUsername", artistUsername);
                myFragment.setArguments(b);
            }
            fragmentSelected = true;
        } else if (id == R.id.edit_profile) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
            }
            myFragment = new EditProfile();
            Bundle b = new Bundle();
            if (username != null){
                b.putString("userID", userID);
                myFragment.setArguments(b);
            } else {
                myFragment = new EditArtistProfile();
                b.putString("artistUsername", artistUsername);
                myFragment.setArguments(b);
            }
            fragmentSelected = true;
        } else if (id == R.id.delete_account) {
            String user = "";
            String userType = "";
            if(username != null){
                user = username;
                userType = "user";
            } else {
                user = artistUsername;
                userType = "artist";
            }
            //These final variables are necessary for the function deleteAccount
            final String finalUser = user;
            final String finalUserType = userType;
            new SweetAlertDialog( MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Estas seguro que quieres eliminar tu cuenta?")
                    .setContentText("Esta acción no se puede revertir")
                    .setConfirmText("Sí, quiero eliminar mi cuenta")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.setCanceledOnTouchOutside(false);
                            sDialog.dismissWithAnimation();
                            deleteAccount(finalUser, finalUserType);
                        }
                    })
                    .show();
        } else if (id == R.id.requests) {
            if(carousel != null){
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
            }
            if(username != null){
                myFragment = new FragmentSolicitudUsuario();
            } else {
                myFragment = new FragmentSolicitud();
            }
            fragmentSelected = true;
        } else if (id == R.id.accepted_requests) {
            if(carousel != null){
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
            }
            if(username != null){
                myFragment = new FragmentSolicitudUsuario();
            } else {
                myFragment = new FragmentSolicitud();
            }
            Bundle b = new Bundle();
            b.putString("typeRequest", "accepted");
            myFragment.setArguments(b);
            fragmentSelected = true;
        } else if (id == R.id.rejected_requests) {
            if(carousel != null){
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
            }
            if(username != null){
                myFragment = new FragmentSolicitudUsuario();
            } else {
                myFragment = new FragmentSolicitud();
            }
            Bundle b = new Bundle();
            b.putString("typeRequest", "rejected");
            myFragment.setArguments(b);
            fragmentSelected = true;
        } else if(id == R.id.add_service) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
                myFragment = new AddArtistService();
                fragmentSelected = true;
            } else {
                myFragment = new AddArtistService();
                fragmentSelected = true;
            }
        } else if(id == R.id.see_services) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
                myFragment = new FragmentViewServices();
                fragmentSelected = true;
            } else {
                myFragment = new FragmentViewServices();
                fragmentSelected = true;
            }
        } else if(id == R.id.delete_services) {
            if (carousel != null) {
                carousel = null;
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_main_carrusel)).commit();
                myFragment = new FragmentDeleteService();
                fragmentSelected = true;
            } else {
                myFragment = new FragmentDeleteService();
                fragmentSelected = true;
            }
        }

        if (fragmentSelected) {
            //getSupportFragmentManager().popBackStack();
            //getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mi_fragment).addToBackStack(null).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment, "Current").addToBackStack(null).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /*=============================MY FUNCTIONS===============================*/

    public void reload(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void deleteAccount(String username, String userType){
        DatabaseReference df;
        if (userType.equals("user")){
            df = FirebaseDatabase.getInstance().getReference("Usuarios").child(username);
        } else {
            df = FirebaseDatabase.getInstance().getReference("data").child(username);
        }
        df.removeValue();
        sharedPreferencesApp.cleanPreferences();
        new SweetAlertDialog( MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Hecho")
                .setContentText("Cuenta eliminada exitosamente")
                .setConfirmText("Entendido")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setCanceledOnTouchOutside(false);
                        sDialog.dismissWithAnimation();
                        sharedPreferencesApp.cleanPreferences();
                        reload();
                    }
                })
                .show();
    }
}
