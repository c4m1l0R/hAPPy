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
import com.example.happy.modelos.Amigo;
import com.example.happy.modelos.Regalo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


public class FragmentAnadirAmigo extends Fragment {

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //RECURSOS
    private EditText codigoAmigo;
    private String idAmigo;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //RECURSOS
        Button botonAnadirAmigo = view.findViewById(R.id.botonConfirmarAmigo);
        codigoAmigo = view.findViewById(R.id.codigoAmigo);
        idAmigo = codigoAmigo.getText().toString();

        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        botonAnadirAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("users").document(idAmigo).get().
                        addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot document) {

                                String nombre = document.getString("nombre");
                                String apellido1 = nombre = document.getString("apellido1");
                                String apellido2 = nombre = document.getString("apellido2");
                                String birthday = nombre = document.getString("birthday");
                                String idAmigo = document.getId();

                                if(!idAmigo.equals(firebaseAuth.getCurrentUser().getUid())){

                                    anadirAmigo(nombre, apellido1, apellido2, birthday, idAmigo);

                                }else{

                                    Toast.makeText(getActivity(), "No te puedes añadir tu mismo, " +
                                            "pero sabemos que es importante ser tu mismo amigo", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getActivity(), "No existe este amigo", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void anadirAmigo(String nombre, String apellido1, String apellido2, String birthday, String idAmigo){

        //MANDAMOS LA INFO A FIRESTORE EN UN HASMAP
        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("nombre", nombre);
        hashMap.put("apellido1", apellido1);
        hashMap.put("apellido2", apellido2);
        hashMap.put("birthday", birthday);
        hashMap.put("idAmigo", idAmigo);

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                collection("amigos").whereEqualTo("idAmigo", idAmigo).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        
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