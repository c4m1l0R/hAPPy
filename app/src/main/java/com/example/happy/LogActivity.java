package com.example.happy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LogActivity extends AppCompatActivity {

    //RECURS0S
    private Button registro;
    private Button entrar;
    private Button restablecerPwd;
    private EditText email;
    private EditText pwd;

    //FIREBASE
    private FirebaseAuth mAuth;

    //AWESOME VALIDATION
    private AwesomeValidation awesomeValidation;

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
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){

            irAPerfil();
        }

        //AWESOME VALIDATION
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.editTextTextEmailAddress, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.editTextTextPassword,".{6,}",R.string.invalid_pwd);


        //BOTONES Y CUADROS DE TEXTOS
        registro = (Button) findViewById(R.id.log_registro);
        entrar = (Button) findViewById(R.id.entrar);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        pwd = (EditText) findViewById(R.id.editTextTextPassword);
        restablecerPwd = (Button) findViewById(R.id.olvidoPwd);

        restablecerPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent recuperarPwd = new Intent(LogActivity.this, RecuperarContrasenaActivity.class);
                startActivity(recuperarPwd);
            }
        });

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

                if(awesomeValidation.validate()){

                    String mail = email.getText().toString().trim();
                    String pass = pwd.getText().toString().trim();

                    mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                irAPerfil();

                            }else{

                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                dameToastdeerror(errorCode);
                            }

                        }
                    });
                }

            }
        });
    }

    private void irAPerfil(){

        Intent paginaPerfil = new Intent(LogActivity.this, PerfilActivity.class);
        startActivity((paginaPerfil));

    }

    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(LogActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(LogActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(LogActivity.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(LogActivity.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                email.setError("La dirección de correo electrónico está mal formateada.");
                email.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(LogActivity.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                pwd.setError("la contraseña es incorrecta ");
                pwd.requestFocus();
                pwd.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(LogActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(LogActivity.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(LogActivity.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(LogActivity.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                email.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                email.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(LogActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(LogActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(LogActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(LogActivity.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(LogActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(LogActivity.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(LogActivity.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                pwd.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                pwd.requestFocus();
                break;

        }

    }

}