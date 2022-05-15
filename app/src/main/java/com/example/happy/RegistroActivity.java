package com.example.happy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {

    //RECURSOS
    private EditText nombre;
    private EditText apellido1;
    private EditText apellido2;
    private EditText email;
    private EditText pwd;
    private EditText editTextBirthDate;
    private Button registrar;
    private ProgressDialog barraDeProgreso;

    //FIREBASE
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //FIREBASE
        mAuth = FirebaseAuth.getInstance();

        //RECURSOS
        nombre = (EditText) findViewById(R.id.editTextTextName);
        apellido1 = (EditText) findViewById(R.id.editTextTextSurname1);
        apellido2 = (EditText) findViewById(R.id.editTextTextSurname2);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        pwd = (EditText) findViewById(R.id.editTextTextPassword);
        editTextBirthDate = (EditText) findViewById(R.id.editTextBirthDate);
        registrar = (Button) findViewById(R.id.registrar);
        barraDeProgreso = new ProgressDialog(this);



        editTextBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog();
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrarUsuario();

            }
        });
    }

    private void showDatePickerDialog(){

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                editTextBirthDate.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    private void registrarUsuario(){

        String rNombre = nombre.getText().toString().trim();
        String rapellido1 = apellido1.getText().toString().trim();
        String rapellido2 = apellido2.getText().toString().trim();
        String rEmail = email.getText().toString().trim();
        String rPwd = pwd.getText().toString().trim();
        String rBirthDay = editTextBirthDate.getText().toString().trim();

        //BERIFICAMOS QUE LOS CAMPOS ESTEN COMPLETADOS
        if(TextUtils.isEmpty(rNombre)){

            Toast.makeText(this, "Se debe ingresar un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(rapellido1)){

            Toast.makeText(this, "Se debe ingresar un Primer Apellido", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(rapellido2)){

            Toast.makeText(this, "Se debe ingresar un Segundo Apellido", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(rEmail)){

            Toast.makeText(this, "Se debe ingresar un Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(rPwd)){

            Toast.makeText(this, "Se debe ingresar una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(rBirthDay)){

            Toast.makeText(this, "Se debe ingresar una fecha de cumpleaños", Toast.LENGTH_SHORT).show();
            return;
        }

        barraDeProgreso.setMessage("Realizando registro en linea...");
        barraDeProgreso.show();

        //CREAMOS USUARIO EN FIREBASE
        mAuth.createUserWithEmailAndPassword(rEmail, rPwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(RegistroActivity.this, "Se ha registrado el usuario", Toast.LENGTH_SHORT).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegistroActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }

                        barraDeProgreso.dismiss();
                    }
                });
    }

}