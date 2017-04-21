package com.programacion.robertomtz.iniciosesionfirebase;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;

public class InicioSesionActivity extends AppCompatActivity implements View.OnClickListener{

    private Button iniciaSesion, crearCuenta;
    private LoginButton btnFacebook;
    private EditText etUsuario, etPassword;
    private CallbackManager callbackManager;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        inicializaVariables();
        evitarInvalidHashKeyFacebook();
    }

    private void evitarInvalidHashKeyFacebook() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.programacion.robertomtz.iniciosesionfirebase", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {}
    }

    private void inicializaVariables() {
        // Views
        iniciaSesion = (Button) findViewById(R.id.inicio_sesion_btn_ingresar);
        iniciaSesion.setOnClickListener(this);
        crearCuenta = (Button) findViewById(R.id.inicio_sesion_btn_crear_cuenta);
        crearCuenta.setOnClickListener(this);
        etUsuario = (EditText) findViewById(R.id.inicio_sesion_et_usuario);
        etUsuario.requestFocus();
        etPassword = (EditText) findViewById(R.id.inicio_sesion_et_password);

        // Facebook Button
        callbackManager = CallbackManager.Factory.create();
        btnFacebook = (LoginButton) findViewById(R.id.inicio_sesion_login_btn_facebook);

        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_cancelado), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_error), Toast.LENGTH_SHORT).show();
            }
        });


        database = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();

        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null)
                   irIngreseActivity();

                }
            };
        }
    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful())
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void irIngreseActivity(){
        Intent intent = new Intent(this, IngreseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean verificaCuenta(String user, String password){
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(listener);
    }

    /*
     * Metodo para asignar Listeners a los View de la pantalla
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();

        String user = etUsuario.getText().toString().trim();
        String pass = etPassword.getText().toString();

        Intent intent = null;

        switch(id){
            case R.id.inicio_sesion_btn_crear_cuenta:
                intent = new Intent(this, CrearCuentaActivity.class);
                break;
            case R.id.inicio_sesion_btn_ingresar:
                if (verificaCuenta(user, pass))
                    intent = new Intent(this, IngreseActivity.class);
                break;
            default:
                return;
        }

        if (intent != null)
            startActivity(intent);
    }
}
