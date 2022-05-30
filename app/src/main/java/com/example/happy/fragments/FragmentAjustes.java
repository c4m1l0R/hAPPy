package com.example.happy.fragments;

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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.happy.LogActivity;
import com.example.happy.R;
import com.example.happy.RegistroActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

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
    private DatabaseReference dbr;

    //AWESOME VALIDATION
    AwesomeValidation awesomeValidation;

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
        dbr = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());

        //AWESOME VALIDATION
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(), R.id.nombreAjustes, "[a-zA-Z\\s]+", R.string.invalid_name);
        awesomeValidation.addValidation(getActivity(), R.id.apellido1Ajustes, "[a-zA-Z\\s]+", R.string.invalid_surname);
        awesomeValidation.addValidation(getActivity(), R.id.apellido2Ajustes, "[a-zA-Z\\s]+", R.string.invalid_surname);
        awesomeValidation.addValidation(getActivity(),R.id.contrasenaAjustes,".{6,}",R.string.invalid_pwd);
        awesomeValidation.addValidation(getActivity(), R.id.birthdayAjustes, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.invalid_birthday);


        //CAMPOS AJUSTES
        nombre = view.findViewById(R.id.nombreAjustes);
        apellido1 = view.findViewById(R.id.apellido1Ajustes);
        apellido2 = view.findViewById(R.id.apellido2Ajustes);
        contrasena = view.findViewById(R.id.contrasenaAjustes);
        birthday = view.findViewById(R.id.birthdayAjustes);

        Button botonCerrarSesion= view.findViewById(R.id.cerrarSesion);
        Button botonActualizarDatos = view.findViewById(R.id.confirmarCambiosAjustes);
        Button botonDeBajaPerfil = view.findViewById(R.id.bajaPerfil);

        //MÉTODOS
        cargarDatos();


        //EVENTOS
        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), LogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);

            }
        });

        botonActualizarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(awesomeValidation.validate()){

                    actualizarDatos();

                }else{

                    Toast.makeText(getActivity(), "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show();

                }
            }
        });

        botonDeBajaPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbr.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(v.getContext(), "Se ha eliminado tu perfil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), LogActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "No se ha podido eliminar tu perfil. Ponte en contacto con el administrador.", Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void actualizarDatos(){

        Map<String, Object> personaMap = new HashMap<>();
        personaMap.put("nombre", nombre.getText().toString().trim());
        personaMap.put("apellido1", apellido1.getText().toString().trim());
        personaMap.put("apellido2", apellido2.getText().toString().trim());
        personaMap.put("pwd", contrasena.getText().toString().trim());
        personaMap.put("birthday", birthday.getText().toString().trim());

        databaseReference.child(firebaseUser.getUid()).updateChildren(personaMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(getActivity(), "Actualización realizada con exito", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "Error en la actualización", Toast.LENGTH_SHORT).show();
            }
        });

    }
}