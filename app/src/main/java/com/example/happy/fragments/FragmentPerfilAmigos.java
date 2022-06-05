package com.example.happy.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
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
import com.example.happy.adapter.RegaloAdapter;
import com.example.happy.adapter.RegaloAdapterAmigo;
import com.example.happy.modelos.Regalo;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentPerfilAmigos extends Fragment {

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //RECURSOS
    private ImageView backBandeja;
    private TextView nombreAmigo;
    private TextView birthdayAmigo;
    private Button eliminarAmigo;
    private String idAmigo;
    private String idAmigoColeccion;

    //RECYCLERVIEW
    private RecyclerView mRecycler;
    private RegaloAdapterAmigo mAdapter;

    public FragmentPerfilAmigos() {
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
        return inflater.inflate(R.layout.fragment_perfil_amigos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //RECURSOS
        backBandeja = view.findViewById(R.id.backBandeja);
        nombreAmigo = view.findViewById(R.id.nombreAmigoPerfil);
        birthdayAmigo = view.findViewById(R.id.fechaCumpleAmigo);
        eliminarAmigo = view.findViewById(R.id.eliminarAmigo);
        idAmigo = getArguments().getString("idAmigo");
        idAmigoColeccion = getArguments().getString("idAmigoColeccion");

        mRecycler = view.findViewById(R.id.reciclerViewSingleAmigosRegalos);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        cargarDatos();
        cargarRegalos();

        backBandeja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.fragmentBandeja);
            }
        });

        eliminarAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Eliminar Amigo");
                builder.setMessage("¿Estas seguro que quieres eliminar este amigo?");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                                collection("amigos").document(idAmigoColeccion).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(getActivity(), "Amigo eliminado", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(v).navigate(R.id.fragmentBandeja);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(getActivity(), "Error al eliminar tu amigo", Toast.LENGTH_SHORT).show();
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

    private void cargarRegalos(){

        Query query = firebaseFirestore.collection("users").document(idAmigo).collection("regalos").
                orderBy("nombre");

        FirestoreRecyclerOptions<Regalo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Regalo>().
                setQuery(query, Regalo.class).build();

        mAdapter = new RegaloAdapterAmigo(firestoreRecyclerOptions);
        mAdapter.setIdAmigo(idAmigo.trim());
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