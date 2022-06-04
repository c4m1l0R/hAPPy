package com.example.happy.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.happy.R;
import com.example.happy.adapter.RegaloAdapter;
import com.example.happy.modelos.Regalo;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class FragmentLista extends Fragment {

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //RECYCLERVIEW
    private RecyclerView mRecycler;
    private RegaloAdapter mAdapter;

    public FragmentLista() {
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
        return inflater.inflate(R.layout.fragment_lista, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState );

        ImageView botonAnadirRegalo = view.findViewById(R.id.botonAnadirRegalo);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mRecycler = view.findViewById(R.id.reciclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        //MÉTODOS
        getRegalosFromFirebase();

        //EVENTOS
        botonAnadirRegalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.fragmentAnadirRegalo);
            }
        });

    }

    private void getRegalosFromFirebase(){

        Query query = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                collection("regalos").orderBy("nombre");

        FirestoreRecyclerOptions <Regalo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Regalo>().
                setQuery(query, Regalo.class).build();

        mAdapter = new RegaloAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        mAdapter.stopListening();
    }

}