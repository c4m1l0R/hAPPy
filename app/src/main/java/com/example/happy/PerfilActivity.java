package com.example.happy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class PerfilActivity extends AppCompatActivity {

    //FRAGMENTS
    FragmentBandeja fragmentBandeja= new FragmentBandeja();
    FragmentPerfil fragmentPerfil= new FragmentPerfil();
    FragmentLista fragmentLista= new FragmentLista();

    //BOTÓN
    Button cerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        loadFragment(fragmentPerfil);

        BottomNavigationView navegation = findViewById(R.id.bottom_navigation);
        navegation.setOnNavigationItemSelectedListener(mOnNavegationItemSelectedListener);

        cerrarSesion = (Button) findViewById(R.id.cerraSesion);

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(PerfilActivity.this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
                irALog();
            }
        });
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavegationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){

                case R.id.fragmentBandeja:
                    loadFragment(fragmentBandeja);
                    return true;

                case R.id.fragmentPerfil:
                    loadFragment(fragmentPerfil);
                    return true;

                case R.id.fragmentLista:
                    loadFragment(fragmentLista);
                    return true;

            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){

        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    private void irALog(){

        Intent paginaLog = new Intent(PerfilActivity.this, LogActivity.class);
        paginaLog.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(paginaLog);

    }


}