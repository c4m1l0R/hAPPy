package com.example.happy.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.happy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


public class FragmentAnadirAmigo extends Fragment {

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //RECURSOS
    private EditText codigoAmigo;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //RECURSOS
        Button botonAnadirAmigo = view.findViewById(R.id.botonConfirmarAmigo);
        codigoAmigo = view.findViewById(R.id.codigoAmigo);

        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        botonAnadirAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                            collection("amigos").whereEqualTo("idAmigo", codigoAmigo.getText().toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                    if(value.isEmpty()){

                                        HashMap<Object, String> map = new HashMap<>();
                                        map.put("id", codigoAmigo.getText().toString());

                                        comprobarQueExisteId(codigoAmigo.getText().toString(), v);

                                    }else{

                                        Toast.makeText(getActivity(), "Este amigo ya existe en tu bandeja", Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });

                }catch (IllegalArgumentException e){

                    HashMap<Object, String> map = new HashMap<>();
                    map.put("id", codigoAmigo.getText().toString());

                    obtenerDatosAmigo(v);

                }finally {

                    Navigation.findNavController(v).navigate(R.id.fragmentBandeja);
                }

            }
        });

    }

    private void comprobarQueExisteId( String id, View v){

        firebaseFirestore.collection("users").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.exists()){

                    obtenerDatosAmigo(v);
                }else{

                    Toast.makeText(getActivity(), "No existe el código hAPPy", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void obtenerDatosAmigo(View v){

        firebaseFirestore.collection("users").document(codigoAmigo.getText().toString()).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {

                        String nombre = document.getString("nombre");
                        String apellido1 = document.getString("apellido1");
                        String apellido2 = document.getString("apellido2");
                        String birthday = document.getString("birthday");
                        String idAmigo = document.getId();

                        anadirAmigo(nombre, apellido1, apellido2, birthday, idAmigo, v);

                    }

                });
    }


    private void anadirAmigo(String nombre, String apellido1, String apellido2, String birthday, String idAmigo, View v){

        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("nombre", nombre);
        hashMap.put("apellido1", apellido1);
        hashMap.put("apellido2", apellido2);
        hashMap.put("birthday", birthday);
        hashMap.put("idAmigo", idAmigo);

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                collection("amigos").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(getActivity(), "Amigo añadido con éxito", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(), "Error al añadir tu amigo. Pongase en contacto con el administrador", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public FragmentAnadirAmigo() {
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
        return inflater.inflate(R.layout.fragment_anadir_amigo, container, false);
    }
}