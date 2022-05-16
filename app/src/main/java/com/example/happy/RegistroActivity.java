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
import android.util.Patterns;
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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class RegistroActivity extends AppCompatActivity {

    //RECURSOS
    private EditText nombre;
    private EditText apellido1;
    private EditText apellido2;
    private EditText email;
    private EditText pwd;
    private EditText editTextBirthDate;
    private Button registrar;

    //FIREBASE
    private FirebaseAuth mAuth;

    //AWESOME VALIDATION
    AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //FIREBASE
        mAuth = FirebaseAuth.getInstance();

        //AWESOME VALIDATION
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.editTextTextEmailAddress, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.editTextTextPassword,".{6,}",R.string.invalid_pwd);
        awesomeValidation.addValidation(this, R.id.editTextTextName, "[a-zA-Z\\s]+", R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.editTextTextSurname1, "[a-zA-Z\\s]+", R.string.invalid_surname);
        awesomeValidation.addValidation(this, R.id.editTextTextSurname2, "[a-zA-Z\\s]+", R.string.invalid_surname);

        //RECURSOS
        nombre = (EditText) findViewById(R.id.editTextTextName);
        apellido1 = (EditText) findViewById(R.id.editTextTextSurname1);
        apellido2 = (EditText) findViewById(R.id.editTextTextSurname2);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        pwd = (EditText) findViewById(R.id.editTextTextPassword);
        editTextBirthDate = (EditText) findViewById(R.id.editTextBirthDate);
        registrar = (Button) findViewById(R.id.registrar);


        editTextBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog();
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = email.getText().toString().trim();
                String pass = pwd.getText().toString().trim();
                String date = editTextBirthDate.getText().toString().trim();
                Log.v("ValorGenerado", "el valor de Birthdate es "+date);

                if(awesomeValidation.validate() && !date.isEmpty()){

                    mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task){

                            if(task.isSuccessful()){
                                Toast.makeText(RegistroActivity.this, "Usuario creado con exito", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                dameToastdeerror(errorCode);
                            }
                        }
                    });

                }else {
                    Toast.makeText(RegistroActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                }

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


    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(RegistroActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(RegistroActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(RegistroActivity.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(RegistroActivity.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                email.setError("La dirección de correo electrónico está mal formateada.");
                email.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(RegistroActivity.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                pwd.setError("la contraseña es incorrecta ");
                pwd.requestFocus();
                pwd.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(RegistroActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(RegistroActivity.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(RegistroActivity.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(RegistroActivity.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                email.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                email.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(RegistroActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(RegistroActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(RegistroActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(RegistroActivity.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(RegistroActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(RegistroActivity.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(RegistroActivity.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                pwd.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                pwd.requestFocus();
                break;

        }

    }

}