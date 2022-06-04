package com.example.happy.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.happy.LogActivity;
import com.example.happy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FragmentAjustes extends Fragment {

    //CAMPOS AJUSTES
    private EditText nombre, apellido1, apellido2, contrasena;
    private TextView birthday;

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //AWESOME VALIDATION
    AwesomeValidation awesomeValidation;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState );

        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendario = Calendar.getInstance();
                int anio = calendario.get(Calendar.YEAR);
                int mes =  calendario.get(Calendar.MONTH);
                int dia = calendario.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String fecha = dayOfMonth + "/" + month + "/" + year;
                        birthday.setText(fecha);
                    }
                }, 2022, mes, dia);

                datePickerDialog.show();

            }
        });


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

                firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        firebaseAuth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(getActivity(), "Se ha eliminado tu usuario con éxito", Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                Intent intent = new Intent(getActivity(), LogActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getActivity(), "Error al eliminar al usuario. Pongase en contacto con el administrador", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(), "Error al eliminar al usuario. Pongase en contacto con el administrador", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void cargarDatos(){

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {

                String nom = document.getString("nombre");
                String ape1 = document.getString("apellido1");
                String ape2 = document.getString("apellido2");
                String con = document.getString("pwd");
                String birthD = document.getString("birthday");

                //SET
                nombre.setText(nom);
                apellido1.setText(ape1);
                apellido2.setText(ape2);
                contrasena.setText(con);
                birthday.setText(birthD);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "Error. Consultar con el administrador", Toast.LENGTH_SHORT).show();

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

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).update(personaMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public FragmentAjustes() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ajustes, container, false);
    }
}