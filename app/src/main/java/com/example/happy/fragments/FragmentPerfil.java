package com.example.happy.fragments;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happy.R;
import com.example.happy.adapter.AmigoAdapter;
import com.example.happy.adapter.AmigoPerfilAdapter;
import com.example.happy.adapter.HoyAdapter;
import com.example.happy.adapter.RegaloAdapterAmigo;
import com.example.happy.modelos.Amigo;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentPerfil extends Fragment {

    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //RECURSOS
    private TextView textNombreUser;
    private ImageView ajustes;
    private TextView codigoHappy;
    private ImageView copyCodigo;

    //RECYCLERVIEW
    private RecyclerView mRecycler;
    private AmigoPerfilAdapter mAdapter;
    private RecyclerView hoyRecicler;
    private HoyAdapter hoyAdapter;

    public FragmentPerfil() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState );

        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //RECURSOS
        ajustes = view.findViewById(R.id.imageViewAjustes);
        textNombreUser = view.findViewById(R.id.text_nombreUser);
        codigoHappy = view.findViewById(R.id.codigoHappy);
        copyCodigo = view.findViewById(R.id.copyCodigoHappy);

        mRecycler = view.findViewById(R.id.reciclerViewSingleAmigosPerfil);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        hoyRecicler = view.findViewById(R.id.reciclerViewSingleCumpleHoy);
        hoyRecicler.setLayoutManager(new LinearLayoutManager(getActivity()));

        //M??TODOS
        comprobarCumpleUser();
        cargarCumpleHoy();
        cargarDatos();
        caragrAmigos();


        //EVENTOS
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

                Toast.makeText(getActivity(), "Se ha copiado tu c??digo hAPPy", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void cargarDatos(){

        codigoHappy.setText(firebaseAuth.getCurrentUser().getUid());
        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {

                textNombreUser.setText(document.getString("nombre"));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "Error a cargar los datos", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void cargarCumpleHoy(){

        String timeStamp = new SimpleDateFormat("dd/MM").format(Calendar.getInstance().getTime());


        Query query = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                collection("amigos").orderBy("birthday").startAt(timeStamp).endAt(timeStamp+'\uf8ff');

        System.out.println("entro");

        FirestoreRecyclerOptions <Amigo> fr = new FirestoreRecyclerOptions.Builder<Amigo>().
                setQuery(query, Amigo.class).build();

        hoyAdapter = new HoyAdapter(fr);
        hoyAdapter.notifyDataSetChanged();
        hoyRecicler.setAdapter(hoyAdapter);


    }

    private void caragrAmigos(){

        Query query = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                collection("amigos").orderBy("birthday");


        FirestoreRecyclerOptions<Amigo> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Amigo>().
                setQuery(query, Amigo.class).build();

        mAdapter = new AmigoPerfilAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

    }


    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
        hoyAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        mAdapter.stopListening();
        hoyAdapter.stopListening();
    }

    private void comprobarCumpleUser(){

        System.out.println("Entro a cumple");
        String timeStamp = new SimpleDateFormat("dd/MM").format(Calendar.getInstance().getTime());
        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {

                String format="";
                String diaMes = document.get("birthday").toString();
                char[] caracteres = diaMes.toCharArray();
                for(int i = 0; i < 5; i++){

                    format = format + caracteres[i];
                }

                if (format.equals(timeStamp)) {

                    Toast.makeText(getActivity(), "??HOY ES TU CUMPLEA??OS! ??FELICIDADES!", Toast.LENGTH_SHORT).show();

                    borrarRegalos();

                }
            }
        });
    }


    private void borrarRegalos(){

        try {
            System.out.println("entro a borrar regalo");
            firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                    collection("regalos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot document) {

                            for(int i = 0; i < document.getDocuments().size(); i++){

                                borrarListaRegalos(document.getDocuments().get(i).getId());
                            }

                        }
                    });
        }catch(IndexOutOfBoundsException e){


        }
    }

    private void borrarListaRegalos(String idRegalo){

        System.out.println("elimino regalo numer "+ idRegalo);
        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).
                collection("regalos").document(idRegalo).delete();
    }
}