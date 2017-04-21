package com.programacion.robertomtz.iniciosesionfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CrearCuentaActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsuario, etPassword, etRepitePassword;
    private Button btnCrearCuenta;

    private FirebaseDatabase database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        inicializaVariables();
    }

    private void inicializaVariables() {
        etUsuario = (EditText) findViewById(R.id.crear_cuenta_et_usuario);
        etUsuario.requestFocus();
        etPassword = (EditText) findViewById(R.id.crear_cuenta_et_password);
        etRepitePassword = (EditText) findViewById(R.id.crear_cuenta_et_repite_password);
        btnCrearCuenta = (Button) findViewById(R.id.crear_cuenta_btn_crear);
        btnCrearCuenta.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = null;

        switch(id){
            case R.id.crear_cuenta_btn_crear:
                break;
            default:
                return;
        }

    }
}
