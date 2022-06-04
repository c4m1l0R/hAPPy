package com.example.happy.fragments;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


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
    /*private RecyclerView mRecycler;
    private AmigoPerfilAdapter mAdapter;
    private ArrayList<Amigo> mAmigosList = new ArrayList<>();*/


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
        firebaseFirestore = FirebaseFirestore.getInstance();

        //RECURSOS
        ajustes = view.findViewById(R.id.imageViewAjustes);
        textNombreUser = view.findViewById(R.id.text_nombreUser);
        codigoHappy = view.findViewById(R.id.codigoHappy);
        copyCodigo = view.findViewById(R.id.copyCodigoHappy);

        //MÉTODOS
        cargarDatos();

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

                Toast.makeText(getActivity(), "Se ha copiado tu código hAPPy", Toast.LENGTH_SHORT).show();
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


}