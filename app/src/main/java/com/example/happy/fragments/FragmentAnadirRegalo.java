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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FragmentAnadirRegalo extends Fragment {

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //RECURSOS
    private EditText nombreRegalo;
    private EditText linkRegalo;

    public FragmentAnadirRegalo() {
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
        return inflater.inflate(R.layout.fragment_anadir_regalo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //RECURSOS
         nombreRegalo = view.findViewById(R.id.nombreRegalo);
         linkRegalo = view.findViewById(R.id.linkRegalo);
         Button botonAnadirRegalo = view.findViewById(R.id.botonConfirmarAnadirRegalo);
         Button cancelarAnadirRegalo = view.findViewById(R.id.botonCancelarRegalo);

        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        botonAnadirRegalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!nombreRegalo.getText().toString().trim().isEmpty()){

                    anadirRegalo(nombreRegalo.getText().toString().trim(), linkRegalo.getText().toString().trim());
                    Navigation.findNavController(v).navigate(R.id.fragmentLista);

                }
            }
        });

        cancelarAnadirRegalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.fragmentLista);
            }
        });
    }

    private void anadirRegalo(String nombre, String link){

        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("nombre", nombreRegalo.getText().toString().trim());
        hashMap.put("link", linkRegalo.getText().toString().trim());
        hashMap.put("regaloReservado", "false");

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                collection("regalos").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(getActivity(), "Regalo añadido", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(), "Error al añadir el regalo", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}