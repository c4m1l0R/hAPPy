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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAnadirAmigo#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FragmentAnadirAmigo extends Fragment {

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference dbr;

    //RECURSOS
    private EditText codigoAmigo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAnadirAmigo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAnadirAmigo newInstance(String param1, String param2) {
        FragmentAnadirAmigo fragment = new FragmentAnadirAmigo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentAnadirAmigo() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_anadir_amigo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //RECURSOS
        Button botonAnadirAmigo = view.findViewById(R.id.botonConfirmarAmigo);
        codigoAmigo = view.findViewById(R.id.codigoAmigo);

        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        botonAnadirAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(codigoAmigo.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){

                            String nombre = snapshot.child("nombre").getValue().toString();
                            String apellido1 = snapshot.child("apellido1").getValue().toString();
                            String apellido2 = snapshot.child("apellido2").getValue().toString();
                            String birthday = snapshot.child("birthday").getValue().toString();
                            String idAmigo = codigoAmigo.getText().toString().trim();

                            Amigo amigo = new Amigo(nombre, apellido1, apellido2, birthday, idAmigo);

                            add(amigo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(getActivity(), "Se ha añadido correctamente tu Amigo", Toast.LENGTH_SHORT).show();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(getActivity(), "Error al añadir tu amigo", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }else{

                            Toast.makeText(getActivity(), "El código de usuario no existe", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }


    private Task<Void> add(Amigo amigo){

        return dbr.push().setValue(amigo);
    }
}