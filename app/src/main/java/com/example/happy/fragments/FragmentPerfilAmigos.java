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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happy.R;
import com.example.happy.adapter.RegaloAdapterAmigo;
import com.example.happy.modelos.Regalo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FragmentPerfilAmigos extends Fragment {

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //RECURSOS
    private TextView nombreAmigo;
    private TextView birthdayAmigo;
    private Button eliminarAmigo;
    private String idAmigo;
    private String idAmigoColeccion;

    //RECYCLERVIEW
    /*private RecyclerView mRecycler;
    private RegaloAdapterAmigo mAdapter;
    private ArrayList<Regalo> mRegalosList = new ArrayList<>();*/

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
        nombreAmigo = view.findViewById(R.id.nombreAmigoPerfil);
        birthdayAmigo = view.findViewById(R.id.fechaCumpleAmigo);
        eliminarAmigo = view.findViewById(R.id.eliminarAmigo);
        idAmigo = getArguments().getString("idAmigo");
        idAmigoColeccion = getArguments().getString("idAmigoColeccion");

        cargarDatos();
        cargarRegalos();

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



    }
}