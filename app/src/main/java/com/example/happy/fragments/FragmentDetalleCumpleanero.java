package com.example.happy.fragments;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happy.R;
import com.example.happy.adapter.RegaloReservadoAdapter;
import com.example.happy.modelos.Regalo;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentDetalleCumpleanero extends Fragment {

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //RECURSOS
    private ImageView backPerfil;
    private TextView nombreAmigo;
    private TextView birthdayAmigo;
    private String idAmigo;
    private String idAmigoColeccion;

    //RECYCLERVIEW
    private RecyclerView mRecycler;
    private RegaloReservadoAdapter mAdapter;

    public FragmentDetalleCumpleanero() {
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
        return inflater.inflate(R.layout.fragment_detalle_cumpleanero, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //RECURSOS
        backPerfil = view.findViewById(R.id.backPerfil);
        nombreAmigo = view.findViewById(R.id.nombreCumpleanero);
        birthdayAmigo = view.findViewById(R.id.fechaCumpleanero);
        idAmigo = getArguments().getString("idAmigo");
        idAmigoColeccion = getArguments().getString("idAmigoColeccion");

        //RECYCLERVIEW
        mRecycler = view.findViewById(R.id.reciclerViewSingleRegalosReservados);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        cargarDatos();
        cargarRegalosReservados();

        backPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.fragmentPerfil);
            }
        });
    }

    private  void cargarDatos(){

        firebaseFirestore.collection("users").document(idAmigo).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {

                nombreAmigo.setText(document.getString("nombre"));
                birthdayAmigo.setText(document.getString("birthday"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "Error a cargar los datos de tu amigo", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void cargarRegalosReservados(){

        Query query = firebaseFirestore.collection("users").document(idAmigo).
                collection("regalos").whereEqualTo("idAmigoReserva", firebaseAuth.getCurrentUser().getUid()).
                orderBy("nombre");

        System.out.println(idAmigo);

        FirestoreRecyclerOptions<Regalo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Regalo>().
                setQuery(query, Regalo.class).build();

        mAdapter = new RegaloReservadoAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}