package com.example.happy.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happy.R;
import com.example.happy.adapter.RegaloAdapter;
import com.example.happy.adapter.RegaloAdapterAmigo;
import com.example.happy.modelos.Regalo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPerfilAmigos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPerfilAmigos extends Fragment {

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //RECURSOS
    private TextView nombreAmigo;
    private TextView birthdayAmigo;
    private Button eliminarAmigo;

    //RECYCLERVIEW
    private RecyclerView mRecycler;
    private RegaloAdapterAmigo mAdapter;
    private ArrayList<Regalo> mRegalosList = new ArrayList<>();
    private ProgressDialog progressDialog;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentPerfilAmigos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPerfilAmigos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPerfilAmigos newInstance(String param1, String param2) {
        FragmentPerfilAmigos fragment = new FragmentPerfilAmigos();
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
        return inflater.inflate(R.layout.fragment_perfil_amigos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");


        //RECURSOS
        nombreAmigo = view.findViewById(R.id.nombreAmigoPerfil);
        birthdayAmigo = view.findViewById(R.id.fechaCumpleAmigo);
        eliminarAmigo = view.findViewById(R.id.eliminarAmigo);

        mRecycler = view.findViewById(R.id.reciclerViewSingleAmigosRegalos);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        cargarDatos();
        cargarRegalos(view);

        eliminarAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Eliminar Amigo");
                builder.setMessage("¿Estas seguro que quieres eliminar este amigo?");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseUser = firebaseAuth.getCurrentUser();
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid()).child("regalos");

                        databaseReference.child(getArguments().getString("idAmigo")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(v.getContext(), "Se ha eliminado tu amigo", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(v).navigate(R.id.fragmentBandeja);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "No se ha podido eliminar tu amigo", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });


    }

    private  void cargarDatos(){


        databaseReference.child(getArguments().getString("idAmigo"))
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String n= snapshot.child("nombre").getValue().toString();
                    String b= snapshot.child("birthday").getValue().toString();

                    nombreAmigo.setText(n);
                    birthdayAmigo.setText(b);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cargarRegalos(View view){

        databaseReference.child(getArguments().getString("idAmigo")).child("regalos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    for(DataSnapshot ds: snapshot.getChildren()){
                        String nombre = ds.child("nombre").getValue().toString();
                        String link = ds.child("link").getValue().toString();
                        String idRegalo = ds.getKey();
                        mRegalosList.add(new Regalo(nombre, link, idRegalo));
                    }

                    mAdapter = new RegaloAdapterAmigo(mRegalosList,R.layout.view_regalo_single);
                    mRecycler.setAdapter(mAdapter);

                }else{

                    Toast.makeText(getActivity(), "No se han añadido aún regalos", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}