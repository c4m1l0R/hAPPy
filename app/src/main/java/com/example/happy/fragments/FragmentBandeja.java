package com.example.happy.fragments;

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
import com.example.happy.adapter.AmigoAdapter;
import com.example.happy.modelos.Amigo;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
public class FragmentBandeja extends Fragment {


    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //RECYCLERVIEW
    private RecyclerView mRecycler;
    private AmigoAdapter mAdapter;


    public FragmentBandeja() {
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
        return inflater.inflate(R.layout.fragment_bandeja, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState );

        ImageView botonAnadirAmigo = view.findViewById(R.id.botonAnadirAmigo);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mRecycler = view.findViewById(R.id.reciclerViewSingleAmigos);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        //MÉTODOS
        getAmigosFromFirebase();


        //EVENTOS
        botonAnadirAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.fragmentAnadirAmigo);
            }
        });

    }

    private void getAmigosFromFirebase(){

        Query query = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                collection("amigos").orderBy("nombre");

        FirestoreRecyclerOptions <Amigo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Amigo>().
                setQuery(query, Amigo.class).build();

        mAdapter = new AmigoAdapter(firestoreRecyclerOptions);
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