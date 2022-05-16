package com.example.happy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class RecuperarContrasenaActivity extends AppCompatActivity {

    private EditText email;
    private Button restablecerPwd;
    private String textEmail = "";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        //BOTONES Y CUADROS DE TEXTOS
        email = (EditText) findViewById(R.id.editTextEmailRecuperacion);
        restablecerPwd = (Button) findViewById(R.id.buttonRestablecer);

        //FIREBASSE
        mAuth =FirebaseAuth.getInstance();


        restablecerPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textEmail = email.getText().toString().trim();

                if(!textEmail.isEmpty()){
                    resetPwd();
                }else{
                    Toast.makeText(RecuperarContrasenaActivity.this, "Debe introducir un Email valido", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void resetPwd(){

        mAuth.sendPasswordResetEmail(textEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(RecuperarContrasenaActivity.this, "Se ha enviado a un correo para restablecer la contraseña", Toast.LENGTH_SHORT).show();
                    finish();

                }else{

                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    dameToastdeerror(errorCode);
                }

            }
        });

    }

    private void dameToastdeerror(String error) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(RecuperarContrasenaActivity.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(RecuperarContrasenaActivity.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(RecuperarContrasenaActivity.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(RecuperarContrasenaActivity.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                email.setError("La dirección de correo electrónico está mal formateada.");
                email.requestFocus();
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(RecuperarContrasenaActivity.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(RecuperarContrasenaActivity.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(RecuperarContrasenaActivity.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(RecuperarContrasenaActivity.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                email.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                email.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(RecuperarContrasenaActivity.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(RecuperarContrasenaActivity.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(RecuperarContrasenaActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(RecuperarContrasenaActivity.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(RecuperarContrasenaActivity.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(RecuperarContrasenaActivity.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;


        }

    }
}