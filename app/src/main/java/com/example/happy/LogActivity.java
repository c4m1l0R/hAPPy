package com.example.happy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogActivity extends AppCompatActivity {

    Button registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        registro = (Button) findViewById(R.id.log_registro);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent paginaRegistro = new Intent(LogActivity.this, RegistroActivity.class);
                startActivity(paginaRegistro);
            }
        });
    }
}