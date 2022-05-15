package com.example.happy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogActivity extends AppCompatActivity {

    private Button registro;
    private Button entrar;
    private EditText email;
    private EditText contraseña;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SPLASH
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //CARGAR INTERFAZ
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        //FIREBASE
        mAuth = FirebaseAuth.getInstance();


        //BOTONES Y CUADROS DE TEXTOS
        registro = (Button) findViewById(R.id.log_registro);
        entrar = (Button) findViewById(R.id.entrar);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        contraseña = (EditText) findViewById(R.id.editTextTextPassword);


        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent paginaRegistro = new Intent(LogActivity.this, RegistroActivity.class);
                startActivity(paginaRegistro);
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent paginaPerfil = new Intent(LogActivity.this, PerfilActivity.class);
                startActivity((paginaPerfil));
            }
        });
    }

}