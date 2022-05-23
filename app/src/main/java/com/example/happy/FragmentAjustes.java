package com.example.happy;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAjustes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAjustes extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    //CAMPOS AJUSTES
    EditText nombre, apellido1, apellido2, contrasena, birthday;

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public FragmentAjustes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAjustes.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAjustes newInstance(String param1, String param2) {
        FragmentAjustes fragment = new FragmentAjustes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ajustes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState );

        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //CAMPOS AJUSTES
        nombre = view.findViewById(R.id.nombreAjustes);
        apellido1 = view.findViewById(R.id.apellido1Ajustes);
        apellido2 = view.findViewById(R.id.apellido2Ajustes);
        contrasena = view.findViewById(R.id.contrasenaAjustes);
        birthday = view.findViewById(R.id.birthdayAjustes);

        Button botonCerrarSesion= view.findViewById(R.id.cerrarSesion);


        //MÉTODOS
        cargarDatos();
        //EVENTOS
        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), LogActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cargarDatos(){

        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String nom = ""+snapshot.child("nombre").getValue();
                String ape1 = ""+snapshot.child("apellido1").getValue();
                String ape2 = ""+snapshot.child("apellido2").getValue();
                String con = ""+snapshot.child("pwd").getValue();
                String birthD = ""+snapshot.child("birthday").getValue();

                //SET
                nombre.setText(nom);
                apellido1.setText(ape1);
                apellido2.setText(ape2);
                contrasena.setText(con);
                birthday.setText(birthD);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}