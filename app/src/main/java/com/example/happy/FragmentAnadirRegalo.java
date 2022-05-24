package com.example.happy;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAnadirRegalo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAnadirRegalo extends Fragment {

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //RECURSOS
    private EditText nombreRegalo;
    private EditText linkRegalo;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAnadirRegalo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAnadirRegalo.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAnadirRegalo newInstance(String param1, String param2) {
        FragmentAnadirRegalo fragment = new FragmentAnadirRegalo();
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
        return inflater.inflate(R.layout.fragment_anadir_regalo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //RECURSOS
         nombreRegalo = view.findViewById(R.id.nombreRegalo);
         linkRegalo = view.findViewById(R.id.linkRegalo);
         Button botonAnadirRegalo = view.findViewById(R.id.botonConfirmarAnadirRegalo);

        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("regalos");



        botonAnadirRegalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Regalo regalo = new Regalo(nombreRegalo.getText().toString().trim(), linkRegalo.getText().toString().trim());
                add(regalo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getActivity(), "Se ha añadido correctamente tu regalo", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(), "Error al añadir tu regalo", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
    private void anadirRegalo(){

        assert  firebaseUser != null; //AFIRMAMOS QUE EL USUARIO NO SEA NULL

        HashMap<String, Object> regaloMap = new HashMap<>();
        regaloMap.put("nombreRegalo", nombreRegalo.getText().toString().trim());
        regaloMap.put("linkRegalo", linkRegalo.getText().toString().trim());

        databaseReference.child(firebaseUser.getUid()).child("regalos").setValue(regaloMap);

                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(getActivity(), "Se ha añadido correctamente tu regalo", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "Error al añadir tu regalo", Toast.LENGTH_SHORT).show();
            }
        });

    }
**/
    private Task<Void> add(Regalo regalo){

        return databaseReference.push().setValue(regalo);
    }
}