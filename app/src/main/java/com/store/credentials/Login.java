package com.store.credentials;

import android.content.Intent;
import android.os.Bundle;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.store.MainActivity;
import com.store.R;
import com.store.SharedPreferencesApp;
import com.store.user.Usuarios;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private String firstname, lastname, gender, email;
    public Usuarios usersObj;

    private Button btnSignIn, btnSignUp;
    private TextInputLayout edtxtUser, edtxtPassword;
    private CheckBox chboxSignInArtist;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Associate variables with id from activity_login layout
        btnSignIn = findViewById(R.id.id_sign_in);
        btnSignUp = findViewById(R.id.id_sign_up);
        edtxtUser = findViewById(R.id.editText3);
        edtxtPassword = findViewById(R.id.editText5);
        chboxSignInArtist = findViewById(R.id.checkBox_sign_in_artist);

        //FacebookLogin
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setPermissions("public_profile","email", "user_birthday", "user_gender");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Log.d(TAG, "facebook:onSuccess:" + loginResult);
                getUserDetailsFromFB(loginResult.getAccessToken());
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {}
        });

        // GoogleLogin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        initFirebase();
        mAuth = FirebaseAuth.getInstance();

        //Normal button of Login
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userValue = edtxtUser.getEditText().getText().toString();
                String passValue = edtxtPassword.getEditText().getText().toString();
                if(validateFields(userValue, passValue)){
                    if(chboxSignInArtist.isChecked()){
                        userPassExist(userValue, passValue, "artist");
                    } else {
                        signIn(userValue, passValue, "user");
                    }
                }
            }
        });

        //Google button
        findViewById(R.id.sign_in_google_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //Registration button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

    }

    // It runs when we click the google button of the facebook button
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, now authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed
                Log.w(TAG, "Google sign in failed", e);
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //=============FACEBOOK AUTH CODE==============================
    //// Start firebase with Facebook data
    private void handleFacebookAccessToken(AccessToken token) {
        //Log.d(TAG, "handleFacebookAccessToken:" + token);

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Aqui guardar en firebase ?
                            final String userID = user.getUid(); //me da el id de firebase
                            //databaseReference.child("Usuarios").child(userID);
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userID);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(!dataSnapshot.exists()){
                                        usersObj = new Usuarios();
                                        usersObj.setNombre(firstname);
                                        usersObj.setApellido(lastname);
                                        usersObj.setCorreo(email);
                                        usersObj.setSexo(gender);
                                        usersObj.setUid(userID);
                                        Log.w(TAG, "CORREO: " + usersObj.getCorreo());
                                        //SAVE INFO IN FIREBASE REALTIME
                                        databaseReference.setValue(usersObj);
                                        //SAVE INFO IN FIREBASE REALTIME
                                    } else {
                                        //AQUI SI EXISTE YA EN REALTIME DB
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            //INIT ....
                            SharedPreferencesApp sharedPreferencesApp = new SharedPreferencesApp(getApplicationContext());
                            sharedPreferencesApp.saveLoginData(firstname, lastname, email ,userID, "user");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finishAffinity();

                        } else {
                            // ME IMPRIMIO ERROR PORQUE YA EXISTIA CUENTA CON MISMO CORREO
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    //Here it retrieves data from Facebook
    public void getUserDetailsFromFB(AccessToken accessToken) {
        //GrapRequest con el metodo newMeRequest recupera los datos del usuario identificado
        GraphRequest req=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try{

                    firstname = object.getString("first_name");
                    lastname = object.getString("last_name");
                    email =  object.getString("email");
                    gender = object.getString("gender");
                    if(gender.equals("male")){
                        gender = "Masculino, 1";
                    } else if (gender.equals("female")){
                        gender = "Femenino, 2";
                    }
                    //String id = object.getString("id");
                    //String photourl =object.getJSONObject("picture").getJSONObject("data").getString("url");

                }catch (JSONException e)
                {}
            }
        });
        Bundle parameters = new Bundle();
        //parameters.putString("fields", "id,first_name,last_name,email,gender,picture.type(large)");
        parameters.putString("fields", "first_name,last_name,email,gender");
        req.setParameters(parameters);
        req.executeAsync();
    }

    //=============GOOGLE AUTH CODE==============================
    // Start firebase with Google data
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        usersObj = new Usuarios();
        usersObj.setNombre(acct.getGivenName());
        usersObj.setApellido(acct.getFamilyName());
        usersObj.setCorreo(acct.getEmail());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String userID = user.getUid(); //me da el id de firebase
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userID);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(!dataSnapshot.exists()){
                                        //SAVE INFO IN FIREBASE REALTIME
                                        databaseReference.setValue(usersObj);
                                        //SAVE INFO IN FIREBASE REALTIME
                                    } else {
                                        //AQUI SI EXISTE
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            //INIT ....
                            SharedPreferencesApp sharedPreferencesApp = new SharedPreferencesApp(getApplicationContext());
                            sharedPreferencesApp.saveLoginData(usersObj.getNombre(), usersObj.getApellido(), usersObj.getCorreo() ,userID, "user");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finishAffinity();

                        } else {
                            //Log.e(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }


    //=============NORMAL AUTH CODE==============================
    private void signIn(String email, String password, final String userType) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID  = user.getUid();

                            //EMAIL VERIFICATION
                            //user.isEmailVerified() returns true when the mail was verified
                            if(user.isEmailVerified()){
                                retrieveDataRealtimeDB(userID, userType);
                            } else {
                                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Debes verificar tu correo primero")
                                        .hideConfirmButton()
                                        .setCancelText("Entendido")
                                        .show();
                                FirebaseAuth.getInstance().signOut();
                            }
                            //EMAIL VERIFICATION

                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Usuario o contraseña erroneos")
                                        .hideConfirmButton()
                                        .setCancelText("Entendido")
                                        .show();
                            }  catch (FirebaseAuthInvalidUserException e){
                                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("El usuario que proporcionaste no existe")
                                        .hideConfirmButton()
                                        .setCancelText("Entendido")
                                        .show();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void retrieveDataRealtimeDB(final String userID, final String userType) {
        if (userType.equals("user")) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userID);
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("data").child(userID);
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String retrievedUserFirstName = "";
                String retrievedUserLastName = "";
                String retrievedUserEmail = "";
                //Try catch handle errors of type NullPointerException
                try {
                    retrievedUserFirstName = dataSnapshot.child("nombre").getValue().toString();
                    retrievedUserLastName = dataSnapshot.child("apellido").getValue().toString();
                    retrievedUserEmail = dataSnapshot.child("correo").getValue().toString();
                } catch (Exception e) {
                }
                SharedPreferencesApp sharedPreferencesApp = new SharedPreferencesApp(getApplicationContext());
                sharedPreferencesApp.saveLoginData(retrievedUserFirstName, retrievedUserLastName, retrievedUserEmail ,userID, userType);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finishAffinity();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //=============THIS IS ONLY FOR ARTISTS FOR THE MOMENT==============================
    private void userPassExist(final String userValue, final String passValue, final String userType){
        if (userType.equals("user")) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userValue);
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("data").child(userValue);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { //Here verify if user exist
                    //Here get password of that user
                    String retrievedPasswordDB = dataSnapshot.child("pass").getValue().toString();
                    //Verify if pass from EditText and pass from DB is the same
                    if (passValue.equals(retrievedPasswordDB)){
                        String retrievedUserFirstName = "";
                        String retrievedUserLastName = "";
                        //Try catch handle errors of type NullPointerException
                        try {
                            retrievedUserFirstName = dataSnapshot.child("nombre").getValue().toString();
                            retrievedUserLastName = dataSnapshot.child("apellido").getValue().toString();
                        } catch (Exception e) { }
                        SharedPreferencesApp sharedPreferencesApp = new SharedPreferencesApp(getApplicationContext());
                        sharedPreferencesApp.saveLoginData(retrievedUserFirstName, retrievedUserLastName, userValue ,userValue, userType);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "El usuario "+userValue+" no existe", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void initFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private boolean validateFields(String user, String pass){
        if (user.equals("")){
            edtxtUser.setError("Required");
            return false;
        } else if (pass.equals("")){
            edtxtPassword.setError("Required");
            return false;
        }
        return true;
    }
}
