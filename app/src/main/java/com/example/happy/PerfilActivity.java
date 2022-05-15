package com.example.happy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PerfilActivity extends AppCompatActivity {

    FragmentBandeja fragmentBandeja= new FragmentBandeja();
    FragmentPerfil fragmentPerfil= new FragmentPerfil();
    FragmentLista fragmentLista= new FragmentLista();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        BottomNavigationView navegation = findViewById(R.id.bottom_navigation);
        navegation.setOnNavigationItemSelectedListener(mOnNavegationItemSelectedListener);
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


}