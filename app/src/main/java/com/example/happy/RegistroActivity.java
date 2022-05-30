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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class RegistroActivity extends AppCompatActivity {

    //RECURSOS
    private EditText nombre;
    private EditText apellido1;
    private EditText apellido2;
    private EditText email;
    private EditText pwd;
    private EditText editTextBirthDate;
    private Button registrar;
    private ProgressDialog progressDialog;

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
        awesomeValidation.addValidation(this, R.id.editTextBirthDate, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.invalid_birthday);

        //RECURSOS
        nombre = (EditText) findViewById(R.id.editTextTextName);
        apellido1 = (EditText) findViewById(R.id.editTextTextSurname1);
        apellido2 = (EditText) findViewById(R.id.editTextTextSurname2);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        pwd = (EditText) findViewById(R.id.editTextTextPassword);
        editTextBirthDate = (EditText) findViewById(R.id.editTextBirthDate);
        registrar = (Button) findViewById(R.id.registrar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando...");


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = email.getText().toString().trim();
                String pass = pwd.getText().toString().trim();
                String date = editTextBirthDate.getText().toString().trim();
                Log.v("ValorGenerado", "el valor de Birthdate es "+date);

                if(awesomeValidation.validate() && !date.isEmpty()){

                    registrarUsuario(mail, pass);

                }else {
                    Toast.makeText(RegistroActivity.this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void registrarUsuario(String pEmail, String pPwd){

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(pEmail, pPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    progressDialog.dismiss();

                    FirebaseUser user = mAuth.getCurrentUser();
                    assert  user != null; //AFIRMAMOS QUE EL USUARIO NO SEA NULL
                    String uid = user.getUid();

                    String uEmail = email.getText().toString().trim();
                    String uPwd = pwd.getText().toString();
                    String uNombre = nombre.getText().toString();
                    String uApellido1 = apellido1.getText().toString();
                    String uApellido2 = apellido2.getText().toString();
                    String uBirthday = editTextBirthDate.getText().toString();

                    //MANDAMOS LA INFO A FIREBASE EN UN HASMAP
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", uEmail);
                    hashMap.put("pwd", uPwd);
                    hashMap.put("uid", uid);
                    hashMap.put("nombre", uNombre);
                    hashMap.put("apellido1", uApellido1);
                    hashMap.put("apellido2", uApellido2);
                    hashMap.put("birthday", uBirthday);

                    //INICIALIZAMOS LA INSTANCIA DE LA BD DE FIREBASE
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //CREAMOS LA BD NO RELACIONAL Users
                    DatabaseReference reference = database.getReference("Users");
                    reference.child(uid).setValue(hashMap);

                    //INFORMAMOS DEL EXITO DE LA OPERACIÓN
                    Toast.makeText(RegistroActivity.this, "Usuario creado con exito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistroActivity.this, LogActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }else {

                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    dameToastdeerror(errorCode);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(RegistroActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

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

            default:
                Toast.makeText(RegistroActivity.this, "Error Desconocido. Pongase en contacto con el administrador", Toast.LENGTH_LONG).show();
                break;

        }

    }

}