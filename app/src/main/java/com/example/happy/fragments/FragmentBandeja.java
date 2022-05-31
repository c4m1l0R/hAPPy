package com.example.happy.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.happy.R;
import com.example.happy.adapter.AmigoAdapter;
import com.example.happy.modelos.Amigo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBandeja#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBandeja extends Fragment {


    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    //RECYCLERVIEW
    private RecyclerView mRecycler;
    private AmigoAdapter mAdapter;
    private ArrayList<Amigo> mAmigosList = new ArrayList<>();
    private ProgressDialog progressDialog;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentBandeja() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBandeja.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBandeja newInstance(String param1, String param2) {
        FragmentBandeja fragment = new FragmentBandeja();
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
        return inflater.inflate(R.layout.fragment_bandeja, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState );

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        mAdapter = new AmigoAdapter(mAmigosList,R.layout.view_amigo_single);
        mRecycler = view.findViewById(R.id.reciclerViewSingleAmigos);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        ImageView botonAnadirAmigo = view.findViewById(R.id.botonAnadirAmigo);

        botonAnadirAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.fragmentAnadirAmigo);
            }
        });

        getAmigosFromFirebase(view);


    }

    private void getAmigosFromFirebase(View v){


        databaseReference.child(firebaseUser.getUid()).child("amigos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    for(DataSnapshot ds: snapshot.getChildren()){
                        String nombre = ds.child("nombre").getValue().toString();
                        String apellido1 = ds.child("apellido1").getValue().toString();
                        String apellido2 = ds.child("apellido2").getValue().toString();
                        String idAmigo = ds.child("idAmigo").getValue().toString();
                        mAmigosList.add(new Amigo(nombre, apellido1, apellido2, idAmigo));
                    }

                    mAdapter = new AmigoAdapter(mAmigosList,R.layout.view_amigo_single);
                    mRecycler.setAdapter(mAdapter);

                }else{

                    Toast.makeText(getActivity(), "No se han añadido aún amigos", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(v).navigate(R.id.fragmentPerfilAmigos);

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