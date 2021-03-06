package com.example.happy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

public class RegistroActivity extends AppCompatActivity {

    //RECURSOS
    private EditText nombre;
    private EditText apellido1;
    private EditText apellido2;
    private EditText email;
    private EditText pwd;
    private TextView textBirthDate;
    private Button registrar;
    private ProgressDialog progressDialog;

    //FIREBASE
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    //AWESOME VALIDATION
    AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //FIREBASE
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //AWESOME VALIDATION
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.editTextTextEmailAddress, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.editTextTextPassword,".{6,}",R.string.invalid_pwd);
        awesomeValidation.addValidation(this, R.id.editTextTextName, "[a-zA-Z\\s]+", R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.editTextTextSurname1, "[a-zA-Z\\s]+", R.string.invalid_surname);
        awesomeValidation.addValidation(this, R.id.editTextTextSurname2, "[a-zA-Z\\s]+", R.string.invalid_surname);
        awesomeValidation.addValidation(this, R.id.textBirthDate, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.invalid_birthday);

        //RECURSOS
        nombre = (EditText) findViewById(R.id.editTextTextName);
        apellido1 = (EditText) findViewById(R.id.editTextTextSurname1);
        apellido2 = (EditText) findViewById(R.id.editTextTextSurname2);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        pwd = (EditText) findViewById(R.id.editTextTextPassword);
        textBirthDate = (TextView) findViewById(R.id.textBirthDate);
        registrar = (Button) findViewById(R.id.registrar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando...");


        // EVENTO CALENDARIO
        textBirthDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                Calendar calendario = Calendar.getInstance();
                int anio = calendario.get(Calendar.YEAR);
                int mes =  calendario.get(Calendar.MONTH);
                int dia = calendario.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistroActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        String fecha = format.format(calendar.getTime());
                        textBirthDate.setText(fecha);

                    }
                }, 2022, mes, dia);

                datePickerDialog.show();
            }

        });


        //EVENTO BOTON REGISTRAR
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = email.getText().toString().trim();
                String pass = pwd.getText().toString().trim();

                if(awesomeValidation.validate()){

                    System.out.println(mail);
                    System.out.println(pass);
                    registrarUsuario(mail, pass);

                }else {
                    Toast.makeText(RegistroActivity.this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void registrarUsuario(String pEmail, String pPwd){

        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(pEmail, pPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    progressDialog.dismiss();

                    String uEmail = email.getText().toString().trim();
                    String uPwd = pwd.getText().toString();
                    String uNombre = nombre.getText().toString();
                    String uApellido1 = apellido1.getText().toString();
                    String uApellido2 = apellido2.getText().toString();
                    String uBirthday = textBirthDate.getText().toString();
                    String id = firebaseAuth.getCurrentUser().getUid();

                    //MANDAMOS LA INFO A FIRESTORE EN UN HASMAP
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", uEmail);
                    hashMap.put("pwd", uPwd);
                    hashMap.put("nombre", uNombre);
                    hashMap.put("apellido1", uApellido1);
                    hashMap.put("apellido2", uApellido2);
                    hashMap.put("birthday", uBirthday);

                    firebaseFirestore.collection("users").document(id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(RegistroActivity.this, "Usuario registrado con ??xito", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegistroActivity.this, LogActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(RegistroActivity.this, "Error en registro de la base de datos", Toast.LENGTH_SHORT).show();

                        }
                    });


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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == event.KEYCODE_BACK){

            Intent logPagina = new Intent(RegistroActivity.this, LogActivity.class);
            startActivity(logPagina);
        }

        return super.onKeyDown(keyCode, event);
    }


    //MUESTRA ERRORES PERSONALIZADOS
    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(RegistroActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentaci??n", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(RegistroActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(RegistroActivity.this, "La credencial de autenticaci??n proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(RegistroActivity.this, "La direcci??n de correo electr??nico est?? mal formateada.", Toast.LENGTH_LONG).show();
                email.setError("La direcci??n de correo electr??nico est?? mal formateada.");
                email.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(RegistroActivity.this, "La contrase??a no es v??lida o el usuario no tiene contrase??a.", Toast.LENGTH_LONG).show();
                pwd.setError("la contrase??a es incorrecta ");
                pwd.requestFocus();
                pwd.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(RegistroActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inici?? sesi??n anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(RegistroActivity.this,"Esta operaci??n es sensible y requiere autenticaci??n reciente. Inicie sesi??n nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(RegistroActivity.this, "Ya existe una cuenta con la misma direcci??n de correo electr??nico pero diferentes credenciales de inicio de sesi??n. Inicie sesi??n con un proveedor asociado a esta direcci??n de correo electr??nico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(RegistroActivity.this, "La direcci??n de correo electr??nico ya est?? siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                email.setError("La direcci??n de correo electr??nico ya est?? siendo utilizada por otra cuenta.");
                email.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(RegistroActivity.this, "Esta credencial ya est?? asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(RegistroActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(RegistroActivity.this, "La credencial del usuario ya no es v??lida. El usuario debe iniciar sesi??n nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(RegistroActivity.this, "No hay ning??n registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(RegistroActivity.this, "La credencial del usuario ya no es v??lida. El usuario debe iniciar sesi??n nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(RegistroActivity.this, "Esta operaci??n no est?? permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(RegistroActivity.this, "La contrase??a proporcionada no es v??lida..", Toast.LENGTH_LONG).show();
                pwd.setError("La contrase??a no es v??lida, debe tener al menos 6 caracteres");
                pwd.requestFocus();
                break;

            default:
                Toast.makeText(RegistroActivity.this, "Error Desconocido. Pongase en contacto con el administrador", Toast.LENGTH_LONG).show();
                break;

        }

    }

}