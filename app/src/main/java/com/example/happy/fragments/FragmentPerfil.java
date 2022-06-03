package com.example.happy.fragments;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happy.R;
import com.example.happy.adapter.AmigoAdapter;
import com.example.happy.adapter.AmigoPerfilAdapter;
import com.example.happy.modelos.Amigo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FragmentPerfil extends Fragment {

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //RECURSOS
    private TextView textNombreUser;
    private ImageView ajustes;
    private TextView codigoHappy;
    private ImageView copyCodigo;

    //RECYCLERVIEW
    private RecyclerView mRecycler;
    private AmigoPerfilAdapter mAdapter;
    private ArrayList<Amigo> mAmigosList = new ArrayList<>();


    public FragmentPerfil() {

        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState );

        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");


        //RECURSOS
        ajustes = view.findViewById(R.id.imageViewAjustes);
        textNombreUser = view.findViewById(R.id.text_nombreUser);
        codigoHappy = view.findViewById(R.id.codigoHappy);
        copyCodigo = view.findViewById(R.id.copyCodigoHappy);

        mAdapter = new AmigoPerfilAdapter(mAmigosList,R.layout.view_amigo_single);
        mRecycler = view.findViewById(R.id.reciclerViewSingleAmigosPerfil);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Métodos
        cargarDatos();
        getAmigosFromFirebase();

        ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.fragmentAjustes);
            }
        });

        copyCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("TextView", codigoHappy.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getActivity(), "Se ha copiado tu código hAPPy", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void cargarDatos(){

        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()){

                    String nombreUser = ""+ ds.child("nombre").getValue();
                    String uid = ""+ ds.child("uid").getValue();
                    textNombreUser.setText(nombreUser);
                    codigoHappy.setText(uid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getAmigosFromFirebase(){


        databaseReference.child(firebaseUser.getUid()).child("amigos").orderByChild("birthday").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    for(DataSnapshot ds: snapshot.getChildren()){

                        String nombre = ds.child("nombre").getValue().toString();
                        String apellido1 = ds.child("apellido1").getValue().toString();
                        String apellido2 = ds.child("apellido2").getValue().toString();
                        String idAmigo = ds.getKey();
                        mAmigosList.add(new Amigo(nombre, apellido1, apellido2, idAmigo));
                    }

                    mAdapter = new AmigoPerfilAdapter(mAmigosList,R.layout.view_amigo_single);
                    mRecycler.setAdapter(mAdapter);

                }else{

                    Toast.makeText(getActivity(), "No se han añadido aún amigos", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();

        mAmigosList.clear();
    }

}